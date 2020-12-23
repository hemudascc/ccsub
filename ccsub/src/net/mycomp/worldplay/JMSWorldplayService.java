package net.mycomp.worldplay;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;

@Service("jmsWorldplayService")
public class JMSWorldplayService {

	private static final Logger logger = Logger.getLogger(JMSWorldplayService.class);
	
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("worlplayNotificationJMSTemplate")
	private JmsTemplate worlplayNotificationJMSTemplate;
	
	@Autowired
	@Qualifier("worlplayCGCallbackJMSTemplate")
	private JmsTemplate worlplayCGCallbackJMSTemplate;
	
	
	public boolean saveWorldplayNotification(final WorldplayNotification worldplayNotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveWorldplayNotification:: worldplayNotification:: "+worldplayNotification);
	        worlplayNotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(worldplayNotification);
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveWorldplayNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(worldplayNotification);			
				return save;
			}
			 logger.debug("saveWorldplayNotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveWorldplayCGCallback(final WorldplayCGCallback worldplayCGCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveWorldplayCGCallback:: worldplayCGCallback:: "+worldplayCGCallback);
	        worlplayCGCallbackJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(worldplayCGCallback);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveWorldplayCGCallback::::::::::::: ",ex);
				boolean save=daoService.saveObject(worldplayCGCallback);			
				return save;
			}
			 logger.debug("saveWorldplayCGCallback:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
}
