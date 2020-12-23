package net.mycomp.etisalat;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAEtisalatServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

@Service("etisalatService")
public class EtisalatService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(EtisalatService.class.getName());
	
	
	@Autowired
	private JPAEtisalatServiceConfig jpaEtisalatServiceConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
   private final String imageUrl;
   
	private final   String cgCallback;
	
	@Autowired
	public EtisalatService(@Value("${etisalat.cg.callback.url}") String cgCallback,
			@Value("${etisalat.image.url}")String imageUrl){
		
	    this.cgCallback=cgCallback;
	    this.imageUrl=imageUrl;
	}
	
	@PostConstruct
	public void init() {
		
		List<EtisalatServiceConfig> list= jpaEtisalatServiceConfig.findEnableEtisalatServiceConfig(true);
		EtisalatConstant.listEtisalatServiceConfig.addAll(list);
		EtisalatConstant.mapServiceIdToEtisalatServiceConfig.putAll(
				list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p -> p))
						);
		EtisalatConstant.mapPackageIdToEtisalatServiceConfig.putAll(

				list.stream().collect(
						Collectors.toMap(p -> p.getPackageId(), p -> p))
						);
		
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
	       //http://pt1.etisalat.ae/lp/etisalatd2c/GamingBout/index.htm?
		//txnid=<token>&packageid=<packageid>&lang=en&device=sf&rurl=<redirecturl>
		//&usertype=paid&servicename=<servicename>&iurl=<imageurl>&cpid=294
		EtisalatServiceConfig etisalatServiceConfig=EtisalatConstant.mapServiceIdToEtisalatServiceConfig.get(adNetworkRequestBean
				.vwserviceCampaignDetail.getServiceId());
		
		String lpImage="";
		if(etisalatServiceConfig.getLpImages()!=null&&etisalatServiceConfig.getLpImages().size()>0){
			int index=MConstants.random.nextInt(etisalatServiceConfig.getLpImages().size());
			lpImage=etisalatServiceConfig.getLpImages().get(index);
		}	
		
		String cgUrl=etisalatServiceConfig.getCgUrl();	
		cgUrl=cgUrl.replaceAll("<token>",
				adNetworkRequestBean.adnetworkToken.getTokenToCg())
				.replaceAll("<packageid>",String.valueOf(etisalatServiceConfig.getPackageId()))
				.replaceAll("<redirecturl>",MUtility.urlEncoding(cgCallback))
				.replaceAll("<servicename>","")
				.replaceAll("<imageurl>",MUtility.urlEncoding(lpImage));
		logger.info("processBilling::: rredirect to cgUrl:::::::::::: "+cgUrl);
		adNetworkRequestBean.adnetworkToken.setRedirectToUrl(cgUrl);
		adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
		modelAndView.setView(new RedirectView(cgUrl));
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
	
	
	
}
