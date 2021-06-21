package net.mycomp.mobipay;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

public class JMSDLRListener implements MessageListener{
	
	private static final Logger logger = Logger.getLogger(JMSDLRListener.class);
	
	@Autowired
	RedisCacheService redisService;
	@Autowired
	private IDaoService daoService;
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Override
	public void onMessage(Message message) {
		boolean update=false;
		long time=System.currentTimeMillis();
		MobiPayDlr mobiPayDlr = null;
		CGToken cgToken = null;
		String token=null;
		String action=null;
		LiveReport liveReport=null;
		MobiPayServiceConfig mobiPayServiceConfig=null;
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			mobiPayDlr = (MobiPayDlr)objectMessage.getObject();
			token = Objects.toString(redisService.getObjectCacheValue(mobiPayDlr.getMsisdn()));
			cgToken= new CGToken(token); 
			if(cgToken.getCampaignId()<=0) {
				cgToken = new CGToken(MobiPayConstant.getDefaultToken(mobiPayDlr.getProvider()));
			}
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			mobiPayServiceConfig = MobiPayConstant.mapServiceIdToMobiPayServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			action = MobiPayConstant.findAction(mobiPayDlr);
			liveReport = new LiveReport(mobiPayServiceConfig.getOperatorId(), 
					new Timestamp(System.currentTimeMillis()), cgToken.getCampaignId()
					,mobiPayServiceConfig.getServiceId(), mobiPayServiceConfig.getProductId());
			liveReport.setMsisdn(mobiPayDlr.getMsisdn());
			liveReport.setResponse(mobiPayDlr.toString());
			if(MConstants.ACT.equals(action)) {
				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(mobiPayServiceConfig.getPrice());
				liveReport.setNoOfDays(1);
			}
		} catch (Exception e) {
			logger.error("error while processing mobiPayDlr::"+mobiPayDlr, e);
		}finally {
			try {
				if(Objects.nonNull(liveReport.getAction())) {
					liveReport=liveReportFactoryService.process(liveReport);
					logger.info("onMessage::::::::::::::::: ::::   ,liveReport "+liveReport);
				}
			} catch (Exception ex) {
				logger.error("onMessage::::::::::finally " + ex);
			}finally {
				daoService.saveObject(mobiPayDlr);
			}
			logger.info("onMessage:::::update::live report "+ update + ", total time:: " + (System.currentTimeMillis() - time));
		}
	}
}
