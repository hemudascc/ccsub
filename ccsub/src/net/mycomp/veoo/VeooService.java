package net.mycomp.veoo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.jpa.repository.JPAVeooServiceConfig;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("veooService")
public class VeooService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(VeooService.class.getName());
	
	@Autowired
	private JPAVeooServiceConfig jpaVeooServiceConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	@Value("${veoo.collectcent.he.url}")
	private String collectcentHeUrl;
	
	@Autowired
	private IDaoService daoService;
	
	private final  String veooHeCallback;
	
	@Autowired
   public VeooService(@Value("${veoo.he.callback}") String  veooHeCallback){
		this.veooHeCallback=veooHeCallback;
	}
	
	@PostConstruct
	public void init() {
		List<VeooServiceConfig> list=jpaVeooServiceConfig.findEnableVeooServiceConfig(true);
		
		VeooConstant.mapIdToVeooServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getId(), p -> p))
						);
		
		VeooConstant.mapServiceIdToVeooServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		VeooConstant.mapVeooServiceIdToVeooServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getVeooServiceId(), p -> p))
						);
		
		VeooConstant.automicIdVeooMTMessage.set(daoService.findNextAutoIncrementId("tb_veoo_mt_message", 
				dbName));
		
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	
	public boolean checkBlocking(String msisdn) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		
		   VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(
				adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		   logger.info("processBilling:: veooServiceConfig:: "+veooServiceConfig);
		 
		   modelAndView.addObject("veooServiceConfig", veooServiceConfig);
		   modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
		   modelAndView.addObject("msisdn", adNetworkRequestBean.adnetworkToken.getMsisdn());
		   if(adNetworkRequestBean.adnetworkToken.getMsisdn()!=null
				  &&adNetworkRequestBean.adnetworkToken.getParam2()!=null
				   &&adNetworkRequestBean.adnetworkToken.getParam2().equalsIgnoreCase("he")){
			   modelAndView.clear();
			   modelAndView.setView(new RedirectView(collectcentHeUrl+adNetworkRequestBean.adnetworkToken.getMsisdn()));
			   return true;
		   }
		   
		   if(adNetworkRequestBean.adnetworkToken.getParam1()!=null
				   &&adNetworkRequestBean.adnetworkToken.getParam1().equalsIgnoreCase("OTP")){
			   modelAndView.setViewName(veooServiceConfig.getPinLandingPage());
		   }else{
		       modelAndView.setViewName(veooServiceConfig.getLandingPage());
		   }
		   return true;	    	
	}
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
		boolean isSubscribed= subscriberRegService.isSubscribedBySericeId(adNetworkRequestBean.getMsisdn()
				, adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		    
		if(isSubscribed){
			logger.debug("campaign:: already subscribed, ");
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_WASTE_URL_ALREADY_SUBSCRIBED);
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl("" + "?substatus=true");		
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
