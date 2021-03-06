package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPAAdnetworkToken;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2UAENotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2UAENotificationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
    @Autowired
    private JPAAdnetworkToken jpaAdnetworkToken;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private Mt2UAEServiceApi mt2UAEServiceApi;
	
	@Autowired
	private RedisCacheService redisCacheService;

	@Override
	public void onMessage(Message m) {

		Mt2UAENotification mt2UAENotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		String msg=null;
		Mt2UAEServiceConfig mt2UAEServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2UAENotification = (Mt2UAENotification) objectMessage
					.getObject();
			
			logger.info("mt2UAENotification::::: "+mt2UAENotification);
			cgToken=new CGToken(Objects.toString(redisCacheService
					.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX
							+mt2UAENotification.getMsisdn())));
			if(cgToken.getCampaignId()<=0){
				AdnetworkToken adnetworkToken=jpaAdnetworkToken.findEnableAdnetworkToken(MUtility.toInt(mt2UAENotification.getTrxid(), 0));
				if(adnetworkToken!=null){
				cgToken=new CGToken(adnetworkToken.getTokenToCg());
				}
			}
			
			mt2UAEServiceConfig=
					 Mt2UAEConstant.mapMt2OperatorIdMt2UAEServiceConfig
						.get(mt2UAENotification.getOperatorID());
			
			Service service=MData.mapServiceIdToService.get(mt2UAEServiceConfig.getServiceId());
			        
				  liveReport=new LiveReport(service.getOpId(),
						   new Timestamp(System.currentTimeMillis())
					 ,cgToken.getCampaignId(),mt2UAEServiceConfig.getServiceId(),mt2UAEServiceConfig.getProductId()); 
			
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setMsisdn(mt2UAENotification.getMsisdn());
					liveReport.setCircleId(0);
					liveReport.setMode(MT2UAENotificationMode.getMode(mt2UAENotification.getChannel()));
					mt2UAENotification.setToken(cgToken.getCGToken());
					
					if(mt2UAENotification.getSubscriptionStatus()
							.equalsIgnoreCase("Active")&&mt2UAENotification.getChargeStatus()
							.equalsIgnoreCase("billed")&&cgToken.getCampaignId()>0){			
						 
						 liveReport.setAction(MConstants.ACT);				
						 liveReport.setConversionCount(1);
						 liveReport.setAmount(MUtility.toDouble(mt2UAENotification.getAmount(),0));
						 liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());
						 liveReport.setParam1(Mt2UAEConstant.getPassword());
						 msg=mt2UAEServiceConfig.getSubMsgTemplate();
						 redisCacheService
						 .removeObjectCacheValue(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX
								 +mt2UAENotification.getMsisdn());
						 
					}else if(mt2UAENotification.getSubscriptionStatus()
							.equalsIgnoreCase("Active")
							&&mt2UAENotification.getChargeStatus()
							.equalsIgnoreCase("billed")){
						
							liveReport.setAction(MConstants.RENEW);
							liveReport.setRenewalCount(1);					
							 liveReport.setRenewalAmount(MUtility.toDouble(mt2UAENotification.getAmount(),0));
							 liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());
								msg=mt2UAEServiceConfig.getRenewMsgTemplate();
								
						}else if(mt2UAENotification.getSubscriptionStatus()
								.equalsIgnoreCase("Canceled")||
								mt2UAENotification.getSubscriptionStatus()
								.equalsIgnoreCase("failed")){
							
							liveReport.setAction(MConstants.DCT);			
							liveReport.setDctCount(1);
							//msg=mt2UAEServiceConfig.getUnsubMsgTemplate();//operator will send it
						}
					
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				
				mt2UAENotification.setMyAction(liveReport.getAction());
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				mt2UAENotification.setSendToAdnetwork(liveReport.getConversionCount()>0?true:false);
				if(msg!=null){
					msg=Mt2UAEConstant.prepareMessage(msg,mt2UAEServiceConfig,liveReport.getSubId(),
							liveReport.getParam1());
					mt2UAEServiceApi.sendSMS(mt2UAEServiceConfig, mt2UAENotification.getMsisdn()
							, msg, liveReport.getAction());	
					}
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2UAENotification:: "
						+ mt2UAENotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2UAENotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
	}
}
