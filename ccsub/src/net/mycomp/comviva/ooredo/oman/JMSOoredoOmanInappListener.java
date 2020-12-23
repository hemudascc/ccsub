package net.mycomp.comviva.ooredo.oman;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.jpa.repository.JPAMT2KSAServiceApiTrans;
import net.jpa.repository.JPAOoredooOmanOCSLogDetail;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSOoredoOmanInappListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSOoredoOmanInappListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private JPAOoredooOmanOCSLogDetail jpaOoredooOmanOCSLogDetail;
	
	@Override
	public void onMessage(Message m) {

		OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail = null;
		
		boolean update = false;
		long time = System.currentTimeMillis();
		LiveReport liveReport=null;
		
		OoredooOmanServiceConfig ooreodoOmanServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			ooredooOmanOCSLogDetail = (OoredooOmanOCSLogDetail) objectMessage
					.getObject();
			
			logger.info("ooredooOmanOCSLogDetail:::::: "+ooredooOmanOCSLogDetail);
			
			CGToken cgToken=new CGToken(ooredooOmanOCSLogDetail.getToken());
			
			VWServiceCampaignDetail vwServiceCampaignDetail=  MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
				  ooreodoOmanServiceConfig=
							OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.get(vwServiceCampaignDetail
									.getServiceId());
				 
				 liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),
						   new Timestamp(System.currentTimeMillis())
					 ,cgToken.getCampaignId(),vwServiceCampaignDetail.getServiceId(),vwServiceCampaignDetail.getProductId()
					 ); 
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setMsisdn(ooredooOmanOCSLogDetail.getMsisdn());
					liveReport.setCircleId(0);					
					if(ooredooOmanOCSLogDetail.getAction().equalsIgnoreCase(OoredooOmanConstant.PIN_VALIDATION)
							&&ooredooOmanOCSLogDetail.getSuccess()){				
						 liveReport.setAction(MConstants.PIN_VALIDATE);				
						 liveReport.setPinValidationCount(1);
						 liveReport.setAmount(0d);
						 liveReport.setNoOfDays(0);						
					}else if(ooredooOmanOCSLogDetail.getAction()
							.equalsIgnoreCase(OoredooOmanConstant.SNED_PIN)
							&&ooredooOmanOCSLogDetail.getSuccess()){
						liveReport.setAction(MConstants.PIN_VALIDATE);				
						 liveReport.setPinSendCount(1);
						 liveReport.setAmount(0d);
						 liveReport.setNoOfDays(0);		
					}
			
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				//not processling 
				if(liveReport!=null&&liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
					//ooredooOmanOCSLogDetail.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
				}
				
			} catch (Exception ex) {			
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				jpaOoredooOmanOCSLogDetail.save(ooredooOmanOCSLogDetail);
			}
			logger.info("onMessage::::::::::::::::: ::  "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			}
	}
}
