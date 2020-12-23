package net.bizao;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPABizaoIpPool;
import net.jpa.repository.JPABizaoServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

@Service("bizaoService")
public class BizaoService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(BizaoService.class.getName());
	
	
	@Autowired
	private JPABizaoIpPool jpaBizaoIpPool;
	
	@Autowired
	private IDaoService daoService;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	
	@Value("${bizao.web.callback.url}")
	private String callbackUrl;
	
	@Value("${bizao.unsub.success}")
	private String bizaoUnsubSuccess;
	@Value("${bizao.not.subscribed}")
	private String bizaoNotSubscribed;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private BizaoApiService bizaoApiService;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private JPABizaoServiceConfig jpaBizaoServiceConfig; 
	
	
	private final String mainImgUrl;
	
	@Autowired
	public BizaoService(@Value("${main.images.url}")String mainImgUrl,
			@Value("#{'${bizao.renewal.daily.renewal.day.pattern}'.split(',')}")List<Integer> dailyNoOfDaysPattern,
			@Value("#{'${bizao.renewal.weekly.renewal.day.pattern}'.split(',')}")List<Integer> weeklyNoOfDaysPattern,
			@Value("#{'${bizao.renewal.monthly.renewal.day.pattern}'.split(',')}")List<Integer> monthlyNoOfDaysPattern ){
		this.mainImgUrl=mainImgUrl;
		BizaoConstant.dailyNoOfDaysPattern.addAll(dailyNoOfDaysPattern);
		BizaoConstant.weeklyNoOfDaysPattern.addAll(weeklyNoOfDaysPattern);
		BizaoConstant.monthlyNoOfDaysPattern.addAll(monthlyNoOfDaysPattern);
	}
	
	
	@PostConstruct
	public void init() {
		
		List<BizaoConfig> list=jpaBizaoServiceConfig.findEnableBizaoConfig(true);
		BizaoConstant.listBizaoConfig.clear();
		BizaoConstant.listBizaoConfig.addAll(list);
		
		BizaoConstant.mapIdToBizaoConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getId(), p -> p))
						);
		
		BizaoConstant.mapServiceIdToBizaoConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		
//		BizaoConstant.mapBizaoOperatorIdToBizaoConfig.putAll(
//				list.stream().collect(
//						Collectors.toMap(p -> p.getOpId(), p -> p))
//						);
		
		
		BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getMsisdnPrefix()+p.getServiceType(), p -> p))
						);
		
		
		for(BizaoConfig bizaoConfig:list){
			if(!BizaoConstant.listMsisdnPrefix.contains(bizaoConfig.getMsisdnPrefix())){
				BizaoConstant.listMsisdnPrefix.add(bizaoConfig.getMsisdnPrefix());
			}
		}
		
		
		List<BizaoIpPool> listIPPool=jpaBizaoIpPool.findEnableBizaoIpPool(true);
		
		if (listIPPool != null) {
			for (BizaoIpPool ippool : listIPPool) {
				try {
					SubnetUtils utils = new SubnetUtils(ippool
							.getIp().replaceAll(" ", "").trim());
					utils.setInclusiveHostCount(true);
					ippool.setSubnetUtils(utils);
				} catch (Exception ex) {
					logger.error("reloadConfiguration:: ippool:: " + ippool
							+ ", exception: " + ex);
				}
			}
		}
		
		BizaoConstant.listIPPool.clear();		
		BizaoConstant.listIPPool.addAll(listIPPool);
		
		Integer id= daoService.
				findNextAutoIncrementId("tb_bizao_payment_trans", dbName);
		BizaoConstant.bizaoPaymentIdAtomicInteger.set(id==null?0:id);
		
		 id= daoService.
				findNextAutoIncrementId("tb_bizao_sms", dbName);
		BizaoConstant.bizaoSmsAtomicInteger.set(id==null?0:id);
		
		
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		
	
		if("gl".equals(adNetworkRequestBean.getToken())) {
			logger.info("Google ad trafic::::::");
			adNetworkRequestBean.setToken(System.currentTimeMillis()+"gl");
			logger.info("set token to token: "+adNetworkRequestBean.getToken());
		}
		
		BizaoConfig bizaoConfig=null;
		BizaoIpPool bizaoIpPool= new BizaoIpPool();
				//BizaoConstant.findBizaoOperatorByIp(adNetworkRequestBean.adnetworkToken.getSource());
		logger.info("processBilling:: bizaoIpPool:: "+bizaoIpPool);

		if(bizaoIpPool!=null){
//			 bizaoConfig=BizaoConstant.
//					mapBizaoOperatorIdToBizaoConfig.get(bizaoIpPool.getBizaoOpId());
			 bizaoConfig=BizaoConstant.
						mapServiceIdToBizaoConfig.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
				
			///long time,int tokenId,int campaignId,int adnetworkId,Integer configId
			 BizaoCGToken bizaoCGToken=
						new BizaoCGToken(adNetworkRequestBean.adnetworkToken.getReqTimeLong(),
								adNetworkRequestBean.adnetworkToken.getTokenId(),
								adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId(),
								adNetworkRequestBean.adnetworkToken.getAdnetworkId(),
								bizaoConfig.getId());			 
			 modelAndView.addObject("bizaoConfig",bizaoConfig); 
			// logger.info("processBilling:: bizaoConfig:: "+bizaoConfig);
			 String cgUrl= bizaoConfig.getCgUrl()+MUtility.urlEncoding(callbackUrl+
					 bizaoCGToken.getBizaoCGToken());
			 modelAndView.addObject("cgtoken",bizaoCGToken.getBizaoCGToken());		
			 logger.info("processBilling:: bizaoConfig:: "+bizaoConfig+" ,cgUrl:: "+cgUrl);
		     modelAndView.addObject("cgUrl",cgUrl);
	    	 modelAndView.setViewName("bizao/lp_sub");	
	    	 
		}else{		    
			
			modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix); 
			bizaoConfig=BizaoConstant.
					mapServiceIdToBizaoConfig.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			BizaoCGToken bizaoCGToken=
					new BizaoCGToken(adNetworkRequestBean.adnetworkToken.getReqTimeLong(),
							adNetworkRequestBean.adnetworkToken.getTokenId(),
							adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId(),
							adNetworkRequestBean.adnetworkToken.getAdnetworkId(),
							bizaoConfig.getId());
			BizaoSecurityValidateTrans bizaoSecurityValidateTrans=bizaoApiService
					.getShield(bizaoCGToken.getBizaoCGToken(), adNetworkRequestBean.adnetworkToken.getQueryStr()
					, adNetworkRequestBean.getHeaderMap(), adNetworkRequestBean.adnetworkToken.getSource());
			modelAndView.addObject("source",bizaoSecurityValidateTrans.getSource());
			modelAndView.addObject("uniqueid",bizaoSecurityValidateTrans.getUniqueId());
			
			modelAndView.addObject("bizaoConfig",bizaoConfig); 
			modelAndView.addObject("lpimg",mainImgUrl+"/bizao/Greedy Gnomes_pw_200X200_1.PNG");
		    modelAndView.addObject("cgtoken",bizaoCGToken.getBizaoCGToken());		    
		    modelAndView.setViewName("bizao/msisdn_missing"); 
		    
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
	
//	@Override
	public String sendOtp(ModelAndView modelAndView, Integer opId,
			String msisdn) {
		
		return null;
	}

//	@Override
	public String validateOtp(ModelAndView modelAndView, Integer opId,
			String msisdn, String otp) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		
		DeactivationResponse deactivationResponse=new DeactivationResponse();
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			BizaoConfig bizaoConfig=BizaoConstant.
					mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
			
//			String msg=bizaoConfig.getDeactivationMsgTemplate()
//					 .replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//					 .replaceAll("<amount>", String.valueOf(bizaoConfig.getPricePoint()))
//					 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//					  .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//					  .replaceAll("<servicename>", String.valueOf(bizaoConfig.getServiceName()))
//					  .replaceAll("<portalurl>", BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),0,""));
			String msg=BizaoConstant.getMsg(bizaoConfig.getDeactivationMsgTemplate(),
					bizaoConfig, bizaoConfig.getPricePoint(), 0);
			logger.info("subscriberReg::::::::::: "+subscriberReg+", bizaoConfig:: "+bizaoConfig);
			subscriberRegService.updateDeactivation(subscriberReg.getMsisdn(), bizaoConfig.getServiceId());
			
			bizaoApiService.sendSms(MConstants.DCT, bizaoConfig.getMsisdnPrefix(), 
							 bizaoConfig, 
							msg, subscriberReg.getParam1()
							,subscriberReg.getMsisdn(),"");
			deactivationResponse.setStatus(true);
			deactivationResponse.setMessgae(BizaoConstant.getMsg(bizaoUnsubSuccess, bizaoConfig));
			  }else{
			 deactivationResponse.setMessgae(BizaoConstant.getMsg(bizaoNotSubscribed, null) );
			}
		return deactivationResponse;
	}
	
	
	
	
}
