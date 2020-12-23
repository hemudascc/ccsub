package net.bizao;


import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JMSBizaoPaymentListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSBizaoPaymentListener.class);
	
	
//	@Value("${bizao.sms.activation.template}")
//	private String bizaoSmsActivationTemplate;
	
//	@Value("${bizao.sms.deactivation.template}")
//	private String bizaoSmsDeactivationTemplate;
//	
//	@Value("${bizao.sms.renewal.template}")
//	private String bizaoSmsRenewalTemplate;
	
	@Autowired
	private IDaoService daoService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private BizaoApiService bizaoApiService;

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
  
	@Override
	public void onMessage(Message m) {

		BizaoPayment bizaoPayment = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		BizaoConfig bizaoConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			bizaoPayment = (BizaoPayment) objectMessage
					.getObject();
			
			logger.debug("BizaoPayment::::::: "+bizaoPayment+",  bizaoPayment id:: "+bizaoPayment.getId());
			
			 bizaoConfig=BizaoConstant.mapIdToBizaoConfig.get(bizaoPayment.getBizaoConfigId());
			 
			BizaoCGToken bizaoCGToken=new BizaoCGToken(bizaoPayment.getToken());
			
			liveReport=new LiveReport(bizaoConfig.getOpId(),new Timestamp(System.currentTimeMillis()),
					 MData.findCampaignId(bizaoCGToken.getAdnetworkId(),
							 bizaoConfig.getServiceId()),bizaoConfig.getServiceId(),bizaoConfig.getProductId()); 
			
		    liveReport.setTokenId(bizaoCGToken.getTokenId());
			liveReport.setToken(bizaoCGToken.getBizaoCGToken());
			liveReport.setMsisdn(bizaoPayment.getBizaoAlias());
			liveReport.setParam1(bizaoPayment.getBizaoToken());
			liveReport.setCircleId(0);		 
			CGToken cgToken=new CGToken(bizaoPayment.getToken());
			bizaoPayment.setCampaignId(cgToken.getCampaignId());
			bizaoPayment.setTokenId(cgToken.getTokenId());
			
			logger.debug("bizaoCGToken::::::: "+bizaoCGToken);
			
			if (bizaoPayment.getAction().equalsIgnoreCase(MConstants.ACT)&&bizaoPayment.getSuccess()==true) {

				liveReport.setAction(MConstants.ACT);
				liveReport.setNoOfDays(bizaoConfig.getValidity());
				liveReport.setConversionCount(1);
				liveReport.setAmount(bizaoPayment.getChargedAmount());
				bizaoPayment.setActKey(MConstants.ACT);				
		     }else if (bizaoPayment.getAction().equalsIgnoreCase(MConstants.RENEW)&&bizaoPayment.getSuccess()==true){
		    	 liveReport.setAction(MConstants.RENEW);
					liveReport.setNoOfDays(bizaoConfig.getValidity());
					liveReport.setRenewalCount(1);
					liveReport.setRenewalAmount(bizaoPayment.getChargedAmount());
					bizaoPayment.setActKey(MConstants.RENEW);	
		     } 
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				if (liveReport.getAction() != null) {
					logger.debug("action::::::: "+liveReport.getAction());
					liveReport = liveReportFactoryService.process(liveReport);
					if(liveReport.getSendConversionCount()>0){
						bizaoPayment.setSendToAdnetwork(true);		
						bizaoPayment.setSendToAdnetworkBY(MConstants.AUTOSENT_CRON);
					}
					
					if (liveReport.getAction().equalsIgnoreCase(MConstants.ACT)){
						
					
//					String msg=bizaoConfig.getActivationMsgTemplate().
//							  replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//							 .replaceAll("<amount>", String.valueOf(bizaoPayment.getChargedAmount()))
//							 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//							 .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//					             .replaceAll("<subid>", String.valueOf(liveReport.getSubId()));
					String msg=BizaoConstant.getMsg(bizaoConfig.getActivationMsgTemplate(),
							bizaoConfig, bizaoPayment.getChargedAmount(), liveReport.getSubId());
					
					bizaoApiService.sendSms(MConstants.ACT, bizaoPayment.getMsisdnPrefix(), 
									 bizaoConfig, 
									msg, bizaoPayment.getBizaoToken(),bizaoPayment.getBizaoAlias(),"");
					
//					String portalUrl=BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),liveReport.getSubId(),"");
//				     msg=bizaoConfig.getInformaticMsgTemplate().
//							 replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//							 .replaceAll("<amount>", String.valueOf(bizaoPayment.getChargedAmount()))
//							 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//							 .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//					             .replaceAll("<portalurl>", portalUrl);
					
					msg=BizaoConstant.getMsg(bizaoConfig.getInformaticMsgTemplate(),
							 bizaoConfig, bizaoPayment.getChargedAmount(), liveReport.getSubId());
					 
					bizaoApiService.sendSms(MConstants.ACT, bizaoPayment.getMsisdnPrefix(), 
									 bizaoConfig, 
									msg, bizaoPayment.getBizaoToken(),bizaoPayment.getBizaoAlias(),"");
					
					msg=BizaoConstant.getMsg(bizaoConfig.getDeacivationLinkMsgTemplate(),
							 bizaoConfig, bizaoPayment.getChargedAmount(), liveReport.getSubId());
					 
					bizaoApiService.sendSms(MConstants.ACT, bizaoPayment.getMsisdnPrefix(), 
									 bizaoConfig, 
									msg, bizaoPayment.getBizaoToken(),bizaoPayment.getBizaoAlias(),"");
					
					}else if (liveReport.getAction().equalsIgnoreCase(MConstants.RENEW)){
						
						//String portalUrl=BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),liveReport.getSubId(),"");
						//Cher client, Vous êtes actif sur le service de jeu PLAY RABBITS. Votre compte sera chargé
						//<currency> <amount> / <validity> journées. 
						//Pour vous désabonner, Envoyer CAPR STOP au 7752.
						
//						String  msg=bizaoConfig.getRenewalMsgTemplate()
//								  .replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//								 .replaceAll("<amount>", String.valueOf(bizaoPayment.getChargedAmount()))
//								 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//								 .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//						         .replaceAll("<portalurl>", portalUrl);
					String	msg= BizaoConstant.getMsg(bizaoConfig.getRenewalMsgTemplate(),
								 bizaoConfig, bizaoPayment.getChargedAmount(), liveReport.getSubId());
						 
						bizaoApiService.sendSms(MConstants.RENEW, bizaoPayment.getMsisdnPrefix(), 
										 bizaoConfig, 
										msg, bizaoPayment.getBizaoToken(),bizaoPayment.getBizaoAlias(),"");
						}
					
				}				
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : bizaoPayment:: "
						+ bizaoPayment);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {				
				update = daoService.saveObject(bizaoPayment);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
