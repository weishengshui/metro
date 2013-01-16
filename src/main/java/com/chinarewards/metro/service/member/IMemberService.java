package com.chinarewards.metro.service.member;

import java.util.Date;
import java.util.List;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.business.OrderInfo;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.model.member.DiscountNumberRecordCriteria;
import com.chinarewards.metro.model.member.IntegralRecordCriteria;
import com.chinarewards.metro.model.member.MemberAttendCriteria;
import com.chinarewards.metro.model.member.MemberBrandVo;
import com.chinarewards.metro.model.member.MemberDiscountNumberVo;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryCriteria;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryVo;
import com.chinarewards.metro.model.member.SavingsAccountRecordCriteria;

public interface IMemberService {

	/**
	 * 保存会员信息
	 * 
	 * @param member
	 */
	public Member saveMember(Member member);

	/**
	 * 修改会员信息
	 * 
	 * @param member
	 */
	public void updateMember(Member member);

	/**
	 * 查询会员信息
	 * 
	 * @param member
	 * @param page
	 * @return
	 */
	public List<Member> findMembers(Member member, Page page);

	/**
	 * 根据ID查询会员信息
	 * 
	 * @param id
	 * @return
	 */
	public Member findMemberById(Integer id);

	/**
	 * 修改状态
	 * 
	 * @param id
	 * @param statusCode
	 */
	public void updateStatus(String ids, Integer statusCode);

	/**
	 * 重置密码
	 * 
	 * @param ids
	 */
	public void resetPassword(String ids);

	/**
	 * 判断手机号是否存在
	 * 
	 * @param phone
	 * @return
	 */
	public Member findMemberByPhone(String phone);

	/**
	 * 根据账户查找会员
	 * 
	 * @param accountId
	 * @return 
	 */
	public Member findMemberByAccountId(String accountId);

	/**
	 * 根据条件查询会员的优惠码记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<MemberDiscountNumberVo> searchDiscountNumberRecords(
			DiscountNumberRecordCriteria criteria);

	/**
	 * 根据条件查询会员的优惠码记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countDiscountNumberRecords(DiscountNumberRecordCriteria criteria);

	/**
	 * 发送激活码
	 */
	public void sendActivationCode(Integer memeberId, String phone);

	/**
	 * 根据条件查询会员 短信发送历史记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<MemberSMSOutboxHistoryVo> searchMemberSMSOutboxHistory(
			MemberSMSOutboxHistoryCriteria criteria);

	/**
	 * 根据条件查询会员 短信发送历史记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countMemberSMSOutboxHistory(
			MemberSMSOutboxHistoryCriteria criteria);

	/**
	 * 根据条件查询等待发送的短信记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<MemberSMSOutboxHistoryVo> searchMemberSMSOutboxWait(
			MemberSMSOutboxHistoryCriteria criteria);

	/**
	 * 根据条件查询等待发送的短信记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countMemberSMSOutboxWait(MemberSMSOutboxHistoryCriteria criteria);

	/**
	 * 查询会员参加的品牌记录
	 * 
	 * @param member
	 * @return
	 */
	public List<MemberBrandVo> searchAttendBrands(MemberAttendCriteria criteria);

	/**
	 * 查询会员参加的品牌记录总数
	 * 
	 * @param member
	 * @return
	 */
	public Long countAttendBrands(MemberAttendCriteria criteria);

	/**
	 * 根据条件查询会员的积分获取记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<OrderInfo> searchIntegralGetRecords(
			IntegralRecordCriteria criteria);

	/**
	 * 根据条件查询会员的积分获取记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countIntegralGetRecords(IntegralRecordCriteria criteria);

	/**
	 * 根据条件查询会员的积分使用记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<OrderInfo> searchIntegralUseRecords(
			IntegralRecordCriteria criteria);

	/**
	 * 根据条件查询会员的积分使用记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countIntegralUseRecords(IntegralRecordCriteria criteria);

	/**
	 * 根据条件查询会员的储蓄账户充值记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<OrderInfo> searchSavingsAccountGetRecords(
			SavingsAccountRecordCriteria criteria);

	/**
	 * 根据条件查询会员的储蓄账户充值记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countSavingsAccountGetRecords(
			SavingsAccountRecordCriteria criteria);

	/**
	 * 根据条件查询会员的储蓄账户使用记录
	 * 
	 * @param criteria
	 * @return
	 */
	public List<OrderInfo> searchSavingsAccountUseRecords(
			SavingsAccountRecordCriteria criteria);

	/**
	 * 根据条件查询会员的储蓄账户使用记录总数
	 * 
	 * @param criteria
	 * @return
	 */
	public Long countSavingsAccountUseRecords(
			SavingsAccountRecordCriteria criteria);
	
	/**
	 * 查询会员的过期积分明细
	 * 
	 * @param member
	 * @param unitCodeBinke
	 * @return
	 */
	public List<AccountBalanceUnits> findExpiredIntegralsByMember(
			Member member, Date fromDate, Date toDate, String unitCodeBinke , Page page);

}
