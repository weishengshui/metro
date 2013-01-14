package com.chinarewards.metro.service.integral.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.rules.BirthRule;
import com.chinarewards.metro.domain.rules.IntegralRule;
import com.chinarewards.metro.service.integral.IRuleService;

@Service
public class RuleService implements IRuleService {

	@Autowired
	private HBDaoSupport hbDaoSupport;
	
	@Override
	public List<IntegralRule> findIntegralRule(Page page, IntegralRule rule) {
		DetachedCriteria criteria = DetachedCriteria.forClass(rule.getClass());
		criteria.addOrder(Order.desc("id"));
		if(StringUtils.isNotEmpty(rule.getRuleName())){
			criteria.add(Restrictions.like("ruleName", rule.getRuleName(),MatchMode.ANYWHERE));
		}
		if(rule.getTimes() != null){
			criteria.add(Restrictions.eq("times", rule.getTimes()));
		}
		if(rule.getRangeFrom() != null){
			criteria.add(Restrictions.eq("rangeFrom", rule.getRangeFrom()));
		}
		if(rule.getRangeTo() != null){
			criteria.add(Restrictions.eq("rangeTo", rule.getRangeTo()));
		}
		return hbDaoSupport.findPageByCriteria(page, criteria);
	}
	
	@Override
	public IntegralRule saveIntegralRule(IntegralRule rule) {
		rule.setCreatedAt(DateTools.dateToHour());
		rule.setLastModifiedAt(DateTools.dateToHour());
		rule.setCreatedBy(UserContext.getUserName());
		return hbDaoSupport.save(rule);
	}
	
	@Override
	public IntegralRule findRuleByName(String ruleName) {
		String hql = "from IntegralRule where ruleName = ?";
		return hbDaoSupport.findTByHQL(hql, ruleName);
	}
	
	@Override
	public IntegralRule findRuleByRule(IntegralRule rule) {
		String hql = "from IntegralRule where times = ? and rangeFrom = ? and rangeTo = ? and rangeAgeFrom = ?" + 
					 " and rangeAgeTo = ? and AmountConsumedFrom = ? and AmountConsumedTo = ? and gender = ?";
		return hbDaoSupport.findTByHQL(hql, rule.getTimes(),rule.getRangeFrom(),rule.getRangeTo(),rule.getRangeAgeFrom(),
				rule.getRangeAgeTo(),rule.getAmountConsumedFrom(),rule.getAmountConsumedTo(),rule.getGender());
	}
	
	@Override
	public IntegralRule findRuleById(Integer id) {
		String hql = "from IntegralRule where id = ?";
		return hbDaoSupport.findTByHQL(hql, id);
	}
	
	@Override
	public void updateIntegralRule(IntegralRule rule) throws Exception {
		rule.setLastModifiedAt(DateTools.dateToHour());
		rule.setLastModifiedBy(UserContext.getUserName());
		hbDaoSupport.update(rule);
	}
	
	@Override
	public void removeRule(String ids) {
		String hql = "delete from IntegralRule where id in(:ids)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ids", CommonUtil.getIntegers(ids));
		hbDaoSupport.executeHQL(hql, map);
	}
	
	@Override
	public BirthRule saveBirthRule(BirthRule birthRule) {
		return hbDaoSupport.save(birthRule);
	}
	
	@Override
	public void updateBirthRule(BirthRule birthRule) {
		String hql = "UPDATE BirthRule SET times = ? WHERE id = ?";
		hbDaoSupport.executeHQL(hql, birthRule.getTimes(), birthRule.getId());
	}
	
	@Override
	public List<BirthRule> findBirthRule() {
		return hbDaoSupport.findAll(BirthRule.class);
	}
}
