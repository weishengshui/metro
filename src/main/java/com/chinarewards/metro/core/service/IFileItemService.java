package com.chinarewards.metro.core.service;

import java.util.List;

import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.user.UserInfo;

public interface IFileItemService {

	/**
	 * 根据ID获得文件信息
	 * 
	 * @param userContext
	 * @param fileId
	 * @return
	 */
	public FileItem findFileItemById(String fileId);

	/**
	 * 保存文件信息
	 * 
	 * @param userContext
	 * @param fileItem
	 * @return
	 */
	public FileItem saveFileItem(UserInfo userInfo, FileItem fileItem);

	/**
	 * 获得商品所有的图片信息
	 * 
	 * @param merchandiseId
	 * @return
	 */
	public List<FileItem> getMerchandiseFileItems(String merchandiseId);

	/**
	 * 获取商户的所有图片信息
	 * 
	 * @param id
	 * @return
	 */
	public List<FileItem> getMerchantFileItems(
			String merchantId);
	
	/**
	 * 根据id查询商品图片
	 * 
	 * @param imageId
	 * @return
	 */
	public MerchandiseFile getMerchandiseFileById(String imageId);

	
}
