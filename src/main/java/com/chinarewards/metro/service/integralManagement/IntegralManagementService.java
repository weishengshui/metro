package com.chinarewards.metro.service.integralManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.account.UnitLedger;
import com.chinarewards.metro.model.integral.IntegralCriteria;

@Service
public class IntegralManagementService implements IIntegralManagementService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

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


}
