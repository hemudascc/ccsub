package net.mycomp.onmobile;

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
import net.jpa.repository.JPAMKMalaysiaConfig;
import net.jpa.repository.JPAMobivateServiceConfig;
import net.jpa.repository.JPAOnmobileServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.jpa.repository.JPAWintelBDServiceConfig;
import net.mycomp.actel.ActelConstant;
import net.mycomp.actel.ActelServiceConfig;
import net.mycomp.oredoo.kuwait.OredoKuwaitConstant;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("onMobileService")
public class OnMobileService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(OnMobileService.class.getName());
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAOnmobileServiceConfig jpaOnmobileServiceConfig;
	
	public OnMobileService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		List<OnMobileServiceConfig> list=jpaOnmobileServiceConfig.findEnableOnMobileServiceConfig(true);
		
		OnMobileConstant.mapServiceIdToOnMobileServiceConfig.clear();
		OnMobileConstant.mapServiceIdToOnMobileServiceConfig.putAll(list.stream().
				collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
		
		OnMobileConstant.mapSrvkeyToOnMobileServiceConfig.clear();
		OnMobileConstant.mapSrvkeyToOnMobileServiceConfig.putAll(list.stream().
				collect(Collectors.toMap(p -> p.getSrvKey(), p -> p)));
		
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			modelAndView.setViewName("onmobile/landingpage");	
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
