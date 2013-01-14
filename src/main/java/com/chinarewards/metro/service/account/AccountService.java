package com.chinarewards.metro.service.account;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.account.Account;
import com.chinarewards.metro.domain.account.AccountBalance;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.account.Business;
import com.chinarewards.metro.domain.account.Ledger;
import com.chinarewards.metro.domain.account.Transaction;
import com.chinarewards.metro.domain.account.TxStatus;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.model.member.SoonExpireAccountBalanceVo;
import com.chinarewards.metro.sequence.IBusinessNumGenerator;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Autowired
	private IBusinessNumGenerator businessNumGenerator;

	private Date calcExpireDate(Date txDate, Unit unit) {

		int available = unit.getAvailable();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(txDate);
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MILLISECOND, 00);
		if (unit.getAvailableUnit().equals(
				Dictionary.INTEGRAL_AVAILABLE_UNIT_DAY)) {
			calendar.add(Calendar.DAY_OF_MONTH, available);
		} else {
			calendar.add(Calendar.MONTH, available);
		}

		return calendar.getTime();
	}

	private AccountBalance insertOrUpdateAccountBalance(String token,
			Transaction tx, Account account, Unit unit, double units,
			Date expireDate) {

		// get account balance
		AccountBalance accBalance = hbDaoSupport.findTByHQL(
				"FROM AccountBalance b WHERE b.account=? AND b.unitCode=?",
				account, unit.getUnitCode());

		if (null != accBalance) {
			double newValue = NumberHelper.add(accBalance.getUnits(), units);
			accBalance.setUnits(newValue);
			hbDaoSupport.update(accBalance);
		} else {

			accBalance = new AccountBalance();
			accBalance.setAccount(account);
			accBalance.setUnitCode(unit.getUnitCode());
			accBalance.setUnits(units);

			accBalance.setCreatedAt(new Date());
			accBalance.setCreatedBy(token);

			accBalance.setLastModifiedAt(new Date());
			accBalance.setLastModifiedBy(token);

			hbDaoSupport.save(accBalance);
		}

		return accBalance;
	}

	private List<AccountBalanceUnits> getAccountBalanceUnits(Account account,
			String unitCode) {
		List<AccountBalanceUnits> accUnits = hbDaoSupport
				.findTsByHQL(
						"FROM AccountBalanceUnits u WHERE u.accountBalance.account=? AND u.accountBalance.unitCode=? AND u.expired=? ORDER BY u.expDate",
						account, unitCode, false);
		return accUnits;
	}

	private void createBalanceUnits(String token, Account account,
			Transaction tx, AccountBalance accBalance, Unit unit, double units) {

		AccountBalanceUnits abu = new AccountBalanceUnits();
		abu.setTx(tx);
		abu.setAccountBalance(accBalance);
		abu.setCreatedAt(new Date());
		abu.setCreatedBy(token);
		abu.setExpDate(calcExpireDate(tx.getTransactionDate(), unit));
		abu.setLastUpadtedAt(new Date());
		abu.setLastUpdatedBy(token);
		abu.setUnit(unit);
		abu.setUnitPrice(unit.getPrice());
		abu.setUnits(units);

		hbDaoSupport.save(abu);
	}

	@Override
	public void deposit(String token, Account account, Unit unit, double value,
			Transaction tx) {

		Date expiryDate = null;
		if (unit.getAvailable() != null && unit.getAvailable() > 0) {
			expiryDate = calcExpireDate(tx.getTransactionDate(), unit);
		}

		deposit(token, account, unit, value, tx, expiryDate);
	}

	@Override
	public void deposit(String token, Account account, Unit unit, double value,
			Transaction tx, Date expiryDate) {

		AccountBalance accountBalance = insertOrUpdateAccountBalance(token, tx,
				account, unit, value, expiryDate);

		// save balance units
		createBalanceUnits(token, account, tx, accountBalance, unit, value);

		// save ledger
		Ledger ledger = new Ledger(expiryDate, unit.getUnitCode(),
				unit.getPrice(), value, tx, account);
		hbDaoSupport.save(ledger);
	}

	@Override
	public void withdrawal(String token, Account account, Unit unit,
			double value, Transaction tx) {
		withdrawal(token, account, unit, value, tx, null);
	}

	public void withdrawal(String token, Account account, String unitCode,
			double value, Transaction tx) {
		withdrawal(token, account, unitCode, null, value, tx, null);
	}

	public void withdrawal(String token, Account account, String unitCode,
			Unit unit, double value, Transaction tx, Date expiryDate) {

		Map<String, Object> params = new HashMap<String, Object>();

		StringBuffer strHQL = new StringBuffer(
				"FROM AccountBalance b WHERE b.account=:account AND b.unitCode=:unitCode");
		params.put("account", account);
		params.put("unitCode", unitCode);

		List<AccountBalance> accBalances = hbDaoSupport.findTsByHQL(
				strHQL.toString(), params);

		double amount = getAccountBalance(account, unitCode);
		if (amount < value) {
			throw new IllegalStateException("Withdrawal error this Account "
					+ account.getAccountId() + " balance not enough " + value);
		}

		if (null != accBalances && accBalances.size() > 1) {
			throw new IllegalStateException("That account:"
					+ account.getAccountId() + " invalid balance size:"
					+ accBalances.size());
		}

		AccountBalance accountBalance = accBalances.get(0);
		double unitcodeBalance = accountBalance.getUnits();
		unitcodeBalance = NumberHelper.sub(unitcodeBalance, value);
		accountBalance.setUnits(unitcodeBalance);
		hbDaoSupport.update(accountBalance);

		List<AccountBalanceUnits> accBalanceUnits = getAccountBalanceUnits(
				account, unitCode);

		for (AccountBalanceUnits abu : accBalanceUnits) {
			double balance = abu.getUnits();
			if (balance > value) {

				double v = NumberHelper.sub(balance, value);
				abu.setUnits(v);
				hbDaoSupport.update(abu);

				Ledger ledger = new Ledger(abu.getExpDate(), unitCode,
						abu.getUnitPrice(), -value, tx, account);
				hbDaoSupport.save(ledger);
				value = 0;
				break;
			} else {
				hbDaoSupport.delete(abu);
				Ledger ledger = new Ledger(abu.getExpDate(),
						unit.getUnitCode(), abu.getUnitPrice(), -balance, tx,
						account);
				hbDaoSupport.save(ledger);
				value = NumberHelper.sub(value, balance);
				if (value == 0) {
					break;
				}
			}
		}
	}

	@Override
	public void withdrawal(String token, Account account, Unit unit,
			double value, Transaction tx, Date expiryDate) {
		withdrawal(token, account, null, unit, value, tx, expiryDate);
	}

	@Override
	public void transfer(String token, Account from, Account to, Unit unit,
			double value, Transaction tx, Date expriyDate) {
		withdrawal(token, from, unit, value, tx, expriyDate);
		deposit(token, to, unit, value, tx, expriyDate);
	}

	public void expiryBalanceUnits(String token, Transaction tx,
			AccountBalanceUnits accBalanceUnits) {

		AccountBalance accBalance = accBalanceUnits.getAccountBalance();
		Account account = accBalance.getAccount();

		accBalanceUnits.setExpired(true);
		hbDaoSupport.update(accBalanceUnits);

		double units = accBalance.getUnits();
		units = NumberHelper.sub(units, accBalanceUnits.getUnits());
		accBalance.setUnits(units);
		hbDaoSupport.update(accBalance);

		Ledger ledger = new Ledger(accBalanceUnits.getExpDate(),
				accBalanceUnits.getUnit().getUnitCode(),
				accBalanceUnits.getUnitPrice(), (-accBalance.getUnits()), tx,
				account);
		hbDaoSupport.save(ledger);
	}

	@Override
	public Account findAccount(String token, String accountId) {

		Account account = hbDaoSupport.findTById(Account.class, accountId);

		return account;
	}

	@Override
	public Account createAccount(String tokne) {

		Account account = new Account();
		account.setAccountId(businessNumGenerator.getMemberAccountNo());
		account.setCreatedAt(new Date());
		account.setCreatedBy(tokne);
		account.setStatus("activated");

		hbDaoSupport.save(account);

		return account;
	}

	public Account createAccount(String tokne, String accountId) {

		Account account = new Account();
		account.setAccountId(accountId);
		account.setCreatedAt(new Date());
		account.setCreatedBy(tokne);
		account.setStatus("activated");

		hbDaoSupport.save(account);

		return account;
	}

	@Override
	public void frozenAccount(String token, String accountId) {

		Account account = hbDaoSupport.findTById(Account.class, accountId);

		account.setStatus("disabled");

		hbDaoSupport.update(account);

	}

	@Override
	public void unfrozenAccount(String token, String accountId) {
		Account account = hbDaoSupport.findTById(Account.class, accountId);

		account.setStatus("activated");

		hbDaoSupport.update(account);
	}

	@Override
	public double getAccountBalance(Account account, String unitCode) {

		Double total = (Double) hbDaoSupport
				.findTByHQL(
						"SELECT SUM(b.units) FROM AccountBalance b WHERE b.account=? AND b.unitCode=?",
						account, unitCode);
		if (null == total) {
			return 0;
		}
		return total;
	}

	public HBDaoSupport getHbDaoSupport() {
		return hbDaoSupport;
	}

	public void setHbDaoSupport(HBDaoSupport hbDaoSupport) {
		this.hbDaoSupport = hbDaoSupport;
	}

	@Override
	public Unit createUnit(String token, Unit unit) {

		unit.setCreatedAt(new Date());
		unit.setCreatedBy(token);
		unit.setLastModifiedAt(new Date());
		unit.setLastModifiedBy(token);

		hbDaoSupport.save(unit);
		return unit;
	}

	@Override
	public Transaction createTransaction(String token, Business business,
			Date transactionDate) {

		return createTransaction(token, business, transactionDate,
				TxStatus.COMPLETED);
	}

	@Override
	public Account findAccountByMember(Member member) {

		return hbDaoSupport.findTById(Account.class, member.getAccount());
	}

	@Override
	public List<SoonExpireAccountBalanceVo> soonExpireAccountBalanceByMember(
			Member member, String unitCode) {

		Account account = findAccountByMember(member);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("unitCode", unitCode);
		map.put("expired", false);
		map.put("status", TxStatus.COMPLETED);
		Page page = new Page();
		page.setPage(1);
		page.setRows(10);
		List<SoonExpireAccountBalanceVo> list = hbDaoSupport.executeQuery("SELECT new com.chinarewards.metro.model.member.SoonExpireAccountBalanceVo(SUM(abu.units) as units, abu.expDate AS expDate)"
				+" FROM AccountBalanceUnits abu WHERE abu.accountBalance.account=:account AND abu.unit.unitCode=:unitCode AND abu.expired=:expired AND abu.tx.status=:status GROUP BY DATE_FORMAT(abu.expDate, '%Y-%m') ORDER BY DATE_FORMAT(abu.expDate, '%Y-%m') ASC ", map, page);

		return list;
	}

	@Override
	public double getFrozenAccountBalance(Account account, String unitCode) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (null != account) {
			params.put("status", TxStatus.FROZEN);
			params.put("account", account);
			List<BigDecimal> list = hbDaoSupport
					.executeQuery(
							"SELECT SUM(o.integration) FROM OrderInfo o WHERE o.type=0 AND o.tx.status=:status AND o.account=:account",
							params, null);
			if (null != list && list.size() > 0 && null != list.get(0)) {
				return list.get(0).doubleValue();
			}
		}
		return 0d;
	}

	@Override
	public AccountBalanceUnits findAccountBalanceUnits(String id) {
		return hbDaoSupport.findTById(AccountBalanceUnits.class, id);
	}

	@Override
	public Transaction createTransaction(String token, Business business,
			Date transactionDate, TxStatus status) {
		Transaction tx = new Transaction();
		tx.setCreatedAt(new Date());
		tx.setCreatedBy(token);
		tx.setLastModifiedAt(new Date());
		tx.setLastModifiedBy(token);
		tx.setTxId(businessNumGenerator.getTransactionNO());
		tx.setBusines(business);
		tx.setTransactionDate(transactionDate);
		tx.setStatus(status);
		hbDaoSupport.save(tx);
		return tx;
	}

	@Override
	public List<AccountBalanceUnits> findAccountBalanceUnits(Date fromDate,
			Date toDate, Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);
		params.put("expired", false);
		List<AccountBalanceUnits> list = hbDaoSupport
				.findTsByHQLPage(
						"FROM AccountBalanceUnits a WHERE a.expired = :expired AND a.createdAt >=:fromDate AND a.createdAt <:toDate",
						params, page);
		return list;
	}
	
	@Override
	public List<AccountBalanceUnits> findAccountBalanceUnits(Date fromDate,
			Date toDate) {
		return findAccountBalanceUnits(fromDate, toDate, null);
	}

	/**
	 * XXX just for test
	 */
	@Override
	public Transaction createTransaction(String token, String txId,
			Business business, Date transactionDate) {
		Transaction tx = new Transaction();
		tx.setCreatedAt(new Date());
		tx.setCreatedBy(token);
		tx.setLastModifiedAt(new Date());
		tx.setLastModifiedBy(token);
		tx.setTxId(txId);
		tx.setBusines(business);
		tx.setTransactionDate(transactionDate);
		tx.setStatus(TxStatus.COMPLETED);
		hbDaoSupport.save(tx);
		return tx;
	}
}
