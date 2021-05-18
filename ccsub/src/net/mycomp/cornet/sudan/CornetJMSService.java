package net.mycomp.cornet.sudan;

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

@Service("cornetJMSService")
public class CornetJMSService {
		
		private static final Logger logger = Logger.getLogger(CornetJMSService.class);
		
		@Autowired
		private IDaoService daoService;
		
		@Autowired
		@Qualifier("cornetCallbackJMSTemplate")
		private JmsTemplate cornetCallbackJMSTemplate;
		
		public boolean saveCornetnotification(CornetNotification cornetNotification) {
			long time=System.currentTimeMillis();
			logger.debug("saving:::::CornetNotification:: "+cornetNotification);
			try {
				cornetCallbackJMSTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						Message message = session.createObjectMessage(cornetNotification);
						return message;
					}
				});
			} catch (Exception e) {
				logger.error("error while saving CornetNotification: "+cornetNotification, e);
				boolean save=daoService.saveObject(cornetNotification);
				return save;
			}
			logger.debug("saved:::::CornetNotification:: "+cornetNotification+" total time "+(System.currentTimeMillis()-time));
			return true;
		}

}
