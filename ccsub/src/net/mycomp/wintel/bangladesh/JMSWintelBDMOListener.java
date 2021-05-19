package net.mycomp.wintel.bangladesh;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSWintelBDMOListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSWintelBDMOListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private WintelBDServiceApi wintelBDServiceApi;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
  
	@Override
	public void onMessage(Message m) {

		WintelBDMO wintelBDMO = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		WintelBDServiceConfig wintelBDServiceConfig=null;
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			wintelBDMO = (WintelBDMO) objectMessage
					.getObject();
			
			logger.debug("wintelBDMO::::::: "+wintelBDMO);
			
			//client_id=clcnt&shortcode=16466&msisdn=576829965374122770&message=cs%20c
			//&transid=3040211171571306553504156&operator=grameenphone&serviceid=PPU00027603111
//			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(wintelBDMO.getMsisdn()
//					, 8);
			wintelBDServiceConfig=
					 WintelBDConstant.mapOperatorToWintelBDServiceConfig.get(wintelBDMO.getOperator());
			
			 liveReport=new LiveReport(wintelBDServiceConfig.getOpId(),wintelBDMO.getCreateTime(),
		    			-1,wintelBDServiceConfig.getServiceId(),wintelBDServiceConfig.getProductId()
							 );
			 liveReport.setMsisdn(wintelBDMO.getMsisdn());
			 
			if(wintelBDMO.getMessage()!=null&&wintelBDMO.getMessage().toLowerCase().contains(
					wintelBDServiceConfig.getKeyword().toLowerCase())){
				
				 String msg=WintelBDConstant.getMsg(wintelBDServiceConfig.getSubMsgTemplate(),wintelBDMO.getMsisdn(),
						 wintelBDServiceConfig);				 
				 WintelBDApiTrans wintelBDApiTrans=	wintelBDServiceApi.sendSMS(msg,
						 wintelBDMO.getTransid(), wintelBDServiceConfig, wintelBDMO.getMsisdn(),MConstants.ACT);
				 liveReport.setResponse(wintelBDMO.toString());
				 if(wintelBDApiTrans.getSuccess()){
					liveReport.setAction(MConstants.ACT);
					liveReport.setNoOfDays(wintelBDServiceConfig.getValidity());
					liveReport.setAmount(wintelBDServiceConfig.getPricePoint());
					liveReport.setParam1(wintelBDMO.getTransid());
					
				 }
			}
//			else if(wintelBDMO.getAction()!=null&&wintelBDMO.getAction().equalsIgnoreCase(MConstants.RENEW)){
//				
//				 String msg=WintelBDConstant.getMsg(wintelBDServiceConfig.getRenewalMsgTemplate()
//						 ,wintelBDMO.getMsisdn(),
//						 wintelBDServiceConfig);				 
//				 WintelBDApiTrans wintelBDApiTrans=	wintelBDServiceApi.sendSMS(msg,
//						 wintelBDMO.getTransid(), wintelBDServiceConfig, wintelBDMO.getMsisdn(),MConstants.RENEW);
//				 if(wintelBDApiTrans.getSuccess()){
//					liveReport.setAction(MConstants.RENEW);
//					liveReport.setNoOfDays(wintelBDServiceConfig.getValidity());
//					liveReport.setAmount(wintelBDServiceConfig.getPricePoint());
//					liveReport.setParam1(wintelBDMO.getTransid());
//				 }
//			}
			 
			 
			 
		} catch (Exception ex) {
			logger.error("onMessage::::: "+wintelBDMO+", veooServiceConfig:: "+wintelBDServiceConfig, ex);
		} finally {
			try {
				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly  , : wintelBDMO:: "+ wintelBDMO,ex);
				
			} finally {
				update = daoService.saveObject(wintelBDMO);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
