package net.mycomp.messagecloud.gateway.za;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMCGZAServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("mcgZAService")
public class MCGZAService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(MCGZAService.class.getName());
	
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAMCGZAServiceConfig jpaMCGZAServiceConfig;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private MCGZAApiService mcgZAApiService;
   
   @Value("${messagecloud.za.portal.url}")
		private String portalUrl;
	   
	   
   @Value("${jdbc.db.name}")
	private String dbName;
   
	
	public MCGZAService(){
		
	}
	
	@PostConstruct
	public void init() {
		List<MCGZAServiceConfig> list=jpaMCGZAServiceConfig.findEnableMCGZAServiceConfig(true);
		MCGZAConstant.mapServiceIdToMCGZAServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
	       
		MCGZAServiceConfig mcgZAServiceConfig=MCGZAConstant.mapServiceIdToMCGZAServiceConfig
				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
        modelAndView.addObject("mcgZAServiceConfig",mcgZAServiceConfig);
        modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
       
        if(adNetworkRequestBean.adnetworkToken.getParam1()!=null&&adNetworkRequestBean.adnetworkToken.
        		getParam1().equalsIgnoreCase(MCGZAConstant.CHECK_SUB)){
        	 modelAndView.setViewName("mcgza/already_sub");
        }else{
        	 modelAndView.setViewName("mcgza/lp");
        }
       
        
//		MCGZAOBSWindowTrans mcgZAOBSWindowTrans=mcgZAApiService.sendOBSWindowRequest(adNetworkRequestBean.adnetworkToken.getMsisdn()
//				, mcgZAServiceConfig, 
//		   		 adNetworkRequestBean.adnetworkToken.getTokenToCg(),
//				adNetworkRequestBean.adnetworkToken.getTokenId()); 
//		logger.info("");
//		if(mcgZAOBSWindowTrans.getResponseStatus()!=null&&mcgZAOBSWindowTrans.getResponseStatus().equalsIgnoreCase("OK")){
//			modelAndView.setView(new RedirectView(mcgZAOBSWindowTrans.getResponseRedirectUrl()));
//		}else{
//			modelAndView.setView(new RedirectView(portalUrl));
//		}
		
		return true;	    	
		 
	}
	
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
		boolean isSubscribed= subscriberRegService.isSubscribedBySericeId(adNetworkRequestBean.getMsisdn()
				, adNetworkRequestBean.vwserviceCampaignDetail.getProductId());		    
		   if(isSubscribed){
			 	 
			logger.debug("campaign:: already subscribed, ");
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_WASTE_URL_ALREADY_SUBSCRIBED);
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(portalUrl+
					"?msisdn="+adNetworkRequestBean.getMsisdn());		
		}
		   return isSubscribed;
	}
	
	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		
		List<SubscriberReg> list=jpaSubscriberReg.findSubscriberRegByMsisdn(msisdn);
		logger.info("checkSub:::::::::: list of subscriberreg "+list);
		SubscriberReg subscriberReg=null;
		if(list!=null&&list.size()>0){
			subscriberReg=list.get(0);
		}
		logger.info("checkSub:::::::::: subscriberReg: "+subscriberReg);
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			return true;
		}		
		return false;
	}
}
