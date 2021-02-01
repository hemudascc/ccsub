package net.mycomp.mt2.uae;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;

public class JMSMt2UAETrackingListener implements MessageListener {

	private static final Logger logger = Logger.getLogger(JMSMt2UAETrackingListener.class);
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	@Override
	public void onMessage(Message m) {
		
		Mt2UAETracking mt2uaeTracking = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2uaeTracking = (Mt2UAETracking)objectMessage.getObject();
			redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2uaeTracking.getMsisdn(), mt2uaeTracking.getToken(), 1);			
			
		}catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}finally {
			try {
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2uaeTracking:: "
						+ mt2uaeTracking);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2uaeTracking);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));		
		}
		
	}
	
}
