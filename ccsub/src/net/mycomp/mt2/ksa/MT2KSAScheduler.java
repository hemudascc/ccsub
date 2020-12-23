package net.mycomp.mt2.ksa;

import java.util.ArrayList;
import java.util.List;

import net.common.service.IDaoService;

import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class MT2KSAScheduler {

	
	private static final Logger logger = Logger
			.getLogger(MT2KSAScheduler.class.getName());
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSMt2KSAService jmsMt2KSAService; 
	
	@Scheduled(cron="${mt2.ksa.stc.content.sms.cron}")
	public void sendAlertSMS(){	
		
		try{
			
			List<Integer> listOperator=new ArrayList<Integer>();
			listOperator.add(MConstants.MT2_KSA_STC_OPERATOR_ID);
			
			List<Integer> subscriberIds=daoService.findValidSubscriberId(listOperator
					,MConstants.SUBSCRIBED);	
			
			logger.info("sendAlertSMS:::::::start:subscriber list::  "+subscriberIds.size()
					+" , subscriberIds "+subscriberIds);
			long time=System.currentTimeMillis();
			logger.info("sendAlertOnCompletionFreePeriod:::::start:::subscriber list size:: "+subscriberIds);
			for(Integer subscriberId:subscriberIds){
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(Mt2KSAConstant.SMS_CONTENT_ALERT);
				suscriberIdMsg.setSubscriberId(subscriberId);
				jmsMt2KSAService.sendAlertSubscriberIdMsg(suscriberIdMsg);
			}	
			logger.info("sendAlertSMS:::::::::end:total time::  "+(System.currentTimeMillis()-time));
			
		}catch(Exception ex){
			logger.error("sendAlertSMS:: ",ex);
		}
	}

}
