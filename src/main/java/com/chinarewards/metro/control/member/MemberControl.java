package com.chinarewards.metro.control.member;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.account.Account;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.business.OrderInfo;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.member.DiscountNumberRecordCriteria;
import com.chinarewards.metro.model.member.IntegralRecordCriteria;
import com.chinarewards.metro.model.member.MemberAttendCriteria;
import com.chinarewards.metro.model.member.MemberBrandVo;
import com.chinarewards.metro.model.member.MemberDiscountNumberVo;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryCriteria;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryVo;
import com.chinarewards.metro.model.member.SavingsAccountRecordCriteria;
import com.chinarewards.metro.model.member.SoonExpireAccountBalanceVo;
import com.chinarewards.metro.service.account.IAccountService;
import com.chinarewards.metro.service.member.IMemberService;

@Controller
@RequestMapping("/member")
public class MemberControl {

	@Autowired
	private IMemberService memberService;
	@Autowired
	private IAccountService accountService;

	@RequestMapping("/memberIndex")
	public String memberIndex(Model model) throws Exception {
		model.addAttribute("status", Dictionary.findMemberStatus());
		model.addAttribute("statusJson",
				CommonUtil.toJson(Dictionary.findMemberStatus()));
		return "member/memberList";
	}

	@RequestMapping("/memberRegist")
	public String memberRegist(Model model) throws Exception {
		model.addAttribute("status", Dictionary.findMemberStatus());
		model.addAttribute("statusJson",
				CommonUtil.toJson(Dictionary.findMemberStatus()));
		return "member/memberRegist";
	}

	@RequestMapping("/memberPage")
	public String memberPage(Model model) throws Exception {
		model.addAttribute("female", Dictionary.MEMBER_SEX_FEMALE);
		return "member/member";
	}

	@RequestMapping("/saveMember")
	@ResponseBody
	public Member saveMember(Member member) throws Exception {
		return memberService.saveMember(member);
	}

	@RequestMapping("/updateMember")
	@ResponseBody
	public void updateMember(HttpServletResponse response, Member member)
			throws Exception {
		memberService.updateMember(member);
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(CommonUtil.toJson(new AjaxResponseCommonVo("保存成功")));
		out.flush();
	}

