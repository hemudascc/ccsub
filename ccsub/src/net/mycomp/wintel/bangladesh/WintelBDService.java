package net.mycomp.wintel.bangladesh;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.jpa.repository.JPAMKMalaysiaConfig;
import net.jpa.repository.JPASubscriberReg;
import net.jpa.repository.JPAWintelBDServiceConfig;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;

@Service("wintelBDService")
public class WintelBDService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(WintelBDService.class.getName());
	
	
	@Autowired
	private JPAWintelBDServiceConfig jpaWintelBDServiceConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	public WintelBDService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		 List<WintelBDServiceConfig> list=jpaWintelBDServiceConfig.findEnableWintelBDServiceConfig(true);
		 WintelBDConstant.mapServiceIdToWintelBDServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
	
		 WintelBDConstant.mapOperatorToWintelBDServiceConfig.putAll(list.stream().collect(
					Collectors.toMap(p -> p.getOperator(), p ->p)));
		 
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			WintelBDServiceConfig wintelBDServiceConfig=WintelBDConstant.mapServiceIdToWintelBDServiceConfig
			.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
			modelAndView.addObject("wintelBDServiceConfig", wintelBDServiceConfig);
			modelAndView.setViewName("wbd/lp");
		
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
