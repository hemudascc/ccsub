package net.mycomp.mt2.zain.iraq;

import java.util.List;

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
import net.mycomp.mt2.uae.Mt2UAEDeliveryNotification;

@Service("jmsZainIraqService")
public class JMSMt2ZainIraqService {

	private static final Logger logger = Logger.getLogger(JMSMt2ZainIraqService.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("mt2ZainIraqNotificationJMSTemplate")
	private JmsTemplate mt2ZainIraqNotificationJMSTemplate;

	@Autowired
	@Qualifier("mt2ZainIraqCGCallbackJMSTemplate")
	private JmsTemplate mt2ZainIraqCGCallbackJMSTemplate;
	
	@Autowired
	@Qualifier("mt2ZainIraqDLRJMSTemplate")
	private JmsTemplate mt2ZainIraqDLRJMSTemplate;
	
	
	
	public boolean saveMt2ZainIraqNotification(final Mt2ZainIraqNotification mt2ZainIraqNotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2ZainIraqNotification:: mt2ZainIraqNotification:: "+mt2ZainIraqNotification);
	        mt2ZainIraqNotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2ZainIraqNotification);
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 2*60*1000);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMt2ZainIraqNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(mt2ZainIraqNotification);			
				return save;
			}
			 logger.debug("saveMt2ZainIraqNotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	public boolean saveMt2ZainIraqCGCallback(final MT2ZainCGCallback mt2ZainCGCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2ZainIraqNotification:: mt2ZainCGCallback:: "+mt2ZainCGCallback);
	        mt2ZainIraqCGCallbackJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2ZainCGCallback);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMt2ZainIraqCGCallback::::::::::::: ",ex);
				boolean save=daoService.saveObject(mt2ZainCGCallback);			
				return save;
			}
			 logger.debug("saveMt2ZainIraqCGCallback:: total time "+(System.currentTimeMillis()-time));
			return true;
		}


	public boolean saveMt2ZainIraqDlr(List<Mt2ZainIraqDeliveryNotification> dlrs) {
		for(Mt2ZainIraqDeliveryNotification dlr:dlrs) {
			long time=System.currentTimeMillis();
			try{
				
				logger.debug("saveMt2ZainIraqDeliveryNotification:: ZainIraqDeliveryNotification:: "+dlr);
				mt2ZainIraqDLRJMSTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						Message message=session.createObjectMessage(dlr);
						return message;
					}
				});
				
			}catch(Exception ex){
				logger.error("saveZainIraqDeliveryNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(dlr);			
				return save;
			}
			logger.debug("saveZainIraqDeliveryNotification:: total time "+(System.currentTimeMillis()-time));
			}
			return true;
	}
	
	
}
