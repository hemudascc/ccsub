package net.mycom.nxt.vas;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.jms.JMSService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPANxtVasConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.HttpURLConnectionUtil;
import net.util.MConstants;

@Service("nxtVasService")
public class NxtVasService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(NxtVasService.class.getName());
	
	
	@Autowired
	private NxtVasServiceApi nxtVasServiceApi;
	
	
	@Autowired
	private JPANxtVasConfig jpaNxtVasConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	

	
	
	public NxtVasService(){
		
	}
	
	@PostConstruct
	public void init() {
		List<NxtVasConfig> listNxtVasConfig= jpaNxtVasConfig.findAllEnableNxtVasConfig(true);
    
		NxtVasConstant.mapServiceIdToNxtVasConfig.clear();
		
		NxtVasConstant.mapServiceIdToNxtVasConfig.putAll(
				listNxtVasConfig.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		NxtVasConstant.mapMccMncToNxtVasConfig.clear();
		NxtVasConstant.mapMccMncToNxtVasConfig.putAll(
				listNxtVasConfig.stream().collect(
						Collectors.toMap(p -> p.getMcc()+p.getMnc(), p -> p))
						);		
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
	 
		logger.info("processBilling::::::::: start");
		NxtVasConfig nxtVasConfig= NxtVasConstant.mapServiceIdToNxtVasConfig.get(
				adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		String cgUrl=nxtVasConfig.getCgUrlTemplate().replaceAll("<action>", NxtVasConstant.SUB)
		.replaceAll("<transaction>", String.valueOf(adNetworkRequestBean.adnetworkToken.getTokenId()))
		.replaceAll("<productid>", String.valueOf(nxtVasConfig.getNxtVasProduct_id()))
		.replaceAll("<language>",String.valueOf( nxtVasConfig.getLanguage()));	
		logger.info("processBilling::::::::: cgUrl: "+cgUrl);
		modelAndView.setView(new RedirectView(cgUrl));		
		return true;		
	}
	
	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		DeactivationResponse deactivationResponse=new DeactivationResponse();
		try{
			NxtVasConfig nxtVasConfig=NxtVasConstant.mapServiceIdToNxtVasConfig.get(subscriberReg.getServiceId());
			NxtVasUnsubTrans nxtVasUnsubTrans=nxtVasServiceApi.callUnsubApi(nxtVasConfig,
					subscriberReg.getMsisdn(), subscriberReg.getParam1());
			if(nxtVasUnsubTrans.isSuccess()){
				deactivationResponse.setStatus(true);
				deactivationResponse.setMessgae("You have successfully unsubscribed "+nxtVasConfig.getServiceName()+" service");
				}else{
				deactivationResponse.setStatus(false);
				deactivationResponse.setMessgae("Your unsubscription request has failed. Please try after some time");
			 }
			
		}catch(Exception ex){
			logger.error("deactivation:: ",ex);
		}
		return deactivationResponse;
	}
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
		SubscriberReg subscriberReg=jpaSubscriberReg.
				findSubscriberRegByMsisdnAndProductId(adNetworkRequestBean.getMsisdn(),
				adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
		
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			NxtVasConfig nxtVasConfig=NxtVasConstant.mapServiceIdToNxtVasConfig.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(nxtVasConfig.getPortalUrl()+"&msisdn="+adNetworkRequestBean.getMsisdn());
			return true;
		}
		return  false;
	}
	
	
}
