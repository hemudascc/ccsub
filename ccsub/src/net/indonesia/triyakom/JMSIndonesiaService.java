package net.indonesia.triyakom;

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

@Service("jmsIndonesiaService")
public class JMSIndonesiaService {

	private static final Logger logger = Logger.getLogger(JMSIndonesiaService.class);

	
	@Autowired
	@Qualifier("indonesiaDlrJMSTemplate")
	private JmsTemplate indonesiaDlrJMSTemplate;
	
	
	@Autowired
	@Qualifier("moJMSTemplate")
	private JmsTemplate moJMSTemplate;
	
	@Autowired
	@Qualifier("indonesiaMTMessageJMSTemplate")
	private JmsTemplate indonesiaMTMessageJMSTemplate;
	
	
	@Autowired
	@Qualifier("indonesiaRenewalJMSTemplate")
	private JmsTemplate indonesiaRenewalJMSTemplate;
	
	@Autowired
	private IDaoService daoService;
	
	
	public boolean saveDLR(final DLRNotification  dlrNotification) {
		
		  long time=System.currentTimeMillis();
			try{
	       // logger.info("saveDLR::   "+dlrNotification);
	        indonesiaDlrJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(dlrNotification);
								}
			});
			}catch(Exception ex){
				logger.error("saveDLR::::::::::::: ",ex);
				
			}
		//	logger.info("saveDLR:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
//	public boolean sendMT(final MTMessage  mtMessage) {
//		
//		  long time=System.currentTimeMillis();
//			try{
//	        logger.info("sendMT::   "+mtMessage);
//	        indonesiaMTMessageJMSTemplate.send(new MessageCreator() {
//				@Override
//				public Message createMessage(Session session) throws JMSException {
//					return session.createObjectMessage(mtMessage);
//			    }
//			});
//			}catch(Exception ex){
//				logger.error("sendMT::::::::::::: ",ex);
//				
//			}
//			logger.info("sendMT:: total time "+(System.currentTimeMillis()-time));
//			return true;
//		}
	
	
	public boolean saveMOMessage(final MOMessage moMessage) {
		  long time=System.currentTimeMillis();
			try{
	     //   logger.info("saveMOMessage:: object "+moMessage);
	        moJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(moMessage);
				}
			});
			}catch(Exception ex){
				logger.error("saveMOMessage::::::::::::: "+ex);
				boolean save=daoService.saveObject(moMessage);
				
				return save;
			}
			// logger.info("saveMOMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	public boolean saveMTMessage(final MTMessage mtMessage) {
		  long time=System.currentTimeMillis();
			try{
	       // logger.info("saveMTMessage:: object "+mtMessage);
	        indonesiaMTMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message= session.createObjectMessage(mtMessage);
				     message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 2*60*1000);
					return message;
				}
			});
			}catch(Exception ex){
				logger.error("saveMTMessage::::::::::::: "+ex);
				//boolean save=daoService.saveObject(mtMessage);
				
				return false;
			}
			 //logger.info("saveMTMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	public boolean sendRenewalRequest(final SuscriberIdMsg suscriberIdMsg) {
		
		   try{
				indonesiaRenewalJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message= session.createObjectMessage(suscriberIdMsg);				    
					return message;
				}
			});	        
			}catch(Exception ex){
				logger.error("sendRenewalRequest::::::::::::: "+ex);				
				return false;
			}			
			return true;
		}
}
