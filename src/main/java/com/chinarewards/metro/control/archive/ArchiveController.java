package com.chinarewards.metro.control.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.FileStore;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.service.IFileItemService;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;

@Controller
@RequestMapping(value = "/archive")
public class ArchiveController {

	@Autowired
	IFileItemService fileItemService;

	/**
	 * 根据图片的id展示图片
	 * 
	 * @param response
	 * @param session
	 * @param imageId
	 *            in temp or database primary key
	 * @param source
	 *            temp means iamgeId from temp others from database
	 */
	@RequestMapping(value = "/image/show/{source}/{imageId}")
	public void showImage(HttpServletResponse response, HttpSession session,
			@PathVariable String imageId, @PathVariable String source) {

		InputStream inputStream = null;
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			response.reset();
			String contentType = null;

			if (source != null && source.equals("merchandise")) { //展示商品图片
				MerchandiseFile fileItem = fileItemService.getMerchandiseFileById(imageId);
				if(null != fileItem){
					inputStream = new FileInputStream(new File(Constants.MERCHANDISE_IMAGE_DIR+fileItem.getUrl()));
					contentType = fileItem.getMimeType();
				}
			} else if(null != source && source.equals("brand")){ //展示品牌logo
				FileItem fileItem = fileItemService.findFileItemById(imageId);
				if(null != fileItem){
					inputStream = new FileInputStream(new File(Constants.BRAND_IMAGE_DIR+fileItem.getUrl())); 
					contentType = fileItem.getMimeType();
				}
			}// TODO
			response.setContentType(contentType);
			int n = 0;
			if(null != inputStream){
				while ((n = inputStream.read()) != -1) {
					outputStream.write(n);
				}
			}
		} catch (IOException e) {
			System.err.println("IO exception happended~!" + e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					inputStream = null;
				}
			}
		}
	}

	/**
	 * Upload image files
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/upload", method = { RequestMethod.POST })
	public @ResponseBody
	String imageUpload(HttpServletRequest req, HttpSession session, Model model)
			throws IOException {

		// 创建一个通用的多部分解析器.
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				req.getSession().getServletContext());
		// 设置编码
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		// 判断是否有文件上传
		MultipartHttpServletRequest request;
		if (commonsMultipartResolver.isMultipart(req)) {
			request = commonsMultipartResolver.resolveMultipart(req);
		} else {
			return "{error:'not found'}";
		}
		org.springframework.util.MultiValueMap<String, MultipartFile> multipartFiles = request
				.getMultiFileMap();

		System.out.println("upload size " + multipartFiles.size());

		FileStore fileStore = (FileStore) session
				.getAttribute(Constants.UPLOADED_ARCHIVES);
		if (fileStore == null) {
			fileStore = new FileStore();
			session.setAttribute(Constants.UPLOADED_ARCHIVES, fileStore);
		}

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		String tempDir = Constants.UPLOAD_TEMP_BASE_DIR + "/"
				+ fmt.format(new Date());
		String fileId = "";

		for (String key : multipartFiles.keySet()) {

			Map<String, String> uploadedInfo = new HashMap<String, String>();

			MultipartFile multipartFile = multipartFiles.getFirst(key);
			fileId = saveInTemp(tempDir, multipartFile);

			uploadedInfo.put(FileStore.UPLOADED_FILE_CONTENT_TYPE,
					multipartFile.getContentType());

			uploadedInfo.put(FileStore.UPLOADED_FILE_FILE_NAME,
					multipartFile.getName());
			uploadedInfo.put(FileStore.UPLOADED_FILE_ORIG_FILE_NAME,
					multipartFile.getOriginalFilename());
			uploadedInfo.put(FileStore.UPLOADED_FILE_ORIG_FILE_SIZE,
					String.valueOf(multipartFile.getSize()));
			uploadedInfo.put(FileStore.UPLOADED_FILE_PATH, tempDir + "/"
					+ fileId);

			fileStore.insert(fileId, uploadedInfo);
			break;
		}

		return "{msg:'" + fileId + "'}";
	}

	private String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			// URLConnection uc = new URL("file://" +
			// file.getAbsolutePath()).openConnection();
			// String mimetype = uc.getContentType();
			// MimetypesFIleTypeMap gives PNG as application/octet-stream, but
			// it seems so does URLConnection
			// have to make dirty workaround
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		System.out.println("mimetype: " + mimetype);
		return mimetype;
	}

	private String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		System.out.println("suffix: " + suffix);
		return suffix;
	}

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
}
