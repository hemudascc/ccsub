package net.mycomp.mobivate;


import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMobivateSMSTrans;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSMobivateSMSDlrListener implements MessageListener {

			private static final Logger logger = Logger
					.getLogger(JMSMobivateSMSDlrListener.class);

			@Autowired
			private IDaoService daoService;
			
			@Autowired
			private SubscriberRegService subscriberRegService; 
			
			@Autowired
			private JPAMobivateSMSTrans jpaMobivateSMSTrans; 
			
			@Autowired
			private LiveReportFactoryService liveReportFactoryService;
			
			@Autowired
			private RedisCacheService  redisCacheService;
			
			@Override
			public void onMessage(Message m) {
				
				MobivateSMSDlr mobivateSMSDlr=null;
				
				boolean update=false;
				long time=System.currentTimeMillis();
				LiveReport liveReport=null;
				CGToken cgToken=null;
				VWServiceCampaignDetail vwServiceCampaignDetail=null;
				try{
					ObjectMessage objectMessage = (ObjectMessage) m;
					mobivateSMSDlr = (MobivateSMSDlr) objectMessage
							.getObject();
					logger.info("onMessage:: mobivateSMSDlr:::: "+mobivateSMSDlr);
					
					
					MobivateSMSTrans mobivateSMSTrans=
							jpaMobivateSMSTrans.findNumeroMobivateSMSTransById(MUtility.toInt(
									mobivateSMSDlr.getDlrReference(),0));
					
					if(Objects.isNull(mobivateSMSTrans)){
						cgToken = new CGToken(mobivateSMSDlr.getDlrReference());
					}else {
						cgToken=new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(MobivateConstant.TOKEN_MSISDN_CHACHE_PREFIX+mobivateSMSDlr.getMsisdn())));
					}
					if(cgToken.getCampaignId()<0) {
						cgToken = new CGToken("-1c-1c228"); 
					}
					vwServiceCampaignDetail = 
							MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
					
					MobivateServiceConfig mobivateServiceConfig=
							MobivateConstant.mapServiceIdToMobivateServiceConfig.
							get(vwServiceCampaignDetail.getServiceId());
					
					liveReport=new LiveReport(
							mobivateServiceConfig.getCcOpId(), 
							MobivateConstant.getTimeByZone(mobivateServiceConfig.getTimeZone()),
							cgToken.getCampaignId()
							,mobivateServiceConfig.getServiceId()
							,mobivateServiceConfig.getServiceId());//product id==service id
					
					liveReport.setMsisdn(mobivateSMSDlr.getMsisdn());
					liveReport.setResponse(mobivateSMSDlr.toString());
					
					if(mobivateSMSDlr.getDlrResult().
							equalsIgnoreCase(MobivateConstant.CG_STTAUS_SUCCESS)
					/* &&mobivateSMSDlr.getAction().equalsIgnoreCase(MConstants.ACT) */){
						liveReport.setAction(MConstants.ACT);
						liveReport.setConversionCount(1);
						liveReport.setAmount(mobivateServiceConfig.getBillingAmount());
						liveReport.setNoOfDays(mobivateServiceConfig.getValidity());
                   }			
 			
				} catch (Exception ex) {
					logger.error("onMessage::::: ", ex);
				} finally {
					try{
						
						if(liveReport.getAction()!=null)
						{				
						liveReport=liveReportFactoryService.process(liveReport);
						}
						logger.info("onMessage::::::::::::::::: ::::   ,after liveReport "+liveReport);	
						}catch(Exception ex){
							logger.error(" fianlly liveReport:: "+liveReport +", : numeroMobivateCellcCGCallback:: "
						
									+mobivateSMSDlr);					
							logger.error("onMessage::::::::::finally " + ex);
						}finally{
							update = daoService.saveObject(mobivateSMSDlr);	
						}
						
					logger.info("onMessage::::::::::::::::: :: update::live report "
							+ update + ", total time:: "
							+ (System.currentTimeMillis() - time));
				}
			}
		}
