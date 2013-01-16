package com.chinarewards.metro.service.brand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.brand.BrandUnionMember;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.model.brand.BrandCriteria;
import com.chinarewards.metro.model.brand.BrandUnionMemberCriteria;
import com.chinarewards.metro.model.brand.UnionMemberVo;

/**
 * 
 * @author weishengshui
 * 
 */

@Service
public class BrandService implements IBrandService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Override
	public Brand createBrand(Brand brand, FileItem logo) {

		if (null != logo) {
			hbDaoSupport.save(logo);
		}

		brand.setCreatedAt(SystemTimeProvider.getCurrentTime());
		brand.setCreatedBy(UserContext.getUserId());
		brand.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		brand.setLastModifiedBy(UserContext.getUserId());
		brand.setLogo(logo);

		hbDaoSupport.save(brand);
		return brand;
	}

	@Override
	public Brand updateBrand(Brand brand, FileItem logo) {
		// TODO
		Brand brandFromDb = hbDaoSupport.findTById(Brand.class, brand.getId());
		FileItem oldLogo = brandFromDb.getLogo();
		if (null != logo && null == logo.getId()) {
			hbDaoSupport.save(logo);
			brandFromDb.setLogo(logo);
		}else if(null == logo){
			brandFromDb.setLogo(null);// delete, no file
		}
		brandFromDb.setCompanyName(brand.getCompanyName());
		brandFromDb.setCompanyWebSite(brand.getCompanyWebSite());
		brandFromDb.setContact(brand.getContact());
		brandFromDb.setDescription(brand.getDescription());
		brandFromDb.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		brandFromDb.setLastModifiedBy(UserContext.getUserId());
		brandFromDb.setName(brand.getName());
		brandFromDb.setPhoneNumber(brand.getPhoneNumber());
		brandFromDb.setUnionInvited(brand.getUnionInvited());
		hbDaoSupport.update(brandFromDb);

		if ((null == logo) || (null != oldLogo && null != logo && null == logo.getId() && !oldLogo.getId().equals(logo.getId()))) {// delete old file
			(new File(Constants.BRAND_IMAGE_DIR, oldLogo.getUrl())).delete(); //物理删除文件
			hbDaoSupport.delete(oldLogo);
		}

		return brandFromDb;
	}

	@Override
	public List<Brand> searchBrands(BrandCriteria brandCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchBrandsHQL(brandCriteria, params, false);
		List<Brand> list = hbDaoSupport.executeQuery(hql, params,
				brandCriteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countBrands(BrandCriteria brandCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchBrandsHQL(brandCriteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0l;
	}

	protected String buildSearchBrandsHQL(BrandCriteria brandCriteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT(b) ");
		} else {
			strBuffer.append("SELECT b ");
		}

		strBuffer.append("FROM Brand b WHERE 1=1 "); // 很奇妙

		if (brandCriteria != null) {
			String name = brandCriteria.getName();
			String companyName = brandCriteria.getCompanyName();
			Date start = brandCriteria.getCreateStart();
			Date end = brandCriteria.getCreateEnd();
			if (null != end) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String str = dateFormat.format(end);
				str = str.substring(0, 10) + " 23:59:59";
				try {
					end = dateFormat.parse(str);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			String unionInvited = brandCriteria.getUnionInvited();

			if (null != name && !name.isEmpty()) {
				strBuffer.append(" AND b.name like :name");
				params.put("name", "%" + name + "%");
			}
			if (null != companyName && !companyName.isEmpty()) {
				strBuffer.append(" AND b.companyName like :companyName");
				params.put("companyName", "%" + companyName + "%");
			}
			if (null != start) {
				strBuffer.append(" AND b.createdAt >= :start");
				params.put("start", start);
			}
			if (null != end) {
				strBuffer.append(" AND b.createdAt <= :end");
				params.put("end", end);
			}
			if (null != unionInvited && !unionInvited.isEmpty()) {
				if (unionInvited.equals("ON")) {
					strBuffer.append(" AND b.unionInvited = :unionInvited");
					params.put("unionInvited", true);
				} else if (unionInvited.equals("OFF")) {
					strBuffer.append(" AND b.unionInvited = :unionInvited");
					params.put("unionInvited", false);
				}
			}
			// TODO
		}
		return strBuffer.toString();
	}

	@Override
	public Brand findBrandById(Integer id) {

		return hbDaoSupport.findTById(Brand.class, id);
	}

	@Override
	public Brand checkValidDelete(Integer id) {

		Brand brand = hbDaoSupport.findTById(Brand.class, id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brand", brand);
		List<Long> list = hbDaoSupport
				.executeQuery(
						"SELECT COUNT(bum) FROM BrandUnionMember bum WHERE bum.brand=:brand",
						params, null);
		if (null != list && list.size() > 0) {
			if (list.get(0) == 0) {
				return null;
			} else {
				return brand;
			}
		}
		return null;
	}

	@Override
	public Integer batchDeleteBrands(Integer[] ids) {

		Integer count = 0;
		if (null != ids && ids.length > 0) {
			for (Integer id : ids) {
				hbDaoSupport.executeHQL("DELETE Brand b WHERE b.id=?", id);
				count++;
			}
		}
		return count;
	}

	protected String buildSearchBrandUnionMembersHQL(
			BrandUnionMemberCriteria brandUnionMemberCriteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT(b) ");
		} else {
			strBuffer.append("SELECT b ");
		}

		strBuffer
				.append("FROM BrandUnionMember b INNER JOIN b.member m WHERE 1=1 "); // 很奇妙

		if (brandUnionMemberCriteria != null) {
			Integer id = brandUnionMemberCriteria.getBrandId();
			String memberName = brandUnionMemberCriteria.getMemberName();
			String cardNumber = brandUnionMemberCriteria.getCardNumber();
			Date joinedStart = brandUnionMemberCriteria.getJoinedStart();
			Date joinedEnd = brandUnionMemberCriteria.getJoinedEnd();
			if (null != joinedEnd) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String str = dateFormat.format(joinedEnd);
				str = str.substring(0, 10) + " 23:59:59";
				try {
					joinedEnd = dateFormat.parse(str);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (null != id) { // 进入修改页面时，id为空
				strBuffer.append(" AND b.brand.id=:id");
				params.put("id", id);
			}
			if (null != memberName && !memberName.isEmpty()) {
				strBuffer.append(" AND m.name like :memberName");
				params.put("memberName", "%" + memberName + "%");
			}
			if (null != cardNumber && !cardNumber.isEmpty()) {
				strBuffer.append(" AND m.card.cardNumber like :cardNumber");
				params.put("cardNumber", "%" + cardNumber + "%");
			}
			if (null != joinedStart) {
				strBuffer.append(" AND b.joinedDate >= :joinedStart");
				params.put("joinedStart", joinedStart);
			}
			if (null != joinedEnd) {
				strBuffer.append(" AND b.joinedDate <= :joinedEnd");
				params.put("joinedEnd", joinedEnd);
			}

			// TODO
		}
		return strBuffer.toString();
	}

	@Override
	public List<BrandUnionMember> searchBrandUnionMembers(
			BrandUnionMemberCriteria brandUnionMemberCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchBrandUnionMembersHQL(brandUnionMemberCriteria,
				params, false);
		List<BrandUnionMember> list = hbDaoSupport.executeQuery(hql, params,
				brandUnionMemberCriteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countBrandUnionMembers(
			BrandUnionMemberCriteria brandUnionMemberCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchBrandUnionMembersHQL(brandUnionMemberCriteria,
				params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0l;
	}

	@Override
	public void exportUnionMember(HttpServletResponse response,
			HttpServletRequest request, BrandUnionMemberCriteria criteria)
			throws Exception {
		/**
		 * prepare data
		 */
		Integer count = new Integer(countBrandUnionMembers(criteria).toString());

		// workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		cellStyle.setFont(font);
		int maxSheetRows = SpreadsheetVersion.EXCEL97.getLastRowIndex();
		int mod = count % maxSheetRows;
		int index = (mod == 0) ? (count / maxSheetRows)
				: (count / maxSheetRows + 1);

		Page paginationDetail = new Page();
		// 一次从数据库读取 65535 条数据
		paginationDetail.setRows(maxSheetRows - 1);
		criteria.setPaginationDetail(paginationDetail);
		if (index > 0) {
			for (int i = 0; i < index; i++) {
				paginationDetail.setPage(i + 1);
				List<UnionMemberVo> list = searchExportUnionMember(criteria);

				Sheet sheet = workbook.createSheet("第" + (i + 1) + "页");
				fillNewSheet(sheet, list, cellStyle);
			}
		} else {
			Sheet sheet = workbook.createSheet("第" + 1 + "页");
			fillNewSheet(sheet, null, cellStyle);
		}

		/**
		 * write to response.getOutputStream()
		 * 
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = "联合会员"
				+ dateFormat.format(SystemTimeProvider.getCurrentTime())
				+ ".xls";
		File dir = new File(Constants.EXPORT_UNIONMEMBER_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileName);
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		fos.close();

		InputStream input = FileUtils.openInputStream(file);
		byte[] data = IOUtils.toByteArray(input);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "ISO8859-1"));
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
		IOUtils.closeQuietly(input);
	}

	private void fillNewSheet(Sheet sheet, List<UnionMemberVo> list,
			HSSFCellStyle cellStyle) {

		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 7000);
		String[] title = new String[] { "会员名称", "会员卡号", "加入时间" };
		Row row = sheet.createRow(0);
		Cell cell;
		for (int i = 0, length = title.length; i < length; i++) {

			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(cellStyle);
		}

		// just for test
		/*
		 * row = sheet.createRow(1);
		 * 
		 * cell = row.createCell(0); cell.setCellValue("张三");
		 * cell.setCellStyle(cellStyle);
		 * 
		 * cell = row.createCell(1); cell.setCellValue("1234568741");
		 * cell.setCellStyle(cellStyle);
		 * 
		 * cell = row.createCell(2); cell.setCellValue(dateFormat.format(new
		 * Date())); cell.setCellStyle(cellStyle);
		 */

		if (null != list && list.size() > 0) {
			int j = 1;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			for (UnionMemberVo unionMemberVo : list) {
				row = sheet.createRow(j);

				cell = row.createCell(0);
				cell.setCellValue(unionMemberVo.getMemberName());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(1);
				cell.setCellValue(unionMemberVo.getCardNumber());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(2);
				cell.setCellValue(dateFormat.format(unionMemberVo.getJoinDate()));
				cell.setCellStyle(cellStyle);

				j++;
			}
		}
	}

	public List<UnionMemberVo> searchExportUnionMember(
			BrandUnionMemberCriteria criteria) {

		Integer brandId = criteria.getBrandId();
		String memberName = criteria.getMemberName();
		String cardNumber = criteria.getCardNumber();
		Date joinedStart = criteria.getJoinedStart();
		Date joinedEnd = criteria.getJoinedEnd();
		if (null != brandId) {
			if (null != joinedEnd) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String str = dateFormat.format(joinedEnd);
				str = str.substring(0, 10) + " 23:59:59";
				try {
					joinedEnd = dateFormat.parse(str);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			StringBuffer strBuffer = new StringBuffer();
			strBuffer
					.append("SELECT new com.chinarewards.metro.domain.brand.UnionMemberVo(m.name, m.card.cardNumber, b.joinedDate) FROM BrandUnionMember b INNER JOIN b.member m WHERE 1=1 ");
			Map<String, Object> params = new HashMap<String, Object>();
			strBuffer.append(" AND b.brand.id=:id");
			params.put("id", brandId);
			if (null != memberName && !memberName.isEmpty()) {
				strBuffer.append(" AND m.name like :memberName");
				params.put("memberName", "%" + memberName + "%");
			}
			if (null != cardNumber && !cardNumber.isEmpty()) {
				strBuffer.append(" AND m.card.cardNumber like :cardNumber");
				params.put("cardNumber", "%" + cardNumber + "%");
			}
			if (null != joinedStart) {
				strBuffer.append(" AND b.joinedDate >= :joinedStart");
				params.put("joinedStart", joinedStart);
			}
			if (null != joinedEnd) {
				strBuffer.append(" AND b.joinedDate <= :joinedEnd");
				params.put("joinedEnd", joinedEnd);
			}

			List<UnionMemberVo> list = hbDaoSupport.executeQuery(
					strBuffer.toString(), params,
					criteria.getPaginationDetail());
			return list;
		} else {
			return new ArrayList<UnionMemberVo>();
		}
	}

	@Override
	public boolean checkNameExists(Brand brand) {

		Brand brand2 = hbDaoSupport.findTByHQL(" FROM Brand WHERE name=?",
				brand.getName());
		if (null == brand2) {
			return false;
		}
		if (brand2.getId().equals(brand.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkCompanyNameExists(Brand brand) {
		Brand brand2 = hbDaoSupport.findTByHQL(
				" FROM Brand WHERE companyName=?", brand.getCompanyName());
		if (null == brand2) {
			return false;
		}
		if (brand2.getId().equals(brand.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkCompanyWebSiteExists(Brand brand) {

		Brand brand2 = hbDaoSupport
				.findTByHQL(" FROM Brand WHERE companyWebSite=?",
						brand.getCompanyWebSite());
		if (null == brand2) {
			return false;
		}
		if (brand2.getId().equals(brand.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkPhoneNumberExists(Brand brand) {

		Brand brand2 = hbDaoSupport.findTByHQL(
				" FROM Brand WHERE phoneNumber=?", brand.getPhoneNumber());
		if (null == brand2) {
			return false;
		}
		if (brand2.getId().equals(brand.getId())) {
			return false;
		}
		return true;
	}

}
