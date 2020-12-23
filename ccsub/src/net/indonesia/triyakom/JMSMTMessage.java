package net.indonesia.triyakom;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;

public class JMSMTMessage implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSMTMessage.class);

	@Autowired
	private TriyakomOperatorService triyakomOperatorService;
	
	
	
	@Override
	public void onMessage(Message m) {

		//logger.debug("onMessage::::::::::::::::: " + m);
		ObjectMessage objectMessage = (ObjectMessage) m;
		MTMessage mtMessage=null;
		try {
			 mtMessage=(MTMessage)objectMessage.getObject();
			// logger.debug("onMessage:::::::::::::::::mtMessage::  " + mtMessage);
			 triyakomOperatorService.handleSubscriptionMTPushMessage(mtMessage);
		} catch (Exception e) {
			logger.error("onMessage::::::::mainException  " + e);
		} finally {
			//daoService.saveObject(mtMessage);
		}
	}
}
