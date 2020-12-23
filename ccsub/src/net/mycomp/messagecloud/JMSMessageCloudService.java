package net.mycomp.messagecloud;

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

@Service("jmsMessageCloudService")
public class JMSMessageCloudService {


	private static final Logger logger = Logger.getLogger(JMSMessageCloudService.class);

	@Autowired
	@Qualifier("messagecloudNotificationJMSTemplate")
	private JmsTemplate messagecloudNotificationJMSTemplate;
	
	@Autowired
	private IDaoService daoService;
	
	public boolean saveMessagecloudNotification(final MessagecloudNotification messagecloudNotification) {
	
		try{
			messagecloudNotificationJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(messagecloudNotification);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveMessagecloudNotification::::::::::::: ",ex);
			boolean save=daoService.saveObject(messagecloudNotification);			
			return save;
		}
		return true;
	}
	
	}
