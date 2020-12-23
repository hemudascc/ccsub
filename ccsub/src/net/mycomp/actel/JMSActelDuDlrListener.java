package net.mycomp.actel;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSActelDuDlrListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSActelDuDlrListener.class);


	@Autowired 
	private IDaoService daoService;
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	@Autowired
	private ActelApiService actelApiService;
	@Autowired 
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired 
	private RedisCacheService redisCacheService;

	@Override 
	public void onMessage(Message m) {
		ActelDuDlr actelDuDlr = null;
		LiveReport liveReport = null; boolean update =false;
		long time = System.currentTimeMillis(); 
		CGToken cgToken=null; 
		try {
			ObjectMessage objectMessage = (ObjectMessage) m; 
			actelDuDlr = (ActelDuDlr)objectMessage .getObject(); 
			cgToken=new CGToken(actelDuDlr.getCorrelator()); 
			VWServiceCampaignDetail vwserviceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			liveReport=new LiveReport(MConstants.ACTEL_OPERATOR_ETISALAT, 
					new Timestamp(System.currentTimeMillis()),
					-1, actelNewServiceConfig.getServiceId(),
					actelNewServiceConfig.getProductId());
			logger.debug("actelDuDlr::::::: "+actelDuDlr);
			if(actelDuDlr.getSubStatus()!=null
					&&actelDuDlr.getSubStatus().equalsIgnoreCase(ActelConstant.ACTIVE)
					&&actelDuDlr.getDescription()!=null
					&&actelDuDlr.getDescription().equalsIgnoreCase("SUCCESS"))
			{ 
				liveReport.setAction(MConstants.ACT);
				liveReport.setAmount(MUtility.toDouble(actelNewServiceConfig.getPrice().toString(), 0));
				liveReport.setNoOfDays(1); liveReport.setConversionCount(1); 
				//boolean sent = actelApiService.sendDuCallback(actelServiceConfig, actelDuDlr);
				//logger.info("callback sent = "+sent);
			}

		} catch (Exception e) { 
			logger.error("onMessage::::: ", e); 
		}
		finally { 
			try {
				liveReportFactoryService.process(liveReport);
			} catch (Exception ex) { 
				logger.error(" fianlly " + ", : actelDuDlr:: " +actelDuDlr);
				logger.error("onMessage::::::::::finally " ,ex); 
			}
			finally {
				update = daoService.saveObject(actelDuDlr); }
			logger.info("onMessage::::::::::::::::: :: update::live report " + update +
					", total time:: " + (System.currentTimeMillis() - time)); 
		} 
	}
}
