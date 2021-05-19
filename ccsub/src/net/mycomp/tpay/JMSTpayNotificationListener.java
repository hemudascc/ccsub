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

public class JMSTpayNotificationListener implements MessageListener
{
    private static final Logger logger = Logger.getLogger(JMSTpayNotificationListener.class);
    @Autowired
    private IDaoService daoService;
    @Autowired
    private LiveReportFactoryService liveReportFactoryService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private JPASubscriberReg jpaSubscriberReg;
       
    public void onMessage(final Message message) {
        TpayNotification tpayNotification = null;
        LiveReport liveReport = null;
        boolean update = false;
        final long time = System.currentTimeMillis();
        CGToken cgToken = new CGToken("");
        TpayServiceConfig tpayServiceConfig = null;
        boolean sameDaysub = false;
        try {
            final ObjectMessage objectMessage = (ObjectMessage)message;
            tpayNotification = (TpayNotification)objectMessage.getObject();
            JMSTpayNotificationListener.logger.info("tpayNotification::::: " + tpayNotification);
            final List<SubscriberReg> subscriberRegs = jpaSubscriberReg.findSubscriberRegByParam1(tpayNotification.getSubscriptionContractId());
            if (Objects.nonNull(subscriberRegs) && subscriberRegs.size() > 0) {
                sameDaysub = LocalDate.now().equals(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(subscriberRegs.get(0).getRegDate())));
                cgToken = new CGToken(subscriberRegs.get(0).getParam3());
            }
            if(cgToken.getCampaignId()<=0) {
                cgToken = new CGToken(getDefaultToken(tpayNotification));
            }
            final VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
            JMSTpayNotificationListener.logger.info("view " + vwServiceCampaignDetail);
            if (vwServiceCampaignDetail != null) {
                tpayServiceConfig = TpayConstant.mapServiceIdToTpayServiceConfig.get(vwServiceCampaignDetail.getServiceId());
            }
            JMSTpayNotificationListener.logger.info((Object)("tpay config " + tpayServiceConfig));
            liveReport = new LiveReport((int)tpayServiceConfig.getOperatorId(), new Timestamp(System.currentTimeMillis()), Integer.valueOf(cgToken.getCampaignId()), (int)tpayServiceConfig.getServiceId(), 0);
            liveReport.setProductId(tpayServiceConfig.getProductId());
            liveReport.setTokenId(Integer.valueOf(cgToken.getTokenId()));
            liveReport.setToken(cgToken.getCGToken());
            liveReport.setMsisdn(tpayNotification.getMsisdn());
            liveReport.setCircleId(0);
            tpayNotification.setToken(cgToken.getCGToken());
            tpayNotification.setTokenId(Integer.valueOf(cgToken.getTokenId()));
            tpayNotification.setAction(findAction(tpayNotification, liveReport, tpayServiceConfig, sameDaysub));
            logger.info((Object)("tpay action : " + tpayNotification.getAction()));
            liveReport.setResponse(tpayNotification.toString());
            final double priceAmount = MUtility.toDouble(tpayNotification.getAmount(), -1.0);
            if (tpayNotification.getAction().equalsIgnoreCase(MConstants.GRACE)) {
                liveReport.setParam1(tpayNotification.getSubscriptionContractId());
                liveReport.setParam2(tpayServiceConfig.getOperatorCode());
                liveReport.setAction(MConstants.GRACE);
                liveReport.setGraceConversionCount(1);
            }
            else if (tpayNotification.getAction().equalsIgnoreCase(MConstants.ACT)) {
                liveReport.setAction(MConstants.ACT);
                liveReport.setConversionCount(1);
                liveReport.setAmount(Double.valueOf((priceAmount > 0.0) ? priceAmount : Double.valueOf(tpayServiceConfig.getPrice())));
                liveReport.setNoOfDays((int)tpayServiceConfig.getValidity());
                liveReport.setParam1(tpayNotification.getSubscriptionContractId());
                liveReport.setParam2(tpayServiceConfig.getOperatorCode());
                tpayNotification.setValidity(Integer.valueOf(liveReport.getNoOfDays()));
                tpayNotification.setAmount(liveReport.getAmount().toString());
            }
            else if (tpayNotification.getAction().equalsIgnoreCase(MConstants.SMSSUB)) {
            	liveReport.setAction("SMSSUB");
            	liveReport.setSmsConversionCount(1);
            	liveReport.setSmsConversionAmount(Double.valueOf((priceAmount > 0.0) ? priceAmount : Double.valueOf(tpayServiceConfig.getPrice())));
            	liveReport.setNoOfDays(tpayServiceConfig.getValidity());
            	liveReport.setParam1(tpayNotification.getSubscriptionContractId());
            	liveReport.setParam2(tpayServiceConfig.getOperatorCode());
            	tpayNotification.setValidity(Integer.valueOf(liveReport.getNoOfDays()));
            	tpayNotification.setAmount(liveReport.getRenewalAmount().toString());
            }
            else if (tpayNotification.getAction().equalsIgnoreCase(MConstants.SMSRENEW)) {
            	liveReport.setAction("SMSRENEW");
            	liveReport.setSmsRenwalCount(1);
            	liveReport.setSmsRenewalAmount(Double.valueOf((priceAmount > 0.0) ? priceAmount : Double.valueOf(tpayServiceConfig.getPrice())));
            	liveReport.setNoOfDays(tpayServiceConfig.getValidity());
            	liveReport.setParam1(tpayNotification.getSubscriptionContractId());
            	liveReport.setParam2(tpayServiceConfig.getOperatorCode());
            	tpayNotification.setValidity(Integer.valueOf(liveReport.getNoOfDays()));
            	tpayNotification.setAmount(liveReport.getRenewalAmount().toString());
            }
            else if (tpayNotification.getAction().equalsIgnoreCase(MConstants.RENEW)) {
                liveReport.setAction("RENEW");
                liveReport.setRenewalCount(1);
                liveReport.setRenewalAmount(Double.valueOf((priceAmount > 0.0) ? priceAmount : Double.valueOf(tpayServiceConfig.getPrice())));
                liveReport.setNoOfDays((int)tpayServiceConfig.getValidity());
                liveReport.setParam1(tpayNotification.getSubscriptionContractId());
                liveReport.setParam2(tpayServiceConfig.getOperatorCode());
                tpayNotification.setValidity(Integer.valueOf(liveReport.getNoOfDays()));
                tpayNotification.setAmount(liveReport.getRenewalAmount().toString());
            }
            else if (tpayNotification.getAction().equalsIgnoreCase(MConstants.DCT)) {
                liveReport.setAction(MConstants.DCT);
                liveReport.setDctCount(1);
            }
            System.out.println("FINAL OBJ::::::"+tpayNotification);
        }
        catch (Exception e) {
            JMSTpayNotificationListener.logger.error("onMessage::::: ",e);
            return;
        }
        finally {
                try {
                    if (liveReport.getAction() != null) {
                        liveReportFactoryService.process(liveReport);
                    }
                    tpayNotification.setSendToAdnetwork(Boolean.valueOf(liveReport.getSendConversionCount() > 0));
                }
                catch (Exception ex) {
                    logger.error("onMessage::::::::::finally ", ex);
                   // update = daoService.saveObject(tpayNotification);
                }
                finally {
                	
                    update = daoService.saveObject(tpayNotification);
                }
            logger.info("onMessage::::::::::::::::: :: update::live report " + update + ", total time:: " + (System.currentTimeMillis() - time));
        }
        logger.info(("onMessage::::::::::::::::: :: update::live report " + update + ", total time:: " + (System.currentTimeMillis() - time)));
    }
    
    private String findAction(final TpayNotification tpayNotification, final LiveReport liveReport, final TpayServiceConfig tpayServiceConfig, final boolean sameDaysub) {
    	
    	if("SubscriptionInitialChargingNotification".equals(tpayNotification.getTpayAction())) {
    		redisCacheService.putCacheValue("SMS_SUB_CACHE_PREFIX"+tpayNotification.getSubscriptionContractId(), 1);
    		return MConstants.SMSSUB;
    	}
    	if("SubscriptionRecurringCharging".equals(tpayNotification.getTpayAction()) && Objects.toString(redisCacheService.getObjectCacheValue("SMS_SUB_CACHE_PREFIX"+tpayNotification.getSubscriptionContractId())).equals("1")) {
    		return MConstants.SMSRENEW;
    	}
    	if(Objects.nonNull(redisCacheService.getObjectCacheValue("TPAY_TEMP_SUBSCRIBE" + tpayNotification.getSubscriptionContractId()))) {
    	  if("SubscriptionChargingNotification".equalsIgnoreCase(tpayNotification.getTpayAction())) {
    		if ("PaymentCompletedSuccessfully".equalsIgnoreCase(tpayNotification.getPaymentTransactionStatusCode())) {
    		   return MConstants.ACT;
    	   }else {
    		   if(Objects.isNull(redisCacheService.getObjectCacheValue("TPAY_GRACE_RECEIVED_CACHE"+tpayNotification.getSubscriptionContractId()))) {
    			   redisCacheService.putObjectCacheValueByEvictionDay("TPAY_GRACE_RECEIVED_CACHE"+tpayNotification.getSubscriptionContractId(), "RECEIVED", 1);
    			   return MConstants.GRACE;
    		   }else {
    			   return "";
    		   }
    	   }
    	}else {
    		return "";
    	}
       }else if("SubscriptionContractStatusChanged".equalsIgnoreCase(tpayNotification.getTpayAction()) && "Canceled".equalsIgnoreCase(tpayNotification.getNotificationStatus())) {
		   return MConstants.DCT;
	   }
       else {
    	   if("PaymentCompletedSuccessfully".equalsIgnoreCase(tpayNotification.getPaymentTransactionStatusCode())) {
    		   return MConstants.RENEW;
    	   }else {
    		   return "";
    	   }
       }
    }
    
    private String getDefaultToken(final TpayNotification tpayNotification) {
    	if("GamePadWEEG".equals(tpayNotification.getProductId())){
			tpayNotification.setToken("-1c-1c249");
		}else if("GamePadEtisalatEG".equals(tpayNotification.getProductId())) {
			tpayNotification.setToken("-1c-1c252");
		}else if("GamePadVodaEG".equals(tpayNotification.getProductId())) {
			tpayNotification.setToken("-1c-1c282");
		}else if("GogamesZainKSA".equals(tpayNotification.getProductId())) {
			tpayNotification.setToken("-1c-1c288");
		}else if("GogamesZainKSAWeekly".equals(tpayNotification.getProductId())) {
			tpayNotification.setToken("-1c-1c289");
		}else {
			tpayNotification.setToken("-1c-1c283");
		}
		return tpayNotification.getToken();
    	
    }
}