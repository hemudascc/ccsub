package net.indonesia.triyakom;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;

@Service("ht1OperatorService")
public class HT1OperatorService implements IOpeartorService{

	private static final Logger logger = Logger.getLogger(HT1OperatorService.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private IndonesiaSmsService indonesiaSmsService;
	
	@Autowired
	private JMSIndonesiaService jmsIndonesiaService;
	
	
	
	
	@Override
	public boolean handleSubscriptionMoMessage(MOMessage moMessage) {
		logger.info("handleSubscriptionMoMessage:::::::::::::::::::: "+moMessage);
		
		return true;
	}
	
	
	@Override
	public boolean handleSubscriptionMTPushMessage(MTMessage tmpMtMessage) {
		
		logger.info("handleSubscriptionMTPushMessage:::::::::::::::::::: "+tmpMtMessage);
		//logger.info("handleSubscriptionMTPushMessage:::::::::: "+mtMessage);		
		return true;
	}


	
	@Override
	public boolean handleSubscriptionMTPushMessage(SubscriberReg subscriberReg) {
		
		logger.info("handleSubscriptionMTPushMessage:::::::::::::::::::: "+subscriberReg);
		//logger.info("handleSubscriptionMTPushMessage:::::::::: "+mtMessage);		
		return true;
	}
	
	
	@Override
	public boolean handleSubscriptionMTRenewalPushMessage(SubscriberReg subscriberReg) {
		logger.info("handleSubscriptionMTRenewalPushMessage:::::::::::::::::::: "+subscriberReg);
		
		return true;
	}
}
