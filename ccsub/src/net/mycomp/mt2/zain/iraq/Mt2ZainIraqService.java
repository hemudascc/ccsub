package net.mycomp.mt2.zain.iraq;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.bizao.BizaoConstant;
import net.bizao.BizaoIpPool;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMt2ZainIraqIpPool;
import net.jpa.repository.JPAMt2ZainIraqServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("mt2ZainIraqService")
public class Mt2ZainIraqService  extends AbstractOperatorService {

	private static final Logger logger = Logger
			.getLogger(Mt2ZainIraqService.class.getName());
	
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private Mt2ZainIraqServiceApi mt2ZainIraqServiceApi;
	
	@Autowired
	private JPAMt2ZainIraqServiceConfig jpaMt2ZainIraqServiceConfig;
	
	@Autowired
	private JPAMt2ZainIraqIpPool jpaMt2ZainIraqIpPool;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private IDaoService daoService;
	
	   @Value("${jdbc.db.name}")
		private String dbName;
	
	public Mt2ZainIraqService(){
	
	}
	
	@PostConstruct
	public void init() {
		
		List<Mt2ZainIraqServiceConfig> list=jpaMt2ZainIraqServiceConfig.findEnableMt2ZainIraqServiceConfig(true);
		 
		 Mt2ZainIraqConstant.mapServiceIdToMt2ZainIrqServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
		 
		 Mt2ZainIraqConstant.mapZainIraqServiceIdToMt2ZainIrqServiceConfig.putAll(list.stream().collect(
					Collectors.toMap(p -> p.getZainIraqServiceId(), p ->p)));
		 
		 Integer id=daoService.
					findNextAutoIncrementId("tb_mt2_zain_iraq_service_api_trans", dbName);
			
		 Mt2ZainIraqConstant.mt2ZainIraqApiTranAtomicInteger.set(id);
		 
		 List<Mt2ZainIraqIpPool> listIPPool=jpaMt2ZainIraqIpPool.findEnableMt2ZainIraqIpPoolIpPool(true);
			
			if (listIPPool != null) {
				for (Mt2ZainIraqIpPool ippool : listIPPool) {
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
			
			Mt2ZainIraqConstant.listIPPool.clear();		
			Mt2ZainIraqConstant.listIPPool.addAll(listIPPool);
			
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		try{
			Operator operator = MData.mapIdToOperator.get(adNetworkRequestBean.getOpId());
			Product product = MData.mapIdToProduct.get(adNetworkRequestBean
					.vwserviceCampaignDetail.getProductId());
			AdnetworkOperatorConfig adnetworkOpConfig = MData.mapAdnetworkOpConfig
					.get(adNetworkRequestBean.getOpId()).get(
							adNetworkRequestBean.getAdNetworkId());
			
			if(Mt2ZainIraqConstant.findMt2ZainIraqOperatorByIp(
					adNetworkRequestBean.adnetworkToken.getSource())==null
					&&!adNetworkRequestBean.adnetworkToken.getToken().equalsIgnoreCase("testingziraq")){
				adNetworkRequestBean.adnetworkToken
				.setAction(MConstants.REDIRECT_TO_WASTE_URL_IP_NOT_MATCHING);			
				//adNetworkRequestBean.adnetworkToken.setRedirectToUrl("https://port16.govisibl.com/dlv/c.php?cca=136850&ccz=4398&token="+adNetworkRequestBean.getToken());
				//adNetworkRequestBean.adnetworkToken.setRedirectToUrl("http://192.241.253.234/ccsub/cnt/mt2zainiraq/wifi/error");
				adNetworkRequestBean.adnetworkToken.setRedirectToUrl("http://play-mob2.com/ccsub/cnt/mt2zainiraq/wifi/error");
				return true;				
			}
			
			if(redisCacheService.isCappingOver(adnetworkOpConfig,operator,product,adNetworkRequestBean.adnetworkToken.getReqTime())){			
				adNetworkRequestBean.adnetworkToken
				.setAction(MConstants.REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING);
				Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=Mt2ZainIraqConstant
						.mapServiceIdToMt2ZainIrqServiceConfig
						.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
				adNetworkRequestBean.adnetworkToken.setRedirectToUrl(mt2ZainIraqServiceConfig.getPortalUrl());
				
				return true;
			}
			
			
		}catch(Exception ex){
			logger.error("checkBlocking::: ",ex);
			return true;
		}
        
		return false;
		
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			
			Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=Mt2ZainIraqConstant
					.mapServiceIdToMt2ZainIrqServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			modelAndView.addObject("mt2ZainIraqServiceConfig",mt2ZainIraqServiceConfig);			
			modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			
//			MT2ZainIraqServiceApiTrans mt2ZainIraqServiceApiTrans=mt2ZainIraqServiceApi.getScriptSource(mt2ZainIraqServiceConfig, adNetworkRequestBean.adnetworkToken.getTokenToCg()
//				, adNetworkRequestBean.getHeaderMap(), adNetworkRequestBean.adnetworkToken.getSource(),
//				"http://192.241.253.234/ccsub/cnt/cmp");
//			String uniqid=null;
//			if(mt2ZainIraqServiceApiTrans.getResponseToCaller()){
//				Map map=JsonMapper.getJsonToObject(mt2ZainIraqServiceApiTrans.getResponse(), Map.class);
//				modelAndView.addObject("source",Objects.toString(map.get("source")));
//				modelAndView.addObject("uniqid",Objects.toString(map.get("uniqid")));
//				uniqid=Objects.toString(map.get("uniqid"));
//			}else{
//				 uniqid = mt2ZainIraqServiceApi.getSha1Hash( adNetworkRequestBean.adnetworkToken.getSource()
//						+"-"+ adNetworkRequestBean.adnetworkToken.getTokenToCg()
//						+"-"+System.currentTimeMillis()); // Unique Key To Use For Block API Call
//				String source = "(function(s, o, u, r, k){b = s.URL;v = (b.substr(b.indexOf(r)).replace(r + '=', '')).toString();"
//						+ "r = (v.indexOf('&') !== -1) ? v.split('&')[0] : v;"
//						+ "a = s.createElement(o),m = s.getElementsByTagName(o)[0];"
//						+ "a.async = 1;a.setAttribute('crossorigin', 'anonymous');"
//						+ "a.src = u+'script.js?ak='+k+'&lpi='+r+'&lpu='+encodeURIComponent(b)+'&key=$uniqid';"
//						+ "m.parentNode.insertBefore(a, m);})"
//						+ "(document, 'script', '"+mt2ZainIraqServiceConfig.getApiSnippetUrl()+"', 'token', '"+mt2ZainIraqServiceConfig.getZainServiceKey()+"');";
//				modelAndView.addObject("source",source);
//				modelAndView.addObject("uniqid",uniqid);
//			}
//			
//			String cgUrl=mt2ZainIraqServiceConfig.getSubUrl()
//					  .replaceAll("<serviceid>", mt2ZainIraqServiceConfig.getZainIraqServiceId())
//					  .replaceAll("<spid>", mt2ZainIraqServiceConfig.getSpid())
//					  .replaceAll("<shortcode>", mt2ZainIraqServiceConfig.getShortCode())
//					  .replaceAll("<uniqid>", uniqid);
//			logger.info("cgUrl:: "+cgUrl);
//			modelAndView.addObject("cgUrl",cgUrl);
			redisCacheService.putObjectCacheValueByEvictionMinute(Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_SOURCE_CACHE_PREFIX+adNetworkRequestBean.adnetworkToken.getSource()
					, adNetworkRequestBean.adnetworkToken.getTokenToCg(), 5);
			
			modelAndView.setViewName("mt2zainiraq/auto_lp");
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl("mt2zainiraq/auto_lp");			
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
		try{

			
		}catch(Exception ex){
			logger.error("searchSubscriber ",ex);
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
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		DeactivationResponse deactivationResponse=new DeactivationResponse();
		try{
			
		}catch(Exception ex){
			logger.error("deactivation   ",ex);
		}
		return deactivationResponse;
	}
}
