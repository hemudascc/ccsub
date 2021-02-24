package net.mycomp.mt2.uae;

import java.sql.Timestamp;
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
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMt2UAEServiceConfig;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("mt2UAEService")
public class Mt2UAEService  extends AbstractOperatorService {

	private static final Logger logger = Logger
			.getLogger(Mt2UAEService.class.getName());

	@Autowired
	private JPAMt2UAEServiceConfig jpaMt2UAEServiceConfig;


	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired
	private SubscriberRegService subscriberRegService;


	@Autowired
	private Mt2UAEServiceApi mt2KSAServiceApi; 

	@Autowired
	private IDaoService daoService;

	@Value("${jdbc.db.name}")
	private String dbName;

	public Mt2UAEService(){

	}

	@PostConstruct
	public void init() {

		List<Mt2UAEServiceConfig> list=jpaMt2UAEServiceConfig.findEnableMt2UAEServiceConfig(true);

		Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.putAll(list.stream().collect(
				Collectors.toMap(p -> p.getServiceId(), p ->p)));

		Mt2UAEConstant.mapMt2OperatorIdMt2UAEServiceConfig.putAll(list.stream().collect(
				Collectors.toMap(p -> p.getMt2OpId(), p ->p)));

		Integer id=daoService.
				findNextAutoIncrementId("tb_mt2_uae_service_api_trans", dbName);

		Mt2UAEConstant.mt2UAEServiceApiTransIdAtomicInteger.set(id);

	}


	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {


		return false;
	}

	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		Mt2UAEServiceConfig mt2UAEServiceConfig =null;
		String cgURL=null;
		try{
			mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());

			if(adNetworkRequestBean.vwserviceCampaignDetail.getOpId()==MConstants.MT2_UAE_DU_OPERATOR_ID
					|| adNetworkRequestBean.vwserviceCampaignDetail.getOpId()==MConstants.MT2_UAE_ETISALAT_OPERATOR_ID){
				if(Objects.nonNull(mt2UAEServiceConfig)) {
					cgURL = mt2UAEServiceConfig.getCgUrl().replaceAll("<token>", 
							adNetworkRequestBean.adnetworkToken.getTokenToCg());
				}
				adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
				modelAndView.setView(new RedirectView(cgURL));
			}else {
			modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);
			modelAndView.addObject("token",adNetworkRequestBean.adnetworkToken.getTokenToCg());
			modelAndView.setViewName(mt2UAEServiceConfig.getLpPage());
			modelAndView.addObject("l", 1);
			}

		}catch(Exception ex){
			logger.error("Exception    ",ex);
		}
		return true;	    	

	}



	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		try{
			if(adNetworkRequestBean.getMsisdn()==null){
				return false;
			}
			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(adNetworkRequestBean.getMsisdn(),
					adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				return true;
			}

		}catch(Exception ex){
			logger.error("isSubscribed ",ex);
		}

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
			Mt2UAEServiceConfig mt2UAEServiceConfig=
					Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(subscriberReg.getServiceId());
			String msg=mt2UAEServiceConfig.getUnsubMsgTemplate();
			msg=Mt2UAEConstant.prepareMessage(msg, mt2UAEServiceConfig, subscriberReg.getSubscriberId(),null);

			deactivationResponse.setMessgae(msg);
			deactivationResponse.setStatus(true);

		}catch(Exception ex){
			logger.error("deactivation   ",ex);
			deactivationResponse.setMessgae("Error occured in processing your unsubscription request");
			deactivationResponse.setStatus(false);
		}
		return deactivationResponse;
	}

	@Override
	public boolean sendOtp(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		try{

			logger.info("sendOtp:: ");
			Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			MT2UAEServiceApiTrans mt2UAEServiceApiTransendOtp=mt2KSAServiceApi
					.sendOTP(mt2UAEServiceConfig, adNetworkRequestBean.getMsisdn()
							, adNetworkRequestBean.adnetworkToken.getParam2()
							,adNetworkRequestBean.adnetworkToken.getTokenToCg());
			if(mt2UAEServiceApiTransendOtp.getSuccess()){
				success=true;
				adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_SEND);
			}			
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return success;
	}

	@Override
	public boolean validateOtp(ModelAndView modelAndView, 
			AdNetworkRequestBean adNetworkRequestBean) {
		boolean success=false;
		try{
			logger.info("validateOtp:: ");
			Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			MT2UAEServiceApiTrans mt2UAEServiceApiTransSubscribe=mt2KSAServiceApi
					.subscribe(mt2UAEServiceConfig, adNetworkRequestBean.getMsisdn(), adNetworkRequestBean.adnetworkToken.getParam1()
							,adNetworkRequestBean.adnetworkToken.getTokenToCg());

			if(mt2UAEServiceApiTransSubscribe.getSuccess()){

				LiveReport liveReport=new LiveReport(adNetworkRequestBean.vwserviceCampaignDetail.getOpId(),
						new Timestamp(System.currentTimeMillis()),
						adNetworkRequestBean.adnetworkToken.getCampaignId()
						,adNetworkRequestBean.vwserviceCampaignDetail.getServiceId(),
						adNetworkRequestBean.vwserviceCampaignDetail.getProductId());
				liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());						   
				SubscriberReg subscriberReg=  subscriberRegService
						.findOrCreateSubscriberByAct(adNetworkRequestBean.getMsisdn()
								,null, liveReport);						 
				// adNetworkRequestBean.set
				success=true;
				adNetworkRequestBean.adnetworkToken.setAction(MConstants.PIN_VALIDATE);
			}
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return success;
	}



}
