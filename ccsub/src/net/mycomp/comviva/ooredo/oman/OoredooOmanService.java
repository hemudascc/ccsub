package net.mycomp.comviva.ooredo.oman;

import java.sql.Timestamp;
import java.util.Arrays;
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
import net.common.service.BlockSeriesRedisCacheService;
import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAOoredooOmanServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.mycomp.mt2.ksa.MT2KSAServiceApiTrans;
import net.mycomp.mt2.ksa.Mt2KSAConstant;
import net.mycomp.mt2.ksa.Mt2KSAServiceConfig;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;
import net.util.MUtility;

@Service("ooredooOmanService")
public class OoredooOmanService extends AbstractOperatorService {

	static final Logger logger = Logger.getLogger(OoredooOmanService.class.getName());

	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private BlockSeriesRedisCacheService blockSeriesRedisCacheService; 
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private OoredooOmanServiceApi ooredooOmanServiceApi;
	
	@Autowired
	private JMSOoredooOmanService jmsOoredooOmanService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private JPAOoredooOmanServiceConfig jpaOoredooOmanServiceConfig;
	
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	
	

	@PostConstruct
	public void init() {
		
		try {
			
	    List<OoredooOmanServiceConfig> list=
	    		jpaOoredooOmanServiceConfig.findEnableOredooOmanServiceConfig(true);
	    OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.putAll(list.stream().collect(
				Collectors.toMap(p -> p.getCcServiceId(), p -> p)));
	    
	    OoredooOmanConstant.mapServiceIdToOoreodoOmanServiceConfig.putAll(list.stream().collect(
				Collectors.toMap(p -> p.getServiceId(), p -> p)));
	    
	    OoredooOmanConstant.ooredooOmanOCSLogDetailId.set(
				daoService.findNextAutoIncrementId("tb_ooredoo_oman_ocs_log_detail", dbName));
		
		} catch (Exception ex) {
			logger.error("init ", ex);
		//	throw ex;
		}

	}

	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		return false;
	}
	
	
	

	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {		
		
		OoredooOmanServiceConfig ooredooOmanServiceConfig=
				OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.
		get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
	    //serviceNode=<servicenode>&serviceId=<serviceid>
		//&sequenceNo=<sequenceno>&redirectURL=http%3A%2F%2Fcp.domain.com%2Fhe%3F
		String heUrl=ooredooOmanServiceConfig.getHeUrl()
				  .replaceAll("<servicenode>", ooredooOmanServiceConfig.getServiceNode())
				  .replaceAll("<serviceid>",""+ ooredooOmanServiceConfig.getServiceId())
				  .replaceAll("<sequenceno>",""+ adNetworkRequestBean.adnetworkToken.getTokenId())
				  .replaceAll("<redirecturl>", 
						 MUtility.urlEncoding( ooredooOmanServiceConfig.getRedirectToUrl()
						  +adNetworkRequestBean.adnetworkToken.getTokenToCg()+"?"));		
		logger.info("processBilling::::: heUrl:: "+heUrl);
	    modelAndView.setView(new RedirectView(heUrl));
		return true;
	}	
	
	
	public boolean isSubscribed(String msisdn) {
		  try{  
		   List<SubscriberReg> list=jpaSubscriberReg.findSubscriberRegByMsisdn(msisdn);
		    
		   if(list!=null&&list.size()>0){
			   SubscriberReg subscriberReg=list.get(0);
			   
			   return subscriberReg.getStatus()==MConstants.SUBSCRIBED?true:false;
		}	
		  }catch(Exception ex){
			  logger.error("isSubscribed:: ",ex);
		  }
		  
		   return false;
	  }
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		    
		    return false;
	  }
	
	
	@Override
	public boolean checkSub(Integer productId,Integer opid,String msisdn) {
		try{

			SubscriberReg subscriberReg =jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn,
					productId);
			if(subscriberReg==null){
				return false;
			}
			
			OoredooOmanServiceConfig ooredooOmanServiceConfig=
					OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
			.get(subscriberReg.getServiceId());
			
			
			if(subscriberReg.getStatus()==MConstants.SUBSCRIBED){
		           return true;
				}
			
			
			
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
			OoredooOmanServiceConfig ooreodoOmanServiceConfig=
					OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.get(subscriberReg.getServiceId());
			
			OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=ooredooOmanServiceApi
					.unsubSubscribe(subscriberReg.getMsisdn(), "",
					ooreodoOmanServiceConfig);
			
		if(ooredooOmanOCSLogDetail.getSuccess()){
			subscriberRegService.updateDeactivation(subscriberReg.getMsisdn(), subscriberReg.getProductId());
			deactivationResponse.setMessgae("You have been successfully unsubscribed service "+ooreodoOmanServiceConfig.getServiceName());
			deactivationResponse.setStatus(true);
		}else{
			deactivationResponse.setMessgae("Error in processing your unsubcription request for service "+ooreodoOmanServiceConfig.getServiceName());
			deactivationResponse.setStatus(false);
		}
		}catch(Exception ex){
			logger.error("deactivation   ",ex);
		}
		return deactivationResponse;
	}
	
	@Override
	public boolean sendOtp(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		   try{
			   
			   logger.info(" service id:: "+adNetworkRequestBean
								.vwserviceCampaignDetail
								.getServiceId());
			   OoredooOmanServiceConfig ooreodoOmanServiceConfig=
						OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.get(adNetworkRequestBean
								.vwserviceCampaignDetail
								.getServiceId());				
			   logger.info(" ooreodoOmanServiceConfig :: "+ooreodoOmanServiceConfig);
			   
			    ooredooOmanOCSLogDetail=ooredooOmanServiceApi
						.sendPinApi( adNetworkRequestBean.getMsisdn(), 
								adNetworkRequestBean.adnetworkToken.getTokenToCg(),
								ooreodoOmanServiceConfig);
					if(ooredooOmanOCSLogDetail.getSuccess()){
						success=true;	
						 adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_SEND);
					
					}						
			}catch(Exception  ex){
				logger.error("Exception" ,ex);
			}finally{
				jmsOoredooOmanService.saveooredooOmanOCSLogDetail(ooredooOmanOCSLogDetail);
			}			
			return success;
	}

	@Override
	public boolean validateOtp(ModelAndView modelAndView, 
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		try{
		   
			
			 OoredooOmanServiceConfig ooreodoOmanServiceConfig=
						OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig.get(adNetworkRequestBean.vwserviceCampaignDetail
								.getServiceId());	
			
			
			  ooredooOmanOCSLogDetail=ooredooOmanServiceApi
						.pinValidation(adNetworkRequestBean.getMsisdn(),
								adNetworkRequestBean.adnetworkToken.getTokenToCg(), 
								ooreodoOmanServiceConfig, adNetworkRequestBean
								.adnetworkToken.getParam1());
				
					
				
				if(ooredooOmanOCSLogDetail.getSuccess()){
					//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
					LiveReport liveReport=new LiveReport(adNetworkRequestBean.vwserviceCampaignDetail.getOpId(),
							   new Timestamp(System.currentTimeMillis()),
							   adNetworkRequestBean.adnetworkToken.getCampaignId()
							   ,adNetworkRequestBean.vwserviceCampaignDetail.getServiceId(),
							   adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
							   liveReport.setNoOfDays(ooreodoOmanServiceConfig.getValidity());						   
				 SubscriberReg subscriberReg=subscriberRegService
						 .findOrCreateSubscriberByAct(adNetworkRequestBean.getMsisdn(),null, liveReport);
				 adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_VALIDATE);
				 success=true;
				 ooredooOmanOCSLogDetail.setSendToAdnetwork(true); 
				}
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsOoredooOmanService.saveooredooOmanOCSLogDetail(ooredooOmanOCSLogDetail);
		}
		return success;
	}

	
}
