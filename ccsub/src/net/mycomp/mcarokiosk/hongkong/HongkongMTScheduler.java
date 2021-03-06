package net.mycomp.mcarokiosk.hongkong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPAHongkongMoMessage;
import net.jpa.repository.JPAMKHongkongMTMessage;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.SuscriberIdMsg;
import net.util.HTTPResponse;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class HongkongMTScheduler {

	private static final Logger logger = Logger.getLogger(HongkongMTScheduler.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	private JPAHongkongMoMessage jpaHongkongMOMessage;

	@Autowired
	protected HongkongSmsService smsService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Value("${macrokiosk.hongkong.mt.url}")
	protected String mtUrl;

	@Scheduled(cron = "${macrokiosk.hongkong.mt.send.cron.scheduler}")
	public void sendAisRenewalBilled() {

		VWServiceCampaignDetail vwServiceCampaignDetail = null;
		Map<String, HongkongMOMessage> mtSentMap = new HashedMap();
		try {
			int count = 1;
			int price = 0;
			List<HongkongMOMessage> hongkongMolist = jpaHongkongMOMessage.findAll();
			for (HongkongMOMessage hongkongMOMessage : hongkongMolist) {
				if (hongkongMOMessage.getMsisdn() != null && hongkongMOMessage.getAction() != null
						&& hongkongMOMessage.getToken() != null) {
					HongkongMOMessage alreadysent = mtSentMap.get(hongkongMOMessage.getMsisdn());
					if (alreadysent == null) {
						try {
							count = redisCacheService.getIntValue(
									MKHongkongConstant.MT_MESSAGE_COUNT_CAHCHE_PREFIX + hongkongMOMessage.getMsisdn());
						} catch (NullPointerException ne) {count = 1;}
						logger.info(hongkongMOMessage.getMsisdn() + ": count: " + count);
						if (count <= 6 && hongkongMOMessage.getAction().equalsIgnoreCase(MConstants.ACT)) {
							vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail
									.get(new MKHongkongCGToken(hongkongMOMessage.getToken()).getCampaignId());
							MKHongkongConfig mkHongkongConfig = MKHongkongConstant.mapServiceIdToMKHongkongConfig
									.get(vwServiceCampaignDetail.getServiceId());
							try {
								price = redisCacheService.getIntValue(MKHongkongConstant.MT_MESSAGE_PRICE_CAHCHE_PREFIX
										+ hongkongMOMessage.getMsisdn());
							} catch (NullPointerException ne) {
								price = 0;
							}
							SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(hongkongMOMessage.getMsisdn(), mkHongkongConfig.getProductId());
							logger.info("checkSub:::::::::: list of subscriberreg "+subscriberReg);
							if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
								SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
								Date date1 = subscriberReg.getLastUpdate();
								Date date2 = MKHongkongConstant.getFormatUTC8Date();
								// Calucalte time difference in milliseconds
								long time_difference = date2.getTime() - date1.getTime();
								long day_difference = (time_difference / (1000 * 60 * 60 * 24)) % 365;
								logger.info(
										"Yash Sheduler Time diffrence in day_difference ========>  ::  " + day_difference);
								if (price <= mkHongkongConfig.getMaxPrice() && day_difference >= 1) {

									logger.info("Yash Sheduler mkHongkongConfig ========>  ::  " + mkHongkongConfig);
									HongkongMTMessage mtMessage = createMTBillableMessage(mkHongkongConfig,
											hongkongMOMessage);
									mtMessage.setServiceId(mkHongkongConfig.getServiceId());

									logger.info("handleSubscriptionhongkongMOMessage:: create MT message::::::mtMessage "
											+ mtMessage);
									redisCacheService.putObjectCacheValueByEvictionDay(MKHongkongConstant.MO_MESSAGE_CAHCHE_PREFIX+hongkongMOMessage.getMsisdn(),
											hongkongMOMessage.getToken(), 1);
									
									HTTPResponse response = smsService.sendMTSMS(mtUrl, mtMessage);
									mtSentMap.put(hongkongMOMessage.getMsisdn(), hongkongMOMessage);
									price = (int) (price + mkHongkongConfig.getPrice());
									count++;
									redisCacheService.putIntValue(MKHongkongConstant.MT_MESSAGE_PRICE_CAHCHE_PREFIX
											+ hongkongMOMessage.getMsisdn(), price);
									redisCacheService.putIntValue(MKHongkongConstant.MT_MESSAGE_COUNT_CAHCHE_PREFIX
											+ hongkongMOMessage.getMsisdn(), count);
									redisCacheService.putObjectCacheValueByEvictionMinute(
											MKHongkongConstant.MT_MESSAGE_CAHCHE_PREFIX + mtMessage.getMsgId(),
											mtMessage.getId(), 10 * 60);
									daoService.updateObject(mtMessage);
								}	
							}
							
						} else {
							redisCacheService.putIntValue(
									MKHongkongConstant.MT_MESSAGE_COUNT_CAHCHE_PREFIX + hongkongMOMessage.getMsisdn(),
									0);

						}
					}
				}
			}
			mtSentMap.clear();
		} catch (Exception ex) {
			logger.error("send MT Billed:: " + ex);
			ex.printStackTrace();
		}
	}

	protected HongkongMTMessage createMTBillableMessage(MKHongkongConfig mkHongkongConfig,
			HongkongMOMessage hongkongMOMessage) {
		if (hongkongMOMessage.getMsisdn().equals(MKHongkongConstant.TEST_NUMBER_1)
				|| hongkongMOMessage.getMsisdn().equals(MKHongkongConstant.TEST_NUMBER_2)) {
			mkHongkongConfig.setPricePoint(0d);
		}
		String msg = mkHongkongConfig.getMtBillingMessageTemplate()
				.replaceAll("<portalurl>", mkHongkongConfig.getPortalUrl())
				.replaceAll("<serviceName>", mkHongkongConfig.getOpServiceName())
				.replaceAll("<shortcode>", mkHongkongConfig.getShortcode())
				.replaceAll("<keyword>", mkHongkongConfig.getKeyword())
				.replaceAll("<price>", String.valueOf(mkHongkongConfig.getPricePoint()))
				.replaceAll("<subid>", hongkongMOMessage.getMsisdn())
				.replaceAll("<cmpid>", String.valueOf(hongkongMOMessage.getCampaignId()));
		String message = MKHongkongConstant.encode(msg);
		HongkongMTMessage mtMessage = new HongkongMTMessage(true);
		mtMessage.setMessageType(MKHongkongConstant.MT_BIILABLE_MESSAGE);
		mtMessage.setMtActionType(MConstants.ACT);
		mtMessage.setUser(mkHongkongConfig.getUser());
		mtMessage.setPass(mkHongkongConfig.getPassword());
		mtMessage.setCat(HongkongMTCat.CONTENT_BRODCAST.getCatId());
		mtMessage.setFromStr(mkHongkongConfig.getShortcode());
		mtMessage.setMsisdn(hongkongMOMessage.getMsisdn());
		mtMessage.setKeyword(mkHongkongConfig.getKeyword());
		mtMessage.setMoMessageId(hongkongMOMessage.getId());
		mtMessage.setMoMessageIdStr(hongkongMOMessage.getMoid());
		mtMessage.setOpId(hongkongMOMessage.getOpId());
		mtMessage.setPrice(mkHongkongConfig.getPricePoint());
		mtMessage.setTelcoId(hongkongMOMessage.getTelcoid());
		mtMessage.setTextMsg(message);
		mtMessage.setType(MKHongkongConstant.MT_TEXT);
		mtMessage.setSenderid(hongkongMOMessage.getShortcode());
		mtMessage.setTokenId(hongkongMOMessage.getTokenId());
		mtMessage.setToken(hongkongMOMessage.getToken());
		mtMessage.setCampaignId(hongkongMOMessage.getCampaignId());
		mtMessage.setCharge(MKHongkongConstant.MT_CHARGE_SUBSCRIPTION);
		mtMessage.setPlatform(mkHongkongConfig.getPlatform());
		return mtMessage;
	}

}
