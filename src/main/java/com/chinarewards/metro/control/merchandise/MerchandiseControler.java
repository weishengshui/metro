package com.chinarewards.metro.control.merchandise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
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

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.core.service.IFileItemService;
import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.merchandise.MerchandiseImageType;
import com.chinarewards.metro.domain.merchandise.MerchandiseSaleform;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.common.ImageInfo;
import com.chinarewards.metro.model.merchandise.CategoryVo;
import com.chinarewards.metro.model.merchandise.MerchandiseCriteria;
import com.chinarewards.metro.model.merchandise.MerchandiseVo;
import com.chinarewards.metro.model.merchandise.SaleFormVo;
import com.chinarewards.metro.service.merchandise.IMerchandiseService;
import com.chinarewards.metro.validator.merchandise.CreateMerchandiseValidator;

@Controller
@RequestMapping("/merchandise")
public class MerchandiseControler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IMerchandiseService merchandiseService;
	@Autowired
	IFileItemService fileItemService;

	/**
	 * 商品维护
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/show")
	public String showCreateMerchandise(HttpSession session, Model model,
			String id) {

		// prepare data
		Merchandise merchandise = merchandiseService
				.findMerchandiseId(id);
		
		Set<MerchandiseSaleform> saleforms = merchandise.getMerchandiseSaleforms();
		List<CategoryVo> categoryVos = merchandiseService
				.findCategorysByMerchandise(merchandise);

		String imageSessionName = UUIDUtil.generate();
		Map<String, MerchandiseFile> images = merchandiseService
				.findMerchandiseFIlesByMerchandise(merchandise);
		System.out.println("merchandise images size is " + images.size());
		
		session.setAttribute(imageSessionName, images);
		model.addAttribute("images", CommonUtil.toJson(images));
		model.addAttribute("imageSessionName", imageSessionName);

		model.addAttribute("merchandise", merchandise);
		model.addAttribute("saleforms", saleforms);
		model.addAttribute("categoryVos", CommonUtil.toJson(categoryVos));

		return "merchandise/show";
	}

	/**
	 * 新增商品
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model) {

		return "merchandise/create";
	}

	@RequestMapping("/maintain")
	public String maintain(Model model) {
		return "merchandise/show";
	}

	/**
	 * 商品类别与商品关系维护
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("merCate")
	public String merCate(Model model) {
		return "merchandise/merCate";
	}

	/**
	 * 创建商品
	 * 
	 * @param session
	 * @param request
	 * @param merchandise
	 * @param result
	 * @param mod
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/create")
	@ResponseBody
	public void createOrUpdateMerchandise(HttpServletResponse response,
			HttpSession session, String imageSessionName,
			@ModelAttribute Merchandise merchandise, BindingResult result,
			Model mod, String rmbUnitId, String binkeUnitId, Double rmbPrice,
			Double binkePrice, Boolean rmb, Boolean binke,
			Boolean rmbPreferential, Boolean binkePreferential,
			Double rmbPreferentialPrice, Double binkePreferentialPrice,
			String[] categId, String[] status, Long[] displaySort)
			throws IOException {

		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();

			System.out.println("enter CreateOrUpdateMerchandise()");
			if (null == merchandise) {
				merchandise = new Merchandise();
			}

			List<MerchandiseFile> files = new ArrayList<MerchandiseFile>();
			Map<String, MerchandiseFile> images = (Map<String, MerchandiseFile>) session
					.getAttribute(imageSessionName);
			if (null != images && images.size() > 0) {
				for (Map.Entry<String, MerchandiseFile> entry : images
						.entrySet()) {
					MerchandiseFile image = entry.getValue();
					if (image.getUrl().startsWith(
							Constants.UPLOAD_TEMP_UID_PREFIX)) { // 把临时文件移到正式目录
						image.setUrl(FileUtil.moveFile(
								Constants.MERCHANDISE_IMAGE_BUFFER,
								image.getUrl(), Constants.MERCHANDISE_IMAGE_DIR));
					}
					files.add(image);
				}
			}
			
			// merchandise category
			List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
			if (null != categId) {
				for (int i = 0, length = categId.length; i < length; i++) {
					System.out.println("categId [] is " + categId[i]);
					categoryVos.add(new CategoryVo(categId[i],
							MerchandiseStatus.fromString(status[i]),
							SystemTimeProvider.getCurrentTime(),
							displaySort[i], merchandise.getId()));
				}
			}
			System.out.println("categoryVos is " + categoryVos.toString());

			// merchandise sale forms
			List<SaleFormVo> saleFormVos = new ArrayList<SaleFormVo>(); // 兑换形式
			if (null != rmb) {// 正常售卖
				if (null == rmbPreferential) { // 没有优惠
					saleFormVos.add(new SaleFormVo(rmbUnitId, rmbPrice, null));
				} else {
					saleFormVos.add(new SaleFormVo(rmbUnitId, rmbPrice,
							rmbPreferentialPrice));
				}
			}
			if (null != binke) {// 积分兑换
				if (null == binkePreferential) {// 没有优惠
					saleFormVos.add(new SaleFormVo(binkeUnitId, binkePrice,
							null));
				} else {
					saleFormVos.add(new SaleFormVo(binkeUnitId, binkePrice,
							binkePreferentialPrice));
				}
			}

			new CreateMerchandiseValidator(merchandiseService).validate(
					merchandise, result);
			if (result.hasErrors()) {
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo(result
						.getAllErrors().get(0).getDefaultMessage())));
				out.flush();
				return;
			} else {
				System.out.println("merchandise.getId() is "
						+ merchandise.getId());
				if (merchandiseService.checkCodeExists(merchandise)) {
					out.println(CommonUtil.toJson(new AjaxResponseCommonVo(
							"商品编号\"" + merchandise.getCode() + "\"已存在")));
					out.flush();
					return;
				}
				if (merchandiseService.checkModelExists(merchandise)) {
					out.println(CommonUtil.toJson(new AjaxResponseCommonVo(
							"商品型号\"" + merchandise.getModel() + "\"已存在")));
					out.flush();
					return;
				}
				if (null != categoryVos && categoryVos.size() > 0) { // 检查类别排序在指定的类别中是否已经存在
					for (CategoryVo categoryVo2 : categoryVos) {
						if (merchandiseService
								.checkDisplaySortExists(categoryVo2)) {
							AjaxResponseCommonVo ajaxResponseCommonVo = new AjaxResponseCommonVo();
							ajaxResponseCommonVo.setCategoryId(categoryVo2
									.getCategoryId());
							out.println(CommonUtil.toJson(ajaxResponseCommonVo));
							out.flush();
							return;
						}
					}
				}
				if (null == merchandise.getId()
						|| merchandise.getId().isEmpty()) { // insert

					merchandiseService.createMerchandise(merchandise, files,
							saleFormVos, categoryVos);
					AjaxResponseCommonVo commonVo = new AjaxResponseCommonVo(
							"保存成功");
					commonVo.setId(merchandise.getId());
					out.println(CommonUtil.toJson(commonVo));
					out.flush();
					return;
				} else {// update
					// 判断兑换形式是否有效，比如去掉一中兑换形式，而该形式已在某商品类别下，此时这种兑换形式就不能去掉
					List<MerchandiseSaleform> saleForms = merchandiseService
							.findSaleFormsByMerchandise(merchandise);
					if (null != saleForms && saleForms.size() > 0) {
						for (MerchandiseSaleform saleform : saleForms) {
							boolean exists = false;
							for (SaleFormVo saleFormVo : saleFormVos) {
								if (saleFormVo.getUnitId().equals(
										saleform.getUnitId())) {
									exists = true;
									break;
								}
							}
							if (!exists) { // 不存在就删除这一兑换形式的商品
								if (!merchandiseService
										.deleteSaleForm(saleform)) {
									if (saleform.getUnitId().equals("0")) {
										out.println(CommonUtil
												.toJson(new AjaxResponseCommonVo(
														"\"正常售卖\"已经与商品类别相关联，不能去掉")));
									} else {
										out.println(CommonUtil
												.toJson(new AjaxResponseCommonVo(
														"\"积分兑换\"已经与商品类别相关联，不能去掉")));
									}
									out.flush();
									return;
								}
							}
						}
					}
					merchandiseService.updateMerchandise(merchandise, files,
							saleFormVos, categoryVos);
					AjaxResponseCommonVo commonVo = new AjaxResponseCommonVo(
							"保存成功");
					commonVo.setId(merchandise.getId());
					out.println(CommonUtil.toJson(commonVo));
					out.flush();
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ fileNotFoundException.getMessage())));
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ e.getMessage())));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
					+ e.getMessage())));
			out.flush();
		}
	}

	private MerchandiseFile saveMerchandiseFile(MultipartFile mfile)
			throws IOException {
		File uploadPath = new File(Constants.MERCHANDISE_IMAGE_DIR);// 商品图片上传正式目录
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		MerchandiseFile file = new MerchandiseFile();
		file.setFilesize(mfile.getSize());
		file.setOriginalFilename(mfile.getOriginalFilename());
		file.setMimeType(mfile.getContentType());
		String suffix = getSuffix(mfile.getOriginalFilename());
		String fileName = UUIDUtil.generate() + suffix;
		// item.write(new File(uploadPath, fileName)); // 移到正式目录
		InputStream input = mfile.getInputStream();
		byte[] data = IOUtils.toByteArray(input);
		IOUtils.write(data,
				new FileOutputStream(new File(uploadPath, fileName)));
		IOUtils.closeQuietly(input);
		// XXX
		// 重置大小之后要不要删除
		// item.delete(); // 删除临时文件

		file.setUrl(fileName);

		if (mfile.getName().equals("overview")) {
			file.setImageType(MerchandiseImageType.OVERVIEW);
			/*
			 * // TODO File newFile = new File(uploadPath, fileName + "335x335"
			 * + suffix); Thumbnails.of(new File(uploadPath, fileName)) //
			 * 重置图片大小 .size(335, 335).toFile(newFile);
			 * file.setUrl(Constants.MERCHANDISE_IMAGE_DIR + fileName +
			 * "335x335" + suffix);
			 */
		} else {
			file.setImageType(MerchandiseImageType.OTHERS);
			/*
			 * // TODO File newFile = new File(uploadPath, fileName + "146x146"
			 * + suffix); Thumbnails.of(new File(uploadPath, fileName)) //
			 * 重置图片大小 .size(146, 146).toFile(newFile);
			 * file.setUrl(Constants.MERCHANDISE_IMAGE_DIR + fileName +
			 * "146x146" + suffix);
			 */
		}
		return file;
	}

	@RequestMapping(value = "/list")
	public String listMerchandises(
			@ModelAttribute MerchandiseCriteria criteria, Integer page,
			Integer rows, Model model) {

		criteria.getName();
		logger.debug(
				"Entry list merchandise controller,That page is:{} criteria name is: {}",
				new Object[] { page, criteria.getName() });

		rows = (rows == null) ? Constants.PERPAGE_SIZE : rows;
		page = page == null ? 1 : page;

		Page paginationDetail = new Page();
		paginationDetail.setPage(page);
		paginationDetail.setRows(rows);
		criteria.setPaginationDetail(paginationDetail);

		List<MerchandiseVo> catalogs = merchandiseService
				.searchMerchandises(criteria);
		Long count = merchandiseService.countMerchandises(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", catalogs);
		return "merchandise/list";
	}

	/**
	 * 获取指定类别下的商品目录
	 */
	@RequestMapping("/getMerCatas")
	public String getMerCatas(@ModelAttribute MerchandiseCriteria criteria,
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

		List<MerchandiseCatalog> catalogs = merchandiseService
				.searchMerCatas(criteria);
		Long count = merchandiseService.countMerCatas(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", catalogs);
		return "merchandise/merCate";
	}

	/**
	 * 获取不是指定商品类别下的商品目录
	 * 
	 * @param criteria
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	@RequestMapping("/getNotMerCatas")
	public String getNotMerCatas(@ModelAttribute MerchandiseCriteria criteria,
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

		List<MerchandiseCatalog> catalogs = merchandiseService
				.searchNotMerCatas(criteria);
		Long count = merchandiseService.countNotMerCatas(criteria);

		model.addAttribute("total", count);
		model.addAttribute("page", page);

		model.addAttribute("rows", catalogs);
		return "merchandise/merCate";
	}

	/**
	 * 改变商品目录的上下架状态
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/changeCataStatus")
	@ResponseBody
	public AjaxResponseCommonVo changeCataStatus(String id, String status,
			Model model) {

		merchandiseService.changeCataStatus(id, status);
		return new AjaxResponseCommonVo("修改成功");
	}

	/**
	 * 商品类别与商品维护中的“修改商品目录信息”
	 * 
	 * @return
	 */
	@RequestMapping("/updateCatalog")
	@ResponseBody
	public void updateCatalog(HttpServletResponse response, String catalogId,
			String status, Long displaySort, Model model) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		MerchandiseCatalog catalog = new MerchandiseCatalog();
		catalog.setId(catalogId);
		catalog.setStatus(MerchandiseStatus.fromString(status));
		catalog.setDisplaySort(displaySort);
		System.out.println("catalog.getId() is " + catalog.getId());
		merchandiseService.updateCatalog(catalog);

		out.println(CommonUtil.toJson(new AjaxResponseCommonVo("修改成功")));
		out.flush();
	}

	/**
	 * 解除商品目录与类别的关系
	 * 
	 * @param catalogIds
	 * @return
	 */
	@RequestMapping("/removeCataFromCategory")
	@ResponseBody
	public AjaxResponseCommonVo removeCataFromCategory(String[] id) {

		merchandiseService.removeCataFromCategory(id);
		return new AjaxResponseCommonVo("删除成功");
	}

//	/**
//	 * 批量删除商品的某一兑换类型
//	 * 
//	 * @param id
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/delete")
//	@ResponseBody
//	public AjaxResponseCommonVo delete(String[] id, Model model) {
//		System.out.println(id);
//		if (null != id && id.length > 0) {
//			for (String cataId : id) {
//				String code = merchandiseService
//						.checkMerchCataHasCategory(cataId);
//				if (null != code) {
//					return new AjaxResponseCommonVo("编号为\"" + code
//							+ "\"的商品已经与商品类别关联，不能删除");
//				}
//			}
//			merchandiseService.batchDelete(id);
//			return new AjaxResponseCommonVo("删除成功");
//		} else {
//			return new AjaxResponseCommonVo("请先选择要删除的行");
//		}
//	}

	/**
	 * 判断指定商品是否在该类别下
	 * 
	 * @param merCode
	 * @param cateId
	 * @param model
	 * @return
	 */
	@RequestMapping("/loadMerByMerCode")
	@ResponseBody
	public Merchandise loadMerByMerCode(String merCode, String cateId,
			Model model) {

		Merchandise merchandise = merchandiseService.loadMerByMerCode(merCode,
				cateId);

		return merchandise;
	}

	@RequestMapping("/checkMerCodeExists")
	@ResponseBody
	public Merchandise checkMerCodeExists(String merCode, Model model) {

		Merchandise merchandise = merchandiseService
				.checkMerCodeExists(merCode);

		return merchandise;
	}

	private String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	@RequestMapping("/addCatalog")
	@ResponseBody
	public void addCatalog(HttpServletResponse response, String[] merCode,
			String[] status, Long[] sort, String cateId, Model model)
			throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		try {
			if (null != merCode && merCode.length > 0 && null != cateId
					&& !cateId.isEmpty()) {
				List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
				for (int i = 0, length = merCode.length; i < length; i++) {
					if (!merCode[i].isEmpty()) {
						categoryVos.add(new CategoryVo(merCode[i],
								MerchandiseStatus.fromString(status[i]),
								sort[i]));
					}
				}
				merchandiseService.addMerchandise(categoryVos, cateId);
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo("添加成功")));
				out.flush();
				return;
			}
			out.println(CommonUtil
					.toJson(new AjaxResponseCommonVo("请先输入要添加的商品")));
			out.flush();
			return;
		} catch (Exception e) {
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo(e
					.getMessage())));
			out.flush();
			return;
		}
	}

	@RequestMapping("/checkDisplaySortExists")
	@ResponseBody
	public String checkDisplaySortExists(String cateId, Long displaySort) {

		CategoryVo categoryVo = new CategoryVo(cateId, null, null, displaySort);
		if (merchandiseService.checkDisplaySortExists(categoryVo)) {
			return "{msg:" + true + "}";
		} else {
			return "{msg:" + false + "}";
		}
	}

	/**
	 * 将上传文件保存至临时文件夹
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public void imageUpload(
			@RequestParam(value = "overview", required = false) MultipartFile overview,
			@RequestParam(value = "others", required = false) MultipartFile others,
			String path, String key, HttpSession session,
			String imageSessionName, HttpServletResponse response) {

		MultipartFile file = null;
		boolean isOverview = false;
		if (null != overview) {
			file = overview;
			isOverview = true;
		} else if (null != others) {
			file = others;
		} else {
			return;
		}
		if (file.isEmpty()) {// 没有数据
			return;
		}

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
				Map<String, MerchandiseFile> images = (HashMap<String, MerchandiseFile>) session
						.getAttribute(imageSessionName);
				if (null == images) {
					images = new HashMap<String, MerchandiseFile>();
				} else {
					MerchandiseFile image = images.get(key);
					if (null != image) { // 删除之前的遗留图片
						if (image.getUrl().startsWith(
								Constants.UPLOAD_TEMP_UID_PREFIX)) {
							File tempFile = new File(path, image.getUrl());
							tempFile.delete();
						}
						images.remove(key);
					}
				}

				MerchandiseFile image = new MerchandiseFile();
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
				if (isOverview) {
					image.setImageType(MerchandiseImageType.OVERVIEW);
				} else {
					image.setImageType(MerchandiseImageType.OTHERS);
				}
				if (null == key || key.isEmpty()) {
					key = "A" + UUIDUtil.generate();
				}
				images.put(key, image);

				System.out.println("images size is " + images.size()
						+ " key is " + key + " imageSessionName is "
						+ imageSessionName);
				session.setAttribute(imageSessionName, images);

				out.write("{url:\'" + image.getUrl() + "\',width:\'"
						+ image.getWidth() + "\',height:\'" + image.getHeight()
						+ "\',imageSessionName:\'" + imageSessionName
						+ "\',contentType:\'" + image.getMimeType()
						+ "\',key:\'" + key + "\'}");
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

}
