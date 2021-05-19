package net.mycomp.mcarokiosk.hongkong;

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
import net.jpa.repository.JPAMKHongkongConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Service("mkHongkongService")
public class MKHongkongService  extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(MKHongkongService.class.getName());
	
	
	@Autowired
	private JPAMKHongkongConfig jpaMKHongkongConfig;
	@Autowired
	private MKHongkongServiceApi mkhongkongServiceApi;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	 @Value("${jdbc.db.name}")
	 private String dbName;
	 
	@Value("${macrokiosk.hongkong.mo.countryCode}")
	private String countryCode; 
	
	@Value("${macrokiosk.hongkong.mo.welcometext}")
	private String welcomeText;
	
	@Autowired
	private MacroKioskHongkongFactoryService macroKioskHongkongFactoryService;
	
//	private final String msisdnForwardingUrl;
	private final String heCallBackUrl;
	
	@Autowired
	public MKHongkongService(
			@Value("${macrokiosk.hongkong.msisdn.forwarding.he.callback.url}")String heCallBackUrl){
			this.heCallBackUrl=heCallBackUrl;
	}
	
	@PostConstruct
	public void init() {
		MKHongkongConstant.yyyyMMddHHmmssAccessToken.setTimeZone(TimeZone.getTimeZone(("GMT+8")));
		  List<MKHongkongConfig> listMKHongkongConfig=jpaMKHongkongConfig.findEnableMKHongkongConfig(true);	
		   
		  MKHongkongConstant.listMKHongkongConfig.addAll(listMKHongkongConfig);
		   
		  MKHongkongConstant.mapIdToMKHongkongConfig.putAll(listMKHongkongConfig.stream().collect(
					Collectors.toMap(p -> p.getId(), p -> p)));
		   
		  MKHongkongConstant.mapServiceIdToMKHongkongConfig.putAll(listMKHongkongConfig.stream().collect(
							Collectors.toMap(p -> p.getServiceId(), p -> p)));	
 
		  Integer id=daoService.
					findNextAutoIncrementId("tb_mk_hongkong_mo_message", dbName);	
		  logger.info(id);
		  MKHongkongConstant.moMessageIdAtomicInteger.set(id);
		   
		    id=daoService.
					findNextAutoIncrementId("tb_mk_hongkong_mt_message", dbName);		
		    MKHongkongConstant.mtMessageIdAtomicInteger.set(id);
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			try{
				AdnetworkOperatorConfig adnetworkOpConfig = MData.mapAdnetworkOpConfig
						.get(adNetworkRequestBean.getOpId()).get(
								adNetworkRequestBean.getAdNetworkId()); 
				logger.info("adnetworkOpConfig : "+adnetworkOpConfig);
				Operator operator = MData.mapIdToOperator.get(adNetworkRequestBean.getOpId());
				Product product = MData.mapIdToProduct.get(adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
				logger.info("operator: "+operator);
				logger.info("product: "+product);
				
				MKHongkongConfig mkHongkongConfig=
						MKHongkongConstant.mapServiceIdToMKHongkongConfig
						.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
				logger.info("mkHongkongConfig: "+mkHongkongConfig);
		if(redisCacheService.isCappingOver(adnetworkOpConfig,operator,product,
				MKHongkongConstant.getFormatUTC8Date())){			  
			adNetworkRequestBean.adnetworkToken  
			.setAction(MConstants.REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING);  
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(mkHongkongConfig.getPortalUrl());
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
		logger.info("msisdn:              :"+adNetworkRequestBean.getMsisdn());
		MKHongkongConfig mkHongkongConfig=MKHongkongConstant.mapServiceIdToMKHongkongConfig
		.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());

		MKHongkongCGToken mkHongkongCGToken=new MKHongkongCGToken(
		adNetworkRequestBean.adnetworkToken.getTokenId(),
		adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
		String adNetworkId = String.valueOf(adNetworkRequestBean.getAdNetworkId());
		int opid=adNetworkRequestBean.getOpId();  
		String msisdn=(adNetworkRequestBean.getMsisdn().startsWith(countryCode))?adNetworkRequestBean.getMsisdn():countryCode+adNetworkRequestBean.getMsisdn();
		String token = mkHongkongCGToken.getCGToken();
		int telcoid = mkHongkongConfig.getTelcoId();
		redisCacheService.putObjectCacheValueByEvictionDay(MKHongkongConstant.MO_MESSAGE_CAHCHE_PREFIX+msisdn,
				token, 1);
		redisCacheService.putObjectCacheValueByEvictionDay(MKHongkongConstant.HONGKONG_AD_NETWORK_CAHCHE_PREFIX+msisdn,
				adNetworkId, 1);
		logger.info("opid: "+opid+"  telcoid : "+telcoid+"  msisdn: "+msisdn+"  token: "+token+"  adNetworkId: "+adNetworkId);
		HongkongMOMessage hongkongMOMessage = new HongkongMOMessage();
		try {	
		String tokenStr[] = token.split(MConstants.TOKEN_SEPERATOR);
		hongkongMOMessage.setTokenId(Integer.parseInt(tokenStr[0]));
		hongkongMOMessage.setCampaignId(adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
		hongkongMOMessage.setMsisdn(msisdn);
		hongkongMOMessage.setToken(token);
		hongkongMOMessage.setTelcoid(telcoid);
		hongkongMOMessage.setText(welcomeText);
		hongkongMOMessage.setOpId(opid);   
		hongkongMOMessage.setIsFreeMt(true);
		}catch(Exception ex){
			logger.error("exception"+ex);
		}finally {
			boolean response=  macroKioskHongkongFactoryService.handleSubscriptionMOMessage(hongkongMOMessage);
			logger.info("   msisdn: "+ hongkongMOMessage.getMsisdn());
			modelAndView.setViewName("mkhongkong/lp2");
			
		}
		
		return true;	
	}
	
	//	@Override
//	public boolean processBilling(ModelAndView modelAndView,
//			AdNetworkRequestBean adNetworkRequestBean) {
//		try{
//		MKHongkongConfig mkHongkongConfig=MKHongkongConstant.mapServiceIdToMKHongkongConfig
//				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
//
//		MKHongkongCGToken mkHongkongCGToken=new MKHongkongCGToken(
//				adNetworkRequestBean.adnetworkToken.getTokenId(),
//				adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
//		logger.info("AdNetworkId: "+adNetworkRequestBean.getAdNetworkId());
//		modelAndView.addObject("token",mkHongkongCGToken.getCGToken());
//		modelAndView.addObject("mkHongkongConfig",mkHongkongConfig);	
//		modelAndView.addObject("adNetworkId", adNetworkRequestBean.getAdNetworkId());
//		modelAndView.addObject("opid",adNetworkRequestBean.getOpId());	
//		modelAndView.setViewName(mkHongkongConfig.getLpPages());
//		
//		}catch(Exception ex){
//			logger.error("Exception    ",ex);
//		}
//		return true;	    	
//		 
//	}
	
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
	
		   return false;
	}
	
	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {

		SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, productId);
		logger.info("checkSub:::::::::: list of subscriberreg "+subscriberReg);
//		SubscriberReg subscriberReg=null;
//		if(list!=null&&list.size()>0){
//			subscriberReg=list.get(0);
//		}
//		logger.info("checkSub:::::::::: subscriberReg: "+subscriberReg);
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			return true;
		}		
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
		return MKHongkongConstant.getFormatUTC8Date();
	}
	
}
