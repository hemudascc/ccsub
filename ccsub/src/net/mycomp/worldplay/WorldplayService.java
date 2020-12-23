package net.mycomp.worldplay;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.jpa.repository.JPAMKMalaysiaConfig;
import net.jpa.repository.JPASubscriberReg;
import net.jpa.repository.JPAWintelBDServiceConfig;
import net.jpa.repository.JPAWorldplayServiceConfig;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("worldplayService")
public class WorldplayService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(WorldplayService.class.getName());
	
	@Autowired
	private JPAWorldplayServiceConfig jpaWorldplayServiceConfig;
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	public WorldplayService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		 List<WorldplayServiceConfig> list=jpaWorldplayServiceConfig.findEnableWorldplayServiceConfig(true);//WintelBDServiceConfig(true);
		 WorldplayConstant.listWorldplayServiceConfig.addAll(list);		 
		 WorldplayConstant.mapServiceIdToWorldplayServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			WorldplayServiceConfig worldplayServiceConfig=
					WorldplayConstant.mapServiceIdToWorldplayServiceConfig
			.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			//public static String worldPlay_Ridrection_URL
			//="http://wap.zero9.co.za/collectcent/join.php?s=<worldplayservicename>&trafficid=<token>";
			
			String cgUrl=worldplayServiceConfig.getCgUrl()
					.replaceAll("<worldplayservicename>", worldplayServiceConfig.getWorldplayServiceName())
					.replaceAll("<token>", adNetworkRequestBean.adnetworkToken.getTokenToCg());
			modelAndView.addObject("cgUrl",cgUrl);
			modelAndView.addObject("termConditionPage",worldplayServiceConfig.getTermConditionPage());
			modelAndView.addObject("worldplayServiceConfig",worldplayServiceConfig);
			modelAndView.setViewName("worldplay/lp");
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(cgUrl);
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			
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
	
}
