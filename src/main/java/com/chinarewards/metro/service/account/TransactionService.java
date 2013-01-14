package com.chinarewards.metro.service.account;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.account.Business;
import com.chinarewards.metro.domain.account.PointExpiredQueue;
import com.chinarewards.metro.domain.account.QueueStatus;
import com.chinarewards.metro.domain.account.Transaction;

@Service
public class TransactionService implements ITransactionService {

	@Autowired
	IAccountService accountService;

	@Autowired
	HBDaoSupport hbDaoSupport;

	public Transaction expiryMemberPoints(String token, Date fromDate,
			Date toDate) {

		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException(
					"Invalid parameters that not allow null!");
		}
		List<AccountBalanceUnits> accBalanceUnits = accountService
				.findAccountBalanceUnits(fromDate, toDate);

		if (null != accBalanceUnits && accBalanceUnits.size() > 0) {
			Transaction tx = accountService.createTransaction(token,
					Business.EXPIRY_POINT, new Date());
			for (AccountBalanceUnits abu : accBalanceUnits) {
				processExpiryAccBalanceUnits(token, tx, abu);
			}
			return tx;
		}
		return null;
	}

	protected void processExpiryAccBalanceUnits(String token, Transaction tx,
			AccountBalanceUnits accBalanceUnits) {

		// 过期积分
		accountService.expiryBalanceUnits(token, tx, accBalanceUnits);

		// 保存队列
		PointExpiredQueue queue = new PointExpiredQueue();
		queue.setAccBalanceUnits(accBalanceUnits);
		queue.setCreatedAt(new Date());
		queue.setStatus(QueueStatus.doned);
		queue.setTx(tx);

		hbDaoSupport.save(queue);
	}

	@Override
	public Transaction expiryMemberPoints(String token,
			String... accBalanceUnitsId) {

		Transaction tx = accountService.createTransaction(token,
				Business.EXPIRY_POINT, new Date());

		for (String id : accBalanceUnitsId) {
			AccountBalanceUnits abu = accountService
					.findAccountBalanceUnits(id);
			processExpiryAccBalanceUnits(token, tx, abu);
		}
		return tx;
	}

}
