package net.mycomp.etisalat;

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

@Service("jmsEtisalatService")
public class JMSEtisalatService {

	private static final Logger logger = Logger.getLogger(JMSEtisalatService.class);
	
	
	@Autowired
    @Qualifier("etisalatChargingJMSTemplate")
	private JmsTemplate etisalatChargingJMSTemplate;
	
	@Autowired
	private IDaoService daoService;
	
	

	
	public boolean saveEtisalatChargingCallback(final EtisalatChargingCallback etisalatChargingCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveEtisalatChargingCallback:: etisalatChargingCallback:: "+etisalatChargingCallback);
	        etisalatChargingJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(etisalatChargingCallback);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveEtisalatChargingCallback::::::::::::: ",ex);
				boolean save=daoService.saveObject(etisalatChargingCallback);			
				return save;
			}
			 logger.debug("saveEtisalatChargingCallback:: total time "+(System.currentTimeMillis()-time));
			return true;
		}

	
}
