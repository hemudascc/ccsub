package net.mycomp.mcarokiosk.hongkong;

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

@Service("jmsHongkongService")
public class JMSHongkongService {

	private static final Logger logger = Logger.getLogger(JMSHongkongService.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("hongkongMOMessageJMSTemplate")
	private JmsTemplate hongkongMOMessageJMSTemplate;
	
	
	@Autowired
	@Qualifier("hongkongMTMessageJMSTemplate")
	private JmsTemplate hongkongMTMessageJMSTemplate;
	
	@Autowired
	@Qualifier("hongkongDNJMSTemplate")
	private JmsTemplate hongkongDNJMSTemplate;
	
	
	public boolean saveMOMessage(final HongkongMOMessage hongkongMOMessage) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMOMessage:: hongkongMOMessage:: "+hongkongMOMessage);
	        hongkongMOMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {  
					Message message=session.createObjectMessage(hongkongMOMessage);
					logger.error("saveMOMessage::::::::::::: "+message);
					return message;
				}
			});   
	        
			}catch(Exception ex){
				logger.error("saveMOMessage::::::::::::: ",ex);
				boolean save=daoService.saveObject(hongkongMOMessage);			
				return save;
			}
			 logger.debug("saveMOMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveMTMessage(final HongkongMTMessage hongkongMTMessage) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMTMessage:: hongkongMTMessage:: "+hongkongMTMessage);
	        hongkongMTMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(hongkongMTMessage);
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("saveMTMessage::::::::::::: ",ex);
				boolean save=daoService.saveObject(hongkongMTMessage);			
				return save;
			}
			 logger.debug("saveMTMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveHongkongDeliveryNotification(final HongkongDeliveryNotification hongkongDeliveryNotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveHongkongDeliveryNotification:: hongkongDeliveryNotification:: "
			+hongkongDeliveryNotification);
	        hongkongDNJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(hongkongDeliveryNotification);
					return message;
				}
			});	          
			}catch(Exception ex){
				logger.error("saveMalaysiaDeliveryNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(hongkongDeliveryNotification);			
				return save;
			}
			 logger.debug("saveMalaysiaDeliveryNotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
//	
//	public void checkStatus(String msisdn) {
//		daoService
//	}
}