	@RequestMapping("/findMemebers")
	public Map<String, Object> findMemebers(Member member, Page page)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", memberService.findMembers(member, page));
		map.put("total", page.getTotalRows());
		return map;
	}

	@RequestMapping("/findMemberById")
	public String findMemberById(Integer id, Model model) throws Exception {
		model.addAttribute("member", memberService.findMemberById(id));
		model.addAttribute("female", Dictionary.MEMBER_SEX_FEMALE);
		return "member/member";
	}

	/**
	 * 发送激活码
	 */
	@RequestMapping(value = "/sendActivationCode")
	@ResponseBody
	public void sendActivationCode(Member member) {
		memberService.sendActivationCode(member.getId(), member.getPhone());
	}

	/**
	 * 手动激活会员
	 */
	@ExceptionHandler(RuntimeException.class)
	@RequestMapping("/activationMember")
	@ResponseBody
	public void activationMember(String ids) throws Exception {
		memberService.updateStatus(ids, Dictionary.MEMBER_STATE_ACTIVATE);
	}

	/**
	 * 注销会员
	 */
	@ResponseBody
	@RequestMapping("/logoutMember")
	public void logoutMember(String ids) throws Exception {
		memberService.updateStatus(ids, Dictionary.MEMBER_STATE_LOGOUT);
	}

	/**
	 * 重置密码
	 */
	@ResponseBody
	@RequestMapping("/resetPassword")
	public String resetPassword(String ids) throws Exception {
		memberService.resetPassword(ids);
		return Constants.RESET_PASSWORD;
	}

	/**
	 * 查询手机号是否存在
	 * 
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findMemberByPhone")
	public String findMemberByPhone(String phone, Integer id) throws Exception {
		Member m = memberService.findMemberByPhone(phone);
		if (m == null)
			return "0";
		else {
			if (m.getId().equals(id)) { // 用==判断不一定正确，注意是两个对象
				return "0";
			} else {
				return "1";
			}
		}
	}

	/**
	 * 修改页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateMemberPage")
	public String updateMemberPage(Model model, Integer id) {
		model.addAttribute("member", memberService.findMemberById(id));
		model.addAttribute("male", Dictionary.MEMBER_SEX_MALE);
		model.addAttribute("female", Dictionary.MEMBER_SEX_FEMALE);
		model.addAttribute("member_state_logout",
				Dictionary.MEMBER_STATE_LOGOUT);
		model.addAttribute("member_state_noactive",
				Dictionary.MEMBER_STATE_NOACTIVATE);
		model.addAttribute("member_state_active",
				Dictionary.MEMBER_STATE_ACTIVATE);
		return "member/memberUpdate";
	}

	/**
	 * 账户信息
	 */
	@RequestMapping("/accountInfo")
	public String accountInfo(Model model, Integer id) {

		Member member = memberService.findMemberById(id);
		Account account = accountService.findAccountByMember(member);

		double avalableJiFen = accountService.getAccountBalance(account,
				Dictionary.UNIT_CODE_BINKE);
		double froznJiFen = accountService.getFrozenAccountBalance(account,
				Dictionary.UNIT_CODE_BINKE);
		double rmbBalance = accountService.getAccountBalance(account,
				Dictionary.UNIT_CODE_RMB);

		model.addAttribute("avalableJiFen", avalableJiFen);
		model.addAttribute("froznJiFen", froznJiFen);
		model.addAttribute("rmbBalance", rmbBalance);
		model.addAttribute("id", id);

		return "member/accountInfo";
	}

	// 获取即将过期的积分
	@RequestMapping("/soonExpire")
	public String soonExpire(Model model, Integer id) {
		List<SoonExpireAccountBalanceVo> soonExpire = null;

		if (null == id) {
			soonExpire = new ArrayList<SoonExpireAccountBalanceVo>();
		} else {
			// prepare data
			Member member = memberService.findMemberById(id);

			soonExpire = accountService.soonExpireAccountBalanceByMember(
					member, Dictionary.UNIT_CODE_BINKE);
		}

		model.addAttribute("id", id);
		model.addAttribute("rows", soonExpire);
		model.addAttribute("total", soonExpire.size());

		return "member/accountInfo";
	}

	// 获取会员的过期积分明细
	@RequestMapping(value = "/expiredIntegralGet", method = RequestMethod.GET)
	public String expiredIntegral(Model model, Integer id) {

		model.addAttribute("id", id);

		return "member/expiredIntegral";
	}

	@RequestMapping(value = "/expiredIntegral")
	public String expiredIntegral(Integer page, Integer rows, Model model,
			Integer id, Date fromDate, Date toDate) {
		
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		
		List<AccountBalanceUnits> list = null;

		if (null == id) {
			list = new ArrayList<AccountBalanceUnits>();
		} else {
			// prepare data
			Member member = memberService.findMemberById(id);

			list = memberService.findExpiredIntegralsByMember(member, fromDate,
					toDate, Dictionary.UNIT_CODE_BINKE, paginationDetail);
		}

		model.addAttribute("id", id);
		model.addAttribute("rows", list);
		model.addAttribute("total", paginationDetail.getTotalRows());
		model.addAttribute("page", page);

		return "member/expiredIntegral";
	}

	/**
	 * 积分获取记录页面
	 * 
	 * @return
	 */
	@RequestMapping("/integralGetRecord")
	public String integralGetRecord(
			@ModelAttribute IntegralRecordCriteria criteria, Integer page,
			Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<OrderInfo> list = memberService.searchIntegralGetRecords(criteria);
		Long count = memberService.countIntegralGetRecords(criteria);

		System.out.println("integralGetRecord list size is " + list.size());

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/integralGetRecord";
	}

	/**
	 * 积分使用记录页面
	 * 
	 * @return
	 */
	@RequestMapping("/integralUseRecord")
	public String integralUseRecord(
			@ModelAttribute IntegralRecordCriteria criteria, Integer page,
			Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<OrderInfo> list = memberService.searchIntegralUseRecords(criteria);
		Long count = memberService.countIntegralUseRecords(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/integralUseRecord";
	}

	/**
	 * 储蓄账户充值记录页面
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/savingsAccountGetRecord")
	public String savingsAccountGetRecord(
			@ModelAttribute SavingsAccountRecordCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<OrderInfo> list = memberService
				.searchSavingsAccountGetRecords(criteria);
		Long count = memberService.countSavingsAccountGetRecords(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/savingsAccountGetRecord";
	}

	/**
	 * 储蓄账户使用记录页面
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/savingsAccountUseRecord")
	public String savingsAccountUseRecord(
			@ModelAttribute SavingsAccountRecordCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<OrderInfo> list = memberService
				.searchSavingsAccountUseRecords(criteria);
		Long count = memberService.countSavingsAccountUseRecords(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/savingsAccountUseRecord";
	}

	/**
	 * 优惠码记录页面
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/discountNumberRecord")
	public String discountNumberRecord(
			@ModelAttribute DiscountNumberRecordCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<MemberDiscountNumberVo> list = memberService
				.searchDiscountNumberRecords(criteria);
		Long count = memberService.countDiscountNumberRecords(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/discountNumberRecord";
	}

	/**
	 * 短信发送历史记录页面 短信发送历史记录
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/memberSMSOutboxHistory")
	public String memberSMSOutboxHistory(
			@ModelAttribute MemberSMSOutboxHistoryCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<MemberSMSOutboxHistoryVo> list = memberService
				.searchMemberSMSOutboxHistory(criteria);
		Long count = memberService.countMemberSMSOutboxHistory(criteria);
		// just for test
		// list.add(new MemberSMSOutboxHistoryVo("13189755310", "你好，现在有以下优惠",
		// new Date(), SMSSendStatus.ERROR));
		// list.add(new MemberSMSOutboxHistoryVo("13189755310", "你好，现在有以下优惠",
		// new Date(), SMSSendStatus.SENT));

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/memberSMSOutboxHistory";
	}

	/**
	 * 短信发送历史记录页面 等待发送的短信的记录
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/memberSMSOutboxWait")
	public String memberSMSOutboxWait(
			@ModelAttribute MemberSMSOutboxHistoryCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<MemberSMSOutboxHistoryVo> list = memberService
				.searchMemberSMSOutboxWait(criteria);
		Long count = memberService.countMemberSMSOutboxWait(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/memberSMSOutboxHistory";
	}

	/**
	 * 会员维护中的联合会员页面 ------ 显示会员是哪些品牌的联合会员
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/attendBrands")
	public String attendBrands(@ModelAttribute MemberAttendCriteria criteria,
			Integer page, Integer rows, Model model) {

		model.addAttribute("id", criteria.getId());
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<MemberBrandVo> list = memberService.searchAttendBrands(criteria);
		Long count = memberService.countAttendBrands(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);
		model.addAttribute("rows", list);

		return "member/attendBrands";
	}

}
