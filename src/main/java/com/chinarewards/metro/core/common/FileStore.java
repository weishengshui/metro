package com.chinarewards.metro.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileStore {

	public static final String UPLOADED_FILE_PATH = "path";
	public static final String UPLOADED_FILE_CONTENT_TYPE = "contentType";
	public static final String UPLOADED_FILE_ORIG_FILE_NAME = "originalFilename";
	public static final String UPLOADED_FILE_FILE_NAME = "name";
	public static final String UPLOADED_FILE_ORIG_FILE_SIZE = "size";

	private Map<String/* fileId in temp container */, Map<String, String>> filesMap = new HashMap<String, Map<String, String>>();

	/**
	 * Obtains uploaded file info
	 * 
	 * @param id
	 * @return
	 */
	public FileItem findFileItemById(String id) {

		if (!filesMap.containsKey(id)) {
			return null;
		}
		Map<String, String> uploadedFileInfo = filesMap.get(id);
		System.out.println("uploadfileInfo id"
				+ uploadedFileInfo.get(UPLOADED_FILE_ORIG_FILE_SIZE));
		File file = new File(uploadedFileInfo.get(UPLOADED_FILE_PATH));

		if (!file.exists()) {
			return null;
		}
		try {
			InputStream inputStream = new FileInputStream(file);
			return new FileItem(id,
					uploadedFileInfo.get(UPLOADED_FILE_ORIG_FILE_NAME),
					uploadedFileInfo.get(UPLOADED_FILE_CONTENT_TYPE),
					inputStream, uploadedFileInfo.get(UPLOADED_FILE_FILE_NAME),
					Long.parseLong(uploadedFileInfo
							.get(UPLOADED_FILE_ORIG_FILE_SIZE)),
					uploadedFileInfo.get(UPLOADED_FILE_PATH));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * insert uploaded file to contains
	 * 
	 * @param id
	 * @param uploadedFileInfo
	 */
	public void insert(String id, Map<String, String> uploadedFileInfo) {
		if (filesMap.containsKey(id)) {

			throw new IllegalArgumentException(
					"File id ["
							+ id
							+ "]exists alreadly,If want modify please remove after insert it!");
		}
		filesMap.put(id, uploadedFileInfo);
	}

	/**
	 * Move file from contains
	 * 
	 * @param id
	 */
	public void remove(String id) {
		if (!filesMap.containsKey(id)) {
			return;
		}
		Map<String, String> uploadedFileInfo = filesMap.get(id);
		File file = new File(uploadedFileInfo.get(UPLOADED_FILE_PATH));
		if (!file.exists()) {
			return;
		}
		file.delete();
	}

	public class FileItem {

		public FileItem(String id, String originalFilename, String contentType,
				InputStream inputStream, String name, long size, String url) {
			super();
			this.id = id;
			this.originalFilename = originalFilename;
			this.contentType = contentType;
			this.inputStream = inputStream;
			this.name = name;
			this.size = size;
			this.url = url;
		}

		private String id;

		private String originalFilename;

		private String contentType;

		private InputStream inputStream;

		private String name;

		private long size;

		private String url;

		public String getOriginalFilename() {
			return originalFilename;
		}

		public void setOriginalFilename(String originalFilename) {
			this.originalFilename = originalFilename;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	public Map<String, Map<String, String>> getFilesMap() {
		return filesMap;
	}

}
