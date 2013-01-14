package com.chinarewards.metro.service.category;

import java.util.List;

import com.chinarewards.metro.domain.category.Category;

public interface ICategoryService {
	
	/**
	 * 更新类别
	 * 
	 * @param catagory
	 */
	void modifyCategory(Category catagory, String parentId);
	
	/**
	 * 添加一个新的类别
	 * 
	 * @param catagory
	 * @param id
	 */
	Category addCategory(Category catagory, String id);
	
	/**
	 * 获取子节点
	 * @param id
	 * @return
	 */
	List<Category> getChildsByParentId(String id);
	
	/**
	 * 获取子节点数
	 * 
	 * @param id
	 * @return
	 */
	Long countChildByParentId(String id);
	
	/**
	 * 根据id删除商品类别
	 * @param id
	 */
	void deleteCategoryById(String id);
	
	/**
	 * 查询所有可能的父节点，在修改父节点时用
	 * 
	 * @param id
	 * @return
	 */
	List<Category> getAllParents(String id);
	
	/**
	 * 添加类别时，检查类别名称是否已经存在
	 * 
	 * @param name
	 * @return
	 */
	boolean checkAddNameExists(String name);
	
	/**
	 * 更新类别时，检查类别名称是否存在
	 * 
	 * @param category
	 * @return
	 */
	boolean checkUpdateNameExists(Category category);
	
	/**
	 * 根据id查询商品类别下是否有商品
	 * 
	 * @param id
	 * @return
	 */
	boolean hasMerchandiseCatagoryById(String id);

}
