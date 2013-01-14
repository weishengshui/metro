package com.chinarewards.metro.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.merchandise.MerchandiseFile;
import com.chinarewards.metro.domain.user.UserInfo;

@Component
public class FileItemService implements IFileItemService {
	
	@Autowired
	private HBDaoSupport hbDaoSupport;

	public FileItem findFileItemById(String fileId) {

		return hbDaoSupport.findTById(FileItem.class, fileId);
	}

	public FileItem saveFileItem(UserInfo userInfo, FileItem fileItem) {
		return hbDaoSupport.save(fileItem);
	}

	@Override
	public List<FileItem> getMerchandiseFileItems(String merchandiseId) {
		List<FileItem> list = hbDaoSupport
				.findTsByHQL(
						"SELECT m.fileItem FROM MerchandiseFile m WHERE m.merchandise.id=?",
						(Object) merchandiseId);
		return list;
	}
	
	@Override
	public List<FileItem> getMerchantFileItems(String merchantId) {
		List<FileItem> list = hbDaoSupport
				.findTsByHQL(
						"SELECT m.fileItem FROM MerchantFile m WHERE m.merchant.id=?",
						merchantId);
		return list;
	}

	@Override
	public MerchandiseFile getMerchandiseFileById(String imageId) {
		
		return hbDaoSupport.findTById(MerchandiseFile.class, imageId);
	}

}
