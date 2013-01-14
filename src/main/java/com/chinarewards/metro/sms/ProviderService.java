package com.chinarewards.metro.sms;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinarewards.metro.sms.chinatelecom.ChinaTelecomService;
import com.chinarewards.metro.sms.mxtong.MxTongSMSQueue;

@Component
public class ProviderService implements IProviderService {

	protected Logger log = LoggerFactory.getLogger(ProviderService.class);

	List<String> allProviders = Arrays.asList(new String[] { //
			// "xunsai", // xunsai, the fallback
			// "mas", // MAS
			// "mas-uc", // MAS for Unicomm
					"smgp", // China Telecomm
					"mxtong" // mxtong
			});

	// ----- chinamobile (mas, mas-uc)
	// IMASService mas;

	// ----- chinatelecom (smgp)
	ChinaTelecomService ct = new ChinaTelecomService();

	// ----- xunsai (xunsai)
	String SP2URL = "http://www.xunsai.net:8000";
	String SP2CODE = "s0902240448";
	String SP2PASSWORD = "13242903149";
	// ISMSSendQueue xunsai = new XunSaiSMSQueue(SP2URL, SP2CODE, SP2PASSWORD);

	// ----- mxtong
	String userId = "990228";
	String accountId = "admin";
	String password = "NLH14M";

	ISMSSendQueue mxtong = new MxTongSMSQueue(userId, accountId, password);

	@PostConstruct
	public void init() {
		log.debug("QueueProcessor Initializing");
		// mas.init();
		ct.init();
		// xunsai.init();
		mxtong.init();
		log.debug("QueueProcessor Initialized");
	}

	@PreDestroy
	public void destroy() {
		log.debug("QueueProcessor Destroying");
		// mas.destroy();
		ct.destroy();
		// xunsai.destroy();
		mxtong.destroy();
		log.debug("QueueProcessor Destroyed");
	}

	@Override
	public ISMSSendQueue getSendQueue(String codein) {
		ISMSSendQueue ret = null;

		log.debug("ProviderService getSendQueue codein is " + codein);

		String code = codein.trim();

		// if ("xunsai".equals(code)) {
		// log.debug("Xunsai provider (SYNC)");
		// ret = xunsai;
		//
		// } else
		// if ("mas".equals(code)) {
		// log.debug("getSendQueue MAS provider (ASYNC)");
		// //ret = mas;
		//
		// } else if ("mas-uc".equals(code)) {
		// log.debug("MAS provider (UNICOMM)");
		// //ret = mas;

		if ("smgp".equals(code)) {
			log.debug("SMGP provider (ChinaTelecom)");
			ret = ct;

		} else if ("mxtong".equals(code)) {
			log.debug("mxtong provider (MxTong)");
			ret = mxtong;

		}
		return ret;
	}

	@Override
	public ISMSReceiveQueue getReceiveQueue(String codein) {

		log.debug("ProviderService getReceiveQueue codein is " + codein);

		String code = codein.trim();

		ISMSReceiveQueue ret = null;
		// if ("xunsai".equals(code)) {
		// log.debug("Xunsai provider (SYNC)");
		// ret = null;
		//
		// } else
		// if ("mas".equals(code)) {
		// log.debug("getReceive Queue MAS provider (ASYNC)");
		// ret = mas;
		//
		// } else if ("mas-uc".equals(code)) {
		// log.debug("MAS provider (UNICOMM)");
		// ret = mas;

		if ("smgp".equals(code)) {
			log.debug("SMGP provider (ChinaTelecom)");
			ret = ct;

		}
		return ret;
	}

	@Override
	public List<String> getAllProviders() {
		return allProviders;
	}

}
