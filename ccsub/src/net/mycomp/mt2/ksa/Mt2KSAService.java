package net.mycomp.mt2.ksa;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMt2KSAServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("mt2KSAService")
public class Mt2KSAService  extends AbstractOperatorService {

	private static final Logger logger = Logger
			.getLogger(Mt2KSAService.class.getName());
	
	@Autowired
	private JPAMt2KSAServiceConfig jbaMt2KSAServiceConfig;
	
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	
 @Autowired
	private Mt2KSAServiceApi mt2KSAServiceApi; 
	
	@Autowired
	private IDaoService daoService;
	
	   @Value("${jdbc.db.name}")
		private String dbName;
	
	public Mt2KSAService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		 List<Mt2KSAServiceConfig> list=jbaMt2KSAServiceConfig.findEnableMt2KSAServiceConfig(true);
		 
		 Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
		 Mt2KSAConstant.mapMt2ServiceIdToMt2KSAServiceConfig.putAll(list.stream().collect(
					Collectors.toMap(p -> p.getMt2ServiceId(), p ->p)));
		 
		  Integer id=daoService.
					findNextAutoIncrementId("tb_mt2_ksa_service_api_trans", dbName);		
		  Mt2KSAConstant.mt2KSAServiceApiTransIdAtomicInteger.set(id);
		   
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		Mt2KSAServiceConfig mt2KSAServiceConfig =null;
		String cgURL=null;
		try{
			 mt2KSAServiceConfig=
					Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig
			.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			
			/*
			 * modelAndView.addObject("mt2KSAServiceConfig",mt2KSAServiceConfig);
			 * modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.
			 * getTokenToCg()); modelAndView.setViewName("mt2ksa/msisdn_missing");
			 * adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			 */
			if(Objects.nonNull(mt2KSAServiceConfig)) {
				cgURL = mt2KSAServiceConfig.getCgURL().replaceAll("<token>", 
						adNetworkRequestBean.adnetworkToken.getTokenToCg());
			}
			
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			modelAndView.setView(new RedirectView(cgURL));
			
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

			SubscriberReg subscriberReg =jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn,
					productId);
			if(subscriberReg==null){
				return false;
			}
			
			Mt2KSAServiceConfig mt2KSAServiceConfig=
					Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig
			.get(subscriberReg.getServiceId());
			
			MT2KSAServiceApiTrans mt2KSAServiceApiTrans= 
					mt2KSAServiceApi.subStatus(mt2KSAServiceConfig, msisdn,null);
			if(mt2KSAServiceApiTrans.getResponseToCaller()){
				if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
					LiveReport liveReport=new LiveReport(opid,
							   new Timestamp(System.currentTimeMillis()),
							   -1
							   ,mt2KSAServiceConfig.getServiceId(),
							   productId);
							   liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());						   
					  subscriberReg=  subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
				
				}
			}
			
			return mt2KSAServiceApiTrans.getResponseToCaller();
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
			Mt2KSAServiceConfig mt2KSAServiceConfig=
					Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(subscriberReg.getServiceId());
			
			MT2KSAServiceApiTrans mt2KSAServiceApiTrans=mt2KSAServiceApi.unsubscribe(mt2KSAServiceConfig,
					subscriberReg.getMsisdn());
		if(mt2KSAServiceApiTrans.getResponseToCaller()){
			subscriberRegService.updateDeactivation(subscriberReg.getMsisdn(), subscriberReg.getProductId());
			deactivationResponse.setMessgae("You have been successfully unsubscribed service "+mt2KSAServiceConfig.getServiceName());
			deactivationResponse.setStatus(true);
		}else{
			deactivationResponse.setMessgae("Error in processing your unsubcription request for service "+mt2KSAServiceConfig.getServiceName());
			deactivationResponse.setStatus(false);
		}
		}catch(Exception ex){
			logger.error("deactivation   ",ex);
		}
		return deactivationResponse;
	}
	
	@Override
	public boolean sendOtp(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		   try{
	    	  
			Mt2KSAServiceConfig mt2ksaServiceConfig=Mt2KSAConstant
						.mapServiceIdToMt2KSAServiceConfig.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());		
				
		    if(checkSub(adNetworkRequestBean.vwserviceCampaignDetail.getProductId()
		    		, adNetworkRequestBean.vwserviceCampaignDetail.getOpId(),
		    		adNetworkRequestBean.getMsisdn())){
		    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
		    			adNetworkRequestBean.getMsisdn(), 
		    			adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
		    	   return false;
			   }
				MT2KSAServiceApiTrans mt2KSAServiceApiTrans=mt2KSAServiceApi.sendOTP(mt2ksaServiceConfig
							, adNetworkRequestBean.getMsisdn(), adNetworkRequestBean.adnetworkToken.getTokenToCg());//OTP(mt2ksaServiceConfig, msisdn,token, "WEB");
					if(mt2KSAServiceApiTrans.getSuccess()){
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
		   
			
			Mt2KSAServiceConfig mt2KSAServiceConfig=Mt2KSAConstant
					.mapServiceIdToMt2KSAServiceConfig.get(adNetworkRequestBean
							.vwserviceCampaignDetail.getServiceId());
			
			 if(checkSub(adNetworkRequestBean.vwserviceCampaignDetail.getProductId(), adNetworkRequestBean
					 .vwserviceCampaignDetail.getOpId(),
					 adNetworkRequestBean.getMsisdn())){
			    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
			    			adNetworkRequestBean.getMsisdn(), 
			    			adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
			    	
					   modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
							   (mt2KSAServiceConfig.getPortalUrl()
								  ,adNetworkRequestBean.getMsisdn(),subscriberReg.getSubscriberId()))); 
					   return false;
				}
				MT2KSAServiceApiTrans mt2KSAServiceApiTrans=mt2KSAServiceApi
						.validateOTP(mt2KSAServiceConfig, adNetworkRequestBean.getMsisdn(),
								adNetworkRequestBean.adnetworkToken.getTokenToCg(),adNetworkRequestBean
								.adnetworkToken.getParam1());
				
				if(mt2KSAServiceApiTrans.getSuccess()){
					//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
					LiveReport liveReport=new LiveReport(adNetworkRequestBean.vwserviceCampaignDetail.getOpId(),
							   new Timestamp(System.currentTimeMillis()),
							   adNetworkRequestBean.adnetworkToken.getCampaignId()
							   ,adNetworkRequestBean.vwserviceCampaignDetail.getServiceId(),
							   adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
							   liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());						   
				 SubscriberReg subscriberReg=subscriberRegService
						 .findOrCreateSubscriberByAct(adNetworkRequestBean.getMsisdn(),null, liveReport);
				 adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_VALIDATE);
				 success=true;
					   
				}
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return success;
	}
}
