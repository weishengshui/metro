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
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.domain.activity.ActivityInfo;
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
	@RequestMapping(value = "/activity/findActivities")
	public Map<String, Object> findActivities(ActivityInfo activity, Page page)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("rows", activityService.findActivity(activity, page));
			map.put("total", page.getTotalRows());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
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
	public Map<String, Object> queryactBands(Brand brand, Page page, String id)
			throws Exception {
		if (id == null) {
			return null;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows",
					activityService.findBrandAct(brand, page,
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
	public void updateActivity(HttpSession session, String id,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ActivityInfo activity, BindingResult result,
			Model model) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (activity == null) {
			activity = new ActivityInfo();
		}

		File uploadPath = new File(Constants.ACTIVITY_IMAGE_DIR);
		File tempPath = new File(Constants.ACTIVITY_IMAGE_BUFFER);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		if (!tempPath.exists()) {
			tempPath.mkdirs();
		}
		String fileNam = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DiskFileItemFactory factory = new DiskFileItemFactory(409600, tempPath);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(4194304);

		com.chinarewards.metro.domain.file.FileItem pic = null;
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					if (item.getFieldName().equals("activityName")) {
						activity.setActivityName(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("startDate")) {
						activity.setStartDate(sdf.parse(item.getString("UTF-8")));
					}
					if (item.getFieldName().equals("endDate")) {
						activity.setEndDate(sdf.parse(item.getString("UTF-8")));
					}
					if (item.getFieldName().equals("description")) {
						activity.setDescription(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("hoster")) {
						activity.setHoster(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("activityNet")) {
						activity.setActivityNet(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("contacts")) {
						activity.setContacts(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("conTel")) {
						activity.setConTel(item.getString("UTF-8"));
					}

				} else {
					if (!item.isFormField()) {
						if (null != item.getName() && !item.getName().isEmpty()) {
							pic = new com.chinarewards.metro.domain.file.FileItem();
							pic.setFilesize(item.getSize());
							pic.setOriginalFilename(item.getName());
							pic.setMimeType(item.getContentType());

							String suffix = getSuffix(item.getName());
							activity.setPicture(item.getName());
							String fileName = UUIDUtil.generate() + suffix;
							fileNam = fileName;
							item.write(new File(uploadPath, fileName)); // 移到正式目录
							item.delete(); // 删除临时文件

							pic.setUrl(fileName);
						}
					}
				}
			}
			activityService.updateActivity(activity, pic);

			String title = request.getParameter("title");
			String descr = request.getParameter("descr");
			activityService.updateDiscountNumberByActId(title, descr,
					activity.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(CommonUtil.toJson(1));
		out.flush();
		out.close();
	}

	/**
	 * 添加活动
	 */
	@RequestMapping("/activity/saveActivity")
	@ResponseBody
	public void saveActivity(HttpSession session, HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute ActivityInfo activity, BindingResult result,
			Model model) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		if (activity == null) {
			activity = new ActivityInfo();
		}
		activity.setTag(1);
		File uploadPath = new File(Constants.ACTIVITY_IMAGE_DIR);
		File tempPath = new File(Constants.ACTIVITY_IMAGE_BUFFER);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		if (!tempPath.exists()) {
			tempPath.mkdirs();
		}
		String fileNam = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DiskFileItemFactory factory = new DiskFileItemFactory(409600, tempPath);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(4194304);

		com.chinarewards.metro.domain.file.FileItem pic = null;
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					if (item.getFieldName().equals("activityName")) {
						activity.setActivityName(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("startDate")) {
						activity.setStartDate(sdf.parse(item.getString("UTF-8")));
					}
					if (item.getFieldName().equals("endDate")) {
						activity.setEndDate(sdf.parse(item.getString("UTF-8")));
					}
					if (item.getFieldName().equals("description")) {
						activity.setDescription(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("hoster")) {
						activity.setHoster(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("activityNet")) {
						activity.setActivityNet(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("contacts")) {
						activity.setContacts(item.getString("UTF-8"));
					}
					if (item.getFieldName().equals("conTel")) {
						activity.setConTel(item.getString("UTF-8"));
					}

				} else {
					if (!item.isFormField()) {
						if (null != item.getName() && !item.getName().isEmpty()) {
							pic = new com.chinarewards.metro.domain.file.FileItem();
							pic.setFilesize(item.getSize());
							pic.setOriginalFilename(item.getName());
							pic.setMimeType(item.getContentType());

							String suffix = getSuffix(item.getName());
							activity.setPicture(item.getName());
							String fileName = UUIDUtil.generate() + suffix;
							fileNam = fileName;
							item.write(new File(uploadPath, fileName)); // 移到正式目录
							item.delete(); // 删除临时文件

							pic.setUrl(fileName);
						}
					}
				}
			}
			activityService.saveActivity(activity, pic);

			DiscountNumber discountNumber = new DiscountNumber();
			discountNumber.setActivityInfo(activity);
			discountNumber.setTitle(request.getParameter("title"));
			discountNumber.setDescr(request.getParameter("descr"));
			activityService.saveDiscountNumber(discountNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// session.setAttribute("id", activity.getId());
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
}
