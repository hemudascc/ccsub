package net.mycomp.mt2.ksa;

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
import net.process.bean.SuscriberIdMsg;

@Service("jmsMt2KSAService")
public class JMSMt2KSAService {

	private static final Logger logger = Logger.getLogger(JMSMt2KSAService.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	@Qualifier("mt2KSASMSAlertJMSTemplate")
	private JmsTemplate mt2KSASMSAlertJMSTemplate;


	@Autowired
	@Qualifier("mt2KSAPinValidationJMSTemplate")
	private JmsTemplate mt2KSAPinValidationJMSTemplate;

	@Autowired
	@Qualifier("mt2KSANotificationJMSTemplate")
	private JmsTemplate mt2KSANotificationJMSTemplate;

	
	@Autowired
	@Qualifier("mt2KSACallbackSdpJMSTemplate")
	private JmsTemplate mt2KSACallbackSdpJMSTemplate;
	@Autowired
	@Qualifier("mt2KSATrakingSdpJMSTemplate")
	private JmsTemplate mt2KSATrakingSdpJMSTemplate;
	@Autowired
	@Qualifier("mt2KSANotificationSdpJMSTemplate")
	private JmsTemplate mt2KSANotificationSdpJMSTemplate;
	@Autowired
	@Qualifier("mt2KSADlrJMSTemplate")
	private JmsTemplate mt2KSADlrJMSTemplate;
	


	public boolean sendAlertSubscriberIdMsg(final SuscriberIdMsg suscriberIdMsg) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("sendAlertSubscriberIdMsg:: suscriberIdMsg:: "+suscriberIdMsg);
			mt2KSASMSAlertJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(suscriberIdMsg);
					return message;
				}
			});

		}catch(Exception ex){
			logger.error("sendAlertSubscriberIdMsg::::::::::::: ",ex);

			return false;
		}
		logger.debug("sendAlertSubscriberIdMsg:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

	public boolean processPinValidation(final MT2KSAServiceApiTrans mt2KSAServiceApiTrans) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("processPinValidation:: mt2KSAServiceApiTrans:: "+mt2KSAServiceApiTrans);
			mt2KSAPinValidationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {

					Message message=session.createObjectMessage(mt2KSAServiceApiTrans);
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});

		}catch(Exception ex){
			logger.error("processPinValidation::::::::::::: ",ex);

			return false;
		}
		logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
		return true;
	}


	public boolean processNotification(final Mt2KSANotification mt2KSANotification) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("processNotification:: mt2KSANotification:: "+mt2KSANotification);
			mt2KSANotificationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mt2KSANotification);
					return message;
				}
			});

		}catch(Exception ex){
			logger.error("processNotification::::::::::::: ",ex);
			daoService.saveObject(mt2KSANotification);		
			return false;
		}
		logger.debug("processNotification:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

	public boolean savemt2ksacgCallbackSdp(Mt2KSACGCallbackSdp mt2ksacgCallback) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("");
			mt2KSACallbackSdpJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage();
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});
		}catch(Exception ex){
			logger.error("",ex);
			return false;
		}
		logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

	public boolean saveMt2KSATrakingSdp(Mt2KSATracking mt2ksaTracking) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("");
			mt2KSATrakingSdpJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage();
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});
		}catch(Exception ex){
			logger.error("",ex);
			return false;
		}
		logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

	public boolean saveMt2KSANotificationSdp(Mt2KSANotificationSdp mt2ksaNotification) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("");
			mt2KSANotificationSdpJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage();
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});
		}catch(Exception ex){
			logger.error("",ex);
			return false;
		}
		logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

	public boolean saveMt2KSADlrSdp(List<Mt2KSADeliveryNotificationSdp> dlrs) {
		long time=System.currentTimeMillis();
		try{
			logger.debug("");
			mt2KSADlrJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage();
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5*60*1000);
					return message;
				}
			});
		}catch(Exception ex){
			logger.error("",ex);
			return false;
		}
		logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
		return true;
	}

}
