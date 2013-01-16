package com.chinarewards.metro.service.integralManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.account.PointExpiredQueue;
import com.chinarewards.metro.domain.account.Transaction;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.account.UnitLedger;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.model.integral.IntegralCriteria;
import com.chinarewards.metro.service.account.IAccountService;
import com.chinarewards.metro.service.member.IMemberService;
import com.chinarewards.metro.vo.integral.ExpiryBalanceUnits;
import com.chinarewards.metro.vo.integral.GroupExpiryBalanceUnits;

@Service
public class IntegralManagementService implements IIntegralManagementService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private IAccountService accountService;
	
	@Override
	public Unit findUnitById(String id) {

		return hbDaoSupport.findTById(Unit.class, id);
	}

	@Override
	public Unit createBinkeUnit(Unit unit) {
		unit.setUnitCode(Dictionary.UNIT_CODE_BINKE);
		unit.setUnitId(Dictionary.INTEGRAL_BINKE_ID);
		unit.setAvailableUnit(Dictionary.INTEGRAL_AVAILABLE_UNIT_MONTH);
		unit.setCreatedAt(SystemTimeProvider.getCurrentTime());
		unit.setCreatedBy(UserContext.getUserName());
		unit.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		unit.setLastModifiedBy(UserContext.getUserName());
		hbDaoSupport.save(new UnitLedger(unit));
		hbDaoSupport.save(unit);
		return unit;
	}

	@Override
	public void updateUnit(Unit unit) {
		
		unit.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		unit.setLastModifiedBy(UserContext.getUserName());
		unit.setAvailableUnit(Dictionary.INTEGRAL_AVAILABLE_UNIT_MONTH);
		unit.setUnitCode(Dictionary.UNIT_CODE_BINKE);
		
		hbDaoSupport.save(new UnitLedger(unit));
		hbDaoSupport.update(unit);
			
	}

	@Override
	public List<UnitLedger> searchUnitLedgers(IntegralCriteria criteria) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchUnitLedgersHQL(criteria, params, false);
		List<UnitLedger> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countUnitLedgers(IntegralCriteria criteria) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchUnitLedgersHQL(criteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0l;
	}
	
	protected String buildSearchUnitLedgersHQL(IntegralCriteria criteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT(u) ");
		} else {
			strBuffer.append("SELECT u ");
		}

		strBuffer.append("FROM UnitLedger u WHERE 1=1 "); // 很奇妙

		if (criteria != null) {
			String operationPeople = criteria.getOperationPeople();
			Date start = criteria.getStart();
			Date end = criteria.getEnd();
			if (null != end) {
				end = DateTools.getDateLastSecond(end);
				strBuffer.append(" AND u.operationTime <= :end");
				params.put("end", end);
			}
			if(null != start){
				strBuffer.append(" AND u.operationTime >= :start");
				params.put("start", start);
			}
			if(null != operationPeople && !operationPeople.isEmpty()){
				strBuffer.append(" AND u.operationPeople like :operationPeople");
				params.put("operationPeople", operationPeople+"%");
			}
			// TODO
			if(!isCount){
				strBuffer.append(" ORDER BY u.operationTime DESC");
			}
		}
		return strBuffer.toString();
	}

	@Override
	public long countAccounts(Date fromDate, Date toDate) {

		long countAccounts = (Long)hbDaoSupport
				.findTByHQL(
						"SELECT COUNT(a.accountBalance.account.accountId) FROM AccountBalanceUnits a WHERE a.expired = :expired AND a.createdAt >=:fromDate AND a.createdAt <:toDate GROUP BY a.accountBalance.account.accountId",
						false, fromDate, toDate);
		return countAccounts;
	}

	@Override
	public long amountPoints(Date fromDate, Date toDate) {
		long amountPonts = (Long)hbDaoSupport
				.findTByHQL(
						"SELECT SUM(units) FROM AccountBalanceUnits a WHERE a.expired = :expired AND a.createdAt >=:fromDate AND a.createdAt <:toDate ",
						false, fromDate, toDate);
		return amountPonts;
	}

	@Override
	public List<ExpiryBalanceUnits> getAccountBlanceUnits(Date fromDate,
			Date toDate, Page page) {

		List<ExpiryBalanceUnits> result = new LinkedList<ExpiryBalanceUnits>();

		List<AccountBalanceUnits> list = accountService
				.findAccountBalanceUnits(fromDate, toDate, page);

		for (AccountBalanceUnits units : list) {
			ExpiryBalanceUnits item = new ExpiryBalanceUnits();

			item.setId(units.getId());
			item.setObtainedAt(units.getCreatedAt());
			item.setOpt(units.getTx().getCreatedBy());
			item.setTransactionNo(units.getTx().getTxId());

			Member member = memberService.findMemberByAccountId(units
					.getAccountBalance().getAccount().getAccountId());

			item.setMemberCard(member.getCard().getCardNumber());
			item.setMemberName(member.getName());

			result.add(item);
		}
		return result;
	}

	@Override
	public List<GroupExpiryBalanceUnits> getExpiryHistory(String operator,
			Date fromDate, Date toDate, Page page) {

		List<GroupExpiryBalanceUnits> result = new LinkedList<GroupExpiryBalanceUnits>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);

		List<Transaction> list = hbDaoSupport
				.findTsByHQLPage(
						"FROM Transaction t WHERE t.createdAt >=:fromDate AND t.createdAt <:toDate",
						params, page);
		String hql = "SELECT SUM(p.accBalanceUnits.units),COUNT(p.accBalanceUnits.account.accountId) FROM PointExpiredQueue p WHERE P.tx=:tx ";

		for (Transaction tx : list) {
			String txId = tx.getTxId();
			GroupExpiryBalanceUnits item = new GroupExpiryBalanceUnits();
			item.setOpt(tx.getCreatedBy());
			item.setStatus(tx.getStatus().toString());
			item.setTransactionDate(tx.getTransactionDate());
			item.setTransactionNo(txId);

			Object[] obj = hbDaoSupport.findTByHQL(hql, tx);
			item.setAmountPoints((Double) obj[0]);
			item.setCountMembers((Long) obj[1]);

			result.add(item);
		}
		return result;
	}

	@Override
	public List<ExpiryBalanceUnits> getExpiedDetailHistory(
			String transactionNo, Page page) {

		List<ExpiryBalanceUnits> result = new LinkedList<ExpiryBalanceUnits>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("transactionNo", transactionNo);
		List<PointExpiredQueue> list = hbDaoSupport.findTsByHQLPage(
				"FROM PointExpiredQueue p WHERE p.tx.txId=:transactionNo ",
				params, page);
		for (PointExpiredQueue units : list) {
			ExpiryBalanceUnits item = new ExpiryBalanceUnits();

			item.setId(units.getId());
			item.setObtainedAt(units.getCreatedAt());
			item.setOpt(units.getTx().getCreatedBy());
			item.setTransactionNo(units.getTx().getTxId());

			Member member = memberService.findMemberByAccountId(units
					.getAccBalanceUnits().getAccountBalance().getAccount()
					.getAccountId());

			item.setMemberCard(member.getCard().getCardNumber());
			item.setMemberName(member.getName());

			result.add(item);
		}
		return result;
	}
}
