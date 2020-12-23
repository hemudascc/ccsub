package net.mycomp.actel;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.indonesia.triyakom.TriyakomConstant;
import net.jpa.repository.JPAActelNewServiceConfig;
import net.jpa.repository.JPAActelServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.mobimind.MobimindConstant;
import net.mycomp.mobimind.MobimindServiceConfig;
import net.persist.bean.IOtp;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("actelService")
public class ActelService extends AbstractOperatorService{

	
	private static final Logger logger = Logger
			.getLogger(ActelService.class.getName());
	
	@Autowired
	private JPAActelServiceConfig jpaActelServiceConfig;
	
	@Autowired
	private JPAActelNewServiceConfig jpaActelNewServiceConfig;
	
	@Autowired
	private IDaoService daoService;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	

	private final String heCallbackURL;
	
	private final String heURL;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	 public ActelService(	@Value("${actel.he.callback.url}")String heCallbackURL, @Value("${actel.he.url}")String heURL) {
		this.heCallbackURL=heCallbackURL;
		this.heURL = heURL;
	}
	
	@PostConstruct
	public void init(){		
		
//	List<ActelServiceConfig> list=jpaActelServiceConfig.findEnableActelServiceConfig(true);
//	ActelConstant.listActelConfig.clear();
//	ActelConstant.listActelConfig.addAll(list);
//	ActelConstant.mapServiceIdToActelServiceConfig.clear();
//	ActelConstant.mapServiceIdToActelServiceConfig.putAll(list.stream().
//			collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
	
	
	ActelConstant.actlelApiTransId.
			set(daoService.findNextAutoIncrementId("tb_actel_api_trans", 
					dbName));
	List<ActelNewServiceConfig> listActelNewServiceConfig=jpaActelNewServiceConfig.findEnableActelNewServiceConfig(true);
	ActelConstant.listActelNewConfig.clear();
	ActelConstant.listActelNewConfig.addAll(listActelNewServiceConfig);
	
	ActelConstant.mapServiceIdToActelNewServiceConfig.clear();
	ActelConstant.mapServiceIdToActelNewServiceConfig.putAll(listActelNewServiceConfig.stream().
			collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
	
	}
	
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			ActelNewServiceConfig actelNewServiceConfig= 
					ActelConstant.mapServiceIdToActelNewServiceConfig
				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			
			if(StringUtils.isEmpty(adNetworkRequestBean.adnetworkToken.getMsisdn())){
				SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
						ActelConstant.formatMsisdn(adNetworkRequestBean.adnetworkToken.getMsisdn()
								, actelNewServiceConfig.getMsisdnPrefix()),
								adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
				if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
					String portalUrl=ActelConstant.getPortalUrl(actelNewServiceConfig.getPortalUrl(),
							"", subscriberReg.getSubscriberId());
					modelAndView.setView(new RedirectView(portalUrl));
					adNetworkRequestBean.adnetworkToken.setAction(MConstants.ALREADY_SUBSCRIBED);
					return true;
				}
				
			}
			
	  modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
	  modelAndView.addObject("actelServiceConfig", actelNewServiceConfig);
	  modelAndView.addObject("l", 0);
	  modelAndView.setViewName("actel/msisdn_missing");
	  
	  if(adNetworkRequestBean.vwserviceCampaignDetail.getOpId()==MConstants.ACTEL_OPERATOR_DU) {
		  
	 String url =heURL.replaceAll("<application_id>", actelNewServiceConfig.getIdApplication())
		.replaceAll("<callbackurl>", heCallbackURL+adNetworkRequestBean.adnetworkToken.getTokenToCg());
	  logger.info("redirected to he url"+url);	  
	  modelAndView.setView(new RedirectView(url));
	  adNetworkRequestBean.adnetworkToken.setRedirectToUrl(url);
	  }else if(adNetworkRequestBean.vwserviceCampaignDetail.getOpId()
			  ==MConstants.ACTEL_OOREDOO_QATAR_OPERATOR_ID) {		  
	   String url =actelNewServiceConfig.getHeUrl()
			 .replaceAll("<application_id>",actelNewServiceConfig.getIdApplication())
			 .replaceAll("<country>",actelNewServiceConfig.getCountry())
	         .replaceAll("<callbackurl>",
	        		 actelNewServiceConfig.getHeCallback()+adNetworkRequestBean.adnetworkToken.getTokenToCg());
	  logger.info("redirected to he url"+url);	  
	  modelAndView.setView(new RedirectView(url));	  
	  adNetworkRequestBean.adnetworkToken.setRedirectToUrl(url);	  
	  }else if(adNetworkRequestBean.vwserviceCampaignDetail.getOpId()
			  ==MConstants.ACTEL_VODAFONE_QATAR_OPERATOR_ID) {		  
			  modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
			  modelAndView.addObject("actelServiceConfig", actelNewServiceConfig);
			  modelAndView.addObject("l", 0);
			  modelAndView.setViewName("actel/vodafone_qatar_msisdn");
	   }
	  adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
		}catch(Exception ex){
		logger.error("Exception ",ex);	
		}
		
		return true;	    	
		 
	}
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		return false;
	}
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		    
		
		   return false;
	  }
	

	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
	     logger.info("deactivation:: not any process");
		 return null;
	}

	
//	@Override
//	public IOtp sendOtp(ModelAndView modelAndView, String msisdn,
//			Integer operatorId, Integer serviceId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public SubscriberReg searchSubscriber(Integer operatorId, String msisdn,
			Integer serviceId,Integer productId) {
		try{
			return jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, productId);
		}catch(Exception ex){
			logger.error("searchSubscriber ",ex);
		}
		return null;
	}

	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
