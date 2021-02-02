package net.mycomp.mcarokiosk.hongkong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.common.service.IDaoService;
public abstract class AbstractMacroKioskMTMessage implements IMacroKioskService{

	private static final Logger logger = Logger.getLogger(AbstractMacroKioskMTMessage.class);

	@Autowired
	protected HongkongSmsService smsService;
	
	@Autowired
	protected IDaoService daoService;

   @Value("${macrokiosk.hongkong.mt.url}")
   protected String mtUrl;
	
	public void handleWelcomeSubscriptionMTMessage(HongkongMTMessage hongkongMTMessage){
		logger.info("handleWelcomeMTMessage::::::::::::: "+hongkongMTMessage);
	}
	
	

}
