package net.mycomp.messagecloud.gateway;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class MCGRenewalScheduler {

	private static final Logger logger = Logger.getLogger(MCGRenewalScheduler.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSMCGService jmsMCGService;
	
	@Scheduled(cron="${mcg.renewal.cron.scheduler}")
	public void sendRenewalBilled(){	
		
		try{
			
			List<Integer> listOperator=new ArrayList<Integer>();
			listOperator.add(MConstants.MESSAGE_CLOUD_GATWAY_CH);
			
			List<Integer> subscriberIds=
				daoService.findValidationExpiredSubscriberIdForRenewal(listOperator,
							MConstants.SUBSCRIBED,4);	
			
			logger.info("sendRenewalBilled:::::::start:subscriber list::  "+subscriberIds.size()
					+" , subscriberIds "+subscriberIds);
			long time=System.currentTimeMillis();
			logger.info("sendAlertOnCompletionFreePeriod:::::start:::subscriber list size:: "+subscriberIds);
			int i=0;
			for(Integer subscriberId:subscriberIds){
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(MConstants.RENEW);
				suscriberIdMsg.setSubscriberId(subscriberId);
				jmsMCGService.sendRenewal(suscriberIdMsg);
				i++;
				if(i>70){
					break;
				}
			 }	
			logger.info("sendRenewalBilled:::::::::total send renewal: "+i+" end:total time::  "+(System.currentTimeMillis()-time));
			
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}

	
	
	  
	
}
