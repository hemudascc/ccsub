package net.mycomp.mcarokiosk.malaysia;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.process.bean.SuscriberIdMsg;
import net.process.request.OperatorRequestService;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSMKMalayisiaRenewalMessageListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMKMalayisiaRenewalMessageListener.class);

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private RedisCacheService redisCacheService;
	

	@Autowired
	private IDaoService daoService;

	@Autowired
	private MacroKioskMalaysiaFactoryService macroKioskFactoryService;
	

	@Value("#{ T(java.time.LocalTime).parse('${macrokiosk.malaysia.digi.renewal.start.time}')}")
	private LocalTime renewalStartTime;

	@Value("#{ T(java.time.LocalTime).parse('${macrokiosk.malaysia.digi.renewal.stop.time}')}")
	private LocalTime renewalStopTime;
	
	

	@Override
	public void onMessage(Message m) {

		SuscriberIdMsg suscriberIdMsg = null;

		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			suscriberIdMsg = (SuscriberIdMsg) objectMessage
					.getObject();

			SubscriberReg subscriberReg = jpaSubscriberReg
					.findSubscriberRegById(suscriberIdMsg
							.getSubscriberId());
			
			LocalTime now = LocalTime.now();

			
			String block=Objects.toString(redisCacheService.
			getObjectCacheValue(MKMalaysiaConstant.DIGI_SUBSCRIBER_RENEWAL_CAHCE_BLOCK
				+subscriberReg.getMsisdn()));
			
			
			boolean isAfter = now.isAfter(renewalStartTime);
			boolean isBefore= now.isBefore(renewalStopTime);
			boolean isBlocked = block.equalsIgnoreCase("1");
			
			logger.info("Ready To renew msisdn="+subscriberReg.getMsisdn()+" isAfter"+isAfter+" isBefore"+isBefore+" isBlocked"+isBlocked);  
			
			if (now.isAfter(renewalStartTime) && now.isBefore(renewalStopTime)&&!block.equalsIgnoreCase("1")) {
				if (suscriberIdMsg.getAction().equalsIgnoreCase(
						MConstants.RENEW)) {					
					int dayDiff = MUtility.noOfDaysDiffrence(new Timestamp(
							System.currentTimeMillis()), subscriberReg
							.getValidityTo());
					if (subscriberReg.getStatus() == MConstants.SUBSCRIBED) {						
						 macroKioskFactoryService.sendSubscriptionRenewalRequest(subscriberReg);
						logger.info("sendRenewalBilled::::::::sent msisdn::  "
								+ subscriberReg.getMsisdn());
						MKMalaysiaConfig mkMalaysiaConfig=MKMalaysiaConstant
								.mapServiceIdToMKMalaysiaConfig.get(subscriberReg.getServiceId());
						Timestamp ts=new Timestamp(
								System.currentTimeMillis());
						subscriberReg.setLastRenewalRetryDate(ts);
						subscriberReg.setValidityFrom(subscriberReg.getLastRenewalRetryDate());
						subscriberReg.setValidityTo(MUtility.addNumberOfDay(ts,
								mkMalaysiaConfig.getValidityForCharge()));
						redisCacheService.
						putObjectCacheValueByEvictionMinute(MKMalaysiaConstant.DIGI_SUBSCRIBER_RENEWAL_CAHCE_BLOCK
								+subscriberReg.getMsisdn()
								, "1", 60*70);						
						daoService.updateObject(subscriberReg);
					}
				}
			}else{
				logger.error("JMSMKMalayisiaRenewalMessageListener renewal time expired or block:"+block+" "
						+ ":::::not send msisdn to renewal "+subscriberReg
						+ " ");
			}
		
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		}
	}
}
