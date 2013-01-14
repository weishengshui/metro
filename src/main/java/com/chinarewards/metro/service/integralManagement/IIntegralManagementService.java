package com.chinarewards.metro.service.integralManagement;

import java.util.List;

import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.account.UnitLedger;
import com.chinarewards.metro.model.integral.IntegralCriteria;

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

}
