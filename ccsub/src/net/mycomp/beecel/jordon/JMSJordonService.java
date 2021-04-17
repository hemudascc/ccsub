package net.mycomp.beecel.jordon;

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
import net.common.service.RedisCacheService;

@Service("jmsJordonMtService")
public class JMSJordonService {


	private static final Logger logger = Logger.getLogger(JMSJordonService.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	@Qualifier("bcJordonMtJMSTemplate")
	private JmsTemplate bcJordonMtJMSTemplate;
	
	@Autowired
	@Qualifier("bcJordonNotificationJMSTemplate")
	private JmsTemplate bcJordonNotificationJMSTemplate;

	@Autowired
	private RedisCacheService redisCacheService;
	
	public boolean bcJordonMTMessageJMSTemplate(final BCJordonMTMessage bcJordonMTMessage) {
		long time=System.currentTimeMillis();
		logger.debug("savebcJordonMTMessage:: bcJordonMTMessage:: "+bcJordonMTMessage);
		try {
			bcJordonMtJMSTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message = session.createObjectMessage(bcJordonMTMessage);
					return message;
				}
			});

		} catch (Exception e) {
			logger.error("savebcJordonMTMessage::::::::::::: ",e);
			boolean save=daoService.saveObject(bcJordonMTMessage);
			return save;
		}
		logger.debug("savebcJordonMTMessage:: total time "+(System.currentTimeMillis()-time));
		return true;
	}


public boolean bcJordonNotificationJMSTemplate(final BCJordonNotification bcJordonNotification) {
		long time=System.currentTimeMillis();
		logger.debug("saveBcJordonNotification:: bcJordonNotification:: "+bcJordonNotification);
		try {
			String token =(String)redisCacheService.getObjectCacheValue(BCJordonConstant.CG_CALLBACK_CAHCHE_PREFIX+bcJordonNotification.getMsisdn());
			
			bcJordonNotificationJMSTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message = session.createObjectMessage(bcJordonNotification);
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 12*1000L);
					return message;
				}
			});

		} catch (Exception e) {
			logger.error("saveBcJordonNotification::::::::::::: ",e);
			boolean save=daoService.saveObject(bcJordonNotification);
			return save;
		}
		logger.debug("saveBcJordonNotification:: total time "+(System.currentTimeMillis()-time));
		return true;
	}
}