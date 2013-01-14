package com.chinarewards.metro.control.integral;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.domain.account.UnitLedger;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.integral.IntegralCriteria;
import com.chinarewards.metro.service.integralManagement.IIntegralManagementService;
import com.chinarewards.metro.validator.integral.CreateUnitValidator;

/**
 * 积分管理control
 * 
 * @author weishengshui
 * 
 */
@Controller
@RequestMapping("/integralManagement")
public class IntegralManagementControl {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IIntegralManagementService integralManagementService;

	/**
	 * 积分基本信息维护页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/show")
	public String showUnit(Model model) {

		Unit unit = integralManagementService
				.findUnitById(Dictionary.INTEGRAL_BINKE_ID);
		model.addAttribute("unit", unit);

		return "integralManagement/show";
	}

	@RequestMapping("/create")
	@ResponseBody
	public void createOrupdateUnit(HttpServletResponse response,
			@ModelAttribute Unit unit, BindingResult result, Model model) {

		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();

			new CreateUnitValidator().validate(unit, result);
			if (result.hasErrors()) {
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo(result
						.getAllErrors().get(0).getDefaultMessage())));
				out.flush();
				return;
			}
			if (null == unit.getUnitId() || unit.getUnitId().isEmpty()) {
				integralManagementService.createBinkeUnit(unit);
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo("添加成功")));
				out.flush();

			} else {
				integralManagementService.updateUnit(unit);
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo("修改成功")));
				out.flush();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 积分信息历史页面 
	 * 
	 * @return
	 */
	@RequestMapping("/unitLedger")
	public String listUnitLedger(@ModelAttribute IntegralCriteria criteria,
			Integer page, Integer rows, Model model) {
			
		logger.debug(
				"Entry list IntegralManagementControl controller,That page is:{} criteria name is: {}",
				new Object[] { page, criteria.getOperationPeople() });

		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;

		Page paginationDetail = new Page();
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);

		criteria.setPaginationDetail(paginationDetail);

		List<UnitLedger> unitLedgers = integralManagementService.searchUnitLedgers(criteria);
		Long count = integralManagementService.countUnitLedgers(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", unitLedgers);
		return "integralManagement/unitLedger";
	}

}
