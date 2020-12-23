package net.mycomp.mcarokiosk.malaysia;

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
import net.jpa.repository.JPAMKMalaysiaConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MData;



@Service("mkMalaysiaService")
public class MKMalaysiaService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(MKMalaysiaService.class.getName());
	
	
	@Autowired
	private JPAMKMalaysiaConfig jpaMKMalaysiaConfig;
	@Autowired
	private MKMalaysiaServiceApi mkmalaysiaServiceApi;
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
	public MKMalaysiaService(
			//@Value("${macrokiosk.malaysia.msisdn.forwardng.url}")String msisdnForwardingUrl,
			@Value("${macrokiosk.malaysia.msisdn.forwarding.he.callback.url}")String heCallBackUrl){
		//this.msisdnForwardingUrl=msisdnForwardingUrl;
		this.heCallBackUrl=heCallBackUrl;
	}
	
	@PostConstruct
	public void init() {
		MKMalaysiaConstant.yyyyMMddHHmmssAccessToken.setTimeZone(TimeZone.getTimeZone(("GMT+8")));
		  List<MKMalaysiaConfig> listMKMalaysiaConfig=jpaMKMalaysiaConfig.findEnableMKMalaysiaConfig(true);	
		   
		  MKMalaysiaConstant.listMKMalaysiaConfig.addAll(listMKMalaysiaConfig);
		   
		  MKMalaysiaConstant.mapIdToMKMalaysiaConfig.putAll(listMKMalaysiaConfig.stream().collect(
					Collectors.toMap(p -> p.getId(), p -> p)));
		   
		  MKMalaysiaConstant.mapServiceIdToMKMalaysiaConfig.putAll(listMKMalaysiaConfig.stream().collect(
							Collectors.toMap(p -> p.getServiceId(), p -> p)));	
		   
//		  MKMalaysiaConstant.mapTelcoIdToMKMalaysiaConfig.putAll(listMKMalaysiaConfig.stream().collect(
//					Collectors.toMap(p -> p.getTelcoId(), p -> p)));	
		  
		  Integer id=daoService.
					findNextAutoIncrementId("tb_mk_malaysia_mo_message", dbName);		
		  MKMalaysiaConstant.moMessageIdAtomicInteger.set(id);
		   
		    id=daoService.
					findNextAutoIncrementId("tb_mk_malaysia_mt_message", dbName);		
		    MKMalaysiaConstant.mtMessageIdAtomicInteger.set(id);
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			try{
				AdnetworkOperatorConfig adnetworkOpConfig = MData.mapAdnetworkOpConfig
						.get(adNetworkRequestBean.getOpId()).get(
								adNetworkRequestBean.getAdNetworkId());
				
				Operator operator = MData.mapIdToOperator.get(adNetworkRequestBean.getOpId());
				Product product = MData.mapIdToProduct.get(adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
				
				
				MKMalaysiaConfig mkMalaysiaConfig=
						MKMalaysiaConstant.mapServiceIdToMKMalaysiaConfig
						.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
				
		if(redisCacheService.isCappingOver(adnetworkOpConfig,operator,product,
				MKMalaysiaConstant.getFormatUTC8Date())){			
			adNetworkRequestBean.adnetworkToken
			.setAction(MConstants.REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING);
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(mkMalaysiaConfig.getPortalUrl());
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
		//http://mis.etracker.cc/MYDCB/MsisdnForwarding?ServiceName=<servicename>&CallBackURL=<callbackurl>&AuthToken=<authtoken>
		MKMalaysiaConfig mkMalaysiaConfig=
				MKMalaysiaConstant.mapServiceIdToMKMalaysiaConfig
				.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		
	//	String authToken=mkmalaysiaServiceApi.getToken(mkMalaysiaConfig, adNetworkRequestBean.adnetworkToken.getTokenToCg());
	//	logger.info("cg token "+adNetworkRequestBean.adnetworkToken.getTokenToCg()+" ,authToken::::::::::::: "+authToken);
//		String url=msisdnForwardingUrl.replaceAll("<servicename>", MUtility.urlEncoding(mkMalaysiaConfig.getOpServiceName())).
//				   replaceAll("<authtoken>", MUtility.urlEncoding(authToken))
//				   .replaceAll("<hecallbackurl>",
//						   MUtility.urlEncoding(heCallBackUrl
//								   +"/"+adNetworkRequestBean.adnetworkToken.getTokenToCg()
//								   +"/"+authToken));
		
		//adNetworkRequestBean.adnetworkToken.setRedirectToUrl(url);
		//adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
		//modelAndView.setView(new RedirectView(url));		
		//logger.info("HE Url "+url);
		MKMalaysiaCGToken mkMalaysiaCGToken=new MKMalaysiaCGToken(
				adNetworkRequestBean.adnetworkToken.getTokenId(),
				adNetworkRequestBean.vwserviceCampaignDetail.getCampaignId());
		
		modelAndView.addObject("token",mkMalaysiaCGToken.getCGToken());
		modelAndView.addObject("mkMalaysiaConfig",mkMalaysiaConfig);				
		//modelAndView.setViewName("mkmalaysia/lp");
		modelAndView.setViewName(mkMalaysiaConfig.getLpPages());
		
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
		return MKMalaysiaConstant.getFormatUTC8Date();
	}
	
}
