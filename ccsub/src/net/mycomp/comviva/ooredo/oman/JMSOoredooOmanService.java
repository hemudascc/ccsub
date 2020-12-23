package net.mycomp.comviva.ooredo.oman;

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

@Service("jmsOoredooOmanService")
public class JMSOoredooOmanService {


	private static final Logger logger = Logger.getLogger(JMSOoredooOmanService.class);


	@Autowired
	@Qualifier("ooredooOmanNotificationJMSTemplate")
	private JmsTemplate ooredooOmanNotificationJMSTemplate;
	
	@Autowired
	@Qualifier("ooredoOmanInappJMSTemplate")
	private JmsTemplate ooredoOmanInappJMSTemplate;
	
	
	@Autowired
	private IDaoService daoService;
	
	
	
	public boolean saveOoredooOmanNotification(final OoredooOmanNotification ooredooOmanNotification) {
		
		try{
			ooredooOmanNotificationJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(ooredooOmanNotification);
				message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1*60*1000);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveOoredooOmanNotification::::::::::::: "+ex);
			boolean save=daoService.saveObject(ooredooOmanNotification);			
			return save;
		}
		return true;
	}
	
public boolean saveooredooOmanOCSLogDetail(final OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail) {
		
		try{
			ooredoOmanInappJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(ooredooOmanOCSLogDetail);
				//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1*60*1000);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveooredooOmanOCSLogDetail::::::::::::: ",ex);
			//boolean save=daoService.saveObject(ooredooOmanNotification);			
			return false;
		}
		return true;
	}

}
