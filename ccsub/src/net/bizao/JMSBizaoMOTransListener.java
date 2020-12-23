package net.bizao;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSBizaoMOTransListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSBizaoMOTransListener.class);
	
	
//	@Value("${bizao.sms.deactivation.template}")
//	private String bizaoSmsDeactivationTemplate;
//	
//	@Value("${bizao.sms.not.subscribed.template}")
//	private String bizaoSmsNotSubscribedTemplate;
//	
//	@Value("${bizao.sms.invalid.message.template}")
//	private String bizaoSmsInvalidMessageTemplate;
	
	
	@Autowired
	private IDaoService daoService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private BizaoApiService bizaoApiService;

	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
  
	@Override
	public void onMessage(Message m) {

		BizaoMoTrans bizaoMoTrans = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		String msg=null;
		BizaoConfig bizaoConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			bizaoMoTrans = (BizaoMoTrans) objectMessage
					.getObject();
			
			logger.debug("bizaoMoTrans::::::: "+bizaoMoTrans);
			
		List<SubscriberReg> list=
				jpaSubscriberReg.findSubscriberRegByMsisdn(bizaoMoTrans.getBizaoAlias());//use bizao alias as msisdn
			if(list==null||list.size()<=0){
				final String destAdress=bizaoMoTrans.getDestinationAddress();
				List<BizaoConfig> listBizaoConfig=BizaoConstant.listBizaoConfig.stream().filter(a->
				a.getShortCode().equalsIgnoreCase(destAdress)).collect(Collectors.toList());//.findFirst().get(); 
				final String inmsg=bizaoMoTrans.getMessage();
				 bizaoConfig=listBizaoConfig.stream().filter(a->inmsg.
						contains(a.getKeyword())).findFirst().orElse(null);
				
				if(bizaoConfig!=null){
					msg=bizaoConfig.getNotSubscribedMsgTemplate();	
				}else{
					bizaoConfig=listBizaoConfig.get(0);
					msg=bizaoConfig.getInvalidKeywordMsgTemplate();
				}
				 
			}else{
			
			SubscriberReg subscriberReg=list.get(0);
			 bizaoConfig=BizaoConstant.mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
			 
			liveReport=new LiveReport(bizaoConfig.getOpId(),new Timestamp(System.currentTimeMillis())
					 ,null,bizaoConfig.getServiceId(),bizaoConfig.getProductId()); 
			
			liveReport.setMsisdn(bizaoMoTrans.getBizaoAlias());
			liveReport.setParam1(bizaoMoTrans.getBizaoToken());
			liveReport.setCircleId(0);		 
			    
				 if (bizaoMoTrans.getMessage()!=null&&bizaoMoTrans.getMessage().toLowerCase().
						 contains(BizaoConstant.STOP)&&
						 bizaoMoTrans.getMessage().toLowerCase().
						 contains(bizaoConfig.getKeyword().toLowerCase())) {
					liveReport.setAction(MConstants.DCT);
					liveReport.setDctCount(1);
					logger.debug("DCT::::::: ");
					 msg=bizaoConfig.getDeactivationMsgTemplate();
				}else{
					 msg=bizaoConfig.getInvalidKeywordMsgTemplate();
				} 
			  
		   }
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				if (liveReport.getAction() != null) {
					logger.debug("action::::::: "+liveReport.getAction());
					liveReport = liveReportFactoryService.process(liveReport);
				}
				msg=BizaoConstant.getMsg(msg, bizaoConfig, bizaoConfig.getPricePoint(), 
						0);
//				msg= msg.replaceAll("<portalurl>", BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),0,""))
//							.replaceAll("<currency>", bizaoConfig.getCurrencyDesc()) 
//							.replaceAll("<amount>", String.valueOf(bizaoConfig.getPricePoint()))
//							 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//							 .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//							  .replaceAll("<servicename>", String.valueOf(bizaoConfig.getServiceName()));
				
				bizaoApiService.sendSms(liveReport.getAction(), bizaoConfig.getMsisdnPrefix(), 
						 bizaoConfig, 
						msg, bizaoMoTrans.getBizaoToken()
						,bizaoMoTrans.getBizaoAlias(),"");
				 
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : bizaoPayment:: "
						+ bizaoMoTrans);
				
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				
				update = daoService.saveObject(bizaoMoTrans);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
