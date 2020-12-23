package net.mycom.nxt.vas;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;

@Service("jmsNxtVasService")
public class JMSNxtVasService {

	private static final Logger logger = Logger.getLogger(JMSNxtVasService.class);
	
	
	
	@Autowired
	 @Qualifier("nxtWebhookNotificationJMSTemplate")
	private JmsTemplate nxtWebhookNotificationJMSTemplate;
	
	
	@Autowired
	private IDaoService daoService;
	
	public boolean saveNxtWebhookNotification(final NxtWebhookNotification nxtWebhookNotification) {
	  long time=System.currentTimeMillis();
		try{
        logger.debug("saveNxtWebhookNotification:::: "+nxtWebhookNotification);
        nxtWebhookNotificationJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(nxtWebhookNotification);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveNxtWebhookNotification::::::::::::: ",ex);
			boolean save=daoService.saveObject(nxtWebhookNotification);			
			return save;
		}
		 logger.debug("saveSwaziRequestTrans:: total time "+(System.currentTimeMillis()-time));
		return true;
	}
	
	
}
