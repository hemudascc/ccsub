package net.indonesia.triyakom;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;


public class SchedularConfig {
	
	@Autowired
	private IDaoService daoService;
	@Autowired
	private JMSIndonesiaService jmsIndonesiaService;
	@Autowired
	private TriyakomOperatorService triyakomOperatorService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	private static final Logger logger = Logger.getLogger(SchedularConfig.class);
	

	
	@Scheduled(cron="${triyakom.renewal.cron}")
	public void sendRenewalBilled(){	
		
		int subscriberId=0;
		try{
		do{
		
     Pageable pageable = new PageRequest(0, 500, Sort.Direction.ASC, "subscriberId");
		List<SubscriberReg> listSubscriberReg=jpaSubscriberReg
				.findValidationExpiredSubscriberForRenewal(TriyakomConstant.operatorIds,
						MConstants.SUBSCRIBED,subscriberId
	    		,pageable);
		
		logger.info("sendRenewalBilled::::::::subscriber subscriberId: "
		+subscriberId+" list::  "+listSubscriberReg.size());
		
		if(listSubscriberReg==null||listSubscriberReg.size()<=0){
			break;
		}
		
		for(SubscriberReg subscriberReg: listSubscriberReg){
			    subscriberId=subscriberReg.getSubscriberId();			
				SuscriberIdMsg suscriberIdMsg=new SuscriberIdMsg();
				suscriberIdMsg.setAction(MConstants.RENEW);
				suscriberIdMsg.setSubscriberId(subscriberId);	
				subscriberReg.setLastRenewalRetryDate(new Timestamp(System.currentTimeMillis()));
				
				jpaSubscriberReg.save(subscriberReg);
				jmsIndonesiaService.sendRenewalRequest(suscriberIdMsg);		
			}
		}while(true);
		    logger.error("sendRenewalBilled:::::::::end::::::::: "+new Timestamp(System.currentTimeMillis()));
		}catch(Exception ex){
			logger.error("sendRenewalBilled:: ",ex);
		}
	}
	


}
