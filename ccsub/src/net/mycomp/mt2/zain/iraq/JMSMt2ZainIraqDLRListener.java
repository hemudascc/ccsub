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
	private LiveReportFactoryService liveReportFactoryService;



	private String string;
	

	@Override
	public void onMessage(Message m) {
		Mt2ZainIraqDeliveryNotification mt2ZainIraqDeliveryNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=null;
		List<SubscriberReg> subscriberRegs=null;
		String token=null;
		Integer serviceId=0;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			
			mt2ZainIraqDeliveryNotification = (Mt2ZainIraqDeliveryNotification) objectMessage.getObject();
			
			logger.info("mt2ZainIraqDeliveryNotification::::: "+mt2ZainIraqDeliveryNotification);
			
			token = Objects.toString(redisCacheService.getObjectCacheValue(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX+mt2ZainIraqDeliveryNotification.getMsisdn()));
			logger.info("token::::::::"+token);
			if(!token.equals("null")){
					 cgToken=new CGToken(token);
			}else{
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
				liveReport.setMsisdn(mt2ZainIraqDeliveryNotification.getMsisdn());
				liveReport.setCircleId(0);
				liveReport.setMode("");
				liveReport.setResponse(mt2ZainIraqDeliveryNotification.toString());
				String status = Objects.toString(redisCacheService.getCacheValue(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX+mt2ZainIraqDeliveryNotification.getMsisdn())); 
			string = "Success";
			if(mt2ZainIraqDeliveryNotification.getStatus().equals(string) 
					&& MConstants.ACT.equals(status)) {
				  //act 
					 liveReport.setAction(MConstants.ACT);	
					 liveReport.setConversionCount(1);
					 liveReport.setAmount(mt2ZainIraqDeliveryNotification.getPrice());
					 liveReport.setNoOfDays(mt2ZainIraqDeliveryNotification.getValidity());
					 redisCacheService.removeObjectCacheValue(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX+mt2ZainIraqDeliveryNotification.getMsisdn());
				}else if(mt2ZainIraqDeliveryNotification.getStatus().equals(string) && Objects.nonNull(subscriberRegs)){
					 liveReport.setAction(MConstants.RENEW);
					 liveReport.setRenewalCount(1);
					 liveReport.setRenewalAmount(MUtility.toDouble(mt2ZainIraqServiceConfig.getPricePoint().toString(),0));
					 liveReport.setNoOfDays(mt2ZainIraqServiceConfig.getValidity());
					 liveReport.setParam1(mt2ZainIraqDeliveryNotification.getId().toString());
				} 
				else {
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
