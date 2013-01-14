package com.chinarewards.metro.service.member.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.MD5;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.account.Account;
import com.chinarewards.metro.domain.account.AccountBalanceUnits;
import com.chinarewards.metro.domain.business.OrderInfo;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.domain.sms.SMSSendStatus;
import com.chinarewards.metro.model.member.DiscountNumberRecordCriteria;
import com.chinarewards.metro.model.member.IntegralRecordCriteria;
import com.chinarewards.metro.model.member.MemberAttendCriteria;
import com.chinarewards.metro.model.member.MemberBrandVo;
import com.chinarewards.metro.model.member.MemberDiscountNumberVo;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryCriteria;
import com.chinarewards.metro.model.member.MemberSMSOutboxHistoryVo;
import com.chinarewards.metro.model.member.SavingsAccountRecordCriteria;
import com.chinarewards.metro.service.account.IAccountService;
import com.chinarewards.metro.service.member.IMemberService;
import com.chinarewards.metro.sms.ICommunicationService;

@Service
public class MemberService implements IMemberService {
	@Autowired
	private HBDaoSupport hbDaoSupport;
	@Autowired
	private IAccountService accountService;
	@Autowired
	ICommunicationService communicationService;

	@Override
	public Member saveMember(Member member) {
		member.setCreateDate(DateTools.dateToHour());
		member.setCreateUser(UserContext.getUserName());
		Account account = accountService.createAccount(member.getName());
		member.setAccount(account != null ? account.getAccountId() : "");
		member.setStatus(Dictionary.MEMBER_STATE_NOACTIVATE);
		hbDaoSupport.save(member);
		if ("on".equals(member.getValiCode())) {// 勾选了发送验证码 调用短信接口发送短信
			sendActivationCode(member.getId(), member.getPhone());
		}
		return member;
	}

	@Override
	public void sendActivationCode(Integer memeberId, String phone) {
		Long l = Math.round(Math.random() * 899999 + 190000);
		String c = "验证码: " + l.toString() + " 【积享通技术有限公司】";
		communicationService.queueSMS(null, memeberId.toString(), phone, c, 5,
				null);
		// 修改验证码
		String hql = "UPDATE Member SET valiCode = ? WHERE id = ?";
		hbDaoSupport.executeHQL(hql, l.toString(), memeberId);
	}

	@Override
	public void updateMember(Member member) {
		member.setUpdateDate(DateTools.dateToHour());
		member.setUpdateUser(UserContext.getUserName());
		hbDaoSupport.update(member);
	}

	@Override
	public List<Member> findMembers(Member member, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from Member where 1=1");
		if (StringUtils.isNotEmpty(member.getName())) {
			hql.append(" and name like :name");
			map.put("name", member.getName());
		}
		if (member.getStatus() != null && !"".equals(member.getStatus())) {
			hql.append(" and status = :status");
			map.put("status", member.getStatus());
		}
		if (StringUtils.isNotEmpty(member.getProvince())) {
			hql.append(" and province = :province");
			map.put("province", member.getProvince());
		}
		if (StringUtils.isNotEmpty(member.getCity())) {
			hql.append(" and city = :city");
			map.put("city", member.getCity());
		}
		if (StringUtils.isNotEmpty(member.getArea())) {
			hql.append(" and area = :area");
			map.put("area", member.getArea());
		}
		if (StringUtils.isNotEmpty(member.getPhone())) {
			hql.append(" and phone = :phone");
			map.put("phone", member.getPhone());
		}
		if (member.getCard() != null) {
			if (StringUtils.isNotEmpty(member.getCard().getCardNumber())) {
				hql.append(" and card.cardNumber = :cardnumber");
				map.put("cardnumber", member.getCard().getCardNumber());
			}
		}
		if (StringUtils.isNotEmpty(member.getEmail())) {
			hql.append(" and email = :email");
			map.put("email", member.getEmail());
		}
		hql.append(" order by id desc");
		return hbDaoSupport.findTsByHQLPage(hql.toString(), map, page);
	}

	@Override
	public Member findMemberById(Integer id) {
		return hbDaoSupport.findTById(Member.class, id);
	}

