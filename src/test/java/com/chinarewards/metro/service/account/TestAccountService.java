package com.chinarewards.metro.service.account;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.domain.account.Account;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.account.Business;
import com.chinarewards.metro.domain.account.Transaction;
import com.chinarewards.metro.domain.account.Unit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration
@Transactional
public class TestAccountService {

	@Autowired
	IAccountService accountService;

	@Autowired
	ITransactionService transactionService;

	@Test
	public void testCreateAccount() {

		Account account = accountService.createAccount("token", "A0000001");

		Assert.assertNotNull(account);

	}

	@Test
	public void testDeposit() {

		Unit unit = new Unit();
		unit.setAvailable(30);
		unit.setAvailableUnit(Dictionary.INTEGRAL_AVAILABLE_UNIT_DAY);
		unit.setNumberOfDecimals(2);
		unit.setPrice(0.8);
		unit.setUnitCode("BK");
		unit.setUnitId("BK");

		unit = accountService.createUnit("token", unit);

		Transaction tx = accountService.createTransaction("token", "t00001",
				Business.POS_SALES, new Date());

		Account account = accountService.createAccount("token", "A0000001");

		accountService.deposit("token", account, unit, 500.23, tx);

		double value = accountService.getAccountBalance(account, "BK");
		Assert.assertEquals(500.23, value);

		accountService.deposit("token", account, unit, 100.23, tx);
		value = accountService.getAccountBalance(account, "BK");
		Assert.assertEquals(600.46, value);
	}

	@Test
	public void testWithdral() {

		Unit unit = new Unit();
		unit.setAvailable(30);
		unit.setAvailableUnit(Dictionary.INTEGRAL_AVAILABLE_UNIT_DAY);
		unit.setNumberOfDecimals(2);
		unit.setPrice(0.8);
		unit.setUnitCode("BK");
		unit.setUnitId("BK");

		unit = accountService.createUnit("token", unit);

		Transaction tx = accountService.createTransaction("token", "t00001",
				Business.POS_SALES, new Date());

		Account account = accountService.createAccount("token", "A0000001");

		accountService.deposit("token", account, unit, 500.23, tx);

		double value = accountService.getAccountBalance(account, "BK");
		Assert.assertEquals(500.23, value);

		tx = accountService.createTransaction("token", "t00002",
				Business.POS_SALES, new Date());
		accountService.withdrawal("token", account, "BK", 100.11, tx);

		value = accountService.getAccountBalance(account, "BK");
		Assert.assertEquals(400.12, value);

	}

	@Test
	public void testExpiry() {

		Unit unit = new Unit();
		unit.setAvailable(30);
		unit.setAvailableUnit(Dictionary.INTEGRAL_AVAILABLE_UNIT_DAY);
		unit.setNumberOfDecimals(2);
		unit.setPrice(0.8);
		unit.setUnitCode("BK");
		unit.setUnitId("BK");

		// deposit 500.23
		unit = accountService.createUnit("token", unit);
		Transaction tx = accountService.createTransaction("token", "t00001",
				Business.POS_SALES, new Date());
		Account account = accountService.createAccount("token", "A0000001");
		accountService.deposit("token", account, unit, 500.23, tx);

		Calendar fromDt = Calendar.getInstance();
		fromDt.add(Calendar.HOUR_OF_DAY, -1);
		Date dtf = fromDt.getTime();

		fromDt.add(Calendar.DAY_OF_MONTH, 1);
		Date dtt = fromDt.getTime();

		List<AccountBalanceUnits> accBalanceUnits = accountService
				.findAccountBalanceUnits(dtf, dtt);

		Assert.assertEquals(1, accBalanceUnits.size());
		Assert.assertEquals(500.23, accBalanceUnits.get(0).getUnits());

		tx = transactionService.expiryMemberPoints("0", dtf, dtt);

		accBalanceUnits = accountService.findAccountBalanceUnits(dtf, dtt);
		Assert.assertEquals(0, accBalanceUnits.size());

		double value = accountService.getAccountBalance(account, "BK");
		Assert.assertEquals(0.0, value);
	}

	public void testTransfer() {

	}
}
