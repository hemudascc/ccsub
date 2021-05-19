package net.mycomp.du;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSDUChargingCallbackListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSDUChargingCallbackListener.class);

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	
	@Override
	public void onMessage(Message m) {
		
		DUChargingNotification duChargingNotification=null;
		LiveReport liveReport=null;
		boolean update = false;
		long time=System.currentTimeMillis();
		try{
			
		ObjectMessage objectMessage = (ObjectMessage) m;
		 duChargingNotification=(DUChargingNotification)objectMessage.getObject();
		
		 DUConfig duConfig=DUConstant.mapPlanIdToDuConfig.get(duChargingNotification.getRequestPlan());
		 
		 liveReport = new LiveReport(MConstants.DU_OPERATOR_ID,
				 new Timestamp(System.currentTimeMillis()),
					null,duConfig.getServiceId(),0);
		// liveReport.setServiceId(duConfig.getServiceId());
		
//		 Long  counter=redisCacheService.
//				 getAndIcementIntValue(DUCGCallback.class.getName()+duChargingNotification.getSequenceNo(), 1);
//		  logger.info("onMessage:::::::::::::::::key:: "+DUCGCallback.class.getName()+duChargingNotification.getSequenceNo()+" counter:: "+counter);
//		 
//		  if(counter!=null&&counter>1){
//				logger.debug("onMessage:::::::::::::::::"
//						+ " duplicate callback");
//				duChargingNotification.setDuplicate(true);	
//				liveReport.setDuplicateRequest(true);
//		  }
			
			liveReport.setMsisdn(duChargingNotification.getCallingParty());		
			liveReport.setCircleId(0);
			
			liveReport.setResponse(duChargingNotification.toString());
		if(duChargingNotification.getAction().equalsIgnoreCase(MConstants.ACT)){
			
//			DUCGCallback duCgCallback= daoService
//					.findDUCGCallbackByTPCGId(duChargingNotification.getSequenceNo());
//			CGToken cgToken =new CGToken(duCgCallback.getTransId()); 
//			
//		      if(duCgCallback!=null){
//		    	  cgToken =new CGToken(duCgCallback.getTransId()); 
//		    	 liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());
//		    	 liveReport.setTokenId(cgToken.getTokenId());
//		      }else{
			        CGToken  cgToken =new CGToken(duChargingNotification.getSequenceNo()); 
			    	 liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());
			    	 liveReport.setTokenId(cgToken.getTokenId());
		   //   }
		      
		      
			liveReport.setAction(duChargingNotification.getAction());
			liveReport.setConversionCount(1);
			liveReport.setAmount(duChargingNotification.getChargeAmount());
			liveReport.setNoOfDays(MUtility.toInt(duChargingNotification.getValidityDays(),0));
			
		}else if(duChargingNotification.getAction().equalsIgnoreCase(MConstants.DCT)){
			liveReport.setAction(duChargingNotification.getAction());
			liveReport.setDctCount(1);
		}else if(duChargingNotification.getAction().equalsIgnoreCase(MConstants.RENEW)){
			liveReport.setAction(duChargingNotification.getAction());
			liveReport.setRenewalCount(1);
			liveReport.setRenewalAmount(duChargingNotification.getChargeAmount());
			liveReport.setNoOfDays(MUtility.toInt(duChargingNotification.getValidityDays(),0));
		}
		
		
		}catch(Exception ex){
			logger.error("onMessage::::: ",ex);
		}finally{
			try{
				
				if(liveReport.getAction()!=null)
				{				
				liveReport=liveReportFactoryService.process(liveReport);
				}
				logger.info("onMessage::::::::::::::::: ::::   ,after liveReport "+liveReport);	
				}catch(Exception ex){
					logger.error(" fianlly liveReport:: "+liveReport +", : duChargingNotification:: "+duChargingNotification);					
					logger.error("onMessage::::::::::finally " + ex);
				}finally{
					duChargingNotification.setToken(liveReport.getToken());
					duChargingNotification.setCampaignId(liveReport.getAdnetworkCampaignId());
					duChargingNotification.setSendToAdnetwork(
					liveReport.getSendConversionCount()>0||liveReport.getDctSendCount()>0?true:false);
					update = daoService.saveObject(duChargingNotification);
				}
				logger.info("onMessage::::JMSDUChargingCallbackListener::::::::::::: :: update::live report " 
				+ update + ", total time:: "
						+ (System.currentTimeMillis() - time));
			}
		}
	}

