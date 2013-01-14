package com.chinarewards.metro.service.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.category.CategoryConstants;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Override
	public void modifyCategory(Category category, String parentId) {

		// String parentId = catagory.getParent().getId();
		String id = category.getId();
		if (null == parentId || parentId.isEmpty()) {
			throw new IllegalArgumentException(
					"updateCategory parentId could't be null!");
		}
		if (null == id || id.isEmpty()) {
			throw new IllegalArgumentException(
					"updateCatagory id could't be null!");
		}
		// find parent
		Category newParent = hbDaoSupport.findTById(Category.class, parentId);

		Category categoryFromDb = hbDaoSupport.findTById(Category.class, id);
		categoryFromDb.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		categoryFromDb.setLastModifiedBy(UserContext.getUserId());
		categoryFromDb.setName(category.getName());
		categoryFromDb.setDisplaySort(category.getDisplaySort());
		// categoryFromDb.setParent(parent);
		Category nowParent = categoryFromDb.getParent();
		if (!newParent.getId().equals(nowParent.getId())) { // 商品类别更换一个父节点
															// update rgt,
			// lft
			// 相当于先在nowParent下删除当前类别，然后在newParent下新增当前类别，参照delete, add 方法
			// first delete:update lft and rgt.
			/*
			 * Map<String, Object> params = new HashMap<String, Object>(); //
			 * params.put("parentRgt", parent.getRgt());
			 * params.put("currentRgt", categoryFromDb.getRgt()); hbDaoSupport
			 * .executeHQL(
			 * "UPDATE Category m SET m.rgt = m.rgt-2 WHERE m.rgt > :currentRgt"
			 * , params); params = new HashMap<String, Object>();
			 * params.put("currentLft", categoryFromDb.getLft()); hbDaoSupport
			 * .executeHQL(
			 * "UPDATE Category m SET m.lft = m.lft-2 WHERE m.lft > :currentLft"
			 * , params);
			 * 
			 * // second add: update lft and rgt. Long parentRgt =
			 * newParent.getRgt(); params = new HashMap<String, Object>();
			 * params.put("parentRgt", parentRgt); hbDaoSupport .executeHQL(
			 * "UPDATE Category m SET m.rgt = m.rgt+2 WHERE m.rgt >= :parentRgt"
			 * , params); params = new HashMap<String, Object>();
			 * params.put("currentLft", parentRgt); hbDaoSupport .executeHQL(
			 * "UPDATE Category m SET m.lft = m.lft+2 WHERE m.lft > :currentLft"
			 * , params);
			 * 
			 * categoryFromDb.setLft(parentRgt); categoryFromDb.setRgt(parentRgt
			 * + 1);
			 */
			categoryFromDb.setParent(newParent);
		}

		hbDaoSupport.update(categoryFromDb);

	}

	@Override
	public Category addCategory(Category category, String id) {

		if (null == id || id.isEmpty()) {
			throw new IllegalArgumentException(
					"addCatagory parentId could't be null!");
		}

		// find parent
		Category parent = hbDaoSupport.findTById(Category.class, id);
		// long parentRgt = parent.getRgt();

		// update lft and rgt.
		/*
		 * Map<String, Object> params = new HashMap<String, Object>();
		 * params.put("parentRgt", parentRgt); hbDaoSupport .executeHQL(
		 * "UPDATE Category m SET m.rgt = m.rgt+2 WHERE m.rgt >= :parentRgt",
		 * params); params = new HashMap<String, Object>();
		 * params.put("currentLft", parentRgt); hbDaoSupport .executeHQL(
		 * "UPDATE Category m SET m.lft = m.lft+2 WHERE m.lft > :currentLft",
		 * params);
		 */

		category.setParent(parent);
		category.setCreatedAt(SystemTimeProvider.getCurrentTime());
		category.setCreatedBy(UserContext.getUserId());
		category.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		category.setLastModifiedBy(UserContext.getUserId());
		/*
		 * category.setLft(parentRgt); category.setRgt(parentRgt + 1);
		 */
		hbDaoSupport.save(category);

		return category;
	}

	@Override
	public List<Category> getChildsByParentId(String id) {

		if (null == id || id.isEmpty()) {
			List<Category> list = hbDaoSupport.findTsByHQL(
					"SELECT m FROM Category m WHERE m.id = ?",
					CategoryConstants.MERCHANDISE_CATEGORY_ROOT_ID);
			if (null != list && list.size() == 0) {
				Category category = new Category();
				category.setCreatedAt(SystemTimeProvider.getCurrentTime());
				category.setCreatedBy(UserContext.getUserId());
				category.setDescription("商品类别根节点");
				category.setId(CategoryConstants.MERCHANDISE_CATEGORY_ROOT_ID);
				category.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
				category.setLastModifiedBy(UserContext.getUserId());
				category.setLft(1);
				category.setName("根节点");
				category.setRgt(2);
				hbDaoSupport.save(category);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("rootId",
						CategoryConstants.MERCHANDISE_CATEGORY_ROOT_ID);
				map.put("id", category.getId());
				hbDaoSupport.executeHQL(
						"UPDATE Category SET id=:rootId WHERE id=:id", map);
				list = hbDaoSupport.findTsByHQL(
						"SELECT m FROM Category m WHERE m.id = ?",
						CategoryConstants.MERCHANDISE_CATEGORY_ROOT_ID);
			}
			return list;
		} else {
			List<Category> list = hbDaoSupport
					.findTsByHQL(
							"SELECT m FROM Category m WHERE m.parent.id = ? ORDER BY m.displaySort ASC",
							id);
			return list;
		}
	}

	@Override
	public Long countChildByParentId(String id) {
		List<Long> list = hbDaoSupport.findTsByHQL(
				"SELECT COUNT(m) FROM Category m WHERE m.parent.id = ?", id);
		return list.get(0);
	}

	@Override
	public void deleteCategoryById(String id) {

		Category category = hbDaoSupport.findTById(Category.class, id);
		Category parent = category.getParent();
		if (parent == null) { // catagory 是商品类别树的根
			return;
		}// 当删除的数据的表与其它的表有关联时，用一下语句删除
		hbDaoSupport.executeHQL("DELETE Category c where c.id=?", id);

		// update lft and rgt.
		/*
		 * Map<String, Object> params = new HashMap<String, Object>(); //
		 * params.put("parentRgt", parent.getRgt()); params.put("currentRgt",
		 * category.getRgt()); hbDaoSupport .executeHQL(
		 * "UPDATE Category m SET m.rgt = m.rgt-2 WHERE m.rgt > :currentRgt",
		 * params); params = new HashMap<String, Object>();
		 * params.put("currentLft", category.getLft()); hbDaoSupport
		 * .executeHQL(
		 * "UPDATE Category m SET m.lft = m.lft-2 WHERE m.lft > :currentLft",
		 * params);
		 */

	}

	@Override
	public List<Category> getAllParents(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkAddNameExists(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		List<Category> list = hbDaoSupport.executeQuery(
				"FROM Category WHERE name=:name", params, null);
		if (null != list && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkUpdateNameExists(Category category) {
		Category categoryFromDB = hbDaoSupport.findTById(Category.class,
				category.getId());
		if (null != categoryFromDB) {
			if (category.getName().equals(categoryFromDB.getName())) {
				return false;
			} else {
				return checkAddNameExists(category.getName());
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean hasMerchandiseCatagoryById(String id) {
		Category category = hbDaoSupport.findTById(Category.class, id);
		if (null != category) {
			List<MerchandiseCatalog> list = hbDaoSupport.findTsByHQL(
					"FROM MerchandiseCatalog m WHERE m.category=?", category);
			if (null != list && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
