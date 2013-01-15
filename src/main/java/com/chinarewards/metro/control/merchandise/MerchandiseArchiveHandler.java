//package com.chinarewards.metro.control.merchandise;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import com.chinarewards.metro.core.common.Constants;
//
//import org.apache.commons.io.FileUtils;
//
//public abstract class MerchandiseArchiveHandler {
//
//	/**
//	 * 把用户上传的临时作为商品图片移到商品图片的正式目录，返回移动后的Url
//	 * 
//	 * @param temp
//	 *            file url
//	 * @return moved file url
//	 * @throws IOException
//	 */
//	public static String moveTempAsMerchandiseImage(String url)
//			throws IOException {
//
//		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
//		File source = new File(url);
//
//		String sourceName = source.getName().replaceFirst(
//				Constants.UPLOAD_TEMP_UID_PREFIX, "");
//
//		String destURL = Constants.MERCHANDISE_IMAGE_DIR + "/"
//				+ fmt.format(new Date()) + "/" + sourceName;
//		
//		FileInputStream fis = new FileInputStream(source);
//		FileUtils.copyInputStreamToFile(fis, new File(
//				destURL));
//		fis.close();
//		
//		source.delete(); //图片转移至正式目录后，删除临时图片
//		
//		return destURL;
//	}
//
//	/**
//	 * 删除商品图片在正式目录中
//	 * 
//	 * @param url
//	 * @throws IOException
//	 */
//	public static void destroyMerchandiseImage(String url) throws IOException {
//
//		FileUtils.deleteDirectory(new File(url));
//	}
//}
