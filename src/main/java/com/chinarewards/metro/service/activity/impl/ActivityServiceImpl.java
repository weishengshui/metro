package com.chinarewards.metro.service.activity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.activity.ActivityInfo;
import com.chinarewards.metro.domain.activity.BrandActivity;
import com.chinarewards.metro.domain.activity.BrandMode;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.pos.PosBind;
import com.chinarewards.metro.domain.shop.DiscountNumber;
import com.chinarewards.metro.service.activity.IActivityService;

@Service
public class ActivityServiceImpl implements IActivityService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Override
	public ActivityInfo saveActivity(ActivityInfo activity) {
		FileItem image = activity.getImage();
		if(null != image){
			hbDaoSupport.save(image);
		}
		activity = hbDaoSupport.save(activity);
		return activity;
	}

	@Override
	public void updateActivity(ActivityInfo activity) {
		FileItem image = activity.getImage();
		if(null != image){
			if(StringUtils.isEmpty(image.getId())){
				hbDaoSupport.save(image);
			}
		}
		hbDaoSupport.update(activity);
		/*String hql = "update ActivityInfo set activityName = ? ,startDate = ?,endDate = ?,description = ?,hoster = ?,activityNet = ?,contacts = ?,conTel = ?,image = ? where id = ?";
		hbDaoSupport.executeHQL(hql, activity.getActivityName(),
				activity.getStartDate(), activity.getEndDate(),
				activity.getDescription(), activity.getHoster(),
				activity.getActivityNet(), activity.getContacts(),
				activity.getConTel(), activity.getImage(), activity.getId());*/
	}

	@Override
	public List<ActivityInfo> findActivity(String activityName,String startDate,String endDate, Page page) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT a FROM ActivityInfo a WHERE 1=1 ");
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(activityName)){
				hql.append(" AND a.activityName LIKE :activityName");
				params.put("activityName", activityName);
			}
			
			if (StringUtils.isNotEmpty(startDate)) {
				hql.append(" AND a.startDate >= :startDate");
				params.put("startDate", sdf.parse(startDate));
			}
			if (StringUtils.isNotEmpty(endDate)) {
				hql.append(" AND a.endDate <= :endDate");
				params.put("endDate", sdf.parse(endDate));
			}
			hql.append(" AND a.tag != :tag");
			params.put("tag", -1);
			List<ActivityInfo> list = hbDaoSupport.executeQuery(hql.toString(), params, page);
			/*DetachedCriteria criteria = DetachedCriteria.forClass(ActivityInfo.class);
			if (StringUtils.isNotEmpty(activityName)) {
				criteria.add(Restrictions.like("activityName", activityName,MatchMode.ANYWHERE));
			}
			
			if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
				criteria.add(Restrictions.between("startDate", sdf.parse(startDate), sdf.parse(endDate)));
			}else if (StringUtils.isNotEmpty(startDate)) {
				criteria.add(Restrictions.between("startDate", sdf.parse(startDate), sdf.parse(startDate)));
			}else if (StringUtils.isNotEmpty(endDate)) {
				criteria.add(Restrictions.between("endDate", sdf.parse(endDate), sdf.parse(endDate)));
			}
			criteria.add(Restrictions.not(Expression.eq("tag",-1)));
			List<ActivityInfo> list = hbDaoSupport.findPageByCriteria(page, criteria);*/
			return list;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ActivityInfo findActivityById(String id) {
		return hbDaoSupport.findTById(ActivityInfo.class, Integer.valueOf(id));
	}

	@Override
	public List<BrandMode> findBrandAct(Brand brand, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from BrandActivity b where 1=1");
		if (StringUtils.isNotEmpty(brand.getName())) {
			hql.append(" and b.brand.name like :name");
			map.put("name", "%"+brand.getName()+"%");
		}
		
		List<BrandMode> bms = new LinkedList<BrandMode>();

		List<BrandActivity> list = hbDaoSupport.findTsByHQLPage(hql.toString(),
				map, page);
		for (BrandActivity bm : list) {
			BrandMode b = new BrandMode(bm.getGid(), String.valueOf(bm
					.getBrand().getId()), bm.getActivityInfo().getId() + "", bm
					.getBrand().getName(), bm.getBrand().getCompanyName(),
					bm.getJoinTime());

			bms.add(b);
			System.out.println("##debug:   " + b.getName() + "companyname: "
					+ b.getCompanyName());
		}
		return bms;
	}

	@Override
	public List<Brand> findBrandNotBandAct(Brand brand, Page page,String id) {

		String sql = "from BrandActivity a where a.activityInfo.id = ?" ;
		List<BrandActivity> bat = new ArrayList<BrandActivity>();
		if(id != null){
			bat = hbDaoSupport.findTsByHQL(sql, Integer.valueOf(id));
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		//SELECT b FROM Brand b WHERE NOT EXISTS (SELECT a.gid FROM BrandActivity a WHERE a.brand.id = b.id)
		hql.append("FROM Brand b WHERE 1=1");
		
		if(bat != null && bat.size() > 0){
			hql.append(" and b.id not in (");
			StringBuffer temphql = new StringBuffer();
			for(BrandActivity ba : bat){
				temphql.append(ba.getBrand().getId());
				temphql.append(",");
			}
			temphql.deleteCharAt(temphql.lastIndexOf(","));
			hql.append(temphql);
			hql.append(")");
		}
		
		if (StringUtils.isNotEmpty(brand.getName())) {
			hql.append(" and b.name like :name");
			map.put("name", "%"+brand.getName()+"%");
		}

		if (StringUtils.isNotEmpty(brand.getCompanyName())) {
			hql.append(" and b.companyName like :companyName");
			map.put("companyName", "%"+brand.getCompanyName()+"%");
		}

		List<Brand> list = hbDaoSupport.findTsByHQLPage(hql.toString(), map, page);
		return list;
	}

	@Override
	public void deleteActAndBranByIds(String[] actIds) {
		String hql = "delete from BrandActivity where gid = ? " ;
		if (null != actIds && actIds.length > 0) {
			for (String id : actIds) {
				hbDaoSupport.executeHQL(hql, Integer.valueOf(id));
			}
		}
	}

	@Override
	public void addBrandAct(String[] brandIds, Integer actId, Date joinTime) {
		ActivityInfo activityInfo = hbDaoSupport.findTById(ActivityInfo.class,
				actId);
		if (null != brandIds && brandIds.length > 0) {
			for (String id : brandIds) {
				if (id != null) {
					Brand brandFromDb = hbDaoSupport.findTById(Brand.class,
							Integer.valueOf(id));
					BrandActivity bt = new BrandActivity();
					bt.setActivityInfo(activityInfo);
					bt.setBrand(brandFromDb);
					bt.setJoinTime(joinTime);
					hbDaoSupport.save(bt);
				}
			}
		}
	}

	@Override
	public void deleteActivity(String id) {
//		String hql = "delete from ActivityInfo where id = ? ";
//		String hql2 = "delete from BrandActivity a where a.activityInfo.id = ? ";
//		hbDaoSupport.executeHQL(hql, Integer.valueOf(id));
//		hbDaoSupport.executeHQL(hql2, Integer.valueOf(id));
		String hql = "update ActivityInfo set tag = ? where id = ?";
		hbDaoSupport.executeHQL(hql.toString(), -1, Integer.valueOf(id));
	}

	@Override
	public void cancerActivity(String id) {
		String hql = "update ActivityInfo set tag = ? where id = ?";
		hbDaoSupport.executeHQL(hql.toString(), 0, Integer.valueOf(id));
	}

	@Override
	public PosBind savePosBind(PosBind posBind) {
		
		return hbDaoSupport.save(posBind);
	}

	@Override
	public int checkPosBand(String code) {
		String hql = "from PosBind where code = ?" ;
		List<PosBind> posBinds = hbDaoSupport.findTsByHQL(hql, code);
		return posBinds.size();
	}

	@Override
	public List<PosBind> queryPosBands(PosBind posBand, Page page) {
		String hql = "from PosBind where mark = 0" ;
		Map<String, Object> map = new HashMap<String, Object>();
		return hbDaoSupport.findTsByHQLPage(hql, map, page);
	}

	@Override
	public void delPosBand(String[] posIds) {
		if (null != posIds && posIds.length > 0) {
			for (String id : posIds) {
				String hql = "delete from PosBind where id = ?" ;
				hbDaoSupport.executeHQL(hql, Integer.valueOf(id));
			}
		}
	}

	@Override
	public DiscountNumber saveDiscountNumber(DiscountNumber discountNumber) {
		// TODO Auto-generated method stub
		return hbDaoSupport.save(discountNumber);
	}

	@Override
	public DiscountNumber findDiscountNumberById(String id) {

		String hql = "from DiscountNumber dn where dn.activityInfo.id = ? " ;
		
		return hbDaoSupport.findTByHQL(hql, Integer.valueOf(id));
	}

	@Override
	public void updateDiscountNumberByActId(String title,String descr,Integer id) {
		String hql="update DiscountNumber dn set dn.title = ?,dn.descr = ? where dn.activityInfo.id = ? " ;
		hbDaoSupport.executeHQL(hql, title,descr,id);
	}

	@Override
	public List<BrandMode> findBrandAct(String name, Page page, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from BrandActivity b where 1=1 and b.activityInfo.id = :id");
		map.put("id",id);
		if (StringUtils.isNotEmpty(name)) {
			hql.append(" and b.brand.name like :name");
			map.put("name", "%"+name+"%");
		}
		
		List<BrandMode> bms = new LinkedList<BrandMode>();
		List<BrandActivity> list = hbDaoSupport.findTsByHQLPage(hql.toString(),
				map, page);
		for (BrandActivity bm : list) {
			BrandMode b = new BrandMode(bm.getGid(), String.valueOf(bm
					.getBrand().getId()), bm.getActivityInfo().getId() + "", bm
					.getBrand().getName(), bm.getBrand().getCompanyName(),
					bm.getJoinTime());

			bms.add(b);
		}
		return bms;
	}

	@Override
	public List<PosBind> queryPosBands(PosBind posBand, Page page, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from PosBind where mark = 0 and fId = :id");
		map.put("id",id);
		return hbDaoSupport.findTsByHQLPage(hql.toString(), map, page);
	}

	@Override
	public int checkActNameAndTime(String name, Date dTime) {
		String hql = "from ActivityInfo where activityName = ? and startDate = ?" ;
		List<ActivityInfo> infos = hbDaoSupport.findTsByHQL(hql, name,dTime);
		return (infos != null) ? infos.size() : 0;
	}

}
