package net.mycomp.mcarokiosk.malaysia;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public abstract class AbstractMacroKioskMTMessage implements IMacroKioskService{

	private static final Logger logger = Logger.getLogger(AbstractMacroKioskMTMessage.class);

	@Autowired
	protected MalaysiaSmsService smsService;
	
	@Autowired
	protected IDaoService daoService;

   @Value("${macrokiosk.malaysia.mt.url}")
   protected String mtUrl;
	
	public void handleWelcomeSubscriptionMTMessage(MalasiyaMTMessage malasiyaMTMessage){
		logger.info("handleWelcomeMTMessage::::::::::::: "+malasiyaMTMessage);
	}
	
	
}
