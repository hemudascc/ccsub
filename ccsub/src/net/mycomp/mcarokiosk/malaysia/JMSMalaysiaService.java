package net.mycomp.mcarokiosk.malaysia;

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
import net.process.bean.SuscriberIdMsg;

@Service("jmsMalaysiaService")
public class JMSMalaysiaService {

	private static final Logger logger = Logger.getLogger(JMSMalaysiaService.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("malaysiaMOMessageJMSTemplate")
	private JmsTemplate malaysiaMOMessageJMSTemplate;
	
	
	@Autowired
	@Qualifier("malaysiaMTMessageJMSTemplate")
	private JmsTemplate malaysiaMTMessageJMSTemplate;
	
	@Autowired
	@Qualifier("malaysiaDNJMSTemplate")
	private JmsTemplate malaysiaDNJMSTemplate;
	
	@Autowired
	@Qualifier("mkMalayisiaRenewalMessageJMSTemplate")
	private JmsTemplate mkMalayisiaRenewalMessageJMSTemplate;
	
	
	public boolean saveMOMessage(final MalasiyaMOMessage malasiyaMOMessage) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMOMessage:: malasiyaMOMessage:: "+malasiyaMOMessage);
	        malaysiaMOMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(malasiyaMOMessage);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMOMessage::::::::::::: ",ex);
				boolean save=daoService.saveObject(malasiyaMOMessage);			
				return save;
			}
			 logger.debug("saveMOMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveMTMessage(final MalasiyaMTMessage malasiyaMTMessage) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMTMessage:: malasiyaMTMessage:: "+malasiyaMTMessage);
	        malaysiaMTMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(malasiyaMTMessage);
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("saveMTMessage::::::::::::: ",ex);
				boolean save=daoService.saveObject(malasiyaMTMessage);			
				return save;
			}
			 logger.debug("saveMTMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean saveMalaysiaDeliveryNotification(final MalaysiaDeliveryNotification malaysiaDeliveryNotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMalaysiaDeliveryNotification:: malaysiaDeliveryNotification:: "
			+malaysiaDeliveryNotification);
	        malaysiaDNJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(malaysiaDeliveryNotification);
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("saveMalaysiaDeliveryNotification::::::::::::: ",ex);
				boolean save=daoService.saveObject(malaysiaDeliveryNotification);			
				return save;
			}
			 logger.debug("saveMalaysiaDeliveryNotification:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	public boolean sendRenewal(final SuscriberIdMsg suscriberIdMsg) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("sendRenewal:: suscriberIdMsg:: "+suscriberIdMsg);
	        mkMalayisiaRenewalMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(suscriberIdMsg);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("sendRenewal::::::::::::: ",ex);
				
			}
			 logger.debug("sendRenewal:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
}
