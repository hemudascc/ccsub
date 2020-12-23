package net.mycomp.messagecloud.gateway.za;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMCGZAOBSWindow;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMCGZADeliveryReportListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMCGZADeliveryReportListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAMCGZAOBSWindow jpaMCGZAOBSWindow;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private SubscriberRegService subscriberRegService; 

	
	
	@Override
	public void onMessage(Message m) {

		MCGZADeliveryReport mcgZADeliveryReport = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mcgZADeliveryReport = (MCGZADeliveryReport) objectMessage
					.getObject();
			
			logger.info("mcgZADeliveryReport::::: "+mcgZADeliveryReport);
			MCGZAOBSWindowTrans mcgZAOBSWindowTrans=jpaMCGZAOBSWindow.
					findMCGZAOBSWindowTransBYOBSWindowId(mcgZADeliveryReport.getMessageId());
			if(mcgZAOBSWindowTrans!=null){
				cgToken=new CGToken(mcgZAOBSWindowTrans.getToken());	
			}
			
		   MCGZAServiceConfig mcgZAServiceConfig=
					MCGZAConstant.mapServiceIdToMCGZAServiceConfig.get(MCGZAConstant.SERVICE_ID);
		   
		   liveReport=new LiveReport(MConstants.MESSAGE_CLOUD_GATWAY_ZA_OPERATOR,
				   new Timestamp(System.currentTimeMillis())
			 ,cgToken.getCampaignId(),MCGZAConstant.SERVICE_ID,0); 
	
	
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(mcgZADeliveryReport.getNumber());
			liveReport.setCircleId(0);
			
			MCGZADeliveryReportEnum mcgDeliveryReportEnum=
					MCGZADeliveryReportEnum.getMCGDeliveryStatusEnum(mcgZADeliveryReport.getReport());
			
		
			if(mcgDeliveryReportEnum.report.equalsIgnoreCase(MCGZADeliveryReportEnum.DELIVERED.report)){
				
				if(mcgZAOBSWindowTrans!=null){
			
					 liveReport.setAction(MConstants.ACT);
					 liveReport.setAmount(mcgZAServiceConfig.getPricePoint());
					 liveReport.setConversionCount(1);
					 liveReport.setNoOfDays(mcgZAServiceConfig.getValidity());
					 
				}else {
					liveReport.setAction(MConstants.RENEW);
					liveReport.setRenewalCount(1);
					liveReport.setRenewalAmount(
							mcgZAServiceConfig.getPricePoint());
					liveReport.setNoOfDays(mcgZAServiceConfig.getValidity());
					
				}
			}else if(mcgDeliveryReportEnum.report.equalsIgnoreCase(MCGZADeliveryReportEnum.NO_CREDIT.report)){
				logger.info("No CREDIT need to retry billing for low price point");
			}	
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
		
			try {
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mcgZADeliveryReport:: "
						+ mcgZADeliveryReport);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mcgZADeliveryReport);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
