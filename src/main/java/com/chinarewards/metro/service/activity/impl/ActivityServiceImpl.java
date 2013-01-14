package com.chinarewards.metro.service.activity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UserContext;
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
	public ActivityInfo saveActivity(ActivityInfo activity, FileItem pic) {
		if (null != pic) {
			pic.setCreatedAt(SystemTimeProvider.getCurrentTime());
			pic.setCreatedBy(UserContext.getUserId());
			pic.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
			pic.setLastModifiedBy(UserContext.getUserId());
			hbDaoSupport.save(pic);
		}
		System.out.println(hbDaoSupport);
		activity = hbDaoSupport.save(activity);
		return activity;
	}

	@Override
	public void updateActivity(ActivityInfo activity, FileItem pic) {

		String hql = "update ActivityInfo set activityName = ? ,startDate = ?,endDate = ?,description = ?,hoster = ?,activityNet = ?,contacts = ?,conTel = ?,picture = ? where id = ?";
		hbDaoSupport.executeHQL(hql, activity.getActivityName(),
				activity.getStartDate(), activity.getEndDate(),
				activity.getDescription(), activity.getHoster(),
				activity.getActivityNet(), activity.getContacts(),
				activity.getConTel(), activity.getPicture(), activity.getId());
	}

	@Override
	public List<ActivityInfo> findActivity(ActivityInfo activity, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from ActivityInfo where 1=1");
		if (StringUtils.isNotEmpty(activity.getActivityName())) {
			hql.append(" and activityName like :activityName");
			map.put("activityName", activity.getActivityName());
		}
		if (activity.getStartDate() != null) {
			hql.append(" and startDate like :startDate");
			map.put("startDate", activity.getStartDate());
		}
		if (activity.getEndDate() != null) {
			hql.append(" and endDate like :endDate");
			map.put("endDate", activity.getEndDate());
		}
		hql.append(" order by id desc");
		return hbDaoSupport.findTsByHQLPage(hql.toString(), map, page);
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
			map.put("name", brand.getName());
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
		hql.append("SELECT b FROM Brand b WHERE 1=1");
		
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
			map.put("name", brand.getName());
		}

		if (StringUtils.isNotEmpty(brand.getCompanyName())) {
			hql.append(" and b.companyName like :name");
			map.put("companyName", brand.getName());
		}

		List<Brand> list = hbDaoSupport.executeQuery(hql.toString(), map, page);
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
		String hql = "delete from ActivityInfo where id = ? ";
		String hql2 = "delete from BrandActivity a where a.activityInfo.id = ? ";
		hbDaoSupport.executeHQL(hql, Integer.valueOf(id));
		hbDaoSupport.executeHQL(hql2, Integer.valueOf(id));
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
	public List<BrandMode> findBrandAct(Brand brand, Page page, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from BrandActivity b where 1=1 and b.activityInfo.id = :id");
		if (StringUtils.isNotEmpty(brand.getName())) {
			hql.append(" and b.brand.name like :name");
			map.put("name", brand.getName());
		}
		map.put("id",id);
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

}
