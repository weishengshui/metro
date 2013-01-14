package com.chinarewards.metro;

import java.util.HashMap;
import java.util.Map;

import com.chinarewards.metro.sequence.IBusinessNumGenerator;

public class BusinessNumGenerator implements IBusinessNumGenerator {

	private static Map<String, Integer> sequencePool = new HashMap<String, Integer>();

	public String getTransactionNO() {
		if (sequencePool.containsKey("getTransactionNO")) {
			int value = sequencePool.get("getTransactionNO") + 1;
			sequencePool.put("getTransactionNO", value);
			return value + "";
		} else {
			sequencePool.put("getTransactionNO", 1);
			return "1";
		}
	}

	public String getMemberAccountNo() {
		if (sequencePool.containsKey("getMemberAccountNo")) {
			int value = sequencePool.get("getMemberAccountNo") + 1;
			sequencePool.put("getMemberAccountNo", value);
			return value + "";
		} else {
			sequencePool.put("getMemberAccountNo", 1);
			return "1";
		}
	}
}
