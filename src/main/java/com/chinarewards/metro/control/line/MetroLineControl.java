package com.chinarewards.metro.control.line;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.metro.MetroLine;
import com.chinarewards.metro.domain.metro.MetroSite;
import com.chinarewards.metro.domain.shop.Shop;
import com.chinarewards.metro.domain.shop.ShopFile;
import com.chinarewards.metro.service.line.ILineService;

@Controller
@RequestMapping("/line")
public class MetroLineControl {

	@Autowired
	private ILineService lineService;
	
	@RequestMapping("/index")
	public String lineIndex(){
		return "line/lineList";
	}
	
	@RequestMapping("/linePage")
	public String linePage(){
		return "line/line";
	}
	
	@RequestMapping("/lineUpdatePage")
	public String lineUpdatePage(Integer id,Model model){
		model.addAttribute("line",lineService.findLineByid(id));
		return "line/lineUpdate";
	}
	
	/**
	 * 查询站台
	 * @param site
	 * @param page
	 * @return
	 */
	@RequestMapping("/findSites")
	public Map<String,Object> findSites(MetroSite site,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findSites(site, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	@RequestMapping("/saveLine")
	public void saveMetroLine(MetroLine line,String siteJson) throws Exception{
		lineService.saveMetroLine(line, siteJson);
	}
	/**
	 * 查询地铁名称或编号是否存在
	 */
	@ResponseBody
	@RequestMapping("/findMetroLineByName")
	public Integer findMetroLineByName(MetroLine line){
		MetroLine l = lineService.findMetroLineByName(line);
		if(l==null){
			return 0;
		}else{
			if(l.getId().equals(line.getId())){
				return 0;
			}
			return 1;
		}
	}
	
	/**
	 * 查询地铁名称或编号是否存在
	 */
	@ResponseBody
	@RequestMapping("/findMetroLineByNum")
	public Integer findMetroLineByNum(MetroLine line){
		MetroLine l = lineService.findMetroLineByNum(line);
		if(l==null){
			return 0;
		}else{
			if(l.getId().equals(line.getId())){
				return 0;
			}
			return 1;
		}
	}
	
	@RequestMapping("/findLines")
	public Map<String,Object> findLines(MetroLine line,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findLines(line, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	@RequestMapping("/findMetroLineSites")
	public Map<String,Object> findMetroLineSites(Integer lindId,Page page){ 
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findMetroLineByLindId(lindId));
		map.put("total", page.getTotalRows());
		return map; 
	}
	
	@RequestMapping("/updateMetroLine")
	public void updateMetroLine(MetroLine line,String inserted,String deleted,String updated ) throws Exception{
		lineService.updateMetroLine(line, inserted, deleted, updated);
	}
	
	@RequestMapping("/delMetroLine")
	public void delMetroLine(Integer id ) throws Exception{
		lineService.delMetroLine(id);
	}
	
	/**
	 * 站台页面
	 */
	@RequestMapping("/sitePage")
	public String sitePage(){
		return "line/site";
	}
	
	@RequestMapping("/siteUpdatePage")
	public String siteUpdatePage(Integer id,Model model){
		model.addAttribute("site", lineService.findSiteById(id));
		return "line/siteUpdate";
	}
	
	@RequestMapping("/siteIndex")
	public String siteIndex(){
		return "line/siteList";
	}
	
	@RequestMapping("/saveSite")
	public void saveMetroSite(MetroSite site,String lineJson,String shopJson) throws Exception{
		lineService.saveMetroSite(site, lineJson,shopJson);
	}
	
	@RequestMapping("/delMetroSite")
	public void delMetroSite(Integer id) throws Exception{
		lineService.delMetroSite(id);
	}
	
	@RequestMapping("/updateMetroSite")
	public void updateMetroSite(MetroSite site,String lineinserted,String linedeleted,
			String lineupdated,String shopinserted,String shopdeleted,String shopupdated) throws Exception{
		lineService.updateMetroSite(site, lineinserted, linedeleted, lineupdated, shopinserted, shopdeleted, shopupdated);
	}
	/**
	 * 
	 */
	@RequestMapping("/findLineBySiteId")
	public Map<String,Object> findLineBySiteId(Integer id){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findLineBySiteId(id));
		map.put("total", 0);
		return map;
	}
	
	@RequestMapping("/findShopBySiteId")
	public Map<String,Object> findShopBySiteId(Integer id){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findShopBySiteId(id));
		map.put("total", 0);
		return map; 
	}
	
	@RequestMapping("/findShops")
	public Map<String,Object> findShops(Shop shop,Page page){ 
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findShops(shop, page));
		map.put("total", page.getTotalRows());
		return map; 
	}
	/**
	 * 解析百度上海地铁地图XML保存 线路/站台
	 * @throws DocumentException 
	 */
//	@RequestMapping("/readXmlSaveLineSite")
	@ResponseBody
	public void readXmlSaveLineSite() throws DocumentException{
		lineService.readXmlSaveLineSiteService();
	}
	
