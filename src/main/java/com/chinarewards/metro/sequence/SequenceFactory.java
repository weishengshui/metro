package com.chinarewards.metro.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceFactory {

	@Autowired 
	@Qualifier("accountNoGenarater")
	private MySQLMaxValueIncrementer accountNoGenerator;

	@Autowired
	@Qualifier("transactionNoGenerator")
	private MySQLMaxValueIncrementer transactionNoGenerator;

	public MySQLMaxValueIncrementer getAccountNoGenerator() {
		return accountNoGenerator;
	}

	public void setAccountNoGenerator(
			MySQLMaxValueIncrementer accountNoGenerator) {
		this.accountNoGenerator = accountNoGenerator;
	}

	public MySQLMaxValueIncrementer getTransactionNoGenerator() {
		return transactionNoGenerator;
	}

	public void setTransactionNoGenerator(
			MySQLMaxValueIncrementer transactionNoGenerator) {
		this.transactionNoGenerator = transactionNoGenerator;
	}

}
