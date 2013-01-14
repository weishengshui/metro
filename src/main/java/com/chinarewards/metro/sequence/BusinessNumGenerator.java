package com.chinarewards.metro.sequence;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessNumGenerator implements IBusinessNumGenerator {

	@Autowired
	private SequenceFactory sequenceFactory;

	public String getTransactionNO() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String str = fmt.format(new Date());
		long vl = sequenceFactory.getTransactionNoGenerator().nextLongValue();
		long tunc = vl % 10000000;
		return str + tunc;
	}

	public String getMemberAccountNo() {
		long seqId = sequenceFactory.getAccountNoGenerator().nextLongValue();
		if (seqId >= 10000000000l) {
			throw new IllegalStateException(
					"Generating account no out of max settings");
		}
		return String.valueOf(10000000000l + seqId).replaceFirst("1", "M");
	}

	public SequenceFactory getSequenceFactory() {
		return sequenceFactory;
	}

	public void setSequenceFactory(SequenceFactory sequenceFactory) {
		this.sequenceFactory = sequenceFactory;
	}
}
