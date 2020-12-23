package net.mycomp.mobivate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import net.common.service.IDaoService;

import org.apache.activemq.ScheduledMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("jmsMobivateService")
public class JMSMobivateService {
	
  private static final Logger logger=Logger.getLogger(JMSMobivateService.class);
	
	@Autowired
    @Qualifier("mobivateCGCallbackJMSTemplate")
	private JmsTemplate mobivateCGCallbackJMSTemplate;
	
	@Autowired
	@Qualifier("mobivateSMSDlrJMSTemplate")
	private JmsTemplate mobivateSMSDlrJMSTemplate;
	
	
	@Autowired
	@Qualifier("mobivateMOJMSTemplate")
	private JmsTemplate mobivateMOJMSTemplate;
	
	
	
	@Autowired
	private IDaoService daoService;
		
	public boolean saveMobivateCGCallback(final MobivateCGCallback  mobivateCGCallback) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("savemobivateCGCallback:: mobivateCGCallback:: "+mobivateCGCallback);
	        mobivateCGCallbackJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mobivateCGCallback);
					//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 15*1000);
					return message;
				}
			});
			
			}catch(Exception ex){
				logger.error("savemobivateCGCallback:::::::::::::"+mobivateCGCallback ,ex);
				boolean save=daoService.saveObject(mobivateCGCallback);			
				return save;
			}
			logger.debug("saveNumeroMobivateCellcCGCallback:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
	public boolean saveMobivateSMSDlrJMSTemplate(final MobivateSMSDlr  mobivateSMSDlr) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMobivateSMSDlrJMSTemplate:: mobivateSMSDlr:: "+mobivateSMSDlr);
	        mobivateSMSDlrJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mobivateSMSDlr);
					//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 15*1000);
					return message;
				}
			});
			
			}catch(Exception ex){
				logger.error("saveMobivateSMSDlrJMSTemplate:::::::::::::"+mobivateSMSDlr ,ex);
				boolean save=daoService.saveObject(mobivateSMSDlr);			
				return save;
			}
			logger.debug("saveMobivateSMSDlrJMSTemplate:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
	public boolean saveMobivateMOJMSTemplate(final MobivateMO  mobivateMO) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMobivateMOJMSTemplate:: mobivateMO:: "+mobivateMO);
	        mobivateMOJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mobivateMO);
					message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 10*1000);
					return message;
				}
			});
			
			}catch(Exception ex){
				logger.error("saveMobivateMOJMSTemplate:::::::::::::"+mobivateMO ,ex);
				boolean save=daoService.saveObject(mobivateMO);			
				return save;
			}
			logger.debug("saveMobivateMOJMSTemplate:: total time "+(System.currentTimeMillis()-time));
			return true;
		}

	
	
	}
