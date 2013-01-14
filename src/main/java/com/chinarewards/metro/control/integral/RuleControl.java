package com.chinarewards.metro.control.integral;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.rules.BirthRule;
import com.chinarewards.metro.domain.rules.IntegralRule;
import com.chinarewards.metro.service.integral.IRuleService;

@Controller
@RequestMapping(value="/integralRule")
public class RuleControl {
	
	@Autowired
	private IRuleService ruleService;

	@RequestMapping("/ruleIndex")
	public String ruleIndex(Model model){
		model.addAttribute("sexJson", CommonUtil.toJson(Dictionary.findMemberSex()));
		return "integral/ruleList";
	}
	
	@RequestMapping("/ruleCreatePage")
	public String ruleCreatePage(Model model){
		model.addAttribute("female", Dictionary.MEMBER_SEX_FEMALE);
		model.addAttribute("male", Dictionary.MEMBER_SEX_MALE);
		return "integral/rule";
	}
	
	/**
	 * 保存积分规则
	 * @param rule
	 * @throws Exception
	 */
	@RequestMapping("/saveRule")
	public void saveRule(IntegralRule rule,HttpServletResponse res)throws Exception{
		ruleService.saveIntegralRule(rule);
		res.setContentType("text/html;charset=utf-8");
		PrintWriter witer = res.getWriter();
		witer.print(CommonUtil.toJson(rule.getId()));
		witer.flush();
		witer.close();
	}
	
	@RequestMapping("/updateRule")
	public void updateRule(IntegralRule rule,HttpServletResponse res)throws Exception{
		ruleService.updateIntegralRule(rule);
		res.setContentType("text/html;charset=utf-8");
		PrintWriter witer = res.getWriter();
		witer.print(0);
		witer.flush();
		witer.close();
	}
	
	/**
	 * 查询积分规则
	 * @param rule
	 * @throws Exception
	 */
	@RequestMapping("/findRules")
	public Map<String,Object> findRules(IntegralRule rule,Page page)throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", ruleService.findIntegralRule(page, rule));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	/**
	 * 验证规则是否存在
	 * @param rule
	 * @return
	 */
	@RequestMapping("/validateRule")
	@ResponseBody
	public Integer validateRule(IntegralRule rule)throws Exception{
		IntegralRule rule_ = ruleService.findRuleByName(rule.getRuleName());
		IntegralRule rule_1 = ruleService.findRuleByRule(rule);
		if( rule_ != null && !rule_.getId().equals(rule.getId())){
			return 1;
		}else if(rule_1 != null && !rule_1.getId().equals(rule.getId())){
			return 2;
		}else{
			return 0;
		}
	}
	
	@RequestMapping("/findRuleById")
	public String findRuleById(Integer id,Model model)throws Exception{
		model.addAttribute("rule", ruleService.findRuleById(id));
		model.addAttribute("female", Dictionary.MEMBER_SEX_FEMALE);
		model.addAttribute("male", Dictionary.MEMBER_SEX_MALE);
		return "integral/rule";
	}
	
	/**
	 * 删除
	 * @param ids
	 */
	@RequestMapping("/removeRule")
	@ResponseBody
	public void removeRule(String ids)throws Exception {
		ruleService.removeRule(ids);
	}
	
	/**
	 * 生日积分倍数
	 */
	@RequestMapping("/birthRule")
	public String birthRule(Model model)throws Exception{
		List<BirthRule> list = ruleService.findBirthRule();
		model.addAttribute("id", list.size()>0 ? list.get(0).getId() : null);
		model.addAttribute("times", list.size()>0 ? list.get(0).getTimes() : null);
		return "integral/birthRule";
	}
	
	/**
	 * 保存生日积分倍数
	 * @param birthRule
	 */
	@RequestMapping("/saveBirthRule")
	@ResponseBody
	public Integer saveBirthRule(BirthRule birthRule)throws Exception{
		BirthRule b = ruleService.saveBirthRule(birthRule);
		return b.getId();
	}
	
	@RequestMapping("/updateBirthRule")
	@ResponseBody
	public void updateBirthRule(BirthRule birthRule)throws Exception{
		ruleService.updateBirthRule(birthRule);
	}
}
