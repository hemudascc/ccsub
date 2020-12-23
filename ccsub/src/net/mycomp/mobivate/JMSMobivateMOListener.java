package net.mycomp.mobivate;


import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSMobivateMOListener implements MessageListener {

			private static final Logger logger = Logger
					.getLogger(JMSMobivateMOListener.class);

			@Autowired
			private IDaoService daoService;
			
			@Autowired
			private JPASubscriberReg jpaSubscriberReg; 
			
			@Autowired
			private MobivateSmsService mobivateSmsService;
			
			@Autowired
			private RedisCacheService  redisCacheService;
			
			@Autowired
			private SubscriberRegService subscriberRegService; 
			
			
			@Override
			public void onMessage(Message m) {
				
				MobivateMO mobivateCellcMO=null;
				
				boolean update=false;
				long time=System.currentTimeMillis();
				
				MobivateServiceConfig mobivateServiceConfig=null;
				String msg=null;
				String action=null;
				try{
					
					ObjectMessage objectMessage = (ObjectMessage) m;
					mobivateCellcMO = (MobivateMO) objectMessage
							.getObject();
					logger.info("onMessage:: mobivateCellcMO:::: "+mobivateCellcMO);
					
					 action=MobivateConstant.findAction(mobivateCellcMO.getMessageText());
					if(action.equalsIgnoreCase(MConstants.DCT)){
						final String textMsg=mobivateCellcMO.getMessageText();
						final String shortCode=mobivateCellcMO.getRecipient();
						mobivateServiceConfig=MobivateConstant.
						 mapProductIdToMobivateCellcConfig.entrySet().stream().
						 filter(a->shortCode.equalsIgnoreCase(a.getValue().getShortcode())&&textMsg.toLowerCase()
								 .contains(a.getValue().getKeyword().toLowerCase())).
								 findFirst().get().getValue();
						
					}else{
					 mobivateServiceConfig=MobivateConstant.
							 mapProductIdToMobivateCellcConfig.
							 get(mobivateCellcMO.getCampaign());
					}
					
					
					CGToken cgToken=new CGToken("");
					MobivateCGCallback mobivateCGCallback=(MobivateCGCallback)
							 redisCacheService.
						getObjectCacheValue(MobivateConstant.CG_CALLBACK_PREFIX+mobivateCellcMO.getOriginator()
								);
					 if(mobivateCGCallback!=null){
						 cgToken=new CGToken(mobivateCGCallback.getToken());
					 }
					 
					logger.info("onMessage:: action:::: "+action);
					 SubscriberReg subscriberReg=
							jpaSubscriberReg.
							findSubscriberRegByMsisdnAndServiceId(
									mobivateCellcMO.getOriginator(),
									mobivateServiceConfig.getServiceId());
					
					
					if(action.equalsIgnoreCase(MConstants.ACT)){
						
						if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
							msg=mobivateServiceConfig.getAlreadySubscribedMessageTemplate();
							action=MConstants.ALREADY_SUBSCRIBED;
						}else{
//							
//							 subscriberReg= subscriberRegService.findOrCreateSubscriberByAct(
//									 mobivateCellcMO.getOriginator()
//									, null, 0,MConstants.ACT,mobivateServiceConfig.getOpId(), null,
//									1, 0, mobivateServiceConfig.getServiceId(),
//									mobivateCellcMO.getFormatedDate(),null,null,
//									mobivateServiceConfig.getServiceId());//product id==service id
//							 LiveReport(int operatorId, Timestamp timestamp,
//										Integer adnetworkCampaignId,int serviceId,int productId)
										
								LiveReport liveReport=new LiveReport(mobivateServiceConfig.getCcOpId(),
										   new Timestamp(System.currentTimeMillis()),
										   cgToken.getCampaignId()
										   ,mobivateServiceConfig.getServiceId(),
										   mobivateServiceConfig.getCcProductId());
								
										   liveReport.setNoOfDays(mobivateServiceConfig.getValidity());						   
										    subscriberReg=  subscriberRegService
										    		.findOrCreateSubscriberByAct(mobivateCellcMO.getOriginator()
										    				,null, liveReport);
								
										   
							 mobivateCellcMO.setToken(cgToken.getCGToken());
							 mobivateCellcMO.setTokenId(cgToken.getTokenId());
							 
							mobivateSmsService.sendMessage(
									mobivateCellcMO.
									getOriginator(),mobivateServiceConfig.getWelcomeMessageTemplate()
									, mobivateServiceConfig,MConstants.ACT,mobivateCellcMO.getFormatedDate());	
							
							mobivateSmsService.sendBilledMessage(mobivateServiceConfig,
									mobivateCellcMO.getToken()
									, MConstants.ACT,mobivateCellcMO.getOriginator(),
									mobivateCellcMO.getProvider(),mobivateCellcMO.getFormatedDate());	
							
							
						}
					}else if(action.equalsIgnoreCase(MConstants.DCT)){
						if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
							subscriberReg.setStatus(MConstants.UNSUBSCRIBED);
							subscriberReg.setStatusDescp(MConstants.UNSUBSCRIBED_DESC);
							subscriberReg.setUnsubDate(mobivateCellcMO.getCreateTime());
							jpaSubscriberReg.save(subscriberReg);
							msg=mobivateServiceConfig.getUnsubMsgTemplate();
							
						}else{
							action="NOT_SUBCRIBED";
							msg=mobivateServiceConfig.getUnsubNotSubscribedMsgTemplate();
						}
					}
					
				} catch (Exception ex) {
					logger.error("onMessage::::: ", ex);
				} finally {
					if(msg!=null){
						mobivateSmsService.sendMessage(
								mobivateCellcMO.
								getOriginator(),msg
								, mobivateServiceConfig,action,mobivateCellcMO.getFormatedDate());
					}
					update = daoService.saveObject(mobivateCellcMO);	
					logger.info("onMessage::::::::::::::::: :: update::live report "
							+ update + ", total time:: "
							+ (System.currentTimeMillis() - time));
				}
			}
		}
