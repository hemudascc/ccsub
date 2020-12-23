package net.bizao;

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

@Service("jmsBizaoService")
public class JMSBizaoService {

	private static final Logger logger = Logger.getLogger(JMSBizaoService.class);
	
	
	@Autowired
    @Qualifier("bizaoPaymentJMSTemplate")
	private JmsTemplate bizaoPaymentJMSTemplate;
	
	@Autowired
	@Qualifier("bizaoMoTransJMSTemplate")
	private JmsTemplate bizaoMoTransJMSTemplate;
	
	@Autowired
	@Qualifier("bizaoRenewalJMSTemplate")
	private JmsTemplate bizaoRenewalJMSTemplate;
	
	
	@Autowired
	private IDaoService daoService;
	
	

	
	public boolean saveBizaoPayment(final BizaoPayment bizaoPayment) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveBizaoPayment:: bizaoPayment:: "+bizaoPayment);
	        bizaoPaymentJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(bizaoPayment);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveBizaoPayment::::::::::::: ",ex);
				boolean save=daoService.saveObject(bizaoPayment);			
				return save;
			}
			 logger.debug("saveBizaoPayment:: total time "+(System.currentTimeMillis()-time));
			return true;
		}

	
	public boolean saveBizaoMOTrans(final BizaoMoTrans bizaoMoTrans) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("saveBizaoMOTrans:: bizaoMoTrans:: "+bizaoMoTrans);
	        bizaoMoTransJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(bizaoMoTrans);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("saveBizaoMOTrans::::::::::::: ",ex);
				boolean save=daoService.saveObject(bizaoMoTrans);			
				return save;
			}
			 logger.debug("saveBizaoMOTrans:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
	
	
	
	public boolean sendRenewalRetry(final BizaoSuscriberIdMsg bizaoSuscriberIdMsg) {
		  long time=System.currentTimeMillis();
			try{
	        logger.debug("sendRenewalRetry:: bizaoSuscriberIdMsg:: "+bizaoSuscriberIdMsg);
	        bizaoRenewalJMSTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message=session.createObjectMessage(bizaoSuscriberIdMsg);
					return message;
				}
			});
	        
			}catch(Exception ex){
				logger.error("sendRenewalRetry::::::::::::: ",ex);
							
				return false;
			}
			 logger.debug("sendRenewalRetry:: total time "+(System.currentTimeMillis()-time));
			return true;
		}
}
