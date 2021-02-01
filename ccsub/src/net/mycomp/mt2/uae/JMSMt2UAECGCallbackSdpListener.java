package net.mycomp.mt2.uae;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;

public class JMSMt2UAECGCallbackSdpListener implements MessageListener {

	private static final Logger logger = Logger.getLogger(JMSMt2UAECGCallbackSdpListener.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	@Override
	public void onMessage(Message m) {

		Mt2UAECGCallbackSdp mt2UAECGCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2UAECGCallback = (Mt2UAECGCallbackSdp) objectMessage.getObject();
			redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_TOKEN_TRACKINGID_PREFIX+mt2UAECGCallback.getTrackingId(), mt2UAECGCallback.getToken(), 1);
		}catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		}finally {
			try {
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2UAECGCallbackSdp:: "
						+ mt2UAECGCallback);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2UAECGCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));		
		}
	}
}
