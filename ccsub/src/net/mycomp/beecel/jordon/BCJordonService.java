package net.mycomp.beecel.jordon;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPABCJordonConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MData;

@Service("bcJordonService")
public class BCJordonService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(BCJordonService.class.getName());
	
	
	@Autowired
	private JPABCJordonConfig jpaBCJordonConfig;
//	@Autowired
//	private BCJordonServiceApi mkbcjordonServiceApi;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	 @Value("${jdbc.db.name}")
		private String dbName;
	 
//	private final String msisdnForwardingUrl;
	private final String heCallBackUrl;
	
	@Autowired
	public BCJordonService(
			//@Value("${macrokiosk.malaysia.msisdn.forwardng.url}")String msisdnForwardingUrl,
			@Value("${macrokiosk.malaysia.msisdn.forwarding.he.callback.url}")String heCallBackUrl){
		//this.msisdnForwardingUrl=msisdnForwardingUrl;
		this.heCallBackUrl=heCallBackUrl;
	}
	
	@PostConstruct
	public void init() {
		BCJordonConstant.yyyyMMddHHmmssAccessToken.setTimeZone(TimeZone.getTimeZone(("GMT+8")));
		  List<BCJordonConfig> listBCJordonConfig=jpaBCJordonConfig.findEnableBCJordonConfig(true);	
		   
		  BCJordonConstant.listBCJordonConfig.addAll(listBCJordonConfig);
		   
		  BCJordonConstant.mapIdToBCJordonConfig.putAll(listBCJordonConfig.stream().collect(
					Collectors.toMap(p -> p.getId(), p -> p)));
		   
		  BCJordonConstant.mapServiceIdToBCJordonConfig.putAll(listBCJordonConfig.stream().collect(
							Collectors.toMap(p -> p.getServiceId(), p -> p)));	
		  	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			try{
				AdnetworkOperatorConfig adnetworkOpConfig = MData.mapAdnetworkOpConfig
						.get(adNetworkRequestBean.getOpId()).get(
								adNetworkRequestBean.getAdNetworkId());
				
				Operator operator = MData.mapIdToOperator.get(adNetworkRequestBean.getOpId());
				Product product = MData.mapIdToProduct.get(adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
				
				
				BCJordonConfig bcJordonConfig=
						BCJordonConstant.mapServiceIdToBCJordonConfig
						.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
				
		if(redisCacheService.isCappingOver(adnetworkOpConfig,operator,product,
				BCJordonConstant.getFormatUTC8Date())){			
			adNetworkRequestBean.adnetworkToken
			.setAction(MConstants.REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING);
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(bcJordonConfig.getPortalUrl());
			return true;
	    	}
			}catch(Exception ex){
				logger.error("Exception ",ex);
			}
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
		BCJordonConfig bcJordonConfig=
				BCJordonConstant.mapServiceIdToBCJordonConfig
				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		
		BCJordonCGToken bcJordonCGToken=new BCJordonCGToken(
				adNetworkRequestBean.adnetworkToken.getTokenId(),
				adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
		
		logger.info("bcJordonConfig: "+bcJordonConfig);
		modelAndView.addObject("token",bcJordonCGToken.getCGToken());
		modelAndView.addObject("bcJordonConfig",bcJordonConfig);				
		//modelAndView.setViewName("bcjordon/lp");
		modelAndView.setViewName(bcJordonConfig.getLpPages());
		
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
	public Timestamp getTimeByOperator(Integer opId) {		
		return BCJordonConstant.getFormatUTC8Date();
	}

}
