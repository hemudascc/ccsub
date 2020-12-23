package net.mycomp.messagecloud.gateway.za;

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
import net.mycomp.messagecloud.gateway.MCGDeliveryReport;
import net.mycomp.messagecloud.gateway.MCGMoMessage;

@Service("jmsMCGZAService")
public class JMSMCGZAService {

	private static final Logger logger = Logger.getLogger(JMSMCGZAService.class);
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("mcgZAMOMessageJMSTemplate")
	private JmsTemplate mcgZAMOMessageJMSTemplate;

	
	
	@Autowired
	@Qualifier("mcgZADeliveryReportJMSTemplate")
	private JmsTemplate mcgZADeliveryReportJMSTemplate;

	
	public boolean saveZAMOMessage(final MCGZAMoMessage mcgZAMoMessage) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveZAMOMessage:: mcgMoMessage:: "+mcgZAMoMessage);
	        mcgZAMOMessageJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mcgZAMoMessage);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveZAMOMessage::::::::::::: ",ex);
				boolean save=daoService.saveObject(mcgZAMoMessage);			
				return save;
			}
			 logger.debug("saveZAMOMessage:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	public boolean saveZADeliveryReport(final MCGZADeliveryReport mcgZADeliveryReport) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveZADeliveryReport:: mcgMoMessage:: "+mcgZADeliveryReport);
	        mcgZADeliveryReportJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(mcgZADeliveryReport);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveZADeliveryReport::::::::::::: ",ex);
				boolean save=daoService.saveObject(mcgZADeliveryReport);			
				return save;
			}
			 logger.debug("saveZADeliveryReport:: total time "+(System.currentTimeMillis()-time));
			return true;
		}

	
}
