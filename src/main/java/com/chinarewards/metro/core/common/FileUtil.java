package com.chinarewards.metro.core.common;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;


public class FileUtil {

	public static Object readCSV(File csv, CsvPreference cp, boolean hasHeader) throws Exception {
		return readCSV(new FileInputStream(csv), cp, hasHeader);
	}
	
	/**
	 * 读取CVS
	 * @param is
	 * @param cp
	 * @param hasHeader
	 * @return
	 * @throws Exception
	 */
	public static Object readCSV(InputStream is, CsvPreference cp, boolean hasHeader) throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		CsvListReader reader = new CsvListReader(br, cp);
		if (hasHeader)
			reader.getHeader(true);
		List<String> line = reader.read();
		try {
			while (line != null) {
				list.add(line.toArray(new String[] {}));
				line = reader.read();
			}
		} catch (Exception e) {
			return line.get(0); // 返回错误行的开头信息
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		}
		return list;
	}
	
	
	/**
	 * 判断路径是否存在，不存在就创建
	 * 
	 * @param path
	 */
	public static void pathExist(String path) {
		if (path.contains(".")) {
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 保存文件
	 * @param is
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(InputStream is,String path, String fileName) throws IOException {
		pathExist(path);
		OutputStream os = new FileOutputStream(new File(path+fileName));
		int bytesRead = 0;
		byte[] buffer = new byte[1024];
		while ((bytesRead = is.read(buffer, 0, 1024)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		is.close();
	}
	
	/**
	 * 输出
	 * @param mFile
	 * @param fileNewName
	 * @throws IOException 
	 */
	public static void outPic(MultipartFile mFile,String path,String fileNewName,HttpServletResponse response) throws IOException{
		BufferedImage sourceImg = ImageIO.read(mFile.getInputStream()); //获取图片尺寸
        Integer width = sourceImg.getWidth() + 30;
        Integer height = sourceImg.getHeight() + 50;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", mFile.getOriginalFilename());
		map.put("size", mFile.getSize());
		map.put("url", "javascript:show('"+CommonUtil.getRequest().getContextPath()+"/line/shopPicShow?path="+path+"&fileName="+fileNewName+"',"+width+","+height+")");
		map.put("thumbnail_url", CommonUtil.getRequest().getContextPath()+"/line/showGetthumbPic?path="+path+"&fileName="+fileNewName);
		map.put("delete_url", "upload?delfile=1");
		map.put("delete_type", "GET");
		List<Object> list = new ArrayList<Object>();
		list.add(map);
		PrintWriter writer = response.getWriter();
        response.setContentType("text/html;charset=utf-8");
        writer.write(CommonUtil.toJson(list));
        writer.close();
	}
	/**
	 * 删除文件/图片
	 * @param path
	 * @param fileName
	 */
	public static void removeFile(String path,String fileName){
		File file = new File(Constants.SHOP_IMG + fileName);
		if(file.exists()){
			file.delete();
		}
	}
}
