package net.mycomp.veoo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

public class VeooScheduler {
	
	private static final Logger logger = Logger
			.getLogger(VeooScheduler.class);

	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSVeooService jmsVeooService;
	
	
	@Scheduled(cron="${veoo.tigo.hn.renewal.cron}")
	public void sendRenewalBilled(){	
		int counter=0;
		try{
			
			List<Integer> operatorIds=new ArrayList<Integer>();
			operatorIds.add(MConstants.VEOO_HONDURAS_TIGO_OPERATOR_ID);
			operatorIds.add(MConstants.VEOO_COSTA_RICA);
		//	operatorIds.add(MConstants.VEOO_NICARAGUA_CLARO_OPERATOR_ID);
		//	operatorIds.add(MConstants.VEOO_NICARAGUA_MOVISTAR_OPERATOR_ID);
			List<Integer> subscriberIds=
				daoService.findValidationExpiredSubscriberIdForRenewal(
						operatorIds,
							MConstants.SUBSCRIBED);
			
			logger.info("sendRenewalBilled:::::::start:subscriber list::  "+subscriberIds.size());
			
			long time=System.currentTimeMillis();
			logger.info("sendAlertOnCompletionFreePeriod:::::start:::subscriber list size:: "+subscriberIds);
			for(Integer subscriberId:subscriberIds){
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(MConstants.RENEW);
				suscriberIdMsg.setSubscriberId(subscriberId);
				jmsVeooService.sendRenewalContentMsg(suscriberIdMsg);
				counter++;				
			}	
			logger.info("sendRenewalBilled:::::::::end, total send:: "+counter+":total time::  "+(System.currentTimeMillis()-time));
			
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}
	
	
	@Scheduled(cron="${veoo.tigo.nicargua.renewal.cron}")
	public void sendRenewalNicarguaBilled(){	
		int counter=0;
		try{			
			List<Integer> operatorIds=new ArrayList<Integer>();
			//operatorIds.add(MConstants.VEOO_HONDURAS_TIGO_OPERATOR_ID);
			//operatorIds.add(MConstants.VEOO_COSTA_RICA);
			operatorIds.add(MConstants.VEOO_NICARAGUA_CLARO_OPERATOR_ID);
			operatorIds.add(MConstants.VEOO_NICARAGUA_MOVISTAR_OPERATOR_ID);
			List<Integer> subscriberIds=
				daoService.findValidationExpiredSubscriberIdForRenewal(
						operatorIds,
							MConstants.SUBSCRIBED);
			
			logger.info("sendRenewalBilled:::::::start:subscriber list::  "+subscriberIds.size());
			
			long time=System.currentTimeMillis();
			logger.info("sendAlertOnCompletionFreePeriod:::::start:::subscriber list size:: "+subscriberIds);
			for(Integer subscriberId:subscriberIds){
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(MConstants.RENEW);
				suscriberIdMsg.setSubscriberId(subscriberId);
				jmsVeooService.sendRenewalContentMsg(suscriberIdMsg);
				counter++;				
			}	
			logger.info("sendRenewalBilled:::::::::end, total send:: "+counter+":total time::  "+(System.currentTimeMillis()-time));
			
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}
}
