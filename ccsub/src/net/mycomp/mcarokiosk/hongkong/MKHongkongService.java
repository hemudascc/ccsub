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
	 
//	private final String msisdnForwardingUrl;
	private final String heCallBackUrl;
	
	@Autowired
	public MKHongkongService(
			//@Value("${macrokiosk.hongkong.msisdn.forwardng.url}")String msisdnForwardingUrl,
			@Value("${macrokiosk.hongkong.msisdn.forwarding.he.callback.url}")String heCallBackUrl){
		//this.msisdnForwardingUrl=msisdnForwardingUrl;
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
		   
//		  MKHongkongConstant.mapTelcoIdToMKHongkongConfig.putAll(listMKHongkongConfig.stream().collect(
//					Collectors.toMap(p -> p.getTelcoId(), p -> p)));	
		  
		  Integer id=daoService.
					findNextAutoIncrementId("tb_mk_hongkong_mo_message", dbName);		
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
		try{
		MKHongkongConfig mkHongkongConfig=MKHongkongConstant.mapServiceIdToMKHongkongConfig
				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());

		MKHongkongCGToken mkHongkongCGToken=new MKHongkongCGToken(
				adNetworkRequestBean.adnetworkToken.getTokenId(),
				adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
		logger.info("AdNetworkId: "+adNetworkRequestBean.getAdNetworkId());
		modelAndView.addObject("token",mkHongkongCGToken.getCGToken());
		modelAndView.addObject("mkHongkongConfig",mkHongkongConfig);	
		modelAndView.addObject("adNetworkId", adNetworkRequestBean.getAdNetworkId());
		modelAndView.addObject("opid",adNetworkRequestBean.getOpId());	
		modelAndView.setViewName(mkHongkongConfig.getLpPages());
		
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
