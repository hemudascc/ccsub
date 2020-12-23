package net.mycomp.du;

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

@Service("jmsDuService")
public class JMSDUService {

	
	@Autowired
	@Qualifier("duChargingCallbackJMSTemplate")
	private JmsTemplate duChargingCallbackJMSTemplate;
	
	@Autowired
	private IDaoService daoService;
	
	

	private static final Logger logger = Logger.getLogger(JMSDUService.class);

	public boolean saveDUChargingCallbackJMSTemplate(final DUChargingNotification duChargingNotification) {
	  long time=System.currentTimeMillis();
		try{
        logger.debug("saveDUChargingCallbackJMSTemplate:: duChargingCallbackJMSTemplate "+duChargingNotification);
        duChargingCallbackJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(duChargingNotification);
				//message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveDUChargingCallbackJMSTemplate::::::::::::: "+ex);
			boolean save=daoService.saveObject(duChargingNotification);			
			return save;
		}
		 logger.debug("saveDUChargingCallbackJMSTemplate:: total time "+(System.currentTimeMillis()-time));
		return true;
	}
	

	
	
}
