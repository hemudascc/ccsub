package net.bizao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class BizaoScheduler {

	private static final Logger logger = Logger
			.getLogger(BizaoScheduler.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private JMSBizaoService jmsBizaoService;
	
	@Autowired
	private BizaoApiService bizaoApiService;
	

	@Scheduled(cron="${bizao.renewal.cron}")
	public void sendRenewalBilled(){	
		
		try{
			
		 List<Integer> subscriberIds=
				daoService.findValidationExpiredSubscriberIdForRenewal(BizaoConstant.operatorIds,
							MConstants.SUBSCRIBED);	
			logger.info("sendRenewalBilled:::::::start:subscriber list::  "+subscriberIds.size());
			long time=System.currentTimeMillis();
			logger.info("sendAlertOnCompletionFreePeriod:::::start:::subscriber list size:: "+subscriberIds);
			for(Integer subscriberId:subscriberIds){
				BizaoSuscriberIdMsg bizaoSuscriberIdMsg=new BizaoSuscriberIdMsg();
				bizaoSuscriberIdMsg.setAction(MConstants.RENEW);
				bizaoSuscriberIdMsg.setSubscriberId(subscriberId);
				jmsBizaoService.sendRenewalRetry(bizaoSuscriberIdMsg);
			}	
			
			logger.info("sendRenewalBilled:::::::::end:total time::  "+(System.currentTimeMillis()-time));
		
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}	
	
	@Scheduled(cron="${bizao.renewal.alert.cron}")
	public void sendAlertOnCompletionFreePeriod(){		
	
		try{
			
		LocalDateTime today =  LocalDateTime.now();     //Today
		LocalDateTime tomorrow = today.plusDays(1);     //Plus 1 day
		
		List<SubscriberReg> listSubscriberReg=
				jpaSubscriberReg.findSubscriberForRenewalAlert
		(BizaoConstant.operatorIds,
				MConstants.SUBSCRIBED,3, Timestamp.from(tomorrow.
						toInstant(ZoneOffset.ofHours(0))));
		
		long time=System.currentTimeMillis();
		logger.info("sendAlert for renewal:::::start:::subscriber list size:: "+listSubscriberReg.size());
		for(SubscriberReg subscriberReg:listSubscriberReg){
			BizaoConfig bizaoConfig=BizaoConstant.mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
			String	msg= BizaoConstant.getMsg(bizaoConfig.getRenewalAlertMsgTemplate(),
					 bizaoConfig, bizaoConfig.getPricePoint(), subscriberReg.getSubscriberId());
			 
			bizaoApiService.sendSms(BizaoConstant.RENEW_ALERT, bizaoConfig.getMsisdnPrefix(), 
							 bizaoConfig, 
							msg, subscriberReg.getParam1(),subscriberReg.getMsisdn(),"");
		}	
		
		logger.info("sendAlertOnCompletionFreePeriod:::::::::end:total time::  "+(System.currentTimeMillis()-time));
		

	}catch(Exception ex){
		logger.error("sendRenewl Alert ",ex);
	}
	}
	
}
