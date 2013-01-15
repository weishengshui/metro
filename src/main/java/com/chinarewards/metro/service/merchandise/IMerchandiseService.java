package com.chinarewards.metro.service.merchandise;

import java.util.List;

import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.merchandise.CatalogVo;
import com.chinarewards.metro.domain.merchandise.CategoryVo;
import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.model.merchandise.MerchandiseCriteria;

public interface IMerchandiseService {

	/**
	 * 根据条件查询商品
	 * 
	 * @param merchandiseCriteria
	 * @return
	 */
	List<MerchandiseCatalog> searchMercCatagorys(MerchandiseCriteria merchandiseCriteria);

	/**
	 * 根据条件查询商品总数
	 * 
	 * @param merchandiseCriteria
	 * @return
	 */
	Long countMercCatagorys(MerchandiseCriteria merchandiseCriteria);

	/**
	 * 获得商品所有没有子类的类别列表
	 * 
	 * @param userInfo
	 * @return
	 */
	List<Category> getLeafs();


	/**
	 * 根据商品id查询商品目录
	 * 
	 * @param id
	 * @return
	 */
	List<MerchandiseCatalog> findMercCatalogsByMercId(String id);
	
	/**
	 * 通过商品图片的Id删除商品图片，一次只能删除一张
	 * 
	 * @param id
	 */
	void deleteMercFileById(String id);
	
	/**
	 * 创建商品
	 * 
	 * @param merchandise
	 * 			商品基本信息
	 * @param files
	 * 			商品图片信息
	 * @param catalogVos
	 * 			商品售卖形式
	 * @param categoryVos
	 * 			商品类别信息
	 */
	void createMerchandise(Merchandise merchandise,
			List<MerchandiseFile> files, List<CatalogVo> catalogVos,
			List<CategoryVo> categoryVos);
	
	/**
	 * 检查商品code是否存在
	 * 
	 * @param merchandise
	 * @return
	 */
	boolean checkCodeExists(Merchandise merchandise);
	
	/**
	 * 检查商品model是否存在
	 * 
	 * @param merchandise
	 * @return
	 */
	boolean checkModelExists(Merchandise merchandise);
	
	/**
	 * 判断商品是否已经有类别，有类别就返回该商品的编号，否则返回null
	 * 
	 * @param cataId
	 * @return
	 */
	String checkMerchCataHasCategory(String cataId);
	
	/**
	 * 批量删除商品
	 * 
	 * @param ids
	 */
	void batchDelete(String[] ids);
	
	/**
	 * 根据商品目录ID查询商品目录
	 * 
	 * @param id
	 * @return
	 */
	MerchandiseCatalog findMercCatalogsByMercCataId(String id);
	
	/**
	 * 根据商品目录id查询商品
	 * 
	 * @param id
	 * @return
	 */
	Merchandise findMerchandiseByMercCataId(String id);
	
	/**
	 * 根据商品id查询不同兑换类型的商品目录
	 * 
	 * @param id
	 * @return
	 */
	List<MerchandiseCatalog> findMercCatalogsByMercIdAndNuit(String id);
	
	/**
	 * 更新商品
	 * 
	 * @param merchandise
	 * @param files
	 * @param catalogVos
	 * @param categoryVos
	 */
	void updateMerchandise(Merchandise merchandise,
			List<MerchandiseFile> files, List<CatalogVo> catalogVos,
			List<CategoryVo> categoryVos);
	
	/**
	 * 查询指定类别的商品目录
	 * 
	 * @param criteria
	 * @return
	 */
	List<MerchandiseCatalog> searchMerCatas(MerchandiseCriteria criteria);
	
	/**
	 * 查询指定类别的商品目录总数
	 * 
	 * @param criteria
	 * @return
	 */
	Long countMerCatas(MerchandiseCriteria criteria);
	
	/**
	 * 改变商品目录的上下架状态
	 * 
	 * @param merchandiseCatalogId
	 */
	void changeCataStatus(String merchandiseCatalogId, String status);
	
	/**
	 * 修改商品目录的上下架状态及排序值
	 * 
	 * @param catalog
	 */
	void updateCatalog(MerchandiseCatalog catalog);

	/**
	 * 解除商品目录与类别的关系
	 * 
	 * @param catalogIds
	 */
	void removeCataFromCategory(String[] catalogIds);
	
	/**
	 * 查询不是指定商品类别下的商品目录
	 * 
	 * @param criteria
	 * @return
	 */
	List<MerchandiseCatalog> searchNotMerCatas(MerchandiseCriteria criteria);
	
	/**
	 * 查询不是指定商品类别下的商品目录总数
	 * 
	 * @param criteria
	 * @return
	 */
	Long countNotMerCatas(MerchandiseCriteria criteria);
	
	/**
	 * 判断指定商品是否在该类别下
	 * 
	 * @param merCode
	 * @param cateId
	 * @return
	 */
	Merchandise loadMerByMerCode(String merCode, String cateId);
	
	/**
	 * 判断商品编号是否存在
	 * 
	 * @param merCode
	 * @return
	 */
	Merchandise checkMerCodeExists(String merCode);
	
	/**
	 * 为指定的类别添加商品
	 * 
	 * @param categoryVos
	 * @param cateId
	 */
	void addCatalog(List<CategoryVo> categoryVos, String cateId);
	
	/**
	 * 获取某一商品的所有兑换形式
	 * 
	 * @param id
	 * @return
	 */
	List<MerchandiseCatalog> findCatalogByMerId(String id);
	
	/**
	 * 删除商品的某一兑换形式
	 * 
	 * @param catalog
	 * @return
	 */
	boolean deleteMer_cataByCatalog(MerchandiseCatalog catalog);
	/**
	 * 检查指定类别中，商品排序号是否存在
	 * 
	 * @param categoryVo2
	 * @return
	 */
	boolean checkDisplaySortExists(CategoryVo categoryVo2);
	
	/**
	 * 查询商品属于哪些类别
	 * 
	 * @param id
	 * @return
	 */
	List<CategoryVo> findCategorysByMerchandise(Merchandise merchandise);
	
}
