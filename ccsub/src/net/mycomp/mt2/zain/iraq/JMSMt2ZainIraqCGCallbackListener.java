package net.mycomp.mt2.zain.iraq;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMCGZAOBSWindow;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.mobimind.MobimindConstant;
import net.mycomp.mobimind.MobimindSubcriberStatus;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2ZainIraqCGCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2ZainIraqCGCallbackListener.class);

	

	@Autowired
	private IDaoService daoService;
	
    @Autowired
    private RedisCacheService redisCacheService;
    
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private SubscriberRegService subscriberRegService; 
	
	@Autowired
	private Mt2ZainIraqServiceApi mt2ZainIraqServiceApi;

	@Override
	public void onMessage(Message m) {

		MT2ZainCGCallback mt2ZainCGCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		String msg=null;
		Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2ZainCGCallback = (MT2ZainCGCallback) objectMessage
					.getObject();
			
			logger.info("mt2ZainCGCallback::::: "+mt2ZainCGCallback);
			String token=Objects.toString(redisCacheService.getObjectCacheValue(
					Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_UNIQUEID_CACHE_PREFIX
					+mt2ZainCGCallback.getUniqeId()));
			
			mt2ZainCGCallback.setToken(token);
			redisCacheService.putObjectCacheValueByEvictionDay(
					Mt2ZainIraqConstant
					.MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX
					+mt2ZainCGCallback.getMsisdn(), token, 10);
			
			 cgToken=new CGToken(token);			  
			 VWServiceCampaignDetail vwServiceCampaignDetail= 
					 MData
					 .mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			 
			 mt2ZainIraqServiceConfig=
					Mt2ZainIraqConstant.mapZainIraqServiceIdToMt2ZainIrqServiceConfig
					.get(vwServiceCampaignDetail.getServiceId());
			 
			  liveReport=new LiveReport(MConstants.MT2_ZAIN_IRAQ_OPERATOR_ID,
					   new Timestamp(System.currentTimeMillis())
				 ,cgToken.getCampaignId(),mt2ZainIraqServiceConfig.getServiceId(),0);		
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setToken(cgToken.getCGToken());			
				liveReport.setMsisdn(mt2ZainCGCallback.getMsisdn());
				liveReport.setCircleId(0);
				if(mt2ZainCGCallback.getSuccess().equalsIgnoreCase("1")){
					subscriberRegService.findOrCreateSubscriberByAct(mt2ZainCGCallback.getMsisdn()
							, null, liveReport);
				}
				
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
			
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2ZainCGCallback:: "
						+ mt2ZainCGCallback);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ZainCGCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
	}
}
