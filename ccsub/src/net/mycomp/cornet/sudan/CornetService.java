package net.mycomp.cornet.sudan;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.RedisCacheService;
//import net.common.service.RedisCacheService;
import net.jpa.repository.JPACornetConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
//import net.jpa.repository.JPASubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("cornetService")
public class CornetService extends AbstractOperatorService {

	private static final Logger logger = Logger.getLogger(CornetService.class);

	@Autowired
	private JPACornetConfig jpaCornetConfig;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	

	@PostConstruct
	public void init() {

		List<CornetConfig> cornetConfigList = jpaCornetConfig.findEnableCornetConfig(true);
		logger.info("Loading Cornet Configuration ....................................");
		CornetConstant.listCornetConfig.addAll(cornetConfigList);
		CornetConstant.mapServiceIdToCornetConfig
				.putAll(cornetConfigList.stream().collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
		logger.info("Cornet CONFIG" + CornetConstant.mapServiceIdToCornetConfig);
		
	}

	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		logger.info("checking blocking..");
		return false;
	}

	@Override
	public boolean processBilling(ModelAndView modelAndView, AdNetworkRequestBean adNetworkRequestBean) {
		try {  
			CornetConfig cornetConfig = CornetConstant.mapServiceIdToCornetConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			String accessToken  =null;
//			String accessToken  = (String)redisCacheService.getObjectCacheValue(CornetConstant.CORNET_UNIQUE_ACCESS_TOKEN_PREFIX);
//			logger.info("accessToken:  "+accessToken);
//			if(accessToken == null) {  
//				logger.info("accessToken:  "+accessToken);
//				accessToken = new CornetUtils().GenerateToken(cornetConfig.getUserName(),cornetConfig.getPassword());
//			}
//			logger.info("accessToken:  "+accessToken+"  cornetConfig process billing : "+cornetConfig);
//			redisCacheService.putObjectCacheValueByEvictionDay(CornetConstant.CORNET_UNIQUE_ACCESS_TOKEN_PREFIX+adNetworkRequestBean.adnetworkToken.getTokenToCg(), accessToken, (CornetConstant.REMEMBER_ME)?30:1);
//			modelAndView.addObject("cornetConfig", cornetConfig);
//			modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
//			modelAndView.addObject("accessToken", accessToken);
//			modelAndView.addObject("status", 0);
//			modelAndView.setViewName("cornetsudan/lp");
			modelAndView.setView(new RedirectView(CornetConstant.CG_URL.replaceAll("<cid>", adNetworkRequestBean.adnetworkToken.getTokenToCg())));
		} catch (Exception e) {
			logger.info("exception: " + e);
		}
		return true;
	}

	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn,productId);
		logger.info("checkSub:::::::::: subscriberReg: "+subscriberReg);
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			return true;
		}		
		return false;
	}  
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		return false;
	}
}
