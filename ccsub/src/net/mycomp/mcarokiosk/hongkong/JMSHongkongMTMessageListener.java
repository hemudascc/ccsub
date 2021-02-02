package net.mycomp.mcarokiosk.hongkong;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;

public class JMSHongkongMTMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSHongkongMTMessageListener.class);

	@Autowired
	private MacroKioskHongkongFactoryService macroKioskHongkongFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Override
	public void onMessage(Message m) {
		
		long time = System.currentTimeMillis();
		logger.info("onMessage::::::::::::::::: " + m);
//		ObjectMessage objectMessage = (ObjectMessage) m;
//		boolean update = false;		
//		MTMessage mtMessage=null;
//		try {			
//			 mtMessage=(MTMessage)objectMessage;
//			 logger.info("onMessage:::::::: "+mtMessage);
//			macroKioskFactoryService.handleSubscriptionMTMessage(mtMessage);
//			logger.info("update:: "+objectMessage+", update:: "+update);
//		} catch (Exception e) {
//			logger.error("onMessage:::::::::::::::::"  + " , Exception  " , e);
//		}finally{
//			daoService.saveObject(mtMessage);
//		}	
		
		logger.info("onMessage::::::::::::::::: :: update:: " + ", total time:: " + (System.currentTimeMillis() - time));
	}	

}
