package com.chinarewards.metro.service.line.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.prefs.CsvPreference;

import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.JDBCDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.metro.MetroLine;
import com.chinarewards.metro.domain.metro.MetroLineSite;
import com.chinarewards.metro.domain.metro.MetroSite;
import com.chinarewards.metro.domain.pos.PosBind;
import com.chinarewards.metro.domain.shop.ConsumptionType;
import com.chinarewards.metro.domain.shop.DiscountCodeImport;
import com.chinarewards.metro.domain.shop.Shop;
import com.chinarewards.metro.domain.shop.ShopBrand;
import com.chinarewards.metro.domain.shop.ShopFile;
import com.chinarewards.metro.service.line.ILineService;

@Service
@SuppressWarnings("rawtypes")
public class LineService implements ILineService {
	
	@Autowired
	private HBDaoSupport hbDaoSupport;
	@Autowired
	private JDBCDaoSupport jdbcDaoSupport;
	
	@Override
	public List<MetroSite> findSites(MetroSite site, Page page) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		List<Object> argsCount = new ArrayList<Object>();
		sql.append("SELECT ms.*,GROUP_CONCAT(ml. NAME) lineName, (SELECT COUNT(1) FROM Shop s WHERE ms.id = s.siteId) orderNo FROM MetroSite ms");
		sql.append(" LEFT JOIN MetroLineSite mls ON ms.id = mls.siteId LEFT JOIN MetroLine ml ON mls.lineId = ml.id WHERE 1=1");
		sqlCount.append("SELECT COUNT(1) FROM MetroSite ms WHERE 1=1");
		if(StringUtils.isNotEmpty(site.getName())){
			sql.append(" AND ms.name like ?");
			sqlCount.append(" AND ms.name like ?");
			args.add("%"+site.getName()+"%");
			argsCount.add("%"+site.getName()+"%");
		}
		sql.append(" GROUP BY ms.id ORDER BY ms.id DESC LIMIT ?,?");
		args.add(page.getStart());
		args.add(page.getRows());
		if(argsCount.size() > 0){
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString(),argsCount.toArray()));
		}else{
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString()));
		}
		return jdbcDaoSupport.findTsBySQL(MetroSite.class, sql.toString(),args.toArray());
	}
	
	@Override
	public <T> void saveMetroLine(MetroLine line,String siteJson) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		hbDaoSupport.save(line);
		if(StringUtils.isNotEmpty(siteJson)){
			List<MetroSite> sites = mapper.readValue(siteJson,new TypeReference<List<MetroSite>>(){});
			for(MetroSite s : sites){
				MetroLineSite lineSite = new MetroLineSite();
				lineSite.setLine(line);
				lineSite.setSite(s);
				lineSite.setOrderNo(s.getOrderNo());
				hbDaoSupport.save(lineSite);
			}
		}
	}
	
	@Override
	public MetroLine findMetroLineByName(MetroLine line) {
		String hql = "FROM MetroLine WHERE name = ?";
		return hbDaoSupport.findTByHQL(hql, line.getName());
	}
	
	@Override
	public MetroLine findMetroLineByNum(MetroLine line) {
		String hql = "FROM MetroLine WHERE numno = ?";
		return hbDaoSupport.findTByHQL(hql, line.getNumno());
	}
	
	@Override
	public List<MetroLine> findLines(MetroLine line, Page page) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		List<Object> argsCount = new ArrayList<Object>();
		sql.append("SELECT ml.*,(SELECT COUNT(*) FROM MetroLineSite mss WHERE mss.lineId = ml.id) c FROM MetroLine ml where 1=1");
		sqlCount.append("SELECT COUNT(1) FROM MetroLine ml WHERE 1=1");
		if(StringUtils.isNotEmpty(line.getName())){
			sql.append(" AND ml.name like ?");
			sqlCount.append(" AND ml.name like ?");
			args.add("%" + line.getName() + "%");
			argsCount.add("%"+line.getName()+"%");
		}
		if(StringUtils.isNotEmpty(line.getNumno())){
			sql.append(" AND ml.numno like ?");
			sqlCount.append(" AND ml.numno like ?");
			args.add("%"+line.getNumno()+"%");
			argsCount.add("%"+line.getNumno()+"%");
		}
		sql.append(" ORDER BY ml.id DESC LIMIT ?,?");
		args.add(page.getStart());
		args.add(page.getRows());
		if(argsCount.size()>0){
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString(),argsCount.toArray()));
		}else{
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString()));
		}
		return jdbcDaoSupport.findTsBySQL(MetroLine.class, sql.toString(),args.toArray());
	}
	
	@Override
	public MetroLine findLineByid(Integer id) {
		return hbDaoSupport.findTById(MetroLine.class, id);
	}
	
	@Override
	public List<MetroLineSite> findMetroLineByLindId(Integer lindId) {
		String hql = "from MetroLineSite where line.id = ?";
		return hbDaoSupport.findTsByHQL(hql,lindId);
	}
	
	@Override
	public List<MetroLineSite> findMetroLineBySiteId(Integer siteId) {
		String hql = "from MetroLineSite where site.id = ?";
		return hbDaoSupport.findTsByHQL(hql,siteId);
	}
	
	@Override
	public void updateMetroLine(MetroLine line, String inserted,String deleted, String updated) throws Exception {
		if(StringUtils.isNotEmpty(inserted)){//增加
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			List<MetroSite> list = mapper.readValue(inserted,new TypeReference<List<MetroSite>>(){});
			for(MetroSite s : list){
				MetroLineSite lineSite = new MetroLineSite();
				lineSite.setLine(line);
				lineSite.setSite(s);
				lineSite.setOrderNo(s.getOrderNo());
				hbDaoSupport.save(lineSite);
			}
		}
		if(StringUtils.isNotEmpty(updated)){//修改
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			List<MetroLineSite> list = mapper.readValue(updated,new TypeReference<List<MetroLineSite>>(){});
			for(MetroLineSite ms : list){
				String hql = "update MetroLineSite set orderNo = ? where id = ?";
				hbDaoSupport.executeHQL(hql, ms.getOrderNo(),ms.getId());
			}
		}
		if(StringUtils.isNotEmpty(deleted)){//删除
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			List<MetroLineSite> list = mapper.readValue(deleted,new TypeReference<List<MetroLineSite>>(){});
			for(MetroLineSite ms : list){
				String hql = "DELETE FROM MetroLineSite where id = ?";
				hbDaoSupport.executeHQL(hql,ms.getId());
			}
		}
		hbDaoSupport.update(line);
	}
	
	@Override
	public void delMetroLine(Integer id) {
		List<MetroLineSite> list = findMetroLineByLindId(id);
		String hql = "DELETE FROM MetroLine where id = ?";
		for(MetroLineSite ms : list){
			String hqls = "DELETE FROM MetroLineSite where id = ?";
			hbDaoSupport.executeHQL(hqls,ms.getId());
		}
		hbDaoSupport.executeHQL(hql,id);
	}
	
	@Override
	public void saveMetroSite(MetroSite site, String lineJson,String shopJson) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		List<MetroLine> lines = mapper.readValue(lineJson,new TypeReference<List<MetroLine>>(){});
		hbDaoSupport.save(site);
		for(MetroLine line : lines){
			MetroLineSite lineSite = new MetroLineSite();
			lineSite.setLine(line);
			lineSite.setSite(site);
			lineSite.setOrderNo(line.getOrderNo());
			hbDaoSupport.save(lineSite);
		}
		if(StringUtils.isNotEmpty(shopJson)){
			List<Shop> shops = mapper.readValue(shopJson,new TypeReference<List<Shop>>(){});
			for(Shop s : shops){
				String hql = "UPDATE Shop SET siteId = ? WHERE id = ?";
				hbDaoSupport.executeHQL(hql, site.getId(),s.getId());
			}
		}
	}
	
	@Override
	public List<Shop> findShops(Shop shop,Page page) {
		DetachedCriteria criteria = DetachedCriteria.forClass(shop.getClass());
		if(StringUtils.isNotEmpty(shop.getName())){
			criteria.add(Restrictions.like("name", shop.getName(),MatchMode.ANYWHERE));
		}
		return hbDaoSupport.findPageByCriteria(page, criteria);
	}
	
	@Override
	public void delMetroSite(Integer id) {
		List<MetroLineSite> list = findMetroLineBySiteId(id);
		String hql = "DELETE FROM MetroSite where id = ?";
		for(MetroLineSite ms : list){
			String hqls = "DELETE FROM MetroLineSite where id = ?";
			hbDaoSupport.executeHQL(hqls,ms.getId());
		}
		hbDaoSupport.executeHQL(hql,id);
	}
	
	@Override
	public MetroSite findSiteById(Integer id) {
		return hbDaoSupport.findTById(MetroSite.class, id);
	}
	
	@Override
	public List<MetroLine> findLineBySiteId(Integer id) {
		String sql = "SELECT mls.id, ml.name,mls.orderNo FROM MetroSite ms" +
					 " LEFT JOIN MetroLineSite mls ON ms.id = mls.siteId LEFT JOIN MetroLine ml ON mls.lineId = ml.id"+
					 " WHERE ms.id = ?";
		return jdbcDaoSupport.findTsBySQL(MetroLine.class, sql, id);
	}
	
	@Override
	public List<Shop> findShopBySiteId(Integer id) {
		String sql  = "SELECT * FROM Shop WHERE siteId = ?";
		return jdbcDaoSupport.findTsBySQL(Shop.class, sql, id);
	}
	
	@Override
	public void updateMetroSite(MetroSite site, String lineinserted,
			String linedeleted, String lineupdated, String shopinserted,
			String shopdeleted, String shopupdated) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		
		if(StringUtils.isNotEmpty(lineinserted)){//增加
			List<MetroLine> list = mapper.readValue(lineinserted,new TypeReference<List<MetroLine>>(){});
			for(MetroLine line : list){
				MetroLineSite lineSite = new MetroLineSite();
				lineSite.setLine(line);
				lineSite.setSite(site);
				lineSite.setOrderNo(line.getOrderNo());
				hbDaoSupport.save(lineSite);
			}
		}
		
		if(StringUtils.isNotEmpty(linedeleted)){//删除
			List<MetroLine> list = mapper.readValue(linedeleted,new TypeReference<List<MetroLine>>(){});
			for(MetroLine msl : list){//msl id
				String hql = "DELETE FROM MetroLineSite where id = ?";
				hbDaoSupport.executeHQL(hql,msl.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(lineupdated)){//修改
			List<MetroLine> list = mapper.readValue(lineupdated,new TypeReference<List<MetroLine>>(){});
			for(MetroLine msl : list){//msl id
				String hql = "update MetroLineSite set orderNo = ? where id = ?";
				hbDaoSupport.executeHQL(hql, msl.getOrderNo(),msl.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(shopinserted)){//增加门店
			List<Shop> shops = mapper.readValue(shopinserted,new TypeReference<List<Shop>>(){});
			for(Shop s : shops){
				String hql = "UPDATE Shop SET siteId = ? WHERE id = ?";
				hbDaoSupport.executeHQL(hql, site.getId(),s.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(shopupdated)){//修改门店
			List<Shop> shops = mapper.readValue(shopupdated,new TypeReference<List<Shop>>(){});
			for(Shop s : shops){
				String hql = "UPDATE Shop SET siteId = ? WHERE id = ?";
				hbDaoSupport.executeHQL(hql, site.getId(),s.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(shopdeleted)){//删除门店
			List<Shop> shops = mapper.readValue(shopdeleted,new TypeReference<List<Shop>>(){});
			for(Shop s : shops){
				String hql = "UPDATE Shop SET siteId = 0 WHERE id = ?";
				hbDaoSupport.executeHQL(hql, site.getId(),s.getSiteId());
			}
		}
		hbDaoSupport.update(site);
	}
	
	@Override
	public void readXmlSaveLineSiteService() throws DocumentException {
		
		String url = "http://map.baidu.com/subways/data/sw_shanghai.xml";
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(url);
		Element rootElement = document.getRootElement();
		for(Iterator i = rootElement.elementIterator();i.hasNext();){
			Element e = (Element) i.next();
			
			MetroLine line = new MetroLine();
			line.setName(e.attributeValue("lid"));
			line.setDescs(e.attributeValue("lid"));
			line.setNumno(e.attributeValue("lb"));
			hbDaoSupport.save(line);
			int orderNo = 1;
			for(Iterator ii = e.elementIterator();ii.hasNext();){
				Element ee = (Element) ii.next();
				if(StringUtils.isNotEmpty(ee.attributeValue("sid"))){
					
					MetroSite site = new MetroSite();
					site.setName(ee.attributeValue("sid"));
					site.setDescs(ee.attributeValue("ln"));
					hbDaoSupport.save(site);
					
					MetroLineSite lineSite = new MetroLineSite();
					lineSite.setLine(line);
					lineSite.setSite(site);
					lineSite.setOrderNo(orderNo++);
					hbDaoSupport.save(lineSite);
				}
			}
		}
	}
	
	@Override
	public Shop saveShop(Shop shop,MultipartFile mFile) throws Exception {
		@SuppressWarnings("unchecked")
		List<String[]> list = (List<String[]>) FileUtil.readCSV(mFile.getInputStream(), CsvPreference.STANDARD_PREFERENCE, false);
		if(shop.getId() == null){
			Shop shop_ = hbDaoSupport.save(shop);
			Date importDate = DateTools.dateToHour();
			for(String[] ss : list){
				for(String s : ss){
					if(StringUtils.isNotEmpty(s)){
						DiscountCodeImport dc = new DiscountCodeImport();
						dc.setImportDate(importDate);
						dc.setDiscountNum(s);
						dc.setIsRecived(0);
						dc.setShopId(shop_.getId());
						hbDaoSupport.save(dc);
					}
				}
			}
			return shop_;
		}else{
			Date importDate = DateTools.dateToHour();
			for(String[] ss : list){
				for(String s : ss){
					if(StringUtils.isNotEmpty(s)){
						DiscountCodeImport dc = new DiscountCodeImport();
						dc.setImportDate(importDate);
						dc.setDiscountNum(s);
						dc.setIsRecived(0);
						dc.setShopId(shop.getId());
						hbDaoSupport.save(dc);
					}
				}
			}
			hbDaoSupport.update(shop);
			return shop;
		}
	}
	
	@Override
	public Shop saveShopWithOut(Shop shop) throws Exception {
		if(shop.getId() == null){
			return hbDaoSupport.save(shop);
		}else{
			hbDaoSupport.update(shop);
			return shop;
		}
	}
	
	@Override
	public void saveShopFile(FileItem fileItem,ShopFile shopFile) {
		hbDaoSupport.save(fileItem);
		shopFile.setFileItem(fileItem);
		hbDaoSupport.save(shopFile);
	}
	
	@Override
	public void saveTypeAndPost(String typeinserted, String typedeleted,
			String typeupdated, String posinserted, String posdeleted,
			String posupdated,Integer shopId) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		/**
		 * 类型
		 */
		if(StringUtils.isNotEmpty(typeinserted)){
			List<ConsumptionType> list = mapper.readValue(typeinserted,new TypeReference<List<ConsumptionType>>(){});
			for(ConsumptionType type : list){
				type.setShopId(shopId);
				hbDaoSupport.save(type);
			}
		}
		
		if(StringUtils.isNotEmpty(typedeleted)){
			List<ConsumptionType> list = mapper.readValue(typedeleted,new TypeReference<List<ConsumptionType>>(){});
			for(ConsumptionType type : list){
				String hql = "DELETE FROM ConsumptionType where id = ?";
				hbDaoSupport.executeHQL(hql,type.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(typeupdated)){
			List<ConsumptionType> list = mapper.readValue(typeupdated,new TypeReference<List<ConsumptionType>>(){});
			for(ConsumptionType type : list){
				String hql = "UPDATE ConsumptionType SET name = ?,num = ? where id = ?";
				hbDaoSupport.executeHQL(hql,type.getName(),type.getNum(),type.getId());
			}
		}
		/**
		 * pos机绑定
		 */
		if(StringUtils.isNotEmpty(posinserted)){
			List<PosBind> list = mapper.readValue(posinserted,new TypeReference<List<PosBind>>(){});
			for(PosBind pos : list){
				pos.setCreatedAt(DateTools.dateToHour());
				pos.setCreatedBy(UserContext.getUserName());
				pos.setLastModifiedAt(DateTools.dateToHour());
				pos.setfId(shopId);
				pos.setMark(1); //绑定门店
				hbDaoSupport.save(pos);
			}
		}
		
		if(StringUtils.isNotEmpty(posdeleted)){
			List<PosBind> list = mapper.readValue(posdeleted,new TypeReference<List<PosBind>>(){});
			for(PosBind pos : list){
				String hql = "DELETE FROM PosBind where id = ?";
				hbDaoSupport.executeHQL(hql,pos.getId());
			}
		}
		
		if(StringUtils.isNotEmpty(posupdated)){
			List<PosBind> list = mapper.readValue(posupdated,new TypeReference<List<PosBind>>(){});
			for(PosBind pos : list){
				String hql = "UPDATE PosBind SET code = ?,bindDate = ?,lastModifiedAt = ?,lastModifiedBy = ? where id = ?";
				hbDaoSupport.executeHQL(hql,pos.getCode(),pos.getBindDate(),DateTools.dateToHour(),UserContext.getUserName(),pos.getId());
			}
		}
	}
	
	@Override
	public List<ConsumptionType> findType(Integer shopId) {
		String hql = "from ConsumptionType where shopId = ?";
		return hbDaoSupport.findTsByHQL(hql, shopId);
	}
	
	@Override
	public List<PosBind> findPost(Integer shopId) {
		String hql = "from PosBind where fid = ?";
		return hbDaoSupport.findTsByHQL(hql, shopId);
	}
	
	@Override
	public void saveShopSite(Integer siteId, Integer orderNo,Integer shopId) {
		String hql = "UPDATE Shop SET siteId = ?,orderNo = ? WHERE id = ?";
		hbDaoSupport.executeHQL(hql, siteId,orderNo,shopId);
	}
	
	@Override
	public void saveShopBrand(String inserted, String deleted, String updated,Integer shopId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		if(StringUtils.isNotEmpty(inserted)){
			List<Brand> list = mapper.readValue(inserted,new TypeReference<List<Brand>>(){});
			for(Brand brand : list){
				ShopBrand shopBrand = new ShopBrand();
				shopBrand.setBrand(brand);
				shopBrand.setShopId(shopId);
				shopBrand.setJoinDate(DateTools.dateToHour());
				hbDaoSupport.save(shopBrand);
			}
		}
		
		if(StringUtils.isNotEmpty(deleted)){
			List<ShopBrand> list = mapper.readValue(deleted,new TypeReference<List<ShopBrand>>(){});
			for(ShopBrand brand : list){
				String hql = "DELETE FROM ShopBrand where id = ?";
				hbDaoSupport.executeHQL(hql,brand.getId());
			}
		}
		
	}
	
	@Override
	public List<ShopBrand> findShopBrand(Integer shopId) {
		String hql = "from ShopBrand where shopId = ?";
		return hbDaoSupport.findTsByHQL(hql, shopId);
	}
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		List<Date> l = new ArrayList<Date>();
		l.add(new Date());
		System.out.println(mapper.writeValueAsString(l));
		String s = "[1357702722000]";
		List<Date> list = mapper.readValue(s, new TypeReference<List<Date>>(){});
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(0)));
	}
	@Override
	public List<Shop> findShopPromoCode(Integer shopId) {
		String sql = "SELECT d.importDate activeDate,s.note,COUNT(1) allCount,(SELECT COUNT(1) FROM DiscountCodeImport WHERE isRecived = 1 GROUP BY d.importDate"+
					 ") validateCount FROM DiscountCodeImport d LEFT JOIN Shop s ON d.shopId = s.id WHERE d.shopId = ? GROUP BY importDate";
		return jdbcDaoSupport.findTsBySQL(Shop.class, sql, shopId);
	}
	
	@Override
	public List<DiscountCodeImport> findShopPromoCodeDetail(String num,String importDate,Page page) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DiscountCodeImport.class);
		criteria.addOrder(Order.desc("importDate"));
		criteria.add(Restrictions.eq("importDate", DateTools.stringToDate(importDate)));
		if(StringUtils.isNotEmpty(num)){
			criteria.add(Restrictions.like("discountNum", num,MatchMode.ANYWHERE));
		}
		return hbDaoSupport.findPageByCriteria(page, criteria);
	}
	
	@Override
	public List<Shop> findShopList(Shop shop, Page page) {
		List<Object> args = new ArrayList<Object>();
		List<Object> argsCount = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		sql.append("SELECT ms.`name` siteName,s.id,s.num,s.name,s.city,s.province,s.region,s.address,s.linkman,s.workphone FROM Shop s");
		sql.append(" LEFT JOIN MetroSite ms ON s.siteId = ms.id WHERE 1=1");
		sqlCount.append("SELECT count(1) FROM Shop s WHERE 1=1");
		
		if(StringUtils.isNotEmpty(shop.getNum())){
			sql.append(" AND s.num = ?");
			sqlCount.append(" AND s.num = ?");
			args.add(shop.getNum());
			argsCount.add(shop.getNum());
		}
		if(StringUtils.isNotEmpty(shop.getName())){
			sql.append(" AND s.name like ?");
			sqlCount.append(" AND s.name like ?");
			args.add(shop.getName());
		}
		if(StringUtils.isNotEmpty(shop.getLinkman())){
			sql.append(" AND s.linkman like ?");
			sqlCount.append(" AND s.linkman like ?");
			args.add(shop.getLinkman());
			argsCount.add(shop.getLinkman());
		}
		if(StringUtils.isNotEmpty(shop.getProvince())){
			sql.append(" AND s.province = ?");
			sqlCount.append(" AND s.province = ?");
			args.add(shop.getProvince());
			argsCount.add(shop.getProvince());
		}
		if(StringUtils.isNotEmpty(shop.getCity())){
			sql.append(" AND s.city = ?");
			sqlCount.append(" AND s.city = ?");
			args.add(shop.getCity());
			argsCount.add(shop.getCity());
		}
		if(StringUtils.isNotEmpty(shop.getRegion())){
			sql.append(" AND s.region = ?");
			sqlCount.append(" AND s.region = ?");
			args.add(shop.getRegion());
			argsCount.add(shop.getRegion());
		}
		
		sql.append(" ORDER BY s.id DESC LIMIT ?,?");
		args.add(page.getStart());
		args.add(page.getRows());
		if(argsCount.size()>0){
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString(),argsCount.toArray()));
		}else{
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString()));
		}
		return jdbcDaoSupport.findTsBySQL(Shop.class, sql.toString(),args.toArray());
	}
	
	@Override
	public Shop findShopById(Integer id) {
		return hbDaoSupport.findTById(Shop.class, id);
	}
	
	@Override
	public List<FileItem> findShopPic(Integer shopId) {
		String sql = "SELECT fi.* FROM ShopFile sf LEFT JOIN FileItem fi ON sf.fileItem_id = fi.id WHERE sf.shop_id = ?";
		return jdbcDaoSupport.findTsBySQL(FileItem.class, sql,shopId);
	}
	
	@Override
	public void delShopPic(String id,String fileName) {
		String hql = "DELETE FROM ShopFile WHERE fileItem.id = ?";
		FileUtil.removeFile(Constants.SHOP_IMG , fileName);
		hbDaoSupport.executeHQL(hql, id);
	}
	
	@Override
	public void delShop(Integer shopId) throws Exception {
		String hql1 = "DELETE FROM Shop WHERE id = ?";
		String hql2 = "DELETE FROM ConsumptionType WHERE shopId = ?";
		String hql3 = "DELETE FROM PosBind WHERE fId = ?";
		String hql4 = "DELETE FROM ShopFile WHERE shop.id = ?";
		String hql5 = "DELETE FROM ShopBrand WHERE shopId = ?";
		String hql6 = "DELETE FROM DiscountCodeImport WHERE shopId = ?";
		hbDaoSupport.executeHQL(hql6, shopId);
		hbDaoSupport.executeHQL(hql5, shopId);
		hbDaoSupport.executeHQL(hql4, shopId);
		hbDaoSupport.executeHQL(hql3, shopId);
		hbDaoSupport.executeHQL(hql2, shopId);
		hbDaoSupport.executeHQL(hql1, shopId);
	}
}