	@Override
	public void updateStatus(String ids, Integer statusCode) {
		String hql = "update Member set status = :status where id in(:ids)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", statusCode);
		map.put("ids", CommonUtil.getIntegers(ids));
		hbDaoSupport.executeHQL(hql, map);
	}

	@Override
	public void resetPassword(String ids) {
		String hql = "update Member set password = :password where id in(:id)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("password", MD5.MD5Encoder(Constants.RESET_PASSWORD));
		map.put("id", CommonUtil.getIntegers(ids));
		hbDaoSupport.executeHQL(hql, map);
	}

	@Override
	public Member findMemberByPhone(String phone) {
		String hql = "from Member where phone = ?";
		return hbDaoSupport.findTByHQL(hql, phone);
	}

	@Override
	public List<MemberDiscountNumberVo> searchDiscountNumberRecords(
			DiscountNumberRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchDiscountNumberRecordsHQL(criteria, params,
				false);
		List<MemberDiscountNumberVo> list = hbDaoSupport.executeQuery(hql,
				params, criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countDiscountNumberRecords(DiscountNumberRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchDiscountNumberRecordsHQL(criteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	@Override
	public List<MemberSMSOutboxHistoryVo> searchMemberSMSOutboxHistory(
			MemberSMSOutboxHistoryCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMemberSMSOutboxHistoryHQL(criteria, params,
				false);
		List<MemberSMSOutboxHistoryVo> list = hbDaoSupport.executeQuery(hql,
				params, criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countMemberSMSOutboxHistory(
			MemberSMSOutboxHistoryCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMemberSMSOutboxHistoryHQL(criteria, params,
				true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	@Override
	public List<MemberSMSOutboxHistoryVo> searchMemberSMSOutboxWait(
			MemberSMSOutboxHistoryCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMemberSMSOutboxWaitHQL(criteria, params, false);
		List<MemberSMSOutboxHistoryVo> list = hbDaoSupport.executeQuery(hql,
				params, criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countMemberSMSOutboxWait(MemberSMSOutboxHistoryCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMemberSMSOutboxWaitHQL(criteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	@Override
	public List<MemberBrandVo> searchAttendBrands(MemberAttendCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchAttendBrandsHQL(criteria, params, false);
		List<MemberBrandVo> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countAttendBrands(MemberAttendCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchAttendBrandsHQL(criteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchAttendBrandsHQL(MemberAttendCriteria criteria,
			Map<String, Object> params, boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(bu) FROM BrandUnionMember bu INNER JOIN bu.brand b WHERE ");
		} else {
			hql.append("SELECT new com.chinarewards.metro.model.member.MemberBrandVo(b.name as brandName, bu.joinedDate as joinedDate) FROM BrandUnionMember bu INNER JOIN bu.brand b WHERE ");
		}
		if (null != member) {
			hql.append(" bu.member=:member");
			params.put("member", member);

			// TODO
		} else {
			hql.append(" 1=0 ");
		}
		hql.append(" ORDER BY bu.joinedDate DESC");
		return hql.toString();
	}

	protected String buildSearchMemberSMSOutboxHistoryHQL(
			MemberSMSOutboxHistoryCriteria criteria,
			Map<String, Object> params, boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(s) FROM SMSOutboxHistory s WHERE ");
		} else {
			hql.append("SELECT new com.chinarewards.metro.model.member.MemberSMSOutboxHistoryVo(s.destination as phoneNumber, s.content as content, s.sentDate as sentDate, s.status as status) FROM SMSOutboxHistory s WHERE ");
		}
		if (null != member) {
			hql.append(" s.destId=:id");
			params.put("id", String.valueOf(criteria.getId()));

			SMSSendStatus status = criteria.getStatus();
			Date sentDateStart = criteria.getSentDateStart();
			Date sentDateEnd = criteria.getSentDateEnd();

			if (null != status && status.equals(SMSSendStatus.QUEUED)) {
				hql.append(" AND 1=0 ");
			} else if (null != status) {
				hql.append(" AND s.status=:status");
				params.put("status", status);
			} else {
				hql.append(" AND (s.status=:status1 OR s.status=:status2)");
				params.put("status1", SMSSendStatus.SENT);
				params.put("status2", SMSSendStatus.ERROR);
			}
			if (null != sentDateEnd) {
				sentDateEnd = DateTools.getDateLastSecond(sentDateEnd);
				hql.append(" AND s.sentDate <=:sentDateEnd");
				params.put("sentDateEnd", sentDateEnd);
			}
			if (null != sentDateStart) {
				hql.append(" AND s.sentDate >=:sentDateStart");
				params.put("sentDateStart", sentDateStart);
			}
			// TODO
		} else {
			hql.append(" 1=0 ");
		}
		hql.append(" ORDER BY s.sentDate DESC");
		return hql.toString();
	}

	protected String buildSearchMemberSMSOutboxWaitHQL(
			MemberSMSOutboxHistoryCriteria criteria,
			Map<String, Object> params, boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(s) FROM SMSOutboxHistory s WHERE ");
		} else {
			hql.append("SELECT new com.chinarewards.metro.model.member.MemberSMSOutboxHistoryVo(s.destination as phoneNumber, s.content as content, s.sentDate as sentDate, s.status as status) FROM SMSOutbox s WHERE ");
		}
		if (null != member) {
			hql.append(" s.destId=:id");
			params.put("id", String.valueOf(criteria.getId()));

			SMSSendStatus status = criteria.getStatus();
			Date sentDateStart = criteria.getSentDateStart();
			Date sentDateEnd = criteria.getSentDateEnd();

			if (null != status && !status.equals(SMSSendStatus.QUEUED)) {
				hql.append(" AND 1=0 ");
			} else { // 只查询等待状态的短信记录
				hql.append(" AND s.status=:status");
				params.put("status", SMSSendStatus.QUEUED);
				if (null != sentDateEnd) {
					sentDateEnd = DateTools.getDateLastSecond(sentDateEnd);
					hql.append(" AND s.sentDate <=:sentDateEnd");
					params.put("sentDateEnd", sentDateEnd);
				}
				if (null != sentDateStart) {
					hql.append(" AND s.sentDate >=:sentDateStart");
					params.put("sentDateStart", sentDateStart);
				}
			}
			// TODO
		} else {
			hql.append(" 1=0 ");
		}
		hql.append(" ORDER BY s.sentDate DESC");
		return hql.toString();
	}

	protected String buildSearchDiscountNumberRecordsHQL(
			DiscountNumberRecordCriteria criteria, Map<String, Object> params,
			boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(d) FROM DiscountNumberHistory d WHERE ");
		} else {// TODO 优惠码来源还没做 XXX
			hql.append("SELECT new com.chinarewards.metro.model.member.MemberDiscountNumberVo(d.id as id, d.generatedDate as generatedDate, d.usedDate as usedDate, d.discountNum as discountNum, d.title as title, d.title as title2, d.status as status) FROM DiscountNumberHistory d WHERE ");
		}
		if (null != member) {
			hql.append(" d.member=:member");
			params.put("member", member);

			String discountNum = criteria.getDiscountNum();
			Integer discountNumStatus = criteria.getDiscountNumStatus();
			Date transactionDateStart = criteria.getTransactionDateStart();
			Date transactionDateEnd = criteria.getTransactionDateEnd();

			if (null != discountNum && !discountNum.isEmpty()) {
				hql.append(" AND d.discountNum LIKE :discountNum");
				params.put("discountNum", "%" + discountNum + "%");
			}
			if (null != transactionDateEnd) {
				transactionDateEnd = DateTools
						.getDateLastSecond(transactionDateEnd);
				hql.append(" AND ( (d.usedDate IS NOT null AND d.usedDate <= :transactionDateEnd) OR ( d.generatedDate <= :transactionDateEnd))");
				params.put("transactionDateEnd", transactionDateEnd);
			}
			if (null != transactionDateStart) { // 交易时间，如果使用时间为null，
												// 则取优惠码生成时间，反之则取优惠码使用时间
				hql.append(" AND ((d.usedDate IS NOT null AND d.usedDate > :transactionDateStart) OR ( d.generatedDate > :transactionDateStart))");
				params.put("transactionDateStart", transactionDateStart);
			}

			if (null != discountNumStatus) {
				hql.append(" AND d.status=:discountNumStatus");
				params.put("discountNumStatus", discountNumStatus);
			}
			hql.append(" ORDER BY d.generatedDate DESC");
			// TODO
		} else {
			hql.append(" 1=0 ");
		}

		return hql.toString();
	}

	@Override
	public List<OrderInfo> searchIntegralGetRecords(
			IntegralRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchIntegralGetRecordsHQL(criteria, params, false);
		List<OrderInfo> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countIntegralGetRecords(IntegralRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchIntegralGetRecordsHQL(criteria, params, true);
		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchIntegralGetRecordsHQL(
			IntegralRecordCriteria criteria, Map<String, Object> params,
			boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(oi) FROM OrderInfo oi WHERE 1=1 ");
		} else {
			hql.append("FROM OrderInfo oi WHERE 1=1");
		}
		// else {
		// hql.append("SELECT new com.chinarewards.metro.model.member.IntegralGetRecordVo(oi.orderNo as orderNo, oi.orderTime as orderTime, oi.shop as shop, oi.tx as tx, oi.orderPrice as orderPrice, oi.merchandise as merchandise, oi.redemptionQuantity as giftCount, oi.orderSource as orderSource, oi.beforeUnits as beforeUnits, oi.integration as integration, oi.matchedRules as matchedRules) FROM OrderInfo oi WHERE 1=1");
		// }
		if (null != member) {
			Account account = accountService.findAccountByMember(member);
			hql.append(" AND oi.account=:account");
			params.put("account", account);

			String businiessNo = criteria.getBusiniessNo();
			Date transactionDateStart = criteria.getTransactionDateStart();
			Date transactionDateEnd = criteria.getTransactionDateEnd();

			if (null != businiessNo && !businiessNo.isEmpty()) {
				hql.append(" AND oi.orderNo LIKE :orderNo");
				params.put("orderNo", businiessNo + "%");
			}
			if (null != transactionDateEnd) {
				transactionDateEnd = DateTools
						.getDateLastSecond(transactionDateEnd);
				hql.append(" AND oi.orderTime <= :transactionDateEnd");
				params.put("transactionDateEnd", transactionDateEnd);
			}
			if (null != transactionDateStart) {
				hql.append(" AND oi.orderTime >= :transactionDateStart");
				params.put("transactionDateStart", transactionDateStart);
			}

			hql.append(" AND oi.type=0  ORDER BY oi.orderTime DESC");
			// TODO
		} else {
			hql.append(" AND 1=0 ");
		}

		return hql.toString();
	}

	@Override
	public List<OrderInfo> searchIntegralUseRecords(
			IntegralRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchIntegralUseRecordsHQL(criteria, params, false);
		List<OrderInfo> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countIntegralUseRecords(IntegralRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchIntegralUseRecordsHQL(criteria, params, true);
		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchIntegralUseRecordsHQL(
			IntegralRecordCriteria criteria, Map<String, Object> params,
			boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(oi) FROM OrderInfo oi WHERE 1=1 ");
		} else {
			hql.append("FROM OrderInfo oi WHERE 1=1");
		}
		// else {
		// hql.append("SELECT new com.chinarewards.metro.model.member.IntegralGetRecordVo(oi.orderNo as orderNo, oi.orderTime as orderTime, oi.shop as shop, oi.tx as tx, oi.orderPrice as orderPrice, oi.merchandise as merchandise, oi.redemptionQuantity as giftCount, oi.orderSource as orderSource, oi.beforeUnits as beforeUnits, oi.integration as integration, oi.matchedRules as matchedRules) FROM OrderInfo oi WHERE 1=1");
		// }
		if (null != member) {
			Account account = accountService.findAccountByMember(member);
			hql.append(" AND oi.account=:account");
			params.put("account", account);

			String businiessNo = criteria.getBusiniessNo();
			Date transactionDateStart = criteria.getTransactionDateStart();
			Date transactionDateEnd = criteria.getTransactionDateEnd();

			if (null != businiessNo && !businiessNo.isEmpty()) {
				hql.append(" AND oi.orderNo LIKE :orderNo");
				params.put("orderNo", businiessNo + "%");
			}
			if (null != transactionDateEnd) {
				transactionDateEnd = DateTools
						.getDateLastSecond(transactionDateEnd);
				hql.append(" AND oi.orderTime <= :transactionDateEnd");
				params.put("transactionDateEnd", transactionDateEnd);
			}
			if (null != transactionDateStart) {
				hql.append(" AND oi.orderTime >= :transactionDateStart");
				params.put("transactionDateStart", transactionDateStart);
			}

			hql.append(" AND oi.type=1  ORDER BY oi.orderTime DESC");
			// TODO
		} else {
			hql.append(" AND 1=0 ");
		}

		return hql.toString();
	}

	@Override
	public List<OrderInfo> searchSavingsAccountGetRecords(
			SavingsAccountRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchSavingsAccountGetRecordsHQL(criteria, params,
				false);
		List<OrderInfo> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countSavingsAccountGetRecords(
			SavingsAccountRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchSavingsAccountGetRecordsHQL(criteria, params,
				true);
		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchSavingsAccountGetRecordsHQL(
			SavingsAccountRecordCriteria criteria, Map<String, Object> params,
			boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(oi) FROM OrderInfo oi WHERE 1=1 ");
		} else {
			hql.append("FROM OrderInfo oi WHERE 1=1");
		}
		// else {
		// hql.append("SELECT new com.chinarewards.metro.model.member.IntegralGetRecordVo(oi.orderNo as orderNo, oi.orderTime as orderTime, oi.shop as shop, oi.tx as tx, oi.orderPrice as orderPrice, oi.merchandise as merchandise, oi.redemptionQuantity as giftCount, oi.orderSource as orderSource, oi.beforeUnits as beforeUnits, oi.integration as integration, oi.matchedRules as matchedRules) FROM OrderInfo oi WHERE 1=1");
		// }
		if (null != member) {
			Account account = accountService.findAccountByMember(member);
			hql.append(" AND oi.account=:account");
			params.put("account", account);

			String businiessNo = criteria.getBusiniessNo();
			Date transactionDateStart = criteria.getTransactionDateStart();
			Date transactionDateEnd = criteria.getTransactionDateEnd();

			if (null != businiessNo && !businiessNo.isEmpty()) {
				hql.append(" AND oi.orderNo LIKE :orderNo");
				params.put("orderNo", businiessNo + "%");
			}
			if (null != transactionDateEnd) {
				transactionDateEnd = DateTools
						.getDateLastSecond(transactionDateEnd);
				hql.append(" AND oi.orderTime <= :transactionDateEnd");
				params.put("transactionDateEnd", transactionDateEnd);
			}
			if (null != transactionDateStart) {
				hql.append(" AND oi.orderTime >= :transactionDateStart");
				params.put("transactionDateStart", transactionDateStart);
			}

			hql.append(" AND oi.type=2  ORDER BY oi.orderTime DESC");
			// TODO
		} else {
			hql.append(" AND 1=0 ");
		}

		return hql.toString();
	}

	@Override
	public List<OrderInfo> searchSavingsAccountUseRecords(
			SavingsAccountRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchSavingsAccountUseRecordsHQL(criteria, params,
				false);
		List<OrderInfo> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countSavingsAccountUseRecords(
			SavingsAccountRecordCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchSavingsAccountUseRecordsHQL(criteria, params,
				true);
		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchSavingsAccountUseRecordsHQL(
			SavingsAccountRecordCriteria criteria, Map<String, Object> params,
			boolean isCount) {

		Member member = hbDaoSupport.findTById(Member.class, criteria.getId());
		StringBuffer hql = new StringBuffer();
		if (isCount) {
			hql.append("SELECT COUNT(oi) FROM OrderInfo oi WHERE 1=1 ");
		} else {
			hql.append("FROM OrderInfo oi WHERE 1=1");
		}
		// else {
		// hql.append("SELECT new com.chinarewards.metro.model.member.IntegralGetRecordVo(oi.orderNo as orderNo, oi.orderTime as orderTime, oi.shop as shop, oi.tx as tx, oi.orderPrice as orderPrice, oi.merchandise as merchandise, oi.redemptionQuantity as giftCount, oi.orderSource as orderSource, oi.beforeUnits as beforeUnits, oi.integration as integration, oi.matchedRules as matchedRules) FROM OrderInfo oi WHERE 1=1");
		// }
		if (null != member) {
			Account account = accountService.findAccountByMember(member);
			hql.append(" AND oi.account=:account");
			params.put("account", account);

			String businiessNo = criteria.getBusiniessNo();
			Date transactionDateStart = criteria.getTransactionDateStart();
			Date transactionDateEnd = criteria.getTransactionDateEnd();

			if (null != businiessNo && !businiessNo.isEmpty()) {
				hql.append(" AND oi.orderNo LIKE :orderNo");
				params.put("orderNo", businiessNo + "%");
			}
			if (null != transactionDateEnd) {
				transactionDateEnd = DateTools
						.getDateLastSecond(transactionDateEnd);
				hql.append(" AND oi.orderTime <= :transactionDateEnd");
				params.put("transactionDateEnd", transactionDateEnd);
			}
			if (null != transactionDateStart) {
				hql.append(" AND oi.orderTime >= :transactionDateStart");
				params.put("transactionDateStart", transactionDateStart);
			}

			hql.append(" AND oi.type=3  ORDER BY oi.orderTime DESC");
			// TODO
		} else {
			hql.append(" AND 1=0 ");
		}

		return hql.toString();
	}

	@Override
	public List<AccountBalanceUnits> findExpiredIntegralsByMember(
			Member member, Date fromDate, Date toDate, String unitCodeBinke,
			Page page) {

		String hql = "FROM AccountBalanceUnits a WHERE a.accountBalance.account = :account AND a.unit.unitCode = :unitCode AND a.expired = :expired";
		Account account = accountService.findAccountByMember(member);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("unitCode", unitCodeBinke);
		params.put("expired", true);
		if (null != fromDate) {
			params.put("fromDate", fromDate);
			hql += " AND a.expDate >=:fromDate";
		}
		if (null != toDate) {
			toDate = DateTools.getDateLastSecond(toDate);
			params.put("toDate", toDate);
			hql += "  AND a.expDate <:toDate";
		}
		List<AccountBalanceUnits> list = hbDaoSupport.findTsByHQLPage(hql,
				params, page);
		return list;
	}

}
