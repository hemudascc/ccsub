package net.mycomp.mt2.ksa;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;

public class JMSMt2KSACGCallbackSdpListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSMt2KSACGCallbackSdpListener.class);
	
	@Autowired
	private IDaoService daoService;
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Override
	public void onMessage(Message message) {
		Mt2KSACGCallbackSdp mt2KSACGCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			mt2KSACGCallback = (Mt2KSACGCallbackSdp) objectMessage.getObject();
			redisCacheService.putObjectCacheValueByEvictionDay(Mt2KSAConstant.MT2_KSA_TOKEN_TRACKINGID_PREFIX+mt2KSACGCallback.getTrackingId(), mt2KSACGCallback.getToken(), 1);
		}catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		}finally {
			try {
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2KSACGCallback:: "
						+ mt2KSACGCallback);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2KSACGCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));		
		}
	}

}
