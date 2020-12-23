package net.mycomp.wintel.bangladesh;

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

@Service("jmsWintelBDService")
public class JMSWintelBDService {

	private static final Logger logger = Logger.getLogger(JMSWintelBDService.class);
	
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("wintelBDMOJMSTemplate")
	private JmsTemplate wintelBDMOJMSTemplate;
	
	
	
	public boolean saveMO(final WintelBDMO wintelBDMO) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMO:: wintelBDMO:: "+wintelBDMO);
	        wintelBDMOJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(wintelBDMO);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMO::::::::::::: ",ex);
				boolean save=daoService.saveObject(wintelBDMO);			
				return save;
			}
			 logger.debug("saveMO:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
}
