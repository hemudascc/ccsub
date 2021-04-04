package net.mycomp.mt2.ksa;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSMt2KSADLRSdpListener implements MessageListener {

	private static final Logger logger = Logger.getLogger(JMSMt2KSADLRSdpListener.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;

	@Override
	public void onMessage(Message m) {
		Mt2KSADeliveryNotificationSdp mt2ksaDeliveryNotificationSdp = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		Mt2KSAServiceConfig mt2ksaServiceConfig =null;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2ksaDeliveryNotificationSdp = (Mt2KSADeliveryNotificationSdp) objectMessage.getObject();
			logger.info("mt2ksaDeliveryNotificationSdp::::: "+mt2ksaDeliveryNotificationSdp);
			cgToken = new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2KSAConstant.MT2_KSA_MSISDN_TOKEN_CAHCHE_PREFIX+mt2ksaDeliveryNotificationSdp.getMsisdn())));
			mt2ksaDeliveryNotificationSdp.setToken(cgToken.getCGToken());
			if(Objects.isNull(mt2ksaServiceConfig)) {
				if("Zain".equalsIgnoreCase(mt2ksaDeliveryNotificationSdp.getOperator())) {
					mt2ksaServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(81);
				}else {
					mt2ksaServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(82);
				}
			}
			Service service = MData.mapServiceIdToService.get(mt2ksaServiceConfig.getServiceId());
			liveReport = new LiveReport(service.getOpId(), new Timestamp(System.currentTimeMillis()), 
					cgToken.getCampaignId(),mt2ksaServiceConfig.getServiceId(),20); 
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(mt2ksaDeliveryNotificationSdp.getMsisdn());
			liveReport.setCircleId(0);
			liveReport.setMode("");
			if(mt2ksaDeliveryNotificationSdp.getSdpStatus().equals("Success") && cgToken.getCampaignId()>0) {
				//act
				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(MUtility.toDouble(mt2ksaDeliveryNotificationSdp.getPrice().toString(), mt2ksaServiceConfig.getPricePoint()));
				liveReport.setNoOfDays(MUtility.toInt(mt2ksaDeliveryNotificationSdp.getValidity().toString(),0));
			}else if(mt2ksaDeliveryNotificationSdp.getSdpStatus().equals("Success") && cgToken.getCampaignId()<0){
				//renew
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setRenewalAmount(MUtility.toDouble(mt2ksaDeliveryNotificationSdp.getPrice().toString(), mt2ksaServiceConfig.getPricePoint()));
				liveReport.setNoOfDays(MUtility.toInt(mt2ksaDeliveryNotificationSdp.getValidity().toString(),0));
			}else {
				//grace
				liveReport.setAction(MConstants.GRACE);
				liveReport.setGraceConversionCount(1);
			}
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}finally {
			try {
				mt2ksaDeliveryNotificationSdp.setAction(liveReport.getAction());
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2ksaDeliveryNotificationSdp:: "
						+ mt2ksaDeliveryNotificationSdp);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ksaDeliveryNotificationSdp);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));	
		}
	}
}