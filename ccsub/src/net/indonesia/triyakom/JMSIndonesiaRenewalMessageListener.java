package net.indonesia.triyakom;

import java.sql.Timestamp;
import java.time.LocalTime;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSIndonesiaRenewalMessageListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSIndonesiaRenewalMessageListener.class);

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private RedisCacheService redisCacheService;
	
	
	@Autowired
	private IDaoService daoService;

	
	@Value("#{ T(java.time.LocalTime).parse('${triyakom.renewal.start.time}')}")
	private LocalTime renewalStartTime;

	@Value("#{ T(java.time.LocalTime).parse('${triyakom..renewal.stop.time}')}")
	private LocalTime renewalStopTime;
	
	@Autowired
	private TriyakomOperatorService triyakomOperatorService;

	@Override
	public void onMessage(Message m) {

		SuscriberIdMsg suscriberIdMsg = null;

		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			suscriberIdMsg = (SuscriberIdMsg) objectMessage
					.getObject();
			logger.info("sendRenewalBilled::::::::sent suscriberIdMsg::  "+ suscriberIdMsg);
			
			SubscriberReg subscriberReg = jpaSubscriberReg
					.findSubscriberRegById(suscriberIdMsg
							.getSubscriberId());
			logger.info("sendRenewalBilled::::::::sent msisdn::  "+ subscriberReg.getMsisdn());
			
			
			LocalTime now = LocalTime.now();

				if (now.isAfter(renewalStartTime) && now.isBefore(renewalStopTime)) {	
				
				if (suscriberIdMsg.getAction().equalsIgnoreCase(
						MConstants.RENEW)) {					
					int dayDiff = MUtility.noOfDaysDiffrence(new Timestamp(
							System.currentTimeMillis()), subscriberReg
							.getValidityTo());
					logger.info("sendRenewalBilled::::::::dayDiff::  "+dayDiff+ subscriberReg.getMsisdn());
					if (subscriberReg.getStatus() == MConstants.SUBSCRIBED&&dayDiff<=0) {	
						
						if(subscriberReg.getParam1()!=null
								&&subscriberReg.getParam1().equalsIgnoreCase(MConstants.GRACE)){
						    triyakomOperatorService.handleSubscriptionMTPushMessage(subscriberReg);
						}else{
							triyakomOperatorService.handleSubscriptionMTRenewalPushMessage(subscriberReg);
						}
					}
				}
			}else{
				logger.error("renewal stop for subscriberReg:: "+subscriberReg);
			}
		
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		}
	}
}
