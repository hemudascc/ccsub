package net.mycomp.mt2.ksa;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.mycomp.mt2.uae.Mt2UAEConstant;
import net.persist.bean.LiveReport;

public class JMSMt2KSATrackingListener implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(JMSMt2KSATrackingListener.class);
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	@Override
	public void onMessage(Message message) {
		Mt2KSATracking mt2ksaTracking = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			mt2ksaTracking = (Mt2KSATracking)objectMessage.getObject();
			redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2ksaTracking.getMsisdn(), mt2ksaTracking.getToken(), 1);			
			
		}catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}finally {
			try {
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2ksaTracking:: "
						+ mt2ksaTracking);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ksaTracking);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));		
		}
		
	}
	
	
}
