package net.mycomp.mobipay;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;

public class JMSCGCallbackListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSCGCallbackListener.class);
	@Autowired
	private IDaoService daoService;
	@Autowired
	MobiPayApiService mobiPayApiService;
	
	@Autowired
	RedisCacheService redisService;
	
	@Override
	public void onMessage(Message message) {
		MobiPayCGCallback mobiPayCGCallback=null;
		VWServiceCampaignDetail vwServiceCampaignDetail=null;
		CGToken cgToken=null;
		MobiPayServiceConfig mobiPayServiceConfig=null;
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			mobiPayCGCallback = (MobiPayCGCallback)objectMessage.getObject();
			cgToken=new CGToken(mobiPayCGCallback.getToken());
			vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail
					.get(cgToken.getCampaignId());
			mobiPayServiceConfig = MobiPayConstant
					.mapServiceIdToMobiPayServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			mobiPayApiService.sendSms(mobiPayCGCallback, mobiPayServiceConfig);
			redisService.putObjectCacheValueByEvictionDay(MobiPayConstant.MOBIPAY_MSISDN_TOKEN_CACHE_PREFIX+mobiPayCGCallback.getUserId(), mobiPayCGCallback.getToken(), 1);
		} catch (Exception e) {
			logger.error("error while processing cg callback"+mobiPayCGCallback, e);
		}finally {
			daoService.saveObject(mobiPayCGCallback);
		}
	}
}
