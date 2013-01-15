package com.chinarewards.metro.control.brand;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.imgscalr.Scalr;
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
		session.removeAttribute("brand_image");
		return "brand/show";
	}

	@RequestMapping("/edit")
	public String edit(Model model, Integer id) {

		if (null != id) {
			Brand brand = brandService.findBrandById(id);
			model.addAttribute("brand", brand);
		} else {
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

	@RequestMapping("/create")
	@ResponseBody
	public void createOrUpdateBrand(HttpSession session,
			HttpServletResponse response, @ModelAttribute Brand brand,
			BindingResult result, Model model) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();
			System.out.println("enter createOrUpdateBrand()");

			FileItem logo = (FileItem) session.getAttribute("brand_image");
			session.removeAttribute("brand_image");
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
	 * 
	 */
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public void imageUpload(@RequestParam("logo2") MultipartFile mFile,
			HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();
			String fileName = mFile.getOriginalFilename();
			String suffix = getSuffix(fileName);
			String fileNewName = UUIDUtil.generate() + suffix;
			FileUtil.saveFile(mFile.getInputStream(),
					Constants.BRAND_IMAGE_DIR, fileNewName);
			FileItem logo = (FileItem) session.getAttribute("brand_image");
			if (null == logo) {
				logo = new FileItem();
			}
			logo.setCreatedAt(SystemTimeProvider.getCurrentTime());
			logo.setCreatedBy(UserContext.getUserId());
			logo.setFilesize(mFile.getSize());
			logo.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
			logo.setLastModifiedBy(UserContext.getUserId());
			logo.setMimeType(mFile.getContentType());
			logo.setOriginalFilename(fileName);
			logo.setUrl(fileNewName);
			session.setAttribute("brand_image", logo);

			// out.write(CommonUtil.toJson(new
			// AjaxResponseCommonVo(fileNewName)));
			out.write(CommonUtil.toJson(logo));
			out.flush();
		} catch (FileNotFoundException e) {
			out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ e.getMessage())));
			out.flush();
		} catch (IOException e) {
			out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ e.getMessage())));
			out.flush();
		}
	}

	/**
	 * 获取展示图片
	 * 
	 * @param mFile
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/imageShow")
	public void shopPicShow(String fileName, HttpServletResponse response)
			throws Exception {
		File file = new File(Constants.BRAND_IMAGE_DIR + fileName);
		response.setContentType("image/png");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition",
				"inline; filename=\"" + file.getName() + "\"");
		byte[] bbuf = new byte[1024];
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		ServletOutputStream op = response.getOutputStream();
		while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
			op.write(bbuf, 0, bytes);
		}
		in.close();
		op.flush();
		op.close();
	}

	/**
	 * 获取展示的缩略图
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/showGetthumbPic")
	public void showGetthumbPic(String fileName, HttpServletResponse response)
			throws Exception {
		File file = new File(Constants.BRAND_IMAGE_DIR + fileName);
		if (file.exists()) {
			String mimetype = "image/png";
			if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")
					|| mimetype.endsWith("gif")) {
				BufferedImage im = ImageIO.read(file);
				if (im != null) {
					BufferedImage thumb = Scalr.resize(im, 75);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					if (mimetype.endsWith("png")) {
						ImageIO.write(thumb, "PNG", os);
						response.setContentType("image/png");
					} else if (mimetype.endsWith("jpeg")) {
						ImageIO.write(thumb, "jpg", os);
						response.setContentType("image/jpeg");
					} else {
						ImageIO.write(thumb, "GIF", os);
						response.setContentType("image/gif");
					}
					ServletOutputStream srvos = response.getOutputStream();
					response.setContentLength(os.size());
					response.setHeader("Content-Disposition",
							"inline; filename=\"" + file.getName() + "\"");
					os.writeTo(srvos);
					srvos.flush();
					srvos.close();
				}
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

	public Brand checkValidDelete(Integer id) {

		return brandService.checkValidDelete(id);
	}

	// return such as ".jpg"
	private String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
