package net.mycomp.veoo;

import java.sql.Timestamp;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSVeooRenewalContentListener implements MessageListener {

			private static final Logger logger = Logger
					.getLogger(JMSVeooRenewalContentListener.class);

		
			@Autowired
			private JPASubscriberReg jpaSubscriberReg;
			
			@Autowired
			private IDaoService daoService;
			
			@Autowired
			private VeooService veooService;
			
			@Autowired
			private VeooApiService veooApiService;
			
			
			@Override
			public void onMessage(Message m) {
				
				SuscriberIdMsg suscriberIdMsg=null;
				
				try{
					
					ObjectMessage objectMessage = (ObjectMessage) m;
					suscriberIdMsg = (SuscriberIdMsg) objectMessage
							.getObject();
					
					if(suscriberIdMsg.getAction().equalsIgnoreCase(MConstants.RENEW)){
						
					SubscriberReg subscriberReg=
							jpaSubscriberReg.findSubscriberRegById(suscriberIdMsg.getSubscriberId());	
					 
					
					int dayDiff= MUtility.
							noOfDaysDiffrence(new Timestamp(System.currentTimeMillis()),
									subscriberReg.getValidityTo());
					
					if(subscriberReg.getStatus()==MConstants.SUBSCRIBED&&
							!veooService.checkBlocking(subscriberReg.getMsisdn())
							&&dayDiff<=0){						
						VeooServiceConfig veooServiceConfig=VeooConstant.
								mapServiceIdToVeooServiceConfig.get(subscriberReg.getServiceId());
						logger.info("sendRenewalBilled::::::::trying to send msisdn::  "+subscriberReg.getMsisdn());
						veooApiService.sendPremiumMTMessage(MConstants.RENEW, veooServiceConfig, subscriberReg.getMsisdn());
						logger.info("sendRenewalBilled::::::::sent msisdn::  "+subscriberReg.getMsisdn());
						subscriberReg.setLastRenewalRetryDate(new Timestamp(System.currentTimeMillis()));
						daoService.updateObject(subscriberReg);
					}
				}
    				} catch (Exception ex) {
					logger.error("onMessage::::: ", ex);
				} 
			}
		}
