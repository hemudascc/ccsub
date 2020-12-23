package net.mycomp.mt2.zain.iraq;

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

public class JMSMt2ZainIraqNotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2ZainIraqNotificationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
    @Autowired
    private RedisCacheService redisCacheService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private Mt2ZainIraqServiceApi mt2ZainIraqServiceApi;

	@Override
	public void onMessage(Message m) {
		
		Mt2ZainIraqNotification mt2ZainIraqNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		String msg=null;
		Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=null;
		Integer serviceId=0;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2ZainIraqNotification = (Mt2ZainIraqNotification) objectMessage .getObject();
			//redisCacheService.putObjectCacheValueByEvictionDay("MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX"+mt2ZainIraqNotification.getMsisdn(), "-1c-1c221", 1);
			logger.info("mt2ZainIraqNotification::::: "+mt2ZainIraqNotification);
			
			String token=Objects.toString(redisCacheService.getObjectCacheValue(
					 Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX+mt2ZainIraqNotification.getMsisdn()));
			logger.info("token:"+token);
			if(token!=null && !token.isEmpty() && !token.contains("c") && !token.equals("ACT")){
					logger.info("calculating cg token..");
					 cgToken=new CGToken(token);
			}else{
				//http://192.241.253.234/ccsub/cnt/cmp?adid=2&cmpid=221&token=zain
					logger.info("setting default cg token");
				 cgToken=new CGToken(System.currentTimeMillis(), -1, 221); 
			}
				
			mt2ZainIraqNotification.setToken(cgToken.getCGToken());
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			serviceId = vwServiceCampaignDetail.getServiceId();
			mt2ZainIraqServiceConfig = Mt2ZainIraqConstant.mapServiceIdToMt2ZainIrqServiceConfig
									   .get(serviceId);
			
			liveReport=new LiveReport(MConstants.MT2_ZAIN_IRAQ_OPERATOR_ID, new
					  Timestamp(System.currentTimeMillis())
					  ,cgToken.getCampaignId(),serviceId,0); 
			
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(mt2ZainIraqNotification.getMsisdn());
			liveReport.setCircleId(0);
			
			if(mt2ZainIraqNotification.getAction().equals(MConstants.ACT)) {
				
				liveReport.setAction(MConstants.ACT); 
				liveReport.setConversionCount(1);
				liveReport.setAmount(0d);
				liveReport.setParam1(mt2ZainIraqNotification.getSubscriberReferenceID());
				liveReport.setNoOfDays(mt2ZainIraqServiceConfig.getValidity()); 
				redisCacheService.putObjectCacheValueByEvictionMinute(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX+mt2ZainIraqNotification.getMsisdn(), MConstants.ACT, 60*10);
				//msg=mt2ZainIraqServiceConfig.getSubMsgTemplate();
			}
			if(mt2ZainIraqNotification.getAction().equals(MConstants.DCT)){
				  liveReport.setAction(MConstants.DCT);			
				  liveReport.setDctCount(1);
				 // msg=mt2ZainIraqServiceConfig.getUnsubMsgTemplate();
			}
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {					
				mt2ZainIraqNotification.setAction(liveReport.getAction());		
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				if(msg!=null){
					msg=Mt2ZainIraqConstant.prepareMessage(msg, mt2ZainIraqServiceConfig,liveReport.getParam1());
					mt2ZainIraqServiceApi.sendContentSms(mt2ZainIraqNotification.getMsisdn(),
							msg, "", mt2ZainIraqNotification.getAction());
				} 
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport+ ", : mt2ZainIraqNotification:: "+ mt2ZainIraqNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ZainIraqNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "+ update + ", total time:: "+(System.currentTimeMillis() - time));
			
		}

		/*
		 * Mt2ZainIraqNotification mt2ZainIraqNotification = null; LiveReport liveReport
		 * = null; boolean update = false; long time = System.currentTimeMillis();
		 * CGToken cgToken=null; String msg=null; Mt2ZainIraqServiceConfig
		 * mt2ZainIraqServiceConfig=null; try {
		 * 
		 * ObjectMessage objectMessage = (ObjectMessage) m; mt2ZainIraqNotification =
		 * (Mt2ZainIraqNotification) objectMessage .getObject();
		 * 
		 * logger.info("mt2ZainIraqNotification::::: RQ "+mt2ZainIraqNotification);
		 * 
		 * mt2ZainIraqServiceConfig=
		 * Mt2ZainIraqConstant.mapZainIraqServiceIdToMt2ZainIrqServiceConfig
		 * .get(mt2ZainIraqNotification.getServiceId());
		 * 
		 * cgToken=new CGToken(Objects.toString(redisCacheService
		 * .getObjectCacheValue(Mt2ZainIraqConstant.
		 * MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX
		 * +mt2ZainIraqNotification.getMsisdn())));
		 * 
		 * liveReport=new LiveReport(MConstants.MT2_ZAIN_IRAQ_OPERATOR_ID, new
		 * Timestamp(System.currentTimeMillis())
		 * ,cgToken.getCampaignId(),mt2ZainIraqServiceConfig.getServiceId(),0);
		 * 
		 * liveReport.setTokenId(cgToken.getTokenId());
		 * liveReport.setToken(cgToken.getCGToken());
		 * mt2ZainIraqNotification.setToken(cgToken.getCGToken());
		 * liveReport.setMsisdn(mt2ZainIraqNotification.getMsisdn());
		 * liveReport.setCircleId(0);
		 * 
		 * if(mt2ZainIraqNotification.getActionType() .equalsIgnoreCase("1")){
		 * liveReport.setAction(MConstants.ACT); liveReport.setConversionCount(1);
		 * liveReport.setAmount(0d); SubscriberReg
		 * subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
		 * mt2ZainIraqNotification.getMsisdn(),
		 * mt2ZainIraqServiceConfig.getProductId()); //
		 * liveReport.setAmount(mt2ZainIraqServiceConfig.getPricePoint()); //
		 * liveReport.setNoOfDays(mt2ZainIraqServiceConfig.getValidity());
		 * if(subscriberReg==null){ msg=mt2ZainIraqServiceConfig.getSubMsgTemplate(); }
		 * 
		 * }else if(mt2ZainIraqNotification.getActionType() .equalsIgnoreCase("2")){
		 * 
		 * liveReport.setAction(MConstants.RENEW); liveReport.setRenewalCount(1);
		 * liveReport.setRenewalAmount(mt2ZainIraqServiceConfig.getPricePoint());
		 * liveReport.setNoOfDays(mt2ZainIraqServiceConfig.getValidity());
		 * msg=mt2ZainIraqServiceConfig.getRenewMsgTemplate();
		 * 
		 * }else if(mt2ZainIraqNotification.getActionType() .equalsIgnoreCase("0")){
		 * liveReport.setAction(MConstants.DCT); liveReport.setDctCount(1);
		 * msg=mt2ZainIraqServiceConfig.getUnsubMsgTemplate(); } } catch (Exception ex)
		 * { logger.error("onMessage::::: ", ex); } finally { try {
		 * mt2ZainIraqNotification.setMyAction(liveReport.getAction());
		 * if(liveReport.getAction()!=null){
		 * liveReportFactoryService.process(liveReport);
		 * mt2ZainIraqNotification.setSendToAdnetwork(liveReport.getSendConversionCount(
		 * )>0?true:false); } if(msg!=null){ msg=Mt2ZainIraqConstant.prepareMessage(msg,
		 * mt2ZainIraqServiceConfig,liveReport.getSubId());
		 * mt2ZainIraqServiceApi.sendContentSms(mt2ZainIraqNotification.getMsisdn() ,
		 * msg, "", mt2ZainIraqNotification.getMyAction()); }
		 * 
		 * } catch (Exception ex) { logger.error(" fianlly liveReport:: " + liveReport +
		 * ", : mobimindNotification:: " + mt2ZainIraqNotification);
		 * logger.error("onMessage::::::::::finally " ,ex); } finally { update =
		 * daoService.saveObject(mt2ZainIraqNotification); }
		 * logger.info("onMessage::::::::::::::::: :: update::live report " + update +
		 * ", total time:: " + (System.currentTimeMillis() - time));
		 * 
		 * }
		 */	}
	
	public static void main(String[] args) {
		System.out.println(Objects.toString(null));
	}
}
