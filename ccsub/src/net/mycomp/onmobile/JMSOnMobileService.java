package net.mycomp.onmobile;

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

@Service("jmsOnMobileService")
public class JMSOnMobileService {


	private static final Logger logger = Logger.getLogger(JMSOnMobileService.class);


	@Autowired
	@Qualifier("onMobileCGCallbackJMSTemplate")
	private JmsTemplate onMobileCGCallbackJMSTemplate;
	
	@Autowired
	@Qualifier("onMobileChargingNotificationJMSTemplate")
	private JmsTemplate onMobileChargingNotificationJMSTemplate;
	
	
	@Autowired
	private IDaoService daoService;
	
	
	
	public boolean saveCGCallback(final OnMobileCGCallback onMobileCGCallback) {
		
		try{
			onMobileCGCallbackJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(onMobileCGCallback);
				//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1*60*1000);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveOoredooOmanNotification::::::::::::: "+ex);
			boolean save=daoService.saveObject(onMobileCGCallback);			
			return save;
		}
		return true;
	}
	
public boolean saveOnMobileChargingNotification(final OnMobileChargingNotification onMobileChargingNotification) {
		
		try{
			onMobileChargingNotificationJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(onMobileChargingNotification);
				//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1*60*1000);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveOnMobileChargingNotification::::::::::::: ",ex);
			boolean save=daoService.saveObject(onMobileChargingNotification);			
			return false;
		}
		return true;
	}

}