	/**
	 * 门店
	 */
	@RequestMapping("/shopPage")
	public String shopPage(){
		return "line/shop";
	}
	/**
	 * 保存门店
	 * @param shop
	 * @throws Exception 
	 */
	@RequestMapping("/saveShop")
	@ResponseBody
	public void saveShop(@RequestParam("csv") MultipartFile mFile,Shop shop,HttpServletResponse res) throws Exception{
		Shop s = lineService.saveShop(shop,mFile);
		res.setContentType("text/html;charset=utf-8");
		PrintWriter witer = res.getWriter();
		witer.print(CommonUtil.toJson(s.getId()));
		witer.flush();
		witer.close();
	}
	/**
	 * 删除门店
	 * @param shop
	 * @throws Exception 
	 */
	@RequestMapping("/delShop")
	@ResponseBody
	public void delShop(Integer shopId) throws Exception{
		lineService.delShop(shopId);
	}
	
	/**
	 * 保存门店 without upload
	 * @param shop
	 * @throws Exception 
	 */
	@RequestMapping("/saveShopWithOut")
	@ResponseBody
	public void saveShopWithOut(Shop shop,HttpServletResponse res) throws Exception{
		Shop s = lineService.saveShopWithOut(shop);
		res.setContentType("text/html;charset=utf-8");
		PrintWriter witer = res.getWriter();
		witer.print(CommonUtil.toJson(s.getId()));
		witer.flush();
		witer.close();
	}
	
	@RequestMapping("/shopPic")
	public String shopPic(Model model,Integer shopId){
		model.addAttribute("list", lineService.findShopPic(shopId));
		model.addAttribute("path", "SHOP_IMG");
		return "line/shopPic";
	}
	
