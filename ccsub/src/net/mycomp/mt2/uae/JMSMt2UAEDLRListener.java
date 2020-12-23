package net.mycomp.mt2.uae;

import java.sql.Timestamp;
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
import net.jpa.repository.JPAAdnetworkToken;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSMt2UAEDLRListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2UAEDLRListener.class);
	
	@Autowired
	private IDaoService daoService;
	
    @Autowired
    private JPAAdnetworkToken jpaAdnetworkToken;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private Mt2UAEServiceApi mt2UAEServiceApi;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Override
	public void onMessage(Message m) {
	
		Mt2UAEDeliveryNotification mt2uaeDeliveryNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		String msg=null;
		Mt2UAEServiceConfig mt2UAEServiceConfig=null;
		List<SubscriberReg> subscriberRegs=null;
		Integer operatorId=0;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2uaeDeliveryNotification = (Mt2UAEDeliveryNotification) objectMessage.getObject();
			logger.info("mt2UAEDeliveryNotification::::: "+mt2uaeDeliveryNotification);
			subscriberRegs = jpaSubscriberReg.findSubscriberRegByMsisdn(mt2uaeDeliveryNotification.getMSISDN());
			cgToken=new CGToken(Objects.toString(redisCacheService
					.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX
							+mt2uaeDeliveryNotification.getMSISDN())));
			if(cgToken.getCampaignId()<=0 && subscriberRegs!=null) {
				operatorId=subscriberRegs.size()>0?subscriberRegs.get(0).getOperatorId():0;
			}
			if(operatorId==0 && cgToken.getCampaignId()>0) {
				VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail
						.get(cgToken.getCampaignId());
				operatorId=vwServiceCampaignDetail.getOpId();
			}
			
			mt2UAEServiceConfig = Mt2UAEConstant.mapMt2OperatorIdMt2UAEServiceConfig.get(operatorId);
			Service service=MData.mapServiceIdToService.get(mt2UAEServiceConfig.getServiceId());
			liveReport=new LiveReport(service.getOpId(),
					   new Timestamp(System.currentTimeMillis())
				 ,cgToken.getCampaignId(),mt2UAEServiceConfig.getServiceId(),mt2UAEServiceConfig.getProductId()); 
		
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setMsisdn(mt2uaeDeliveryNotification.getMSISDN());
				liveReport.setCircleId(0);
				liveReport.setMode("");
				//mt2uaeDeliveryNotification.setToken(cgToken.getCGToken());
				if(mt2uaeDeliveryNotification.getStatus().equals("Success") && subscriberRegs==null) {
					//act
					liveReport.setAction(MConstants.ACT);
					liveReport.setConversionCount(1);
					liveReport.setAmount(MUtility.toDouble(mt2UAEServiceConfig.getPricePoint().toString(),0));
					liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());
					redisCacheService.removeObjectCacheValue(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX+mt2uaeDeliveryNotification.getMSISDN());
				}else if(mt2uaeDeliveryNotification.getStatus().equals("Success") && subscriberRegs!=null){
					//renew
					liveReport.setAction(MConstants.RENEW);
					liveReport.setRenewalCount(1);
					liveReport.setRenewalAmount(MUtility.toDouble(mt2UAEServiceConfig.getPricePoint().toString(),0));
					liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());
				}
				else {
					//grace
					liveReport.setAction(MConstants.GRACE);
					liveReport.setGraceConversionCount(1);
				}
				
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}
		finally {
			try {		
				
				//mt2UAENotification.setMyAction(liveReport.getAction());
				mt2uaeDeliveryNotification.setAction(liveReport.getAction());
				
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				
				//mt2UAENotification.setSendToAdnetwork(liveReport.getConversionCount()>0?true:false);
				if(msg!=null){
					msg=Mt2UAEConstant.prepareMessage(msg,mt2UAEServiceConfig,liveReport.getSubId(),
							liveReport.getParam1());
					mt2UAEServiceApi.sendSMS(mt2UAEServiceConfig, mt2uaeDeliveryNotification.getMSISDN()
							, msg, liveReport.getAction());	
					}
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2uaeDeliveryNotification:: "
						+ mt2uaeDeliveryNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2uaeDeliveryNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
		
		
	}

}
