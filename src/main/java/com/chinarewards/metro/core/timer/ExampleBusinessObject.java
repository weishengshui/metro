package com.chinarewards.metro.core.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExampleBusinessObject {
	
	
	public void doIt(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("ExampleBusinessObject doIt() time is "+dateFormat.format(new Date()));
	}
}
