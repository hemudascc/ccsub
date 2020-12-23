package net.mycomp.mobivate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.IDaoService;
import net.jpa.repository.JPAMobivateServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.oredoo.kuwait.OCSResponse;
import net.mycomp.oredoo.kuwait.OredoKuwaitConstant;
import net.mycomp.oredoo.kuwait.OredooKuwaitServiceConfig;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

@Service("mobivateService")
public class MobivateService extends AbstractOperatorService {

	
	private static final Logger logger = Logger
			.getLogger(MobivateService.class.getName());
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	@Autowired
	private JPAMobivateServiceConfig jpaMobivateServiceConfig;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private MobivateApiService mobivateApiService;
	
	@Autowired
	private IDaoService daoService;
	
	public MobivateService(){
	
	}
	
	@PostConstruct
	public void init() {
		try{
		 List<MobivateServiceConfig> list=jpaMobivateServiceConfig.findEnableMobivateServiceConfig(true);
		 MobivateConstant.mapServiceIdToMobivateServiceConfig.putAll(list.stream().collect(
						Collectors.toMap(p -> p.getServiceId(), p ->p)));
		 
		 MobivateConstant.mapProductIdToMobivateCellcConfig.putAll(list.stream().collect(
					Collectors.toMap(p -> p.getProductId(), p -> p)));
		 
		 MobivateConstant.mobivateApiTransId.set(
					daoService.findNextAutoIncrementId("tb_mobivate_api_trans", dbName));
		}catch(Exception ex){
			logger.error("exception ",ex);
		}
	
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
			
		
		return false;
	}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		try{
			MobivateServiceConfig mobivateServiceConfig= MobivateConstant.mapServiceIdToMobivateServiceConfig
			 .get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			
			if(mobivateServiceConfig.getCcOpId()==MConstants.MOBIVATE_SOUTH_AFRICA_CELLC_OPERATOR_ID
					||mobivateServiceConfig.getCcOpId()==MConstants.MOBIVATE_SOUTH_AFRICA_MTN_OPERATOR_ID){
			//https://<content portal url>/lookup/za/?
			//shortcode=<shortcode>&service=<keyword>=<productid>
			//&freq=<validity>&campaign=<campaign>
			//	&amount=<amount>&bl=<brandlogo>&bc=<backgroundcolor>
			//	&tc=<textcolour>&return=<callbackurl>
				
			
			String cgUrl=mobivateServiceConfig.getCgUrl()
				.replaceAll("<shortcode>",MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
				.replaceAll("<keyword>",MUtility.urlEncoding( mobivateServiceConfig.getKeyword()))
				.replaceAll("<productid>", MUtility.urlEncoding(mobivateServiceConfig.getProductId()))
				.replaceAll("<validity>", MUtility.urlEncoding(Objects.toString(mobivateServiceConfig.getValidity())))
				.replaceAll("<campaign>", MUtility.urlEncoding(mobivateServiceConfig.getCampaignName()))
				.replaceAll("<amount>", MUtility.urlEncoding(Objects.toString(mobivateServiceConfig.getBillingAmount())))
				.replaceAll("<brandlogo>",MUtility.urlEncoding( mobivateServiceConfig.getBrandLogo()))
				.replaceAll("<backgroundcolor>",MUtility.urlEncoding( mobivateServiceConfig.getBackgroundColor()))
				.replaceAll("<textcolour>", MUtility.urlEncoding(mobivateServiceConfig.getTextColour()))
				.replaceAll("<callbackurl>",MUtility.urlEncoding( mobivateServiceConfig.getCallbackUrl()+adNetworkRequestBean.adnetworkToken.getTokenToCg()))
			    .replaceAll("<token>",MUtility.urlEncoding(adNetworkRequestBean.adnetworkToken.getTokenToCg()))
			    ;
			
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(cgUrl);	
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			logger.info("cgUrl:: "+cgUrl);
			modelAndView.setView(new RedirectView(cgUrl));
			
			}else{
		    	modelAndView.addObject("mobivateServiceConfig",mobivateServiceConfig );	
		    //	modelAndView.addObject("callbackurl",mobivateServiceConfig.getCallbackUrl()+adNetworkRequestBean.adnetworkToken.getTokenToCg());
			    modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			//    modelAndView.addObject("notification","http://192.241.253.234/ccsub/cnt/mobic/noti");
			
		    	modelAndView.setViewName("mobivate/uk_vodafone_lp");
			}
			
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
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		
		MobivateServiceConfig mobivateServiceConfig= MobivateConstant.mapServiceIdToMobivateServiceConfig
				 .get(subscriberReg.getServiceId());
		DeactivationResponse deactivationResponse=new DeactivationResponse();
		MobivateApiTrans mobivateApiTrans=mobivateApiService.unsubApi(subscriberReg.getMsisdn(), mobivateServiceConfig);
		if(mobivateApiTrans!=null&&mobivateApiTrans.getSuccess()==true){
		    	 deactivationResponse.setStatus(true);
		     deactivationResponse.setMessgae("Successfully unsubscribed "+mobivateServiceConfig.getServiceName());
			  }else{
				  deactivationResponse.setMessgae("Technical issue. Please try after sometime");
			  }
		logger.info("deactivationResponse:::::::::::: "+deactivationResponse.getMessgae());
		     return deactivationResponse;
		}
	
	
}
