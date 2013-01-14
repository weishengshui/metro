package com.chinarewards.metro.domain.member;

public enum IntegralStatus {
	
	UNDO, // 撤销
	
	/**
	 * 获得积分的状态
	 */
	TO_ACCOUNT, // 到账
	FROZEN, // 冻结
	

	/**
	 * 使用积分的状态
	 */
	COMPLETED //完成

}
