package com.chinarewards.metro.sms;

import java.util.List;

public interface IProviderService {

	ISMSSendQueue getSendQueue(String code);

	ISMSReceiveQueue getReceiveQueue(String code);

	List<String> getAllProviders();

	void init();

	void destroy();
}
