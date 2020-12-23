package net.mycomp.mt2.zain.iraq;

import java.sql.Timestamp;
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
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSMt2ZainIraqDLRListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2ZainIraqDLRListener.class);

	

	@Autowired
	private IDaoService daoService;
	
    @Autowired
    private RedisCacheService redisCacheService;
    
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private Mt2ZainIraqServiceApi mt2ZainIraqServiceApi;

	@Override
	public void onMessage(Message m) {
		Mt2ZainIraqDeliveryNotification mt2ZainIraqDeliveryNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		String msg=null;
		Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=null;
		List<SubscriberReg> subscriberRegs=null;
		String token=null;
		Integer serviceId=0;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			
			mt2ZainIraqDeliveryNotification = (Mt2ZainIraqDeliveryNotification) objectMessage.getObject();
			
			logger.info("mt2ZainIraqDeliveryNotification::::: "+mt2ZainIraqDeliveryNotification);
			
			subscriberRegs = jpaSubscriberReg.findSubscriberRegByMsisdn(mt2ZainIraqDeliveryNotification.getMSISDN());
				
			if(subscriberRegs!=null) {
				token=subscriberRegs.get(0).getToken();
			}
			if(token!=null && !token.isEmpty()){
				 cgToken=new CGToken(token);
			}else{
			//http://192.241.253.234/ccsub/cnt/cmp?adid=2&cmpid=221&token=zain
			 cgToken=new CGToken(System.currentTimeMillis(), -1, 221); 
			}	
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			serviceId = vwServiceCampaignDetail.getServiceId();	
			mt2ZainIraqServiceConfig = Mt2ZainIraqConstant.mapServiceIdToMt2ZainIrqServiceConfig.get(serviceId);
			logger.info("mt2ZainIraqServiceConfig"+mt2ZainIraqServiceConfig);
			Service service=MData.mapServiceIdToService.get(serviceId);
			
			liveReport=new LiveReport(service.getOpId(),
					   new Timestamp(System.currentTimeMillis())
				 ,cgToken.getCampaignId(),mt2ZainIraqServiceConfig.getServiceId(),mt2ZainIraqServiceConfig.getProductId()); 
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setMsisdn(mt2ZainIraqDeliveryNotification.getMSISDN());
				liveReport.setCircleId(0);
				liveReport.setMode("");
				String status = Objects.toString(redisCacheService.getCacheValue(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX+mt2ZainIraqDeliveryNotification.getMSISDN())); 
			if(mt2ZainIraqDeliveryNotification.getStatus().equals("Success") 
					&& MConstants.ACT.equals(status)) {
				  //act 
					 liveReport.setAction(MConstants.ACT);				
					 liveReport.setAmount(MUtility.toDouble(mt2ZainIraqDeliveryNotification.getPrice(), 0));
					 liveReport.setNoOfDays(MUtility.toInt(mt2ZainIraqDeliveryNotification.getValidaityDays(), 0));
					// msg=mt2ZainIraqServiceConfig.getSubMsgTemplate();
					 redisCacheService.removeObjectCacheValue(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX+mt2ZainIraqDeliveryNotification.getMSISDN());
				}else if(mt2ZainIraqDeliveryNotification.getStatus().equals("Success") && Objects.nonNull(subscriberRegs)){
					 liveReport.setAction(MConstants.RENEW);
					 liveReport.setRenewalCount(1);
					 liveReport.setRenewalAmount(MUtility.toDouble(mt2ZainIraqServiceConfig.getPricePoint().toString(),0));
					 liveReport.setNoOfDays(mt2ZainIraqServiceConfig.getValidity());
					 liveReport.setParam1(mt2ZainIraqDeliveryNotification.getId());
					// msg = mt2ZainIraqServiceConfig.getRenewMsgTemplate();
				} 
				else {
					//grace
					liveReport.setAction(MConstants.GRACE);
					liveReport.setGraceConversionCount(1);
				}
				
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}
		finally {
			try {		
				
				mt2ZainIraqDeliveryNotification.setAction(liveReport.getAction());
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				if(msg!=null){
					msg=Mt2ZainIraqConstant.prepareMessage(msg, mt2ZainIraqServiceConfig,liveReport.getParam1());
					mt2ZainIraqServiceApi.sendContentSms(mt2ZainIraqDeliveryNotification.getMSISDN(),
							msg, "", mt2ZainIraqDeliveryNotification.getAction());
				} 
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2ZainIraqDeliveryNotification:: "
						+ mt2ZainIraqDeliveryNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ZainIraqDeliveryNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
	}
}
