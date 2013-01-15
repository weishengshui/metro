package com.chinarewards.metro.control.merchandise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.core.service.IFileItemService;
import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.merchandise.CatalogVo;
import com.chinarewards.metro.domain.merchandise.CategoryVo;
import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.merchandise.MerchandiseImageType;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.common.ImageInfo;
import com.chinarewards.metro.model.common.SelectVO;
import com.chinarewards.metro.model.merchandise.MerchandiseCriteria;
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
	 * 获得商品所有没有子类的类别列表
	 * 
	 * @param userContext
	 * @return
	 */
	public List<SelectVO> getMerchandiseCatagories() {

		List<SelectVO> list = new ArrayList<SelectVO>();

		List<Category> catagories = merchandiseService.getLeafs();
		if (null == catagories) {
			return (new ArrayList<SelectVO>());
		}
		for (Category ct : catagories) {
			list.add(new SelectVO(ct.getName(), ct.getId()));
		}

		return list;
	}

	/**
	 * 商品维护
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/show")
	public String showCreateMerchandise(Model model, String id) {

		// prepare data
		Merchandise merchandise = merchandiseService
				.findMerchandiseByMercCataId(id);
		List<MerchandiseCatalog> catalogs = merchandiseService
				.findMercCatalogsByMercIdAndNuit(merchandise.getId());
		List<CategoryVo> categoryVos = merchandiseService.findCategorysByMerchandise(merchandise);

		model.addAttribute("merchandise", merchandise);
		model.addAttribute("catalogs", catalogs);
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
	@RequestMapping("/create")
	@ResponseBody
	public void createOrUpdateMerchandise(
			@RequestParam("overview") MultipartFile overview,
			@RequestParam("others") MultipartFile[] others,
			HttpServletResponse response,
			@ModelAttribute Merchandise merchandise, BindingResult result,
			Model mod, String rmbUnitId, String binkeUnitId, Double rmbPrice,
			Double binkePrice, Boolean rmb, Boolean binke, String[] categId,
			String[] status, Long[] displaySort) throws IOException {

		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();

			System.out.println("enter CreateOrUpdateMerchandise()");
			if (null == merchandise) {
				merchandise = new Merchandise();
			}

			new CreateMerchandiseValidator(merchandiseService).validate(
					merchandise, result);

			List<MerchandiseFile> files = new ArrayList<MerchandiseFile>();
			List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
			if (null != categId) {
				for (int i = 0, length = categId.length; i < length; i++) {
					categoryVos.add(new CategoryVo(categId[i], merchandise.getCode(),
							MerchandiseStatus.fromString(status[i]),
							displaySort[i]));
				}
			}

			System.out.println("categoryVos is " + categoryVos.toString());
			List<CatalogVo> catalogVos = new ArrayList<CatalogVo>(); // 兑换形式
			if (null != rmb) {
				catalogVos.add(new CatalogVo(rmbUnitId, rmbPrice));// 正常售卖
			}
			if (null != binke) {
				catalogVos.add(new CatalogVo(binkeUnitId, binkePrice)); // 积分兑换
			}

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
				if (!overview.isEmpty()) {
					files.add(saveMerchandiseFile(overview));
				}
				if (null != others && others.length > 0) {
					for (MultipartFile other : others) {
						if (!other.isEmpty()) {
							files.add(saveMerchandiseFile(other));
						}
					}
				}
				if (null == merchandise.getId()
						|| merchandise.getId().isEmpty()) { // insert

					merchandiseService.createMerchandise(merchandise, files,
							catalogVos, categoryVos);
					out.println(CommonUtil.toJson(new AjaxResponseCommonVo(
							"添加成功")));
					out.flush();
					return;
				} else {// update
					// 判断兑换形式是否有效，比如去掉一中兑换形式，而该形式已在某商品类别下，此时这种兑换形式就不能去掉
					List<MerchandiseCatalog> catalogsList = merchandiseService
							.findCatalogByMerId(merchandise.getId());
					if (null != catalogsList && catalogsList.size() > 0) {
						for (MerchandiseCatalog catalog : catalogsList) {
							boolean exists = false;
							for (CatalogVo catalogVo : catalogVos) {
								if (catalogVo.getUnitId().equals(
										catalog.getUnitId())) {
									exists = true;
									break;
								}
							}
							if (!exists) { // 不存在就删除这一兑换形式的商品
								if (!merchandiseService
										.deleteMer_cataByCatalog(catalog)) {
									if (catalog.getUnitId().equals("0")) {
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
							catalogVos, categoryVos);
					out.println(CommonUtil.toJson(new AjaxResponseCommonVo(
							"修改成功")));
					out.flush();
					return;
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

		List<MerchandiseCatalog> catalogs = merchandiseService
				.searchMercCatagorys(criteria);
		Long count = merchandiseService.countMercCatagorys(criteria);

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

	/**
	 * 批量删除商品的某一兑换类型
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public AjaxResponseCommonVo delete(String[] id, Model model) {
		System.out.println(id);
		if (null != id && id.length > 0) {
			for (String cataId : id) {
				String code = merchandiseService
						.checkMerchCataHasCategory(cataId);
				if (null != code) {
					return new AjaxResponseCommonVo("编号为\"" + code
							+ "\"的商品已经与商品类别关联，不能删除");
				}
			}
			merchandiseService.batchDelete(id);
			return new AjaxResponseCommonVo("删除成功");
		} else {
			return new AjaxResponseCommonVo("请先选择要删除的行");
		}
	}

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
				merchandiseService.addCatalog(categoryVos, cateId);
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
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public void imageUpload(MultipartFile mFile,
			HttpSession session, String imageSessionName,
			HttpServletResponse response) {

		FileUtil.pathExist(Constants.MERCHANDISE_IMAGE_BUFFER);
		PrintWriter out = null;
		if (null == imageSessionName || imageSessionName.isEmpty()) {
			imageSessionName = UUIDUtil.generate();
		}
		try {
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();
			String fileName = mFile.getOriginalFilename();
			String suffix = getSuffix(fileName);
			String fileNewName = Constants.UPLOAD_TEMP_UID_PREFIX
					+ UUIDUtil.generate() + suffix;
			FileUtil.saveFile(mFile.getInputStream(),
					Constants.BRAND_IMAGE_BUFFER, fileNewName);
			FileItem logo = (FileItem) session.getAttribute(imageSessionName);
			if (null != logo) {// 删除之前的遗留图片
				if (logo.getUrl().startsWith(Constants.UPLOAD_TEMP_UID_PREFIX)) {
					File file = new File(Constants.BRAND_IMAGE_BUFFER,
							logo.getUrl());
					file.delete();
				}
			}

			logo = new FileItem();
			System.out.println("logo is " + logo);
			ImageInfo imageInfo = FileUtil.getImageInfo(
					Constants.BRAND_IMAGE_BUFFER, fileNewName);
			logo.setWidth(imageInfo.getWidth());
			logo.setHeight(imageInfo.getHeight());
			logo.setCreatedAt(SystemTimeProvider.getCurrentTime());
			logo.setCreatedBy(UserContext.getUserId());
			logo.setFilesize(mFile.getSize());
			logo.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
			logo.setLastModifiedBy(UserContext.getUserId());
			logo.setMimeType(mFile.getContentType());
			logo.setOriginalFilename(fileName);
			logo.setUrl(fileNewName);

			session.setAttribute(imageSessionName, logo);

			out.write("{url:\'" + logo.getUrl() + "\',width:\'"
					+ logo.getWidth() + "\',height:\'" + logo.getHeight()
					+ "\',imageSessionName:\'" + imageSessionName
					+ "\',contentType:\'" + logo.getMimeType() + "\'}");
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
