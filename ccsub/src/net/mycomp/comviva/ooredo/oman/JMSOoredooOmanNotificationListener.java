package net.mycomp.comviva.ooredo.oman;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPAOredooCGCallback;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.oredoo.kuwait.OredoKuwaitConstant;
import net.mycomp.oredoo.kuwait.OredooKuwaitCGCallback;
import net.mycomp.oredoo.kuwait.OredooKuwaitCGNotification;
import net.mycomp.oredoo.kuwait.OredooKuwaitCGToken;
import net.mycomp.oredoo.kuwait.OredooKuwaitServiceConfig;
import net.mycomp.oredoo.kuwait.OredooKuwaitSubcriptionMode;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSOoredooOmanNotificationListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSOoredooOmanNotificationListener.class);

	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAOredooCGCallback jpaOredooCGCallback;
	
	@Autowired
	private OoredooOmanServiceApi ooredooOmanServiceApi;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private RedisCacheService redisCacheService;
	

	@Override
	public void onMessage(Message m) {
		
		OoredooOmanNotification ooredooOmanNotification=null;
		OoredooOmanServiceConfig ooredooOmanServiceConfig=null;
		LiveReport liveReport=null;
		String action=null;
		String msisdn=null;
		String msg=null;
		CGToken cgToken=null;
		
		try{
		ObjectMessage objectMessage = (ObjectMessage) m;
		ooredooOmanNotification=(OoredooOmanNotification)objectMessage.getObject();
		
			 msisdn=ooredooOmanNotification.getMsisdn();
			 
			//http://IP:PORT/<CP_Context>?serviceId=1234&appliedPlan=9898
				//&sequenceNo=123432939
			 //&operationId=SN&bearerId=WAP&validityDays=1&chargeAmount=5&callingParty=968XXXXXXXXX
			
			 
			  ooredooOmanServiceConfig=
					OoredooOmanConstant.mapServiceIdToOoreodoOmanServiceConfig
					.get(ooredooOmanNotification.getServiceId());
			 
			String token=Objects.toString(redisCacheService.getObjectCacheValue(OoredooOmanConstant.
					OOREDO_OMAN_CACHE_TRANS_PREFIX_ID+
					  ooredooOmanNotification.getMsisdn()));
			
			 cgToken=new CGToken(
					Objects.toString(token));
			
			 liveReport=new LiveReport(MConstants.COMVIVA_OOREDOO_OMAN_OPERATOR_ID,
					  new Timestamp(System.currentTimeMillis()),cgToken.getCampaignId(),ooredooOmanServiceConfig.getCcServiceId()
					  ,0);
				// ooredooOmanNotification.setCampaignId(cgToken.getCampaignId());
				 liveReport.setTokenId(cgToken.getTokenId());	
				 ooredooOmanNotification.setTokenId(cgToken.getTokenId());
				liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());		 
			 liveReport.setServiceId(ooredooOmanServiceConfig.getCcServiceId());
			 //liveReport.setProductId(oredooKuwaitServiceConfig.getCcProductId());//product id==service id		 
			 
		
			  
			 action=OoredooOmanConstant.findAction(ooredooOmanNotification.getOperationId());
			 liveReport.setMsisdn(ooredooOmanNotification.getMsisdn());
			 logger.info(msisdn+", action:: "+action);
			 liveReport.setMode(OredooKuwaitSubcriptionMode.getMode(ooredooOmanNotification.getBearerId()));
			 
			if(action.equalsIgnoreCase(MConstants.ACT)){
				
				liveReport.setAmount(ooredooOmanNotification.getChargeAmount());
				liveReport.setConversionCount(1);
				liveReport.setNoOfDays(ooredooOmanNotification.getValidityDays());
				if(ooredooOmanNotification.getOperationId().equalsIgnoreCase("SN")){
					msg=ooredooOmanServiceConfig.getSubMsg();
				}
			}else if(action.equalsIgnoreCase(MConstants.DCT)){
				liveReport.setDctCount(1);
				
				SubscriberReg subscriberReg=
						jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, liveReport.getProductId());

			}else if(action.equalsIgnoreCase(MConstants.RENEW)){
				liveReport.setRenewalCount(1);
				liveReport.setNoOfDays(ooredooOmanNotification.getValidityDays());
				liveReport.setRenewalAmount(ooredooOmanNotification.getChargeAmount());
			}else if(action.equalsIgnoreCase(MConstants.GRACE)){
				liveReport.setGraceConversionCount(1);
				liveReport.setAction(MConstants.ACT);
			}
			
			}catch(Exception ex){
				
				logger.error(msisdn+" onMessage::::: "+ooredooOmanNotification,ex);
				
			}finally{		
				
				try {
					liveReport.setAction(action);
					ooredooOmanNotification.setMyAction(liveReport.getAction());
					liveReportFactoryService.process(liveReport);
					ooredooOmanNotification.setSendToAdnetwork(
							liveReport.getSendConversionCount() > 0 ? true : false);
				     if(msg!=null){
				    	 msg=OoredooOmanConstant.prepareMessage(msg, ooredooOmanServiceConfig, liveReport.getSubId());
				    	 ooredooOmanServiceApi.sendMT(msisdn, cgToken.getCGToken(), ooredooOmanServiceConfig, msg);
				     }
				} catch (Exception e) {	
					logger.error(msisdn+" finally::: ",e);
				}
				daoService.saveObject(ooredooOmanNotification);
			  }
			}
		
	}

