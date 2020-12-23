package net.mycomp.onmobile;

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
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSOnMobileChargingNotificationListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSOnMobileChargingNotificationListener.class);

	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAOredooCGCallback jpaOredooCGCallback;
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private RedisCacheService redisCacheService;
	

	@Override
	public void onMessage(Message m) {
		
		OnMobileChargingNotification onMobileChargingNotification=null;
		OnMobileServiceConfig onMobileServiceConfig=null;
		LiveReport liveReport=null;
		String action=null;
		String msisdn=null;
		String msg=null;
		CGToken cgToken=null;
		
		try{
		ObjectMessage objectMessage = (ObjectMessage) m;
		onMobileChargingNotification=(OnMobileChargingNotification)objectMessage.getObject();
		
		      cgToken=new CGToken("");
			 
			 onMobileServiceConfig=
					  OnMobileConstant.mapSrvkeyToOnMobileServiceConfig.
					get(onMobileChargingNotification.getSrvKey());
			Service service=MData.mapServiceIdToService.get(onMobileServiceConfig.getServiceId());
			 liveReport=new LiveReport(service.getOpId(),
					  new Timestamp(System.currentTimeMillis()),cgToken.getCampaignId(),
					  onMobileServiceConfig.getServiceId()
					  ,service.getProductId());
				// ooredooOmanNotification.setCampaignId(cgToken.getCampaignId());
				 liveReport.setTokenId(cgToken.getTokenId());	
				 liveReport.setMsisdn(onMobileChargingNotification.getMsisdn());
				liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());		 
			 liveReport.setServiceId(onMobileServiceConfig.getServiceId());
			// liveReport.setMode(OredooKuwaitSubcriptionMode.getMode(ooredooOmanNotification.getBearerId()));
			 
			if(onMobileChargingNotification.getAction().equalsIgnoreCase(MConstants.ACT)){
				liveReport.setAction(MConstants.ACT);
				liveReport.setAmount(onMobileServiceConfig.getPrice());
				liveReport.setConversionCount(1);
				liveReport.setNoOfDays(onMobileServiceConfig.getValidity());
				
			}else if(onMobileChargingNotification.getAction().equalsIgnoreCase(MConstants.DCT)){
				liveReport.setDctCount(1);
				liveReport.setAction(MConstants.DCT);
				SubscriberReg subscriberReg=
						jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, liveReport.getProductId());

			}else if(onMobileChargingNotification.getAction().equalsIgnoreCase(MConstants.RENEW)){
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setNoOfDays(onMobileServiceConfig.getValidity());
				liveReport.setRenewalAmount(onMobileServiceConfig.getPrice());
			}else if(onMobileChargingNotification.getAction().equalsIgnoreCase("SUS")){
				liveReport.setGraceConversionCount(1);
				liveReport.setAction(MConstants.GRACE);
			}
			
			}catch(Exception ex){
				
				logger.error(msisdn+" onMessage::::: "+onMobileChargingNotification,ex);
				
			}finally{		
				
				try {
					
					liveReportFactoryService.process(liveReport);
					onMobileChargingNotification.setSendToAdnetwork(
							liveReport.getSendConversionCount() > 0 ? true : false);
				    
				} catch (Exception e) {	
					logger.error(msisdn+" finally::: ",e);
				}
				daoService.saveObject(onMobileChargingNotification);
			  }
			}
		
	}

