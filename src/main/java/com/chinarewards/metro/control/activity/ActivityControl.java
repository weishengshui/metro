package com.chinarewards.metro.control.activity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.domain.activity.ActivityInfo;
import com.chinarewards.metro.domain.activity.Token;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.pos.PosBind;
import com.chinarewards.metro.domain.shop.DiscountNumber;
import com.chinarewards.metro.service.activity.IActivityService;

@Controller
public class ActivityControl {

	@Autowired
	private IActivityService activityService;

	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 跳转列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/activity/activityList")
	public String activityList() {
		return "activity/activityList";
	}

	/**
	 * 分页查询活动信息
	 */
	// @RequestMapping(value = "/activity/findActivities")
	// public Map<String, Object> findActivities(String activityName,String
	// startDate,String endDate, Page page)
	// throws Exception {
	// Map<String, Object> map = new HashMap<String, Object>();
	// try {
	// map.put("rows",
	// activityService.findActivity(activityName,startDate,endDate, page));
	// map.put("total", page.getTotalRows());
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return map;
	// }

	/**
	 * 分页查询活动信息
	 */
	@RequestMapping(value = "/activity/findActivities")
	public String findActivities(Integer page, Integer rows, Model model,
			String activityName, String startDate, String endDate)
			throws Exception {
		Page paginationDetail = new Page();
		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);

		List<ActivityInfo> list = activityService.findActivity(activityName,
				startDate, endDate, paginationDetail);
		model.addAttribute("rows", list);
		model.addAttribute("total", paginationDetail.getTotalRows());
		model.addAttribute("page", page);

