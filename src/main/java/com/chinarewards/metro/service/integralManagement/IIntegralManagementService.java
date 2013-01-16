package com.chinarewards.metro.service.integralManagement;

import java.util.Date;
import java.util.List;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.account.UnitLedger;
import com.chinarewards.metro.model.integral.IntegralCriteria;
import com.chinarewards.metro.vo.integral.ExpiryBalanceUnits;
import com.chinarewards.metro.vo.integral.GroupExpiryBalanceUnits;

public interface IIntegralManagementService {

	Unit findUnitById(String id);

	/**
	 * 没有积分单位。系统就根据默认信息创建
	 * 
	 * @param unit
	 * @return
	 */
	Unit createBinkeUnit(Unit unit);

	/**
	 * 更新积分信息
	 * 
	 * @param unit
	 */
	void updateUnit(Unit unit);

	/**
	 * 根据条件查询积分维护历史记录
	 * 
	 * @param criteria
	 * @return
	 */
	List<UnitLedger> searchUnitLedgers(IntegralCriteria criteria);

	/**
	 * 根据条件查询积分维护历史记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	Long countUnitLedgers(IntegralCriteria criteria);

	/**
	 * 获取时间段内的有积分的账户总数
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public long countAccounts(Date fromDate, Date toDate);

	/**
	 * 获取时间段内的有积分总数
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public long amountPoints(Date fromDate, Date toDate);

	/**
	 * 分页查询时间段内所有的会员积分明细,失效前的查询
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param page
	 * @return
	 */
	public List<ExpiryBalanceUnits> getAccountBlanceUnits(Date fromDate,
			Date toDate, Page page);

	/**
	 * 分页获取失效积分交易，汇总概要明细
	 * 
	 * @param operator
	 *            交易操作人
	 * @param fromDate
	 *            >= from
	 * @param toDate
	 *            < to
	 * @param page
	 * @return
	 */
	public List<GroupExpiryBalanceUnits> getExpiryHistory(String operator,
			Date fromDate, Date toDate, Page page);

	/**
	 * 获取失效积分交易的明细
	 * 
	 * @param transactionNo
	 *            交易id
	 * @param page
	 * @return
	 */
	public List<ExpiryBalanceUnits> getExpiedDetailHistory(
			String transactionNo, Page page);
}
