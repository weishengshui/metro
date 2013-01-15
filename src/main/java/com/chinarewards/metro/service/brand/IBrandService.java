package com.chinarewards.metro.service.brand;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.brand.BrandUnionMember;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.model.brand.BrandCriteria;
import com.chinarewards.metro.model.brand.BrandUnionMemberCriteria;

/**
 * 
 * @author weishengshui
 *
 */
public interface IBrandService {
	
	/**
	 * 创建品牌
	 * 
	 * @param brand
	 * @param logo
	 * @return
	 */
	Brand createBrand(Brand brand,
			FileItem logo);
	
	/**
	 * 更新品牌
	 * 
	 * @param brand
	 * @param logo
	 * @return
	 */
	Brand updateBrand(Brand brand,
			FileItem logo);
	
	/**
	 * 根据条件查询品牌
	 * 
	 * @param brandCriteria
	 * @return
	 */
	List<Brand> searchBrands(BrandCriteria brandCriteria);
	
	/**
	 * 根据条件查询品牌总数
	 * 
	 * @param brandCriteria
	 * @return
	 */
	Long countBrands(BrandCriteria brandCriteria);
	
	/**
	 * 根据id查询品牌信息
	 * 
	 * @param id
	 * @return
	 */
	Brand findBrandById(Integer id);
	
	/**
	 * 检查是否可以删除指定的品牌，如果品牌下有联合会员将返回null
	 * 
	 * @param id
	 * @return
	 */
	Brand checkValidDelete(Integer id);
	
	/**
	 * 批量删除品牌，并返回删除的行数
	 * 
	 * @param ids
	 * @return
	 */
	Integer batchDeleteBrands(Integer[] ids);
	
	/**
	 * 根据条件查询联合会员
	 * 
	 * @param criteria
	 * @return
	 */
	List<BrandUnionMember> searchBrandUnionMembers(
			BrandUnionMemberCriteria brandUnionMemberCriteria);
	
	/**
	 * 根据条件查询联合会员总数
	 * 
	 * @param criteria
	 * @return
	 */
	Long countBrandUnionMembers(BrandUnionMemberCriteria brandUnionMemberCriteria);
	
	/**
	 * 导出联合会员到EXCEL
	 * 
	 * @param response
	 * @param request
	 * @param criteria
	 */
	void exportUnionMember(HttpServletResponse response,
			HttpServletRequest request, BrandUnionMemberCriteria criteria) throws Exception;
	
	/**
	 * 检查品牌名称是否已存在
	 * 
	 * @param brand
	 * @return
	 */
	boolean checkNameExists(Brand brand);
	
	/**
	 * 检查公司名称是否已存在
	 * 
	 * @param brand
	 * @return
	 */
	boolean checkCompanyNameExists(Brand brand);
	
	/**
	 * 检查公司网址是否已存在
	 * 
	 * @param brand
	 * @return
	 */
	boolean checkCompanyWebSiteExists(Brand brand);
	
	/**
	 * 检查联系电话是否已存在
	 * 
	 * @param brand
	 * @return
	 */
	boolean checkPhoneNumberExists(Brand brand);

}
