package net.mycomp.actel;


import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;


import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSActelDlrListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSActelDlrListener.class);

	
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private ActelApiService actelApiService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
   @Autowired
   private RedisCacheService redisCacheService; 
	
  
	@Override
	public void onMessage(Message m) {

		ActelDlr actelDlr = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		ActelNewServiceConfig actelServiceConfig =null;
		String msg=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			actelDlr = (ActelDlr) objectMessage
					.getObject();
			//if("Etisalat".equalsIgnoreCase(actelDlr.getOperator())) {
				cgToken=new CGToken(Objects.toString(
						redisCacheService.getObjectCacheValue(ActelConstant.ACTEL_OTP_VALIDATION_CACHE
								+actelDlr.getMsisdn())));
				actelDlr.setToken(cgToken.getCGToken());
				
				 for(ActelNewServiceConfig tmpActelServiceConfig:ActelConstant.listActelNewConfig){
					 if(tmpActelServiceConfig.getApiAppid().equalsIgnoreCase(actelDlr.getIdApplication())
							 &&(tmpActelServiceConfig.getOperatorId()+"").equalsIgnoreCase(actelDlr.getOpid())
							 &&tmpActelServiceConfig.getValidityDesc().equalsIgnoreCase("weekly")){
						 actelServiceConfig=tmpActelServiceConfig;
					 }
				 }
				
				 Service service=MData.mapServiceIdToService.get(actelServiceConfig.getServiceId());
				 
				liveReport=new LiveReport(service.getOpId(),
						new Timestamp(System.currentTimeMillis()),
						cgToken.getCampaignId(), actelServiceConfig.getServiceId(),
						service.getProductId());
				
				
				/*
				 * }else { cgToken=new CGToken(actelDlr.getClickId());
				 * actelDlr.setToken(cgToken.getCGToken()); VWServiceCampaignDetail
				 * vwserviceCampaignDetail=MData
				 * .mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				 * actelServiceConfig=ActelConstant.mapServiceIdToActelServiceConfig.get(
				 * vwserviceCampaignDetail.getServiceId());
				 * 
				 * liveReport=new LiveReport(MConstants.ACTEL_OPERATOR_DU, new
				 * Timestamp(System.currentTimeMillis()), -1, actelServiceConfig.getServiceId(),
				 * actelServiceConfig.getProductId()); }
				 */
			
			liveReport.setMsisdn(actelDlr.getMsisdn());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setTokenId(cgToken.getTokenId());
			
			actelDlr.setCcMode(ActelMode.getFlowDesc(actelDlr.getFlow()));
			liveReport.setParam1(actelDlr.getCcMode());
			liveReport.setMode(ActelMode.getMode(actelDlr.getFlow()));
			
			liveReport.setResponse(actelDlr.toString());
			logger.debug("actelDlr::::::: "+actelDlr);
			if(actelDlr.getAction()!=null&&actelDlr.getAction().equalsIgnoreCase(ActelConstant.SUB)&&
					actelDlr.getDlrStatus()!=null&&actelDlr.getDlrStatus().equalsIgnoreCase("DELIVERED")){
				liveReport.setAction(MConstants.ACT);
				liveReport.setAmount(MUtility.toDouble(actelDlr.getRate(), 0));		
				liveReport.setNoOfDays(1);
				liveReport.setConversionCount(1);
				msg=actelServiceConfig.getSubMessageTemplate();
							
				//actelApiService.sendCallback(actelServiceConfig, actelDlr);			
				//actelDlr.setCcMode(ActelMode.getFlowDesc(actelDlr.getFlow()));
				//liveReport.setParam1(actelDlr.getCcMode());
				//liveReport.setMode(ActelMode.getMode(actelDlr.getFlow()));
				
			}else if(actelDlr.getAction()!=null&&actelDlr.getAction().equalsIgnoreCase(ActelConstant.SUB)&&
					actelDlr.getDlrStatus()!=null&&actelDlr.getDlrStatus().equalsIgnoreCase("failed")){
				liveReport.setAction(MConstants.GRACE);
				liveReport.setNoOfDays(1);
				liveReport.setGraceConversionCount(1);		
				
			}else if(actelDlr.getAction()!=null&&actelDlr.getAction().equalsIgnoreCase(ActelConstant.UNSUB)){
				liveReport.setAction(MConstants.DCT);	
				liveReport.setDctCount(1);
				
			}else if(actelDlr.getAction()!=null&&actelDlr.getAction().equalsIgnoreCase(ActelConstant.RENEW)
					&&actelDlr.getDlrStatus()!=null&&actelDlr.getDlrStatus().equalsIgnoreCase("DELIVERED")){
				liveReport.setAction(MConstants.RENEW);	
				liveReport.setRenewalAmount(MUtility.toDouble(actelDlr.getRate(), 0));
				liveReport.setNoOfDays(7);
				liveReport.setRenewalCount(1);
				SubscriberReg subscriberReg=jpaSubscriberReg
						.findSubscriberRegByMsisdnAndProductId(actelDlr.getMsisdn(), 
								actelServiceConfig.getProductId());
				
			    if(subscriberReg!=null&&subscriberReg.getParam1()!=null){
			    	liveReport.setParam1(subscriberReg.getParam1());
			    	actelDlr.setCcMode(subscriberReg.getParam1());
			    	liveReport.setMode(subscriberReg.getMode());
			    }
			}			
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				
				liveReportFactoryService.process(liveReport);
				
				if(msg!=null){
					msg=ActelConstant.getMessage(msg,actelDlr.getMsisdn(), liveReport.getSubId());
					logger.info("getMessage:::::::::msg "+msg);
				    actelApiService.pushSms(actelDlr, actelServiceConfig, msg);
				}
			} catch (Exception ex) {
				logger.error(" fianlly " 
						+ ", : actelDlr:: "
						+ actelDlr);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(actelDlr);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
