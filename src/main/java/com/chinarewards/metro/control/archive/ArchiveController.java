package com.chinarewards.metro.control.archive;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.FileStore;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.SystemTimeProvider;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.core.service.IFileItemService;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.merchandise.MerchandiseImageType;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.model.common.ImageInfo;

@Controller
@RequestMapping(value = "/archive")
public class ArchiveController {

	@Autowired
	IFileItemService fileItemService;

	public String saveInTemp(String tempDir, MultipartFile multipartFile)
			throws IOException {

		File dir = new File(tempDir);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		String id = Constants.UPLOAD_TEMP_UID_PREFIX + UUIDUtil.generate();
		File file = new File(dir, id);
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);

		return id;
	}

	public IFileItemService getFileItemService() {
		return fileItemService;
	}

	public void setFileItemService(IFileItemService fileItemService) {
		this.fileItemService = fileItemService;
	}
	
	/**
	 * 获取展示图片
	 * 
	 * @param mFile
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/imageShow")
	public void shopPicShow(String path, String fileName, String contentType,
			HttpServletResponse response) throws Exception {
		contentType = contentType.toUpperCase();
		System.out.println("==============sssssaaa=======>  "+contentType);
		path = Dictionary.getPicPath(path);
		if (!"".equals(path)) {
			FileUtil.pathExist(path);
			File file = new File(path, fileName);
			response.setContentType(contentType);
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "inline; filename=\""
					+ file.getName() + "\"");
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
	}
	
	/**
	 * 获取展示的缩略图
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/showGetthumbPic")
	public void showGetthumbPic(String path, String fileName,
			String contentType, HttpServletResponse response) throws Exception {
		System.out.println("=====================>  "+contentType);
		path = Dictionary.getPicPath(path);
		if (!"".equals(path)) {
			FileUtil.pathExist(path);
			File file = new File(path, fileName);
			if (file.exists()) {
				contentType = contentType.toLowerCase();
				if (contentType.endsWith("png") || contentType.endsWith("jpeg")
						|| contentType.endsWith("gif")
						|| contentType.endsWith("jpg")
						|| contentType.endsWith("bmp")) {
					BufferedImage im = ImageIO.read(file);
					if (im != null) {
						BufferedImage thumb = Scalr.resize(im, 75);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						if (contentType.endsWith("png")) {
							ImageIO.write(thumb, "PNG", os);
							System.out.println("***********************************************");
						} else if (contentType.endsWith("jpeg")
								|| contentType.endsWith("jpg")) {
							ImageIO.write(thumb, "JPG", os);
						} else if (contentType.endsWith("bmp")) {
							ImageIO.write(thumb, "BMP", os);
						} else {
							ImageIO.write(thumb, "GIF", os);
						}
						response.setContentType(contentType);
						ServletOutputStream srvos = response.getOutputStream();
						response.setContentLength(os.size());
						response.setHeader("Content-Disposition",
								"inline; filename=\"" + file.getName() + "\"");
						os.writeTo(srvos);
						srvos.flush();
						srvos.close();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteImage")
	@ResponseBody
	public void deleteImage(HttpServletResponse response, HttpSession session,
			String imageSessionName, String key) throws Exception {

		Map<String, Object> images = (Map<String, Object>) session
				.getAttribute(imageSessionName);
		images.remove(key);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("{msg:\'删除成功\'}");
		out.flush();
	}
	
	/**
	 * 将上传文件保存至临时文件夹
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public void imageUpload(@RequestParam MultipartFile file,
			String path, String key, HttpSession session,
			String imageSessionName, HttpServletResponse response) {
		
		System.out.println(" imageUpload path is "+path);
		if (file.isEmpty()) {// 没有数据
			return;
		}

		path = Dictionary.getPicPath(path);
		System.out.println("path is "+path);
		if (!"".equals(path)) {
			FileUtil.pathExist(path);
			PrintWriter out = null;
			if (null == imageSessionName || imageSessionName.isEmpty()) {
				imageSessionName = UUIDUtil.generate();
			}
			try {
				response.setContentType("text/html; charset=utf-8");
				out = response.getWriter();
				String fileName = file.getOriginalFilename();
				String suffix = FileUtil.getSuffix(fileName);
				String fileNewName = Constants.UPLOAD_TEMP_UID_PREFIX
						+ UUIDUtil.generate() + suffix;
				FileUtil.saveFile(file.getInputStream(), path, fileNewName);
				Map<String, FileItem> images = (HashMap<String, FileItem>) session
						.getAttribute(imageSessionName);
				if (null == images) {
					images = new HashMap<String, FileItem>();
				} else {
					FileItem tmpImage = images.get(key);
					if (null != tmpImage) { // 删除之前的遗留图片
						if (tmpImage.getUrl().startsWith(
								Constants.UPLOAD_TEMP_UID_PREFIX)) {
							File tempFile = new File(path, tmpImage.getUrl());
							tempFile.delete();
						}
						images.remove(key);
					}
				}

				FileItem image = new FileItem();
				System.out.println("image is " + image);
				ImageInfo imageInfo = FileUtil.getImageInfo(path, fileNewName);
				image.setWidth(imageInfo.getWidth());
				image.setHeight(imageInfo.getHeight());
				image.setCreatedAt(SystemTimeProvider.getCurrentTime());
				image.setCreatedBy(UserContext.getUserId());
				image.setFilesize(file.getSize());
				image.setLastModifiedAt(SystemTimeProvider.getCurrentTime());
				image.setLastModifiedBy(UserContext.getUserId());
				image.setMimeType(file.getContentType());
				image.setOriginalFilename(fileName);
				image.setUrl(fileNewName);
				if (null == key || key.isEmpty()) {
					key = "A" + UUIDUtil.generate();
				}
				images.put(key, image);

				System.out.println("images size is " + images.size()
						+ " key is " + key + " imageSessionName is "
						+ imageSessionName);
				session.setAttribute(imageSessionName, images);

				out.write("{url:\'" + image.getUrl() + "\',width:\'"
						+ image.getWidth() + "\',height:\'" + image.getHeight()
						+ "\',imageSessionName:\'" + imageSessionName
						+ "\',contentType:\'" + image.getMimeType()
						+ "\',key:\'" + key + "\'}");
				out.flush();
			} catch (FileNotFoundException e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			} catch (IOException e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			} catch (Exception e) {
				out.write(CommonUtil.toJson(new AjaxResponseCommonVo("错误："
						+ e.getMessage())));
				out.flush();
				e.printStackTrace();
			}
		}
	}
}
