package net.mycomp.beecel.jordon;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
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
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;


public class JMSJordonNotificationListener  implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSJordonNotificationListener.class);

	@Autowired
	private IDaoService daoService;
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private BCJordonSmsService bcJordonSmsService;

	@Override
	public void onMessage(Message message) {
		BCJordonNotification bcJordonNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		CGToken cgToken=new CGToken("");
		long time = System.currentTimeMillis();
		String token = null;
		BCJordonConfig bcJordonConfig=null;
		try {
//			Thread.sleep(120000);
			
			ObjectMessage objectMessage = (ObjectMessage) message;
			bcJordonNotification = (BCJordonNotification)objectMessage.getObject();
			logger.info("bcJordonNotification::::: "+bcJordonNotification);
			token = getToken(bcJordonNotification);
			cgToken=new CGToken(getToken(bcJordonNotification));
			logger.info("token: "+getToken(bcJordonNotification));
//			}
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			if(vwServiceCampaignDetail==null) {
				vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			}
			bcJordonConfig = BCJordonConstant.mapServiceIdToBCJordonConfig.get(vwServiceCampaignDetail.getServiceId());
			bcJordonNotification.setToken(token);
					liveReport=new LiveReport(bcJordonConfig.getOperatorId(),
					new Timestamp(System.currentTimeMillis())
					,cgToken.getCampaignId(),bcJordonConfig.getServiceId(),0);
			liveReport.setProductId(bcJordonConfig.getProductId());
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(bcJordonNotification.getMsisdn());
			liveReport.setCircleId(0);
			if(bcJordonNotification.getNotificationtype().equalsIgnoreCase(BCJordonConstant.ACTIVATE) || bcJordonNotification.getNotificationtype().equalsIgnoreCase(BCJordonConstant.SUBSCRIBE)) {
				liveReport.setAction(MConstants.ACT);					
				liveReport.setConversionCount(1);
				liveReport.setParam2(bcJordonConfig.getOpCode());
				if(bcJordonConfig.getOperatorName().equalsIgnoreCase(BCJordonConstant.OPERATOR_ORANGE)) {
					bcJordonSmsService.sendMTSMS(bcJordonConfig,bcJordonNotification.getMsisdn());
				}
			}else if(bcJordonNotification.getNotificationtype().equalsIgnoreCase(BCJordonConstant.RENEW)) {
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);					
				liveReport.setParam2(bcJordonConfig.getOpCode());
			}else if(bcJordonNotification.getNotificationtype().equalsIgnoreCase(BCJordonConstant.DEACTIVATE) || bcJordonNotification.getNotificationtype().equalsIgnoreCase(BCJordonConstant.UNSUBSCRIBE)) {
				liveReport.setAction(MConstants.DCT);			
				liveReport.setDctCount(1);
			}else {
				logger.info("getNotificationtype: is null :   "+bcJordonNotification);
				logger.info("Livereport:  :   "+liveReport);
			}
			
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		} finally {
			try {
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				bcJordonNotification.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : bcJordonNotification:: "
						+ bcJordonNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(bcJordonNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
	
	private String getToken(BCJordonNotification bcJordonNotification){
		String token = null;
		try {
//		Thread.sleep(120000);
		token =(String)redisCacheService.getObjectCacheValue(BCJordonConstant.CG_CALLBACK_CAHCHE_PREFIX+bcJordonNotification.getMsisdn());		
		if(token==null) {
		if(bcJordonNotification.getSid().equals("9536")) {
			return"-1c1c286";
		}else if(bcJordonNotification.getSid().equals("9557")) {
			return"-1c1c287";
		} else {
			return"-1c1c291";
		}
		}
		}catch(Exception e) {
			
		}
		
		return token;
		
	}

}
