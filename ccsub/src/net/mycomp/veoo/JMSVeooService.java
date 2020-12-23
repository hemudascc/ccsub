package net.mycomp.veoo;

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
import net.process.bean.SuscriberIdMsg;

@Service("jmsVeooService")
public class JMSVeooService {

	private static final Logger logger = Logger.getLogger(JMSVeooService.class);
	
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("veooMoJMSTemplate")
	private JmsTemplate veooMoJMSTemplate;
	
	
	@Autowired
	@Qualifier("veooDeliveryReceiptJMSTemplate")
	private JmsTemplate veooDeliveryReceiptJMSTemplate;
	
	@Autowired
	@Qualifier("veooRenewalJMSTemplate")
	private JmsTemplate veooRenewalJMSTemplate;
	
	@Autowired
	@Qualifier("veooPinValidationJMSTemplate")
	private JmsTemplate veooPinValidationJMSTemplate;
	
	
	public boolean saveMO(final VeooMo veooMo) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveMO:: veooMo:: "+veooMo);
	        veooMoJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(veooMo);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveMO::::::::::::: ",ex);
				boolean save=daoService.saveObject(veooMo);			
				return save;
			}
			 logger.debug("saveMO:: total time "+(System.currentTimeMillis()-time));
			return true;
		}	
	
	
	public boolean saveDeliveryReceipt(final VeooDeliveryReceipt veooDeliveryReceipt) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveDeliveryReceipt:: veooDeliveryReceipt:: "+veooDeliveryReceipt);
	        veooDeliveryReceiptJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(veooDeliveryReceipt);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveDeliveryReceipt::::::::::::: ",ex);
				boolean save=daoService.saveObject(veooDeliveryReceipt);			
				return save;
			}
			 logger.debug("saveDeliveryReceipt:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	public boolean sendRenewalContentMsg(final SuscriberIdMsg suscriberIdMsg) {
		
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("sendRenewalContentMsg:: suscriberIdMsg:: "+suscriberIdMsg);
	        veooRenewalJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(suscriberIdMsg);
					//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 15*1000);
					return message;
				}
			});
			
			}catch(Exception ex){
				logger.error("sendRenewalContentMsg::::::::::::: ",ex);
				
			}
			logger.debug("sendRenewalContentMsg  :: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
	public boolean processPinValidation(final VeooPinValidation veooPinValidation) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("processPinValidation:: veooPinValidation:: "+veooPinValidation);
	        veooPinValidationJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(veooPinValidation);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("processPinValidation::::::::::::: ",ex);
				//boolean save=daoService.saveObject(veooPinValidation);			
				return false;
			}
			 logger.debug("processPinValidation:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
}
