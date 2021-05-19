package net.mycomp.veoo;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.jpa.repository.JPAVeooMtMessage;
import net.mycomp.etisalat.EtisalatConstant;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSVeooDeliveryReciptListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSVeooDeliveryReciptListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private VeooApiService veooApiService;
	
	@Autowired
	private JPAVeooMtMessage jpaVeooMtMessage;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
  
	@Override
	public void onMessage(Message m) {

		VeooDeliveryReceipt veooDeliveryReceipt = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			veooDeliveryReceipt = (VeooDeliveryReceipt) objectMessage
					.getObject();
			cgToken=new CGToken("");
			VeooMtMessage veooMtMessage=jpaVeooMtMessage.
					findVeooMtMessageById(MUtility.toInt(veooDeliveryReceipt.getMtid(),
					0),"paid");
			
			if(veooMtMessage!=null){
				cgToken=new CGToken(veooMtMessage.getNetworkId());
			}
			
			logger.debug("VeooDeliveryReceipt::::::: "+veooDeliveryReceipt);
			
			
			VeooServiceConfig veooServiceConfig=
					VeooConstant.mapVeooServiceIdToVeooServiceConfig.get(
							veooDeliveryReceipt.getVeooServiceId());
			
			
			liveReport=new LiveReport(veooServiceConfig.getOpId()
					,new Timestamp(System.currentTimeMillis())
			 ,cgToken.getCampaignId(),veooServiceConfig.getServiceId(),0); //update campaign id
			liveReport.setProductId(veooServiceConfig.getCcProductId());
			liveReport.setMsisdn(veooDeliveryReceipt.getMsisdn());
			liveReport.setCircleId(0);
			liveReport.setTokenId(cgToken.getTokenId());
			 
//			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(veooDeliveryReceipt.getMsisdn(),
//					veooServiceConfig.getCcProductId());
			
			String action=VeooConstant.findAction(
					veooDeliveryReceipt,veooMtMessage);
			
			liveReport.setResponse(veooMtMessage.toString());
			if(action.equalsIgnoreCase(MConstants.ACT)){
              
				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(veooServiceConfig.getCostWTaxes());
				liveReport.setNoOfDays(veooServiceConfig.getValidity());
				
			}else if(action.equalsIgnoreCase(MConstants.RENEW)){
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setRenewalAmount(veooServiceConfig.getCostWTaxes());
				liveReport.setNoOfDays(veooServiceConfig.getValidity());				
			}
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);
					veooDeliveryReceipt.setAction(liveReport.getAction());
				}
				
				
			} catch (Exception ex) {
				logger.error(" fianlly " 
						+ ", : veooDeliveryReceipt:: "
						+ veooDeliveryReceipt);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(veooDeliveryReceipt);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
