package net.mycomp.mobipay;

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

@Service("jmsMobiPayService")
public class JMSMobiPayService {

	
	private static final Logger logger = Logger.getLogger(JMSMobiPayService.class);
	
	@Autowired
    @Qualifier("mobiPayCGCallbackJMSTemplate")
	private JmsTemplate mobiPayCGCallbackJMSTemplate;
	
	@Autowired
	@Qualifier("mobiPayDlrJMSTemplate")
	private JmsTemplate mobiPayDlrJMSTemplate;
	@Autowired
	private IDaoService daoService;
	public boolean saveMobiPayCGCallback(MobiPayCGCallback mobiPayCGCallback) {
		
		long time=System.currentTimeMillis();
		try{
        logger.debug("saving mobiPayCGCallback :::::::::"+mobiPayCGCallback);
        mobiPayDlrJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(mobiPayCGCallback);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("error while saving mobiPayCGCallback:::::::::::::"+mobiPayCGCallback ,ex);
			boolean save=daoService.saveObject(mobiPayCGCallback);			
			return save;
		}
		logger.debug("saved mobiPayCGCallback:: total time "+(System.currentTimeMillis()-time));
		return true;
		
	}
	
	public boolean saveMobiPayDlrJMSTemplate(MobiPayDlr mobiPayDlr) {
		long time=System.currentTimeMillis();
		try{
        logger.debug("saving mobiPayDlr :::::::::"+mobiPayDlr);
        mobiPayDlrJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(mobiPayDlr);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("error while saving mobiPayDlr:::::::::::::"+mobiPayDlr ,ex);
			boolean save=daoService.saveObject(mobiPayDlr);			
			return save;
		}
		logger.debug("saved mobiPayDlr:: total time "+(System.currentTimeMillis()-time));
		return true;
		
	}

}