		return "activity/activityList";
	}

	/**
	 * 查询参加活动的品牌信息
	 */
	@RequestMapping("/activity/findBrandAct")
	public Map<String, Object> findBrandAct(Brand bm, Page page)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("rows", activityService.findBrandAct(bm, page));
			map.put("total", page.getTotalRows());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 查询没有参加活动的品牌信息
	 */
	@RequestMapping("/activity/findBrandNotBandAct")
	public Map<String, Object> findBrandNotBandAct(Brand bm, Page page,
			String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("rows", activityService.findBrandNotBandAct(bm, page, id));
			map.put("total", page.getTotalRows());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping("/activity/addActivity")
	public String actMaintain() {
		return "activity/actMaintain";
	}

	/**
	 * 删除参加活动的品牌信息
	 */
	@RequestMapping(value = "/activity/delActAndBran", method = RequestMethod.POST)
	public String delActAndBran(String ids) {
		try {
			if (null != ids && ids.length() > 0) {
				String[] actIds = ids.split(",");
				activityService.deleteActAndBranByIds(actIds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "activity/activityList";
	}

	/**
	 * 删除活动信息
	 */
	@RequestMapping(value = "/activity/deleteActivity", method = RequestMethod.POST)
	public String deleteActivity(String ids) {
		try {
			if (null != ids && ids.length() > 0) {
				String[] actIds = ids.split(",");
				for (String id : actIds) {
					activityService.deleteActivity(id);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "activity/activityList";
	}

	/**
	 * 删除绑定活动的POS机
	 */
	@RequestMapping(value = "/activity/delPosBand", method = RequestMethod.POST)
	public String delPosBand(String ids) {
		try {
			if (null != ids && ids.length() > 0) {
				String[] posIds = ids.split(",");
				activityService.delPosBand(posIds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "activity/activityList";
	}

	/**
	 * 添加参加活动的品牌信息
	 */
	@RequestMapping(value = "/activity/addBrandAct", method = RequestMethod.POST)
	public String addBrandAct(String ids, String actId) {
		if (null != ids && ids.length() > 0) {
			String[] brandIds = ids.split(",");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date joinTime = sdf.parse(sdf.format(new Date()));
				activityService.addBrandAct(brandIds, Integer.valueOf(actId),
						joinTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "activity/activityList";
	}

	/**
	 * 添加绑定的Pos机
	 */
	@RequestMapping(value = "/activity/savePos", method = RequestMethod.POST)
	@ResponseBody
	public void savePos(PosBind posBind, String code, String actId,
			String bindDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(sdf.format(new Date()));
			posBind.setCode(code);
			posBind.setBindDate(sdf.parse(bindDate));
			posBind.setfId(Integer.valueOf(actId));
			posBind.setMark(0);
			posBind.setCreatedAt(d);
			posBind.setLastModifiedAt(d);
			activityService.savePosBind(posBind);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 检测pos机是否绑定门店
	 */
	@RequestMapping(value = "/activity/checkPosBand", method = RequestMethod.POST)
	public void checkPosBand(String code, HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");
		try {
			PrintWriter out = response.getWriter();

			int count = activityService.checkPosBand(code);

			out.print(CommonUtil.toJson(count));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询绑定活动的POS机
	 */
	@RequestMapping(value = "/activity/queryPosBands", method = RequestMethod.POST)
	public Map<String, Object> queryPosBands(PosBind posBind, Page page)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("rows", activityService.queryPosBands(posBind, page));
			map.put("total", page.getTotalRows());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 查询单个活动所绑定的品牌
	 */
	@RequestMapping(value = "/activity/query_actBands", method = RequestMethod.POST)
	public Map<String, Object> queryactBands(String name, Page page, String id)
			throws Exception {
		if (id == null) {
			return null;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows",
					activityService.findBrandAct(name, page,
							Integer.valueOf(id)));
			map.put("total", page.getTotalRows());
			return map;
		}

	}

	/**
	 * 查询单个活动所绑定的POS机
	 */
	@RequestMapping(value = "/activity/query_posBands", method = RequestMethod.POST)
	public Map<String, Object> queryposBands(PosBind posBind, Page page,
			String id) throws Exception {
		if (id == null) {
			return null;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows",
					activityService.queryPosBands(posBind, page,
							Integer.valueOf(id)));
			map.put("total", page.getTotalRows());
			return map;
		}
	}

	/**
	 * 查询对象，跳转到修改页面
	 */
	@RequestMapping(value = "/activity/queryActivity")
	public String queryActivity(HttpSession session, Model model, String id) {
		try {
			String imageSessionName = UUIDUtil.generate();
			model.addAttribute("imageSessionName", imageSessionName);
			if (id != null) {
				model.addAttribute("activity",
						activityService.findActivityById(id));
				model.addAttribute("discount",
						activityService.findDiscountNumberById(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "activity/updateActivity";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/activity/update")
	@ResponseBody
	public void updateActivity(HttpSession session, String id,String imageSessionName,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ActivityInfo activity, BindingResult result,
			Model model) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		com.chinarewards.metro.domain.file.FileItem actImage = null;
		Map<String, com.chinarewards.metro.domain.file.FileItem> images = (HashMap<String, com.chinarewards.metro.domain.file.FileItem>) session
				.getAttribute(imageSessionName);
		if (null != images && images.size() > 0) {
			for (Map.Entry<String, com.chinarewards.metro.domain.file.FileItem> image : images
					.entrySet()) {
				actImage = image.getValue();
				break;
			}
		}
		if (null != actImage
				&& actImage.getUrl().startsWith(
						Constants.UPLOAD_TEMP_UID_PREFIX)) {
			actImage.setUrl(FileUtil.moveFile(Constants.ACTIVITY_IMAGE_BUFFER,
					actImage.getUrl(), Constants.ACTIVITY_IMAGE_DIR));
		}
		activity.setPicture(actImage.getUrl());
		activityService.updateActivity(activity);

		String title = request.getParameter("title");
		String descr = request.getParameter("descr");
		activityService.updateDiscountNumberByActId(title, descr,
				activity.getId());
		out.print(CommonUtil.toJson(1));
		out.flush();
		out.close();
	}

	@RequestMapping("/activity/checkActNameAndTime")
	@ResponseBody
	public void checkActNameAndTime(HttpServletResponse response, String name,
			String dTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		int count = 0;
		try {
			out = response.getWriter();
			count = activityService.checkActNameAndTime(name, sdf.parse(dTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (count > 0) {
			out.print(CommonUtil.toJson(1));
		} else {
			out.print(CommonUtil.toJson(0));
		}
		out.flush();
		out.close();
	}

	/**
	 * 添加活动
	 */
	@RequestMapping("/activity/saveActivity")
	@ResponseBody
	public void saveActivity(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, String imageSessionName,
			@ModelAttribute ActivityInfo activity, BindingResult result,
			Model model) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		Token token = Token.getInstance();
		if (token.isTokenValid(request)) {

			com.chinarewards.metro.domain.file.FileItem actImage = null;
			Map<String, com.chinarewards.metro.domain.file.FileItem> images = (HashMap<String, com.chinarewards.metro.domain.file.FileItem>) session
					.getAttribute(imageSessionName);
			if (null != images && images.size() > 0) {
				for (Map.Entry<String, com.chinarewards.metro.domain.file.FileItem> image : images
						.entrySet()) {
					actImage = image.getValue();
					break;
				}
			}
			if (null != actImage
					&& actImage.getUrl().startsWith(
							Constants.UPLOAD_TEMP_UID_PREFIX)) {
				actImage.setUrl(FileUtil.moveFile(
						Constants.ACTIVITY_IMAGE_BUFFER, actImage.getUrl(),
						Constants.ACTIVITY_IMAGE_DIR));
			}
			activity.setPicture(actImage.getUrl());
			activity.setTag(1);
			activityService.saveActivity(activity);

			DiscountNumber discountNumber = new DiscountNumber();
			discountNumber.setActivityInfo(activity);
			discountNumber.setTitle(request.getParameter("title"));
			discountNumber.setDescr(request.getParameter("descr"));
			activityService.saveDiscountNumber(discountNumber);

			// session.setAttribute("id", activity.getId());

		} else {
			activityService.updateActivity(activity);

			String title = request.getParameter("title");
			String descr = request.getParameter("descr");
			activityService.updateDiscountNumberByActId(title, descr,
					activity.getId());
			token.saveToken(request);
		}
		out.print(CommonUtil.toJson(activity.getId()));
		out.flush();
		out.close();

	}

	/**
	 * 字符串截取
	 */
	private String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 取消活动
	 */
	@RequestMapping(value = "/activity/cancerActivity", method = RequestMethod.POST)
	public String cancerActivity(String ids) {
		try {
			if (null != ids && ids.length() > 0) {
				String[] actIds = ids.split(",");
				for (String id : actIds) {
					activityService.cancerActivity(id);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "activity/activityList";
	}

	@RequestMapping(value = "/showPicture")
	public void shopPicUpload(HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=utf-8");
			// writer.write(CommonUtil.toJson(list));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
