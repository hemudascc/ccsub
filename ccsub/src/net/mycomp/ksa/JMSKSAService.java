package net.mycomp.ksa;

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
import net.mycomp.messagecloud.gateway.MCGDeliveryReport;
import net.mycomp.messagecloud.gateway.MCGMoMessage;

@Service("jmsKSAService")
public class JMSKSAService {

	private static final Logger logger = Logger.getLogger(JMSKSAService.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("ksaPinValidationJMSTemplate")
	private JmsTemplate ksaPinValidationJMSTemplate;

	@Autowired
	@Qualifier("ksaNotificationJMSTemplate")
	private JmsTemplate ksaNotificationJMSTemplate;
	
	
	public boolean saveKsaApiTrans(final KsaApiTrans ksaApiTrans) {
		
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveKsaApiTrans:: mobimindNotification:: "+ksaApiTrans);
	        ksaPinValidationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(ksaApiTrans);
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("saveKsaApiTrans::::::::::::: ",ex);
				boolean save=daoService.saveObject(ksaApiTrans);			
				return save;
			}
			 logger.debug("saveKsaApiTrans:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveKsaNotification(final KsaNotification ksaNotification) {
		
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveKsaNotification:: ksaNotification:: "+ksaNotification);
	        ksaNotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(ksaNotification);
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("saveKsaNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(ksaNotification);			
				return save;
			}
			 logger.debug("saveKsaNotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
}
