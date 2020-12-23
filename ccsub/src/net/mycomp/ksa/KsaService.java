package net.mycomp.ksa;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAKsaServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("ksaService")
public class KsaService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(KsaService.class.getName());
	
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private KsaServiceApi ksaServiceApi;
	
	@Autowired
	private JPAKsaServiceConfig jpaKsaServiceConfig;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	
	

	@PostConstruct
	public void init() {
		List<KsaServiceConfig> list=jpaKsaServiceConfig.findEnableKsaServiceConfig(true);
		KsaConstant.listKsaServiceConfig.addAll(list);
		KsaConstant.mapServiceIdToKsaServiceConfig.putAll(list.stream().collect(
							Collectors.toMap(p -> p.getServiceId(), p -> p)));	
		
		KsaConstant.mapKsaServiceIdToKsaServiceConfig.putAll(list.stream().collect(
							Collectors.toMap(p -> p.getKsaServiceId(), p -> p)));
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			KsaServiceConfig ksaServiceConfig=KsaConstant.mapServiceIdToKsaServiceConfig.
			get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			
		
		   if(adNetworkRequestBean.adnetworkToken.getMsisdn()!=null){
			   
				boolean profilecheck=ksaServiceApi.checkProfile(
						adNetworkRequestBean.adnetworkToken.getMsisdn(), ksaServiceConfig,adNetworkRequestBean.adnetworkToken.getTokenToCg());
			if(profilecheck){
			   SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(adNetworkRequestBean
					   .adnetworkToken.getMsisdn(), adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
			   if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
				   
				   LiveReport liveReport=new LiveReport(subscriberReg.getOperatorId(),
						   adNetworkRequestBean.adnetworkToken.getReqTime(),
						   -1
						   ,adNetworkRequestBean.vwserviceCampaignDetail.getServiceId(),
						   adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
						   liveReport.setNoOfDays(ksaServiceConfig.getValidity());						   
				   subscriberRegService.findOrCreateSubscriberByAct(adNetworkRequestBean
						   .adnetworkToken.getMsisdn(),null, liveReport);
				   
			   }
			  modelAndView.setView(new RedirectView(ksaServiceConfig.getPortalUrl()
					  +"?msisdn="+adNetworkRequestBean.adnetworkToken.getMsisdn())); 
		   }else{
			   modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			   modelAndView.addObject("ksaServiceConfig",ksaServiceConfig);
			   modelAndView.addObject("msisdn",adNetworkRequestBean.adnetworkToken.getMsisdn());
			 //  modelAndView.setViewName("ksa/lp");
			   modelAndView.setViewName("ksa/msisdn_missing");
		   }
		   }else{
			   modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			   modelAndView.addObject("ksaServiceConfig",ksaServiceConfig);
			   modelAndView.addObject("msisdn",adNetworkRequestBean.adnetworkToken.getMsisdn());
			   modelAndView.setViewName("ksa/msisdn_missing"); 
		   }
		
		}catch(Exception ex){
			logger.error("Exception    ",ex);
		}
		return true;	    	
		 
	}
	
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
	
		   return false;
	}
	
	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		
	
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
			KsaServiceConfig ksaServiceConfig=
					KsaConstant.mapServiceIdToKsaServiceConfig.get(subscriberReg.getServiceId());
			
			if(ksaServiceApi.checkProfile(subscriberReg.getMsisdn(), ksaServiceConfig,null)){
				
	   if(ksaServiceApi.unsubscription(subscriberReg.getMsisdn(), ksaServiceConfig)){
		
		    subscriberRegService.updateDeactivation(subscriberReg.getMsisdn(), subscriberReg.getProductId());
			deactivationResponse.setStatus(true);
			deactivationResponse.setMessgae("You have successfully unsubscribed "
			+ksaServiceConfig.getServiceName()+" service");
			ksaServiceApi.sendSms(subscriberReg.getMsisdn(), ksaServiceConfig,
					ksaServiceConfig.getUnsubscriptionMsgTemplate(),KsaConstant.ACTIVATION_SMS_PUSH);
			
		 }else{
			deactivationResponse.setStatus(false);
			deactivationResponse.setMessgae("Your unsubscription request has failed. Please try after some time");
		 }
		}else{
			deactivationResponse.setStatus(false);
			deactivationResponse.setMessgae("Your unsubscription request has failed. Please try after some time");
			
		}
		}catch(Exception ex){
			logger.error("deactivation",ex);
		}finally{
			
		}
		return deactivationResponse;
	}

}
