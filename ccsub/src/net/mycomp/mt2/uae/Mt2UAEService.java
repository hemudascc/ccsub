package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMt2UAEServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

@Service("mt2UAEService")
public class Mt2UAEService  extends AbstractOperatorService {

	private static final Logger logger = Logger
			.getLogger(Mt2UAEService.class.getName());
	
	@Autowired
	private JPAMt2UAEServiceConfig jpaMt2UAEServiceConfig;
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	
    @Autowired
	private Mt2UAEServiceApi mt2KSAServiceApi; 
	
	@Autowired
	private IDaoService daoService;
	
	   @Value("${jdbc.db.name}")
		private String dbName;
	
	public Mt2UAEService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		 List<Mt2UAEServiceConfig> list=jpaMt2UAEServiceConfig.findEnableMt2UAEServiceConfig(true);
		 
		 Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
		 
		 Mt2UAEConstant.mapMt2OperatorIdMt2UAEServiceConfig.putAll(list.stream().collect(
					Collectors.toMap(p -> p.getMt2OpId(), p ->p)));
		 
		 Integer id=daoService.
					findNextAutoIncrementId("tb_mt2_uae_service_api_trans", dbName);
			
		 Mt2UAEConstant.mt2UAEServiceApiTransIdAtomicInteger.set(id);
		   
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			Mt2UAEServiceConfig mt2UAEServiceConfig=
					Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
			.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			
			modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);
			modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			modelAndView.setViewName(mt2UAEServiceConfig.getLpPage());
			
			
			  modelAndView.addObject("l", 1);
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			if(adNetworkRequestBean.vwserviceCampaignDetail.getOpId()==MConstants.MT2_UAE_DU_OPERATOR_ID){
				/*
				 * MT2UAEServiceApiTrans mt2UAEServiceApiTrans=
				 * mt2KSAServiceApi.getSubscription(mt2UAEServiceConfig,
				 * adNetworkRequestBean.adnetworkToken.getSource()
				 * ,adNetworkRequestBean.adnetworkToken.getTokenToCg());
				 */
				//String cgUrl=Mt2UAEConstant.getRedirectionUrl(mt2UAEServiceApiTrans.getResponse());
				
				String cgUrl=Mt2UAEConstant.CG_REDIRECT_URL;
				
				//http://mt-2.co/SecureD_Redirect/S_Redirect.aspx?origin=MT2&uid=#uid#&
				//trxid=#trxid#&serviceProvider=mt2&serviceid=kidokingdomweekly&plan=weekly&price=10&
				//locale=en&utm_source=#utm_source#&utm_medium=#utm_medium#&utm_content=#utm_content#&
				//trafficSource=#trafficSource#&publisher=#publisher#&subPublisher=#subPublisher#&
				//clickID=#clickID#&redirectUrl=#redirectUrl#&opt1=#opt1#&opt2=#opt2#
				String trafficSource="";
				if(mt2UAEServiceConfig.getTrafficSources()!=null&&mt2UAEServiceConfig.getTrafficSources().size()>0){
		  			int index=MConstants.random.nextInt(mt2UAEServiceConfig.getTrafficSources().size());
		  			//lpImage="../images/oredoo/"+oredooKuwaitServiceConfig.getLpImages().get(index);
		  			if(index<mt2UAEServiceConfig.getTrafficSources().size()){
		  			trafficSource=mt2UAEServiceConfig.getTrafficSources().get(index);
		  			}
		  		}
				cgUrl=cgUrl
				.replaceAll("#uid#","")
				.replaceAll("#trxid#",adNetworkRequestBean.adnetworkToken.getTokenToCg())
				.replaceAll("#utm_source#","")
				.replaceAll("#utm_medium#","")
				.replaceAll("#utm_content#","")
				.replaceAll("#trafficSource#",trafficSource)
				.replaceAll("#publisher#","")
				.replaceAll("#subPublisher#","")
				.replaceAll("#clickID#",adNetworkRequestBean.adnetworkToken.getTokenToCg())
				.replaceAll("#redirectUrl#",MUtility.urlEncoding(mt2UAEServiceConfig.getCgCallBackUrl()+"/"+adNetworkRequestBean.adnetworkToken.getTokenToCg()+"?trxid="+adNetworkRequestBean.adnetworkToken.getTokenId()))
				.replaceAll("#opt1#","")
				.replaceAll("#opt1#","")
				.replaceAll("#opt2#","")
				;
				
				logger.info("cg url:: "+cgUrl);
				adNetworkRequestBean.adnetworkToken.setRedirectToUrl(cgUrl); 
				adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
				
//				url+="?redirectUrl="+MUtility.urlEncoding(mt2UAEServiceConfig.getCgCallBackUrl())
//						+"&trxid="+adNetworkRequestBean.adnetworkToken.getTokenToCg();
				modelAndView.addObject("cgUrl",cgUrl);
			}
			
		}catch(Exception ex){
			logger.error("Exception    ",ex);
		}
		return true;	    	
		 
	}
	
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		try{
			  if(adNetworkRequestBean.getMsisdn()==null){
				  return false;
			  }
		  SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(adNetworkRequestBean.getMsisdn(),
					   adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
		   if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			   return true;
		   }
		   
		   }catch(Exception ex){
			   logger.error("isSubscribed ",ex);
		   }
	
		   return false;
	}
	
	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		try{

			
			
			
			
		}catch(Exception ex){
			logger.error("searchSubscriber ",ex);
		}
		return false;
	}
	
	public SubscriberReg searchSubscriber(Integer operatorId,String msisdn, 
			Integer serviceId,Integer productId){
		try{
			return jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, productId);
		}catch(Exception ex){
			logger.error("searchSubscriber ",ex);
		}
		return null;		
	}
	
	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		DeactivationResponse deactivationResponse=new DeactivationResponse();
		try{
			Mt2UAEServiceConfig mt2UAEServiceConfig=
					Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(subscriberReg.getServiceId());
			String msg=mt2UAEServiceConfig.getUnsubMsgTemplate();
			msg=Mt2UAEConstant.prepareMessage(msg, mt2UAEServiceConfig, subscriberReg.getSubscriberId(),null);
			
		deactivationResponse.setMessgae(msg);
		deactivationResponse.setStatus(true);
		
		}catch(Exception ex){
			logger.error("deactivation   ",ex);
			deactivationResponse.setMessgae("Error occured in processing your unsubscription request");
			deactivationResponse.setStatus(false);
		}
		return deactivationResponse;
	}
	
	@Override
	public boolean sendOtp(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		 try{
			 
			 logger.info("sendOtp:: ");
			 Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
						.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
					MT2UAEServiceApiTrans mt2UAEServiceApiTransendOtp=mt2KSAServiceApi
							.sendOTP(mt2UAEServiceConfig, adNetworkRequestBean.getMsisdn()
									, adNetworkRequestBean.adnetworkToken.getParam2()
									,adNetworkRequestBean.adnetworkToken.getTokenToCg());
					if(mt2UAEServiceApiTransendOtp.getSuccess()){
						success=true;
						adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_SEND);
					}			
			}catch(Exception  ex){
				logger.error("Exception" ,ex);
			}
			return success;
	}

	@Override
	public boolean validateOtp(ModelAndView modelAndView, 
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		try{
			 logger.info("validateOtp:: ");
			Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		MT2UAEServiceApiTrans mt2UAEServiceApiTransSubscribe=mt2KSAServiceApi
					.subscribe(mt2UAEServiceConfig, adNetworkRequestBean.getMsisdn(), adNetworkRequestBean.adnetworkToken.getParam1()
							,adNetworkRequestBean.adnetworkToken.getTokenToCg());
						
				if(mt2UAEServiceApiTransSubscribe.getSuccess()){
			
					LiveReport liveReport=new LiveReport(adNetworkRequestBean.vwserviceCampaignDetail.getOpId(),
							   new Timestamp(System.currentTimeMillis()),
							   adNetworkRequestBean.adnetworkToken.getCampaignId()
							   ,adNetworkRequestBean.vwserviceCampaignDetail.getServiceId(),
							   adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
							   liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());						   
							   SubscriberReg subscriberReg=  subscriberRegService
									   .findOrCreateSubscriberByAct(adNetworkRequestBean.getMsisdn()
											   ,null, liveReport);						 
							  // adNetworkRequestBean.set
					 success=true;
					 adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_VALIDATE);
				}
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return success;
	}
	
	
	
}
