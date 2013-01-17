package com.chinarewards.metro.service.merchandise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.merchandise.CatalogVo;
import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.merchandise.MerchandiseSaleform;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;
import com.chinarewards.metro.model.merchandise.CategoryVo;
import com.chinarewards.metro.model.merchandise.MerchandiseCriteria;
import com.chinarewards.metro.model.merchandise.SaleFormVo;

@Service
public class MerchandiseService implements IMerchandiseService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Override
	public List<MerchandiseCatalog> searchMercCatagorys(
			MerchandiseCriteria merchandiseCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMerchandisesHQL(merchandiseCriteria, params,
				false);
		List<MerchandiseCatalog> list = hbDaoSupport.executeQuery(hql, params,
				merchandiseCriteria.getPaginationDetail());
		return list;
	}

	@Override
	public Long countMercCatagorys(MerchandiseCriteria merchandiseCriteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMerchandisesHQL(merchandiseCriteria, params,
				true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0l;
	}

	@Override
	public List<Category> getLeafs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MerchandiseCatalog> findMercCatalogsByMercId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMercFileById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		MerchandiseFile file = hbDaoSupport.executeQuery(
				"FROM MerchandiseFile WHERE id=:id", map, null);
		if (null != file) {
			// FileItem fileItem = file.getFileItem();
			// new File(fileItem.getUrl()).delete(); //物理删除
		}
		hbDaoSupport
				.executeHQL("DELETE FROM MerchandiseFile WHERE id=:id", map); // 删除数据库记录
	}

	@Override
	public void createMerchandise(Merchandise merchandise,
			List<MerchandiseFile> files, List<SaleFormVo> saleFormVos,
			List<CategoryVo> categoryVos) {
		merchandise.setCreatedAt(SystemTimeProvider.getCurrentTime());
		merchandise.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		merchandise.setCreatedBy(UserContext.getUserId());
		merchandise.setLastModifiedBy(UserContext.getUserId());
		hbDaoSupport.save(merchandise);

		// sale merchandise files
		if (null != files && files.size() > 0) {
			for (MerchandiseFile file : files) {
				file.setMerchandise(merchandise);
				hbDaoSupport.save(file);
			}
		}

		// save merchandise sale forms
		if (null != saleFormVos && saleFormVos.size() > 0) {
			for (SaleFormVo saleFormVo : saleFormVos) {
				MerchandiseSaleform merchandiseSaleform = new MerchandiseSaleform();

				merchandiseSaleform.setCreatedAt(SystemTimeProvider
						.getCurrentTime());
				merchandiseSaleform.setCreatedBy(UserContext.getUserId());
				merchandiseSaleform.setLastModifiedAt(SystemTimeProvider
						.getCurrentTime());
				merchandiseSaleform.setLastModifiedBy(UserContext.getUserId());
				merchandiseSaleform.setMerchandise(merchandise);
				merchandiseSaleform.setPreferentialPrice(saleFormVo
						.getPreferentialPrice());
				merchandiseSaleform.setPrice(saleFormVo.getPrice());
				merchandiseSaleform.setUnitId(saleFormVo.getUnitId());

				hbDaoSupport.save(merchandiseSaleform);
			}
		}

		// save merchandise catalogs
		if (null != categoryVos && categoryVos.size() > 0) {
			for (CategoryVo categoryVo : categoryVos) {
				Category category = hbDaoSupport.findTById(Category.class,
						categoryVo.getCategoryId());
				if (null != category) { // 为安全起见
					MerchandiseCatalog catalog = new MerchandiseCatalog();

					catalog.setCategory(category);
					catalog.setCreatedAt(SystemTimeProvider.getCurrentTime());
					catalog.setCreatedBy(UserContext.getUserId());
					catalog.setDisplaySort(categoryVo.getDisplaySort());
					catalog.setLastModifiedAt(SystemTimeProvider
							.getCurrentTime());
					catalog.setLastModifiedBy(UserContext.getUserId());
					catalog.setMerchandise(merchandise);
					catalog.setOn_offTIme(categoryVo.getOn_offTime());
					catalog.setStatus(categoryVo.getStatus());

					hbDaoSupport.save(catalog);
				}
			}
		}
	}

	@Override
	public boolean checkCodeExists(Merchandise merchandise) {

		Merchandise merchandise2 = hbDaoSupport.findTByHQL(
				"FROM Merchandise WHERE code=?", merchandise.getCode());
		if (null == merchandise2) {
			return false;
		}
		if (merchandise2.getId().equals(merchandise.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkModelExists(Merchandise merchandise) {

		Merchandise merchandise2 = hbDaoSupport.findTByHQL(
				"FROM Merchandise WHERE model=?", merchandise.getModel());
		if (null == merchandise2) {
			return false;
		}
		if (merchandise2.getId().equals(merchandise.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkDisplaySortExists(CategoryVo categoryVo) {

		String categoryId = categoryVo.getCategoryId();
		Long displaySort = categoryVo.getDisplaySort();
		String merchandiseId = categoryVo.getMerchandiseId();

		MerchandiseCatalog catalog = hbDaoSupport
				.findTByHQL(
						"FROM MerchandiseCatalog m WHERE m.category.id=? AND m.displaySort=?  ",
						categoryId, displaySort);
		if (null == catalog) {
			return false;
		}
		if (catalog.getMerchandise().getId().equals(merchandiseId)) {
			return false;
		}
		return true;
	}

	protected String buildSearchMerchandisesHQL(
			MerchandiseCriteria merchandiseCriteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT(m) ");
		} else {
			strBuffer.append("SELECT m ");
		}

		strBuffer
				.append("FROM MerchandiseCatalog m WHERE 1=1 AND m.category IS null "); // 很奇妙

		if (merchandiseCriteria != null) {
			String name = merchandiseCriteria.getName();
			String code = merchandiseCriteria.getCode();
			String unitId = merchandiseCriteria.getUnitId();
			String model = merchandiseCriteria.getModel();

			if (null != name && !name.isEmpty()) {
				strBuffer.append(" AND m.merchandise.name like :name");
				params.put("name", "%" + name + "%");
			}
			if (null != code && !code.isEmpty()) {
				strBuffer.append(" AND m.merchandise.code like :code");
				params.put("code", "%" + code + "%");
			}
			if (null != model && !model.isEmpty()) {
				strBuffer.append(" AND m.merchandise.model like :model");
				params.put("model", "%" + model + "%");
			}
			if (null != unitId && !unitId.isEmpty()) {
				strBuffer.append(" AND m.unitId=:unitId");
				params.put("unitId", unitId);
			}
			// TODO
		}
		// strBuffer.append(" GROUP BY m.merchandise.code, m.unitId");
		return strBuffer.toString();
	}

	@Override
	public void batchDelete(String[] ids) {
		for (String id : ids) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			Merchandise merchandise = hbDaoSupport.findTById(Merchandise.class,
					id);

			hbDaoSupport.executeHQL(
					"DELETE MerchandiseSaleform m WHERE m.merchandise=? ",
					merchandise);
			hbDaoSupport.executeHQL(
					"DELETE MerchandiseFile m WHERE m.merchandise=? ",
					merchandise);
			hbDaoSupport.delete(merchandise);

			/*
			 * if (merchandise.getMerchandiseCatalogs() != null &&
			 * merchandise.getMerchandiseCatalogs().size() == 0) { map = new
			 * HashMap<String, Object>(); map.put("id", merchandise.getId());
			 * List<MerchandiseFile> files = hbDaoSupport.executeQuery(
			 * "FROM MerchandiseFile m WHERE m.merchandise.id=:id", map, null);
			 * if (null != files && files.size() > 0) { // 先删除商品对应的图片 // TODO
			 * 物理删除图片 // 删除数据库记录 hbDaoSupport.deleteAll(files); }
			 * hbDaoSupport.executeHQL("DELETE Merchandise WHERE id=:id", map);
			 * }
			 */

		}

	}

	@Override
	public MerchandiseCatalog findMercCatalogsByMercCataId(String id) {
		return hbDaoSupport.findTById(MerchandiseCatalog.class, id);
	}

	@Override
	public Merchandise findMerchandiseByMercCataId(String id) {
		MerchandiseCatalog catalog = findMercCatalogsByMercCataId(id);
		if (null != catalog) {
			return catalog.getMerchandise();
		}
		return null;
	}

	@Override
	public List<MerchandiseCatalog> findMercCatalogsByMercIdAndNuit(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return hbDaoSupport
				.executeQuery(
						"FROM MerchandiseCatalog m WHERE m.merchandise.id=:id GROUP BY m.unitId",
						map, null);
	}

	@Override
	public void updateMerchandise(Merchandise merchandise,
			List<MerchandiseFile> files, List<SaleFormVo> saleFormVos,
			List<CategoryVo> categoryVos) {

		Merchandise merchandiseFromDB = hbDaoSupport.findTById(
				Merchandise.class, merchandise.getId());
		merchandiseFromDB.setName(merchandise.getName());
		merchandiseFromDB.setCode(merchandise.getCode());
		merchandiseFromDB.setPurchasePrice(merchandise.getPurchasePrice());
		merchandiseFromDB.setModel(merchandise.getModel());
		merchandiseFromDB.setDescription(merchandise.getDescription());
		merchandiseFromDB
				.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
		merchandiseFromDB.setLastModifiedBy(UserContext.getUserId());
		hbDaoSupport.update(merchandiseFromDB);

		// delete old merchandise files
		hbDaoSupport.executeHQL(
				"DELETE MerchandiseFile m WHERE m.merchandise=?",
				merchandiseFromDB);

		// save files
		if (null != files && files.size() > 0) {
			for (MerchandiseFile file : files) {
				file.setMerchandise(merchandiseFromDB);
				hbDaoSupport.save(file);
			}
		}

		// delete merchandise sale form
		hbDaoSupport.executeHQL(
				"DELETE MerchandiseSaleform WHERE merchandise=?",
				merchandiseFromDB);
		// save merchandise sale from
		if (null != saleFormVos && saleFormVos.size() > 0) {
			for (SaleFormVo saleFormVo : saleFormVos) {
				MerchandiseSaleform saleform = new MerchandiseSaleform();

				saleform.setCreatedAt(SystemTimeProvider.getCurrentTime());
				saleform.setCreatedBy(UserContext.getUserId());
				saleform.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
				saleform.setLastModifiedBy(UserContext.getUserId());
				saleform.setMerchandise(merchandiseFromDB);
				saleform.setPreferentialPrice(saleFormVo.getPreferentialPrice());
				saleform.setPrice(saleFormVo.getPrice());
				saleform.setUnitId(saleFormVo.getUnitId());

				hbDaoSupport.save(saleform);
			}
		}

		// delete catalogs
		hbDaoSupport.executeHQL(
				"DELETE MerchandiseCatalog m WHERE m.merchandise=? ",
				merchandiseFromDB);
		// save catalogs
		if (null != categoryVos && categoryVos.size() > 0) {
			for (CategoryVo categoryVo : categoryVos) {
				Category category = hbDaoSupport.findTById(Category.class,
						categoryVo.getCategoryId());
				if (null != category) { // 为安全起见
					MerchandiseCatalog catalog = new MerchandiseCatalog();

					catalog.setCategory(category);
					catalog.setCreatedAt(SystemTimeProvider.getCurrentTime());
					catalog.setCreatedBy(UserContext.getUserId());
					catalog.setDisplaySort(categoryVo.getDisplaySort());
					catalog.setLastModifiedAt(SystemTimeProvider
							.getCurrentTime());
					catalog.setLastModifiedBy(UserContext.getUserId());
					catalog.setMerchandise(merchandiseFromDB);
					catalog.setOn_offTIme(categoryVo.getOn_offTime());
					catalog.setStatus(categoryVo.getStatus());

					hbDaoSupport.save(catalog);
				}
			}
		}
	}

	@Override
	public List<MerchandiseCatalog> searchMerCatas(MerchandiseCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMerCatasHQL(criteria, params, false);
		List<MerchandiseCatalog> list = hbDaoSupport.executeQuery(hql, params,
				criteria.getPaginationDetail());
		return list;

	}

	@Override
	public Long countMerCatas(MerchandiseCriteria criteria) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = buildSearchMerCatasHQL(criteria, params, true);

		List<Long> list = hbDaoSupport.executeQuery(hql, params, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0l;

	}

	/**
	 * 构建查询指定类别下商品目录的HQL
	 * 
	 * @param merchandiseCriteria
	 * @param params
	 * @param isCount
	 * @return
	 */
	protected String buildSearchMerCatasHQL(
			MerchandiseCriteria merchandiseCriteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT( m  ) ");
		} else {
			strBuffer.append("SELECT m ");
		}

		strBuffer.append("FROM MerchandiseCatalog m WHERE 1=1 "); // 很奇妙

		if (merchandiseCriteria != null) {

			String categoryId = merchandiseCriteria.getCategoryId();
			if (null == categoryId) {
				categoryId = "";
			}
			strBuffer.append(" AND m.category.id=:categoryId");
			params.put("categoryId", categoryId);

			String name = merchandiseCriteria.getName();
			String code = merchandiseCriteria.getCode();

			if (null != name && !name.isEmpty()) {
				strBuffer.append(" AND m.merchandise.name like :name");
				params.put("name", "%" + name + "%");
			}
			if (null != code && !code.isEmpty()) {
				strBuffer.append(" AND m.merchandise.code like :code");
				params.put("code", "%" + code + "%");
			}
			// TODO
			strBuffer.append(" GROUP BY m.merchandise ");
			strBuffer.append(" ORDER BY m.displaySort ASC");
		}
		return strBuffer.toString();
	}

	/**
	 * 构建查询不是指定类别下商品目录的HQL
	 * 
	 * @param merchandiseCriteria
	 * @param params
	 * @param isCount
	 * @return
	 */
	protected String buildSearchNotMerCatasHQL(
			MerchandiseCriteria merchandiseCriteria,
			Map<String, Object> params, boolean isCount) {

		StringBuffer strBuffer = new StringBuffer();
		if (isCount) {
			strBuffer.append("SELECT COUNT(m) ");
		} else {
			strBuffer.append("SELECT m ");
		}

		String categoryId = merchandiseCriteria.getCategoryId();
		if (null == categoryId) {
			categoryId = "";
		}
		params.put("categoryId", categoryId);
		strBuffer
				.append("FROM MerchandiseCatalog m WHERE 1=1 AND m.category.id!=:categoryId"); // 很奇妙

		if (merchandiseCriteria != null) {
			String name = merchandiseCriteria.getName();
			String code = merchandiseCriteria.getCode();

			if (null != name && !name.isEmpty()) {
				strBuffer.append(" AND m.merchandise.name like :name");
				params.put("name", "%" + name + "%");
			}
			if (null != code && !code.isEmpty()) {
				strBuffer.append(" AND m.merchandise.code like :code");
				params.put("code", "%" + code + "%");
			}
			// TODO
		}
		strBuffer.append(" GROUP BY m.merchandise.id, m.unitId");
		return strBuffer.toString();
	}

	@Override
	public void changeCataStatus(String merchandiseCatalogId, String status) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", merchandiseCatalogId);
		map.put("status", MerchandiseStatus.fromString(status));
		hbDaoSupport
				.executeHQL(
						"UPDATE MerchandiseCatalog m SET m.status=:status WHERE m.id=:id ",
						map);

	}

	@Override
	public void updateCatalog(MerchandiseCatalog catalog) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", catalog.getId());
		map.put("status", catalog.getStatus());
		map.put("displaySort", catalog.getDisplaySort());
		hbDaoSupport
				.executeHQL(
						"UPDATE MerchandiseCatalog m SET m.status=:status, m.displaySort=:displaySort WHERE m.id=:id ",
						map);
	}

	@Override
	public void removeCataFromCategory(String[] catalogIds) {

		for (String id : catalogIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			hbDaoSupport.executeHQL(
					"DELETE MerchandiseCatalog m WHERE m.id=:id ", map);
		}
	}

	@Override
	public List<MerchandiseCatalog> searchNotMerCatas(
			MerchandiseCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countNotMerCatas(MerchandiseCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Merchandise loadMerByMerCode(String merCode, String cateId) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cateId", cateId);
		map.put("merCode", merCode);
		List<Merchandise> list = hbDaoSupport
				.executeQuery(
						"SELECT m.merchandise FROM MerchandiseCatalog m WHERE m.merchandise.code=:merCode AND m.category.id=:cateId",
						map, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Merchandise checkMerCodeExists(String merCode) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("merCode", merCode);

		List<Merchandise> list = hbDaoSupport
				.executeQuery(
						"SELECT m.merchandise FROM MerchandiseCatalog m WHERE m.merchandise.code=:merCode ",
						map, null);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addMerchandise(List<CategoryVo> categoryVos, String cateId) {

		Category category = hbDaoSupport.findTById(Category.class, cateId);
		if (null != categoryVos && categoryVos.size() > 0 && null != category) {
			for (CategoryVo categoryVo : categoryVos) {
				Merchandise merchandise = hbDaoSupport.findTById(Merchandise.class, categoryVo.getMerchandiseId());
				if( null != merchandise){
					MerchandiseCatalog catalog = new MerchandiseCatalog();
					
					catalog.setCategory(category);
					catalog.setCreatedAt(SystemTimeProvider.getCurrentTime());
					catalog.setCreatedBy(UserContext.getUserId());
					catalog.setDisplaySort(categoryVo.getDisplaySort());
					catalog.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
					catalog.setLastModifiedBy(UserContext.getUserId());
					catalog.setMerchandise(merchandise);
					catalog.setOn_offTIme(categoryVo.getOn_offTime());
					catalog.setStatus(categoryVo.getStatus());
					
					hbDaoSupport.save(catalog);
				}
			}
		}
	}

	@Override
	public List<MerchandiseCatalog> findCatalogByMerId(String id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<MerchandiseCatalog> list = hbDaoSupport
				.executeQuery(
						"FROM MerchandiseCatalog m WHERE m.merchandise.id=:id AND m.category IS null",
						params, null);
		return list;
	}

	@Override
	public boolean deleteMerchandiseSaleform(MerchandiseSaleform merchandiseSaleform) {
		
		List<MerchandiseCatalog> list = hbDaoSupport.findTsByHQL("FROM MerchandiseCatalog WHERE merchandise=?", merchandiseSaleform.getMerchandise());
		if(null != list && list.size() > 0){
			return false;
		}
		hbDaoSupport.delete(merchandiseSaleform);
		return true;
	}

	@Override
	public List<CategoryVo> findCategorysByMerchandise(Merchandise merchandise) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchandise", merchandise);
		List<CategoryVo> list = hbDaoSupport
				.executeQuery(
						"SELECT new com.chinarewards.metro.domain.merchandise.CategoryVo(m.category as category, m.status as status, m.displaySort as displaySort) FROM MerchandiseCatalog m WHERE m.category IS NOT null AND m.merchandise=:merchandise GROUP BY m.category",
						params, null);
		if (null != list && list.size() > 0) {
			for (CategoryVo vo : list) {
				Category category = vo.getCategory();
				String fullName = getCategoryFullName(category);
				vo.setFullName(fullName);
			}
		}
		return list;
	}

	protected String getCategoryFullName(Category category) {
		String fullName = "";
		while (category.getParent() != null) {
			fullName = category.getName() + "/" + fullName;
			category = category.getParent();
		}
		if (fullName.length() > 0) {
			fullName = fullName.substring(0, fullName.length() - 1);
		}
		return fullName;
	}

	@Override
	public Map<String, MerchandiseFile> findMerchandiseFIlesByMerchandise(
			Merchandise merchandise) {

		List<MerchandiseFile> list = hbDaoSupport.findTsByHQL(
				"FROM MerchandiseFile WHERE merchandise=?", merchandise);
		Map<String, MerchandiseFile> files = new HashMap<String, MerchandiseFile>();
		if (null != list && list.size() > 0) {
			for (MerchandiseFile file : list) {
				files.put("A" + UUIDUtil.generate(), file);
			}
		}
		return files;
	}

	@Override
	public List<MerchandiseSaleform> findSaleFormsByMerchandise(
			Merchandise merchandise) {

		List<MerchandiseSaleform> list = hbDaoSupport.findTsByHQL(
				"FROM MerchandiseSaleform WHERE merchandise=?", merchandise);
		return list;
	}

	@Override
	public boolean deleteSaleForm(MerchandiseSaleform saleform) {

		List<MerchandiseCatalog> list = hbDaoSupport.findTsByHQL(
				"FROM MerchandiseCatalog WHERE merchandise=?",
				saleform.getMerchandise());
		if (null != list && list.size() > 0) {// 商品已经与商品类别关联了，不能删除？
			return false;
		}
		hbDaoSupport.delete(saleform);
		return true;
	}
}