	/**
	 *  图片上传
	 * @throws Exception 
	 */
	@RequestMapping(value = "/shopPicUpload")
	public void shopPicUpload(@RequestParam("files") MultipartFile mFile,HttpServletResponse response,Shop shop) throws Exception{
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.length()-4,fileName.length());
		String fileNewName = UUIDUtil.generate() + suffix;
		FileUtil.saveFile(mFile.getInputStream(), Constants.SHOP_IMG, fileNewName);
		ShopFile shopFile = new ShopFile();
		FileItem fileItem = new FileItem();
		fileItem.setCreatedAt(DateTools.dateToHour());
		fileItem.setCreatedBy(UserContext.getUserId());
		fileItem.setFilesize(mFile.getSize());
		fileItem.setOriginalFilename(fileName);
		fileItem.setUrl(fileNewName);
		fileItem.setMimeType(mFile.getContentType());
		fileItem.setLastModifiedAt(DateTools.dateToHour());
		fileItem.setLastModifiedBy(UserContext.getUserId());
		shopFile.setShop(shop);
		lineService.saveShopFile(fileItem,shopFile);
		FileUtil.outPic(mFile, "SHOP_IMG", fileNewName, response);
	}
	
	/**
	 * 获取显示图片
	 * @param mFile
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/shopPicShow")
	public void shopPicShow(String path,String fileName,HttpServletResponse response) throws Exception{
		File file = new File(Dictionary.getPicPath(path) + fileName);
		 response.setContentType("image/jpeg");
         response.setContentLength((int) file.length());
         response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );
         byte[] bbuf = new byte[1024];
         DataInputStream in = new DataInputStream(new FileInputStream(file));
         int bytes = 0;
         ServletOutputStream op = response.getOutputStream();
         while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
        	 op.write(bbuf, 0, bytes);
         }
         in.close();
         op.flush();
         op.close();
	}
	
	/**
	 * 显示缩略图
	 * @throws Exception 
	 */
	@RequestMapping("/showGetthumbPic")
	public void showGetthumbPic(String path ,String fileName,HttpServletResponse response) throws Exception{
		File file = new File(Dictionary.getPicPath(path) + fileName);
        if (file.exists()) {
            String mimetype = "image/jpeg";
            if (mimetype.endsWith("png") || mimetype.endsWith("jpeg") || mimetype.endsWith("gif")) {
                BufferedImage im = ImageIO.read(file);
                if (im != null) {
                    BufferedImage thumb = Scalr.resize(im, 75); 
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    if (mimetype.endsWith("png")) {
                        ImageIO.write(thumb, "PNG" , os);
                        response.setContentType("image/png");
                    } else if (mimetype.endsWith("jpeg")) {
                        ImageIO.write(thumb, "jpg" , os);
                        response.setContentType("image/jpeg");
                    } else {
                        ImageIO.write(thumb, "GIF" , os);
                        response.setContentType("image/gif");
                    }
                    ServletOutputStream srvos = response.getOutputStream();
                    response.setContentLength(os.size());
                    response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );
                    os.writeTo(srvos);
                    srvos.flush();
                    srvos.close();
                }
            }
        }
	}
	/**
	 * 删除图片
	 * @param id
	 * @param fileName
	 */
	@RequestMapping("/delShopPic")
	public void delShopPic(String id,String fileName){
		lineService.delShopPic(id, fileName);
	}
	
	@RequestMapping("/typeAndPost")
	public String typeAndPost(Integer shopId,Model model){
		model.addAttribute("shopId",shopId);
		return "line/typeAndPost";
	}
	
	@RequestMapping("/saveTypeAndPost")
	public void saveTypeAndPost(String typeinserted,String typedeleted,Integer shopId,
			String typeupdated,String posinserted,String posdeleted,String posupdated) throws Exception{
		lineService.saveTypeAndPost(typeinserted, typedeleted, typeupdated, posinserted, posdeleted, posupdated,shopId);
	}
	
	/**
	 * 查询消费类型
	 */
	@RequestMapping("/findType")
	public Map<String,Object> findType(Integer shopId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findType(shopId));
		map.put("total", 0);
		return map;
	}
	
	/**
	 * 查询消费类型
	 */
	@RequestMapping("/findPost")
	public Map<String,Object> findPost(Integer shopId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findPost(shopId));
		map.put("total", 0);
		return map 	;
	}
	
	/**
	 * 所属站台
	 * @return
	 */
	@RequestMapping("/shopSite")
	public String shopSite(Integer id,Integer orderNo,Model model){
		model.addAttribute("site", lineService.findSiteById(id));
		model.addAttribute("orderNo", orderNo);
		return  "line/shopSite";
	}

	@RequestMapping("/saveShopSite")
	public void saveShopSite(Integer siteId,Integer orderNo,Integer shopId){
		lineService.saveShopSite(siteId, orderNo, shopId);
	}
	
	/**
	 * 品牌维护
	 * @return
	 */
	@RequestMapping("/shopBrand")
	public String shopBrand(Integer shopId,Model model){
		model.addAttribute("shopId", shopId);
		return  "line/shopBrand";
	}
	
	@RequestMapping("/saveShopBrand")
	public void saveShopBrand(String inserted,String deleted,String updated,Integer shopId) throws Exception{
		lineService.saveShopBrand(inserted, deleted, updated, shopId);
	}
	
	@RequestMapping("/findShopBrand")
	public Map<String,Object> findShopBrand(Integer shopId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findShopBrand(shopId));
		map.put("total", 0);
		return map;
	}
	
	/**
	 * 门店优惠码导入列表
	 */
	@RequestMapping("/shopPromoCode")
	public String shopPromoCode(Integer shopId,Model model){
		model.addAttribute("shopId", shopId);
		return "line/shopPromoCode";
	}
	
	@RequestMapping("/findShopPromoCode")
	public Map<String,Object> findShopPromoCode(Integer shopId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Shop> list = lineService.findShopPromoCode(shopId);
		map.put("rows", list);
		map.put("total", list.size());
		return map;
	}
	
	/**
	 * 优惠码Detail
	 */
	@RequestMapping("/shopPromoCodeDetail")
	public String shopPromoCodeDetail(Model model,String importDate){
		model.addAttribute("importDate", importDate);
		return "line/shopPromoCodeDetail";
	}
	
	@RequestMapping("/findShopPromoCodeDetail")
	public Map<String,Object> findShopPromoCodeDetail(String num,String importDate,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findShopPromoCodeDetail(num,importDate, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	/**
	 * 门店维护
	 * @return
	 */
	@RequestMapping("/shopList")
	public String shopList(){
		return "line/shopList";
	}
	
	@RequestMapping("/findShopList")
	public Map<String,Object> findShopList(Shop shop,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", lineService.findShopList(shop, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	@RequestMapping("/updateShopPage")
	public String updateShopPage(Model model,Integer id){
		model.addAttribute("shop", lineService.findShopById(id));
		return "line/shop";
	}

}
