package net.mycomp.mt2.uae;

import java.util.List;

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

@Service("jmsMt2UAEService")
public class JMSMt2UAEService {

	private static final Logger logger = Logger.getLogger(JMSMt2UAEService.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("mt2UAENotificationJMSTemplate")
	private JmsTemplate mt2UAENotificationJMSTemplate;

	@Autowired
	@Qualifier("mt2UAECGCallbackJMSTemplate")
	private JmsTemplate mt2UAECGCallbackJMSTemplate;
	
	public boolean saveMt2UAENotification(final Mt2UAENotification mt2UAENotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2UAENotification:: mt2UAENotification:: "+mt2UAENotification);
	        mt2UAENotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2UAENotification);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMt2UAENotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(mt2UAENotification);			
				return save;
			}
			 logger.debug("saveMt2UAENotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	public boolean saveMt2UAEDlr(final List<Mt2UAEDeliveryNotification> dlrs) {
		for(Mt2UAEDeliveryNotification dlr:dlrs) {
		long time=System.currentTimeMillis();
		try{
			
			logger.debug("saveMt2UAEDeliveryNotification:: mt2UAEDeliveryNotification:: "+dlr);
			mt2UAENotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(dlr);
					return message;
				}
			});
			
		}catch(Exception ex){
			logger.error("saveMt2UAEDeliveryNotification::::::::::::: ",ex);
			boolean save=daoService.saveObject(dlr);			
			return save;
		}
		logger.debug("saveMt2UAEDeliveryNotification:: total time "+(System.currentTimeMillis()-time));
		}
		return true;
	}
	
	public boolean saveMt2UAECGCallback(final Mt2UAECGCallback mt2UAECGCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2UAECGCallback:: mt2UAECGCallback:: "+mt2UAECGCallback);
	        mt2UAECGCallbackJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2UAECGCallback);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMt2UAECGCallback::::::::::::: ",ex);
				boolean save=daoService.saveObject(mt2UAECGCallback);			
				return save;
			}
			 logger.debug("saveMt2UAECGCallback:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
}
