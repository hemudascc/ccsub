package net.mycomp.mcarokiosk.malaysia;

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

public class MKMalaysiaRenewalScheduler {

	private static final Logger logger = Logger.getLogger(MKMalaysiaRenewalScheduler.class);
	
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSMalaysiaService jmsMalaysiaService;
	
	
	
	
	@Scheduled(cron="${macrokiosk.malaysia.digi.renewal.cron.scheduler}")
	public void sendAisRenewalBilled(){	
		
		try{
			
			List<Integer> listOperator=new ArrayList<Integer>();
			listOperator.add(MConstants.MK_MALAYSIA_DIGI_OPERATOR_ID);
		
			List<Integer> subscriberIds=
				daoService.findValidationExpiredSubscriberIdForRenewal(listOperator,
							MConstants.SUBSCRIBED);	
			
			logger.info("sendRenewalBilled:::::::start:subscriber list::  "+subscriberIds.size());
			long time=System.currentTimeMillis();
			for(Integer subscriberId:subscriberIds){
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(MConstants.RENEW);
				suscriberIdMsg.setSubscriberId(subscriberId);
				jmsMalaysiaService.sendRenewal(suscriberIdMsg);
			}	
			logger.info("sendRenewalBilled:::::::::end:total time::  "+(System.currentTimeMillis()-time));
			
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}
}
