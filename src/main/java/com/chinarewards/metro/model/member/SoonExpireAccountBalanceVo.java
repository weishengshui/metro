package com.chinarewards.metro.model.member;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SoonExpireAccountBalanceVo {
	
	private String months;
	private double units;
	
	public SoonExpireAccountBalanceVo(double units, Date months) throws Exception{
		this.units = units;
		if(null != months){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			this.months = dateFormat.format(months);
		}else{
			this.months = "";
		}
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public double getUnits() {
		return units;
	}

	public void setUnits(double units) {
		this.units = units;
	}
	
	
}
