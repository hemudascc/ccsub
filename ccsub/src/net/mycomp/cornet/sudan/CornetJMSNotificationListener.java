package net.mycomp.cornet.sudan;

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
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

public class CornetJMSNotificationListener implements MessageListener{

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private IDaoService daoService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	private static final Logger logger = Logger.getLogger(CornetJMSNotificationListener.class);
	@Override
	public void onMessage(Message message) {
		CornetNotification cornetNotification=null;
		long time = System.currentTimeMillis();
		LiveReport liveReport=null;
		boolean save = false;
		CGToken cgToken = new CGToken("");
		String action = null;
		String accessToken="";
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			cornetNotification = (CornetNotification)objectMessage.getObject();
			cgToken = new CGToken(cornetNotification.getToken());
			if(cgToken.getCampaignId()<0) {
				cgToken = new CGToken("-1c0c290");
			}
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			CornetConfig cornetConfig = CornetConstant.mapServiceIdToCornetConfig.get(vwServiceCampaignDetail.getServiceId());
			accessToken = new CornetUtils().GenerateToken(cornetConfig.getUserName(),cornetConfig.getPassword());
			redisCacheService.putObjectCacheValueByEvictionDay(CornetConstant.CORNET_UNIQUE_ACCESS_TOKEN_PREFIX+cornetNotification.getMsisdn(),accessToken, (CornetConstant.REMEMBER_ME)?30:1);
			redisCacheService.putObjectCacheValueByEvictionDay(CornetConstant.CORNET_UNIQUE_TOKEN_PREFIX+cornetNotification.getMsisdn(),cornetNotification.getToken(), 2);
			liveReport = new LiveReport(MConstants.CORNET_SUDAN_ZAIN_OPERATOR_ID, new Timestamp(System.currentTimeMillis()),
					cgToken.getCampaignId(), cornetConfig.getServiceId(), 8);
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			action = CornetConstant.findAction(cornetNotification);
			logger.debug(cornetNotification+"::::action:::"+action);

			if(Objects.nonNull(action)) {
				if(MConstants.ACT.equals(action)) {  
					liveReport.setMsisdn(cornetNotification.getMsisdn());
					liveReport.setAction(MConstants.ACT);
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setAmount(Double.valueOf(cornetNotification.getPrice()));
					liveReport.setConversionCount(1);
				}else if(MConstants.GRACE.equals(action)) {
					liveReport.setMsisdn(cornetNotification.getMsisdn());
					liveReport.setAction(MConstants.GRACE);
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setGraceConversionCount(1);
				} else if(MConstants.RENEW.equals(action)) {
					liveReport.setMsisdn(cornetNotification.getMsisdn());
					liveReport.setAction(MConstants.RENEW);
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setRenewalAmount(Double.valueOf(cornetNotification.getPrice()));
					liveReport.setRenewalCount(1);
				}else if(MConstants.DCT.equals(action)){
					liveReport.setMsisdn(cornetNotification.getMsisdn());
					liveReport.setAction(MConstants.DCT);
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setTokenId(cgToken.getTokenId());
					liveReport.setDctCount(1);
				}
			} 
		}catch (Exception e) {
			logger.error("error while saving cornetNotification::"+cornetNotification,e);
		}finally {
			try {
				logger.debug("live report:::::"+liveReport);
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
					logger.info("processed");
//					cornetNotification.setAction(liveReport.getAction());
				}
			}catch (Exception e) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : cornetNotification:: "
						+ cornetNotification);
				logger.error("onMessage::::::::::finally " ,e);
			}finally {
				daoService.saveObject(cornetNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ save + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}

}
