package com.chinarewards.metro.service.account;

import java.util.Date;

import com.chinarewards.metro.domain.account.Transaction;

public interface ITransactionService {

	/**
	 * 失效指定的余额积分
	 * 
	 * @param token
	 *            userId
	 * @param accBalanceUnitsId
	 *            余额明细Id
	 * @return
	 */
	public Transaction expiryMemberPoints(String token,
			String... accBalanceUnitsId);

	/**
	 * 失效时间段内的会员积分
	 * 
	 * @param token
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Transaction expiryMemberPoints(String token, Date fromDate,
			Date toDate);

}
