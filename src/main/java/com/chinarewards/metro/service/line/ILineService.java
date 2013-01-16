package com.chinarewards.metro.service.line;

import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import com.chinarewards.metro.core.common.Page;
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

public interface ILineService {

	/**
	 * 查询站台
	 * @param site
	 * @param page
	 * @return
	 */
	public List<MetroSite> findSites(MetroSite site,Page page);
	/**
	 * 查询线路
	 * @param line
	 * @param page
	 * @return
	 */
	public List<MetroLine> findLines(MetroLine line,Page page);
	/**
	 * 保存线路
	 * @param line
	 */
	public <T> void saveMetroLine(MetroLine line,String siteJson)throws Exception;
	/**
	 * 查询地铁名称或编号是否存在
	 */
	public MetroLine findMetroLineByName(MetroLine line);
	
	public MetroLine findMetroLineByNum(MetroLine line);
	
	public MetroLine findLineByid(Integer id);
	/**
	 * 根据线路查询站台
	 * @param lindId
	 * @return
	 */
	public List<MetroLineSite> findMetroLineByLindId(Integer lindId);
	/**
	 * 根据线路查询站台
	 * @param lindId
	 * @return
	 */
	public List<MetroLineSite> findMetroLineBySiteId(Integer siteId);
	/**
	 * 修改线路
	 * @param line
	 * @param inserted
	 * @param deleted
	 * @param updated
	 */
	public void updateMetroLine(MetroLine line,String inserted,String deleted,String updated)throws Exception;
	/**
	 * 删除线路
	 * @param id
	 */
	public void delMetroLine(Integer id);
	/**
	 * 保存站台
	 * @param site
	 * @param lineJson
	 */
	public void saveMetroSite(MetroSite site,String lineJson,String shopJson)throws Exception;
	
	/**
	 * 删除站台
	 * @param id
	 */
	public void delMetroSite(Integer id);
	/**
	 * 根据id查站台
	 * @param id
	 * @return
	 */
	public MetroSite findSiteById(Integer id);
	/**
	 * 判断站台是否存在
	 * @param name
	 * @return
	 */
	public MetroSite findSiteByName(String name);
	
	public List<MetroLine> findLineBySiteId(Integer id);
	/**
	 * 根据SiteID查询门店
	 * @return
	 */
	public List<Shop> findShopBySiteId(Integer id);
	/**
	 * 修改site
	 * @param site
	 * @param lineinserted
	 * @param linedeleted
	 * @param lineupdated
	 * @param shopinserted
	 * @param shopdeleted
	 * @param shopupdated
	 */
	public void updateMetroSite(MetroSite site,String lineinserted,String linedeleted,
			String lineupdated,String shopinserted,String shopdeleted,String shopupdated)throws Exception;
	
	/**
	 * 查询门店
	 */
	public List<Shop> findShops(Shop shop,Page page);
	/**
	 * 解析百度上海地铁地图XML保存 线路/站台
	 */
	public void readXmlSaveLineSiteService()throws DocumentException;
	/**
	 * 保存门店
	 * @param shop
	 */
	public Shop saveShop(Shop shop,MultipartFile mFile)throws Exception;
	/**
	 * 删除门店
	 * @param shop
	 */
	public void delShop(Integer shopId)throws Exception;
	
	public Shop saveShopWithOut(Shop shop)throws Exception;
	/**
	 * 保存图片文件
	 * @param shopFile
	 */
	public void saveShopFile(FileItem fileItem,ShopFile shopFile);
	/**
	 * 保存消费类型
	 * @param typeinserted
	 * @param typedeleted
	 * @param typeupdated
	 * @param posinserted
	 * @param posdeleted
	 * @param posupdated
	 */
	public void saveTypeAndPost(String typeinserted,String typedeleted,
			String typeupdated,String posinserted,String posdeleted,String posupdated,Integer shopId)throws Exception;
	
	/**
	 * 查询消费类型
	 */
	public List<ConsumptionType> findType(Integer shopId);
	/**
	 * 查询pos机绑定
	 */
	public List<PosBind> findPost(Integer shopId);
	/**
	 * 所属站台维护
	 * @param siteId
	 * @param orderNo
	 */
	public void saveShopSite(Integer siteId,Integer orderNo,Integer shopId);
	/**
	 * 品牌维护
	 * @param siteId
	 * @param orderNo
	 * @param shopId
	 */
	public void saveShopBrand(String inserted,String deleted,String updated,Integer shopId)throws Exception;
	/**
	 * 查询品牌
	 * @param shopId
	 * @return
	 */
	public List<ShopBrand> findShopBrand(Integer shopId);
	/**
	 * 查询优惠码
	 * findShopPromoCode
	 */
	public List<Shop> findShopPromoCode(Integer shopId);
	/**
	 * 优惠码详细
	 */
	public List<DiscountCodeImport> findShopPromoCodeDetail(String num,String importDate,Page page);
	/**
	 * 查询门店
	 */
	public List<Shop> findShopList(Shop shop,Page page);
	
	public Shop findShopById(Integer id);
	/**
	 *  查询门店图片
	 * @param shopId
	 * @return
	 */
	public List<FileItem> findShopPic(Integer shopId);
	/**
	 *  删除图片
	 */
	public void delShopPic(String id,String fileName);
}
