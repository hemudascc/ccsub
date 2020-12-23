package net.mycomp.ksa;

import java.sql.Timestamp;
import java.util.List;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class KsaScheduler {

	private static final Logger logger = Logger
			.getLogger(KsaServiceApi.class.getName());
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private KsaServiceApi ksaServiceApi;
	
	@Scheduled(cron="${ksa.cron.renewal}")
	public void renewalForLastChargingSuccessWithinNoDays(){	
		
		try{
		
			for(KsaServiceConfig ksaServiceConfig:KsaConstant.listKsaServiceConfig){
				SubscriberReg subscriberReg=jpaSubscriberReg
						.findSubscriberRegById(ksaServiceConfig.getRenewalSubId());
				subscriberReg.setMsisdn(KsaConstant.formatMsisdn(""+KsaConstant.randomPIN()));
				subscriberReg.setValidityFrom(new Timestamp(System.currentTimeMillis()));
				subscriberReg.setValidityTo(MUtility.addNumberOfDay(new Timestamp(System.currentTimeMillis()),
						ksaServiceConfig.getValidity()));
				ksaServiceApi.sendBulkSms(subscriberReg.getMsisdn(), ksaServiceConfig, ksaServiceConfig.getRenewalMsgTemplate()
						);
				jpaSubscriberReg.save(subscriberReg);
			}
			
			
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}
	
	
}
