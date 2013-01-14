package com.chinarewards.metro.service.integral;

import java.util.List;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.rules.BirthRule;
import com.chinarewards.metro.domain.rules.IntegralRule;

public interface IRuleService {

	/**
	 * 查询积分规则
	 * @param page
	 * @param rule
	 * @return
	 */
	public List<IntegralRule> findIntegralRule(Page page,IntegralRule rule)throws Exception;
	/**
	 * 保存积分规则
	 * @param rule
	 */
	public IntegralRule saveIntegralRule(IntegralRule rule) throws Exception;
	/**
	 * 查询规则名称是否存在
	 * @param ruleName
	 * @return
	 */
	public IntegralRule findRuleByName(String ruleName);
	/**
	 * 查询规则是否存在
	 * @param rule
	 * @return
	 */
	public IntegralRule findRuleByRule(IntegralRule rule);
	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	public IntegralRule findRuleById(Integer id);
	/**
	 * 修改积分规则
	 * @param rule
	 */
	public void updateIntegralRule(IntegralRule rule) throws Exception;
	/**
	 * 删除
	 * @param ids
	 */
	public void removeRule(String ids);
	/**
	 * 保存生日倍数
	 * @param birthRule
	 */
	public BirthRule saveBirthRule(BirthRule birthRule);
	
	public void updateBirthRule(BirthRule birthRule);
	/**
	 * 根据ID查询生日倍数
	 * @param Id
	 * @return
	 */
	public List<BirthRule> findBirthRule();
}
