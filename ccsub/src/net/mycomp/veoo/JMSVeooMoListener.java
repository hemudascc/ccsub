package net.mycomp.veoo;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSVeooMoListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSVeooMoListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private VeooApiService veooApiService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
  
	@Override
	public void onMessage(Message m) {

		VeooMo veooMo = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		VeooServiceConfig veooServiceConfig=null;
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			veooMo = (VeooMo) objectMessage
					.getObject();
			
			logger.debug("veooMo::::::: "+veooMo);
			
			 veooServiceConfig=
					VeooConstant.mapVeooServiceIdToVeooServiceConfig.get(veooMo.getServiceId());
			 liveReport=new LiveReport(veooServiceConfig.getOpId(),veooMo.getCreateTime(),
		    			-1,veooServiceConfig.getServiceId(),0
							 );
			 
			 if(veooMo.getMessage()!=null&&veooMo.getMessage().toLowerCase().contains("salir")){
				
				 liveReport.setMsisdn(veooMo.getMsisdn());
				 liveReport.setAction(MConstants.DCT);
				 liveReport.setProductId(veooServiceConfig.getCcProductId());
				 veooApiService.sendFreeDCTMessage(MConstants.DCT,veooServiceConfig, veooMo.getMsisdn());
				 
			 }else if(veooMo.getMessage()!=null
					 &&veooMo.getMessage().toLowerCase()
					 .contains(veooServiceConfig.getKeyword().toLowerCase())){
				 
				 SubscriberReg subscriberReg=
						 jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(veooMo.getMsisdn(),
								 veooServiceConfig.getCcProductId());
		  if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
					 veooApiService.sendFreeMTMessage(MConstants.ALREADY_SUBSCRIBED
							 , veooServiceConfig,
							 veooMo.getMsisdn(),veooMo.getShortcode()
							 ,veooServiceConfig.getAlreadySubscribedMessage());
				 }else{
			veooApiService.sendFreeMTMessage("FREE",veooServiceConfig,
					veooMo.getMsisdn(),veooMo.getShortcode());			
			veooApiService.sendPremiumMTMessage(MConstants.ACT,
					veooServiceConfig, veooMo.getMsisdn());			 
			 liveReport.setMsisdn(veooMo.getMsisdn());
			 liveReport.setAction(MConstants.ACT);
			 liveReport.setProductId(veooServiceConfig.getCcProductId());	
			 liveReport.setNoOfDays(veooServiceConfig.getValidity());
			 liveReport.setConversionCount(0);
				 }
		}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: "+veooMo+", veooServiceConfig:: "+veooServiceConfig, ex);
		} finally {
			try {
				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly " 
						+ ", : veooMo:: "
						+ veooMo);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(veooMo);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
