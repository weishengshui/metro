package com.chinarewards.metro.domain.merchandise;

import java.util.HashMap;
import java.util.Map;

public enum MerchandiseStatus {

	// 上架状态
	ON,

	// 下架状态
	OFF;
	
	private static final Map<String, MerchandiseStatus> stringToEnum = new HashMap<String, MerchandiseStatus>();
	static{
		for(MerchandiseStatus status: values()){
			stringToEnum.put(status.toString(), status);
		}
	}
	
	public static MerchandiseStatus fromString(String str){
		return stringToEnum.get(str);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
