package net.mycomp.mt2.ksa;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.jpa.repository.JPAMT2KSAServiceApiTrans;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2KSAPinValidationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2KSAPinValidationListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private Mt2KSAServiceApi mt2KSAServiceApi;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private JPAMT2KSAServiceApiTrans jpaMT2KSAServiceApiTrans;
	
	@Override
	public void onMessage(Message m) {

		MT2KSAServiceApiTrans mt2KSAServiceApiTrans = null;
		
		boolean update = false;
		long time = System.currentTimeMillis();
		LiveReport liveReport=null;
		
		Mt2KSAServiceConfig mt2KSAServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2KSAServiceApiTrans = (MT2KSAServiceApiTrans) objectMessage
					.getObject();
			
			logger.info("mt2KSAServiceApiTrans:::::: "+mt2KSAServiceApiTrans);
			CGToken cgToken=new CGToken(mt2KSAServiceApiTrans.getToken());
			
			VWServiceCampaignDetail vwServiceCampaignDetail=  MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
			if(mt2KSAServiceApiTrans.getAction().equalsIgnoreCase(Mt2KSAConstant.VALIDATE_OTP)
					&&mt2KSAServiceApiTrans.getResponseToCaller()
					&&vwServiceCampaignDetail.getOpId()==MConstants.MT2_KSA_ZAIN_OPERATOR_ID){
				
				 mt2KSAServiceConfig=
						Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(vwServiceCampaignDetail.getServiceId());
				 liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),
						   new Timestamp(System.currentTimeMillis())
					 ,cgToken.getCampaignId(),vwServiceCampaignDetail.getServiceId(),vwServiceCampaignDetail.getProductId()
					 ); 
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setMsisdn(mt2KSAServiceApiTrans.getMsisdn());
					liveReport.setCircleId(0);
					MT2KSAServiceApiTrans mt2KSAServiceApiTransSubStatus= mt2KSAServiceApi
							.subStatus(mt2KSAServiceConfig, mt2KSAServiceApiTrans.getMsisdn(),cgToken.getCGToken());
					
					if(mt2KSAServiceApiTrans.getResponseToCaller()
							&&mt2KSAServiceApiTransSubStatus.getResponseToCaller()){				
						 liveReport.setAction(MConstants.ACT);				
						 liveReport.setConversionCount(1);
						 liveReport.setAmount(0d);
						 liveReport.setNoOfDays(1);						
					}
			}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				//not processling 
				if(liveReport!=null&&liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
					mt2KSAServiceApiTrans.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
				}
				
			} catch (Exception ex) {			
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				jpaMT2KSAServiceApiTrans.save(mt2KSAServiceApiTrans);
			}
			logger.info("onMessage::::::::::::::::: ::  "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			}
	}
}
