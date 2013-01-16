package com.chinarewards.metro.control.brand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.brand.BrandUnionMember;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.model.brand.BrandCriteria;
import com.chinarewards.metro.model.brand.BrandUnionMemberCriteria;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.common.ImageInfo;
import com.chinarewards.metro.service.brand.IBrandService;
import com.chinarewards.metro.validator.brand.CreateBrandValidator;

@Controller
@RequestMapping("/brand")
public class BrandControler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IBrandService brandService;

	@RequestMapping("/show")
	public String show(HttpSession session, Model model, Integer id) {
		return "brand/show";
	}

	@RequestMapping("/edit")
	public String edit(HttpSession session, Model model, Integer id) {

		String imageSessionName = UUIDUtil.generate();
		if (null != id) {
			Brand brand = brandService.findBrandById(id);
			Map<String, FileItem> images = new HashMap<String, FileItem>();
			images.put("A"+UUIDUtil.generate(), brand.getLogo());
			session.setAttribute(imageSessionName, images);
			model.addAttribute("brand", brand);
			model.addAttribute("images", CommonUtil.toJson(images));
			model.addAttribute("imageSessionName", imageSessionName);
		}
		return "brand/edit";
	}

	@RequestMapping(value = "/list")
	public String listBrands(@ModelAttribute BrandCriteria criteria,
			Integer page, Integer rows, Model model) {

		logger.debug(
				"Entry list merchandise controller,That page is:{} criteria name is: {}",
				new Object[] { page, criteria.getName() });

		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;

		Page paginationDetail = new Page();
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);

		criteria.setPaginationDetail(paginationDetail);

		List<Brand> brands = brandService.searchBrands(criteria);
		Long count = brandService.countBrands(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", brands);
		return "brand/list";
	}

	@RequestMapping(value = "/listUnionMember")
	public String listUnionMember(
			@ModelAttribute BrandUnionMemberCriteria criteria, Integer page,
			Integer rows, Model model) {

		logger.debug(
				"Entry list merchandise controller,That page is:{} criteria name is: {}",
				new Object[] { page, criteria.getMemberName() });

		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;

		Page paginationDetail = new Page();
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);

		criteria.setPaginationDetail(paginationDetail);
		List<BrandUnionMember> brandUnionMembers;
		Long count;
		if (null == criteria.getBrandId()) {
			brandUnionMembers = new ArrayList<BrandUnionMember>();
			count = 0l;
		} else {
			brandUnionMembers = brandService.searchBrandUnionMembers(criteria);
			count = brandService.countBrandUnionMembers(criteria);
		}

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", brandUnionMembers);
		return "brand/edit";
	}

	/**
	 * 导出联合会员
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportUnionMember")
	public ModelAndView exportUnionMember(HttpServletResponse response,
			HttpServletRequest request,
			@ModelAttribute BrandUnionMemberCriteria criteria, Integer page,
			Integer rows, Model model) throws Exception {

		brandService.exportUnionMember(response, request, criteria);

		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/create")
	@ResponseBody
	public void createOrUpdateBrand(HttpSession session,
			String imageSessionName, HttpServletResponse response,
			@ModelAttribute Brand brand, BindingResult result, Model model) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();
			System.out.println("enter createOrUpdateBrand()");
			FileItem logo = null;
			Map<String, FileItem> images = (HashMap<String, FileItem>) session
					.getAttribute(imageSessionName);
			if (null != images && images.size() > 0) {
				for (Map.Entry<String, FileItem> image : images.entrySet()) {
					logo = image.getValue();
					break;
				}
			}
			if (null != logo
					&& logo.getUrl().startsWith(
							Constants.UPLOAD_TEMP_UID_PREFIX)) {
				logo.setUrl(FileUtil.moveFile(Constants.BRAND_IMAGE_BUFFER,
						logo.getUrl(), Constants.BRAND_IMAGE_DIR));
			}

			System.out.println("brand name is " + brand.getName());
			System.out.println("brand unionInvited is "
					+ brand.getUnionInvited());
			new CreateBrandValidator(brandService).validate(brand, result);
			if (result.hasErrors()) {
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo(result
						.getAllErrors().get(0).getDefaultMessage())));
			} else {
				if (null == brand.getId()) { // insert
					brandService.createBrand(brand, logo);
				} else {// update
					brandService.updateBrand(brand, logo);
				}
				AjaxResponseCommonVo ajaxResponseCommonVo = new AjaxResponseCommonVo();
				ajaxResponseCommonVo.setMsg("保存成功");
				ajaxResponseCommonVo.setId(brand.getId());
				out.println(CommonUtil.toJson(ajaxResponseCommonVo));
				out.flush();
			}
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ fileNotFoundException.getMessage())));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ e.getMessage())));
			out.flush();
		}
	}

	/**
	 * 将上传文件保存至临时文件夹
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public void imageUpload(@RequestParam MultipartFile file, String path,
			String key, HttpSession session, String imageSessionName,
			HttpServletResponse response) {

		path = Dictionary.getPicPath(path);
		if (!"".equals(path)) {
			FileUtil.pathExist(path);
			PrintWriter out = null;
			if (null == imageSessionName || imageSessionName.isEmpty()) {
				imageSessionName = UUIDUtil.generate();
			}
			try {
				response.setContentType("text/html; charset=utf-8");
				out = response.getWriter();
				String fileName = file.getOriginalFilename();
				String suffix = getSuffix(fileName);
				String fileNewName = Constants.UPLOAD_TEMP_UID_PREFIX
						+ UUIDUtil.generate() + suffix;
				FileUtil.saveFile(file.getInputStream(), path, fileNewName);
				Map<String, FileItem> images = (HashMap<String, FileItem>) session
						.getAttribute(imageSessionName);
				if (null == images) {
					images = new HashMap<String, FileItem>();
				} else {
					FileItem image = images.get(key);
					if (null != image) { // 删除之前的遗留图片
						if (image.getUrl().startsWith(
								Constants.UPLOAD_TEMP_UID_PREFIX)) {
							File tempFile = new File(path, image.getUrl());
							tempFile.delete();
						}
						images.remove(key);
					}
				}

				FileItem image = new FileItem();
				System.out.println("image is " + image);
				ImageInfo imageInfo = FileUtil.getImageInfo(path, fileNewName);
				image.setWidth(imageInfo.getWidth());
				image.setHeight(imageInfo.getHeight());
				image.setCreatedAt(SystemTimeProvider.getCurrentTime());
				image.setCreatedBy(UserContext.getUserId());
				image.setFilesize(file.getSize());
				image.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
				image.setLastModifiedBy(UserContext.getUserId());
				image.setMimeType(file.getContentType());
				image.setOriginalFilename(fileName);
				image.setUrl(fileNewName);
				if(null == key || key.isEmpty()){
					key = "A"+UUIDUtil.generate();
				}
				images.put(key, image);

				session.setAttribute(imageSessionName, images);

				out.write("{url:\'" + image.getUrl() + "\',width:\'"
						+ image.getWidth() + "\',height:\'" + image.getHeight()
						+ "\',imageSessionName:\'" + imageSessionName
						+ "\',contentType:\'" + image.getMimeType() + "\',key:\'"+key+"\'}");
				out.flush();
			} catch (FileNotFoundException e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			} catch (IOException e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			} catch (Exception e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			}
		}
	}

	/**
	 * 批量删除品牌
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponseCommonVo batchDeleteBrands(Integer[] ids) {

		if (null != ids && ids.length > 0) {
			for (Integer id : ids) {
				Brand brand = checkValidDelete(id);
				if (null != brand) {
					return new AjaxResponseCommonVo("品牌\"" + brand.getName()
							+ "\"下已经有联合会员，不能删除");
				}
			}
			Integer count = brandService.batchDeleteBrands(ids);
			if (count == ids.length) {
				return new AjaxResponseCommonVo("删除成功");
			} else {
				return new AjaxResponseCommonVo("删除出错");
			}
		} else {
			return new AjaxResponseCommonVo("请选择要删除的品牌");
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteImage")
	@ResponseBody
	public void deleteImage(HttpServletResponse response, HttpSession session,
			String imageSessionName, String key) throws Exception {

		Map<String, FileItem> images = (Map<String, FileItem>) session
				.getAttribute(imageSessionName);
		images.remove(key);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("{msg:\'删除成功\'}");
		out.flush();
	}

	public Brand checkValidDelete(Integer id) {

		return brandService.checkValidDelete(id);
	}

	// return such as ".jpg"
	private String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
