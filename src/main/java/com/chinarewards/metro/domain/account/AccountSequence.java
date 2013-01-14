package com.chinarewards.metro.domain.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountSequence {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long sequenceId;

	public long getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(long sequenceId) {
		this.sequenceId = sequenceId;
	}

}
