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
	@Qualifier("mt2UAENotificationSdpJMSTemplate")
	private JmsTemplate mt2UAENotificationSdpJMSTemplate;

	@Autowired
	@Qualifier("mt2UAEDLRSdpJMSTemplate")
	private JmsTemplate mt2UAEDLRSdpJMSTemplate;

	@Autowired
	@Qualifier("mt2UAECGCallbackSdpJMSTemplate")
	private JmsTemplate mt2UAECGCallbackSdpJMSTemplate;
	
	@Autowired
	@Qualifier("mt2UAENotificationJMSTemplate")
	private JmsTemplate mt2UAENotificationJMSTemplate;

	@Autowired
	@Qualifier("mt2UAECGCallbackJMSTemplate")
	private JmsTemplate mt2UAECGCallbackJMSTemplate;
	
	@Autowired
	@Qualifier("mt2UAETrackingJMSTemplate")
	private JmsTemplate mt2UAETrackingJMSTemplate;
	
	public boolean saveMt2UAENotificationSdp(final Mt2UAENotificationSdp mt2UAENotification) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2UAENotification:: mt2UAENotification:: "+mt2UAENotification);
	        mt2UAENotificationSdpJMSTemplate.send(new MessageCreator() {
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
	
	
	public boolean saveMt2UAEDlrSdp(final List<Mt2UAEDeliveryNotificationSdp> dlrs) {
		for(Mt2UAEDeliveryNotificationSdp dlr:dlrs) {
		long time=System.currentTimeMillis();
		try{
			
			logger.debug("saveMt2UAEDeliveryNotification:: mt2UAEDeliveryNotification:: "+dlr);
			mt2UAEDLRSdpJMSTemplate.send(new MessageCreator() {
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
	
	public boolean saveMt2UAECGCallbackSdp(final Mt2UAECGCallbackSdp mt2UAECGCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2UAECGCallback:: mt2UAECGCallback:: "+mt2UAECGCallback);
	        mt2UAECGCallbackSdpJMSTemplate.send(new MessageCreator() {
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
	
	public boolean saveMt2UAETrakingSdp(final Mt2UAETracking mt2uaeTracking) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMt2UAETracking:: mt2uaeTracking:: "+mt2uaeTracking);
	        mt2UAETrackingJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2uaeTracking);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("savemt2uaeTracking::::::::::::: ",ex);
				boolean save=daoService.saveObject(mt2uaeTracking);			
				return save;
			}
			 logger.debug("savemt2uaeTracking:: total time "+(System.currentTimeMillis()-time));
			return true;
	}
	
	
}
