package net.mycomp.mt2.ksa;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2KSASMSAlertListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2KSASMSAlertListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private Mt2KSAServiceApi mt2KSAServiceApi;
	
	@Override
	public void onMessage(Message m) {

		SuscriberIdMsg suscriberIdMsg = null;
		
		boolean update = false;
		long time = System.currentTimeMillis();
	
		String msg=null;
		Mt2KSAServiceConfig mt2KSAServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			suscriberIdMsg = (SuscriberIdMsg) objectMessage
					.getObject();
			if(suscriberIdMsg.getAction().equalsIgnoreCase(Mt2KSAConstant.SMS_CONTENT_ALERT)){
				SubscriberReg subscriberReg=jpaSubscriberReg
						.findSubscriberRegById(suscriberIdMsg.getSubscriberId());
				 mt2KSAServiceConfig=
						Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(subscriberReg.getServiceId());
				 msg=mt2KSAServiceConfig.getAlertSmsTemplate();
				 msg=Mt2KSAConstant.prepareMessage(msg, mt2KSAServiceConfig, subscriberReg.getSubscriberId());
				 mt2KSAServiceApi.sendMTSMS(mt2KSAServiceConfig, subscriberReg.getMsisdn(),
						 "", msg,Mt2KSAConstant.SMS_CONTENT_ALERT);
			}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			logger.info("onMessage::::::::::::::::: ::  "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
	}
}
