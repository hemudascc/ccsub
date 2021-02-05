package net.mycomp.tpay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSTpayNotificationListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSTpayNotificationListener.class);

	@Autowired
	private IDaoService daoService;
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Override
	public void onMessage(Message message) {
		TpayNotification tpayNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		TpayServiceConfig tpayServiceConfig=null;
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			tpayNotification = (TpayNotification)objectMessage.getObject();
			logger.info("tpayNotification::::: "+tpayNotification);
			findAction(tpayNotification);
			cgToken= new CGToken(tpayNotification.getToken());
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			tpayServiceConfig = TpayConstant.mapServiceIdToTpayServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			liveReport=new LiveReport(tpayServiceConfig.getOperatorId(),
					new Timestamp(System.currentTimeMillis())
					,cgToken.getCampaignId(),tpayServiceConfig.getServiceId(),0);
			liveReport.setProductId(tpayServiceConfig.getProductId());
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(tpayNotification.getMsisdn());
			liveReport.setCircleId(0);
			tpayNotification.setToken(cgToken.getCGToken());
			tpayNotification.setTokenId(cgToken.getTokenId());
			double priceAmount=MUtility.toDouble(tpayNotification.getAmount(),-1);
			if(tpayNotification.getAction().equalsIgnoreCase(MConstants.ACT)) {
				liveReport.setAction(MConstants.ACT);					
				liveReport.setConversionCount(1);
				liveReport.setAmount(priceAmount>0?priceAmount:Double.valueOf(tpayServiceConfig.getPrice()));
				liveReport.setNoOfDays(tpayServiceConfig.getValidity());
				liveReport.setParam1(tpayNotification.getSubscriptionContractId());
				liveReport.setParam2(tpayServiceConfig.getOperatorCode());
				tpayNotification.setValidity(liveReport.getNoOfDays());
				tpayNotification.setAmount(liveReport.getAmount().toString());	
			}else if(tpayNotification.getAction().equalsIgnoreCase(MConstants.RENEW)) {
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);					
				liveReport.setRenewalAmount(priceAmount>0?priceAmount:Double.valueOf(tpayServiceConfig.getPrice()));
				liveReport.setNoOfDays(tpayServiceConfig.getValidity());
				liveReport.setParam1(tpayNotification.getSubscriptionContractId());
				liveReport.setParam2(tpayServiceConfig.getOperatorCode());
				tpayNotification.setValidity(liveReport.getNoOfDays());
				tpayNotification.setAmount(liveReport.getRenewalAmount().toString());
			}else if(tpayNotification.getAction().equalsIgnoreCase(MConstants.GRACE)) {
				liveReport.setParam1(tpayNotification.getSubscriptionContractId());
				liveReport.setParam2(tpayServiceConfig.getOperatorCode());
				liveReport.setAction(MConstants.GRACE);				
				liveReport.setGraceConversionCount(1);
			}else if(tpayNotification.getAction().equalsIgnoreCase(MConstants.DCT)) {
				liveReport.setAction(MConstants.DCT);			
				liveReport.setDctCount(1);
			}else {}
			
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		} finally {
			try {
				if(liveReport.getAction()!=null){
					tpayNotification.setAction(liveReport.getAction());
					liveReportFactoryService.process(liveReport);
				}
				tpayNotification.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : tpayNotification:: "
						+ tpayNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(tpayNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}

	private TpayNotification findAction(TpayNotification tpayNotification) {
		
		List<SubscriberReg> subscriberRegs = jpaSubscriberReg.findSubscriberRegByParam1(tpayNotification.getSubscriptionContractId());
		boolean subToday=false;
		if("SubscriptionContractStatusChanged".equals(tpayNotification.getTpayAction())) {
			if("Active".equals(tpayNotification.getNotificationStatus())) {
				tpayNotification.setAction(MConstants.SUBSCRIBED_DESC);
			}else if("Canceled".equals(tpayNotification.getNotificationStatus()) || "Suspended".equals(tpayNotification.getNotificationStatus()) || "Purged".equals(tpayNotification.getNotificationStatus())) {
				tpayNotification.setAction(MConstants.DCT);
			}else {
				tpayNotification.setAction("NONE");
			}
		}else if("SubscriptionChargingNotification".equals(tpayNotification.getTpayAction())) {
			if("RecurringPayment".equalsIgnoreCase(tpayNotification.getBillingAction()) || "RetrailPayment".equalsIgnoreCase(tpayNotification.getBillingAction())) {
				boolean newUser = !Objects.toString(redisCacheService.getCacheValue(TpayConstant.ACTIVE_CACHE_PREFIX+tpayNotification.getSubscriptionContractId())).isEmpty();
				if(subscriberRegs.size()>0) {subToday= LocalDate.now().equals(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(subscriberRegs.get(0).getSubDate())));}
				if(newUser && subToday) {
					if("PaymentCompletedSuccessfully".equals(tpayNotification.getPaymentTransactionStatusCode())) {
						tpayNotification.setAction(MConstants.ACT);
						redisCacheService.removeObjectCacheValue(TpayConstant.ACTIVE_CACHE_PREFIX+tpayNotification.getSubscriptionContractId());
					}else {
						if(Objects.toString(redisCacheService.getCacheValue(TpayConstant.GRACE_CACHE_PREFIX+tpayNotification.getSubscriptionContractId())).isEmpty()) {
							tpayNotification.setAction(MConstants.GRACE);
							redisCacheService.putObjectCacheValueByEvictionDay(TpayConstant.GRACE_CACHE_PREFIX+tpayNotification.getSubscriptionContractId(), "1", 1);
						}else {
							tpayNotification.setAction("NONE");
						}
					}
				}else {
					if("PaymentCompletedSuccessfully".equals(tpayNotification.getPaymentTransactionStatusCode())) {
						tpayNotification.setAction(MConstants.RENEW);
					}else {
						tpayNotification.setAction("NONE");
					}
				}
			}else {
				if("PaymentCompletedSuccessfully".equals(tpayNotification.getPaymentTransactionStatusCode())) {
					tpayNotification.setAction(MConstants.RENEW);
				}else {
					tpayNotification.setAction("NONE");
				}
			}
		}else {
			tpayNotification.setAction("NONE");
		}
		
		if(Objects.nonNull(subscriberRegs) && subscriberRegs.size()>0) {
			tpayNotification.setToken(Objects.toString(subscriberRegs.get(0).getParam3()));
		}
		if(Objects.isNull(tpayNotification.getToken())) {
			if("GamePadWEEG".equals(tpayNotification.getProductId())){
				tpayNotification.setToken("-1c-1c249");
			}else if("GamePadEtisalatEG".equals(tpayNotification.getProductId())) {
				tpayNotification.setToken("-1c-1c252");
			}else if("GamePadVodaEG".equals(tpayNotification.getProductId())) {
				tpayNotification.setToken("-1c-1c282");
			}else {
				tpayNotification.setToken("-1c-1c283");
			}
		}

		
		return tpayNotification;
	} 
}
