package net.mycomp.messagecloud;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMessageCloudServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("messageCloudService")
public class MessageCloudService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(MessageCloudService.class.getName());
	
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
   @Autowired
   private JPAMessageCloudServiceConfig jpaMessageCloudServiceConfig;
   
	
	
	public MessageCloudService(){
		
	}
	
	@PostConstruct
	public void init() {
		List<MessageCloudServiceConfig> list=jpaMessageCloudServiceConfig.findEnableMessageCloudServiceConfig(true);
		
		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		
		MessageCloudConstant.mapNetworkNameMessageCloudServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getNetworkName(), p -> p))
						);
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		
		  MessageCloudServiceConfig messageCloudServiceConfig=MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
		  modelAndView.addObject("messageCloudServiceConfig", messageCloudServiceConfig);
		  modelAndView.addObject("token", adNetworkRequestBean.adnetworkToken.getTokenToCg());
		  modelAndView.setViewName("messagecloud/landingpage");
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
	
	
	@Override
	public SubscriberReg searchSubscriber(Integer operatorId, String msisdn,
			Integer serviceId,Integer productId) {
		try{
		return jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, productId);
		}catch(Exception ex){
			logger.info("searchSubscriber:::::::::: : ",ex);
		}
		return null;
	}

}
