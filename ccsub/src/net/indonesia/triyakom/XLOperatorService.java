package net.indonesia.triyakom;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

@Service("xlOperatorService")
public class XLOperatorService implements IOpeartorService{

	private static final Logger logger = Logger.getLogger(XLOperatorService.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private IndonesiaSmsService indonesiaSmsService;
	
	@Autowired
	private JMSIndonesiaService jmsIndonesiaService;
	
	
	
	
	@Override
	public boolean handleSubscriptionMoMessage(MOMessage moMessage) {
		
		IndonesiaChargingConfig selectedIndonesiaChargingConfig=null;
		for(IndonesiaChargingConfig indonesiaChargingConfig:TriyakomConstant.listIndonesiaChargingConfig){
			if(moMessage.getOp().equalsIgnoreCase(indonesiaChargingConfig.getOperator())&&
					indonesiaChargingConfig.getType().equalsIgnoreCase(TriyakomConstant.PUSH)){
				selectedIndonesiaChargingConfig=indonesiaChargingConfig;
				break;
			}
		}
		
		TriyakomConfig selectedTriyakomConfig=null;
		for(TriyakomConfig triyakomConfig:TriyakomConstant.listTriyakomConfig){
			if(moMessage.getOp().equalsIgnoreCase(triyakomConfig.getOp())){
				selectedTriyakomConfig=triyakomConfig;
				break;
			}
		}
		
		String message="";
		MTMessage mtMessage=new MTMessage(null);
		if(moMessage.getActivationKey()!=null
				&&moMessage.getActivationKey().equalsIgnoreCase(TriyakomConstant.ACTIVATION_KEY)&&
				moMessage.getServiceKey().equalsIgnoreCase(selectedTriyakomConfig.getKey())){
			message=selectedIndonesiaChargingConfig.getSubMessage();
			mtMessage.setAction(TriyakomConstant.MT_WELCOME_MSG);
		}else if(moMessage.getActivationKey()!=null
				&&moMessage.getActivationKey().equalsIgnoreCase(TriyakomConstant.DEACTIVATION_KEY)){
			mtMessage.setAction(MConstants.DCT);
			message=selectedIndonesiaChargingConfig.getUnsubMessage();
		}
		
		
		
		mtMessage.setIndonesiaChargingConfigId(selectedIndonesiaChargingConfig.getId());
		mtMessage.setShortCodeToPrepareMtUrl(selectedIndonesiaChargingConfig.getShortCodeToPrepareMtUrl());
		mtMessage.setShortCode(selectedIndonesiaChargingConfig.getShortCode());
		mtMessage.setDestAddr(moMessage.getMsisdn());
		mtMessage.setAppId(selectedIndonesiaChargingConfig.getAppId());
		mtMessage.setAppPwd(selectedIndonesiaChargingConfig.getAppPwd());
		mtMessage.setData(message);
		mtMessage.setOp(moMessage.getOp());
		mtMessage.setRtxId(moMessage.getTxId());
		mtMessage.setService(selectedIndonesiaChargingConfig.getService());
		mtMessage.setAlphabetd("0");
		
		mtMessage.setToken(moMessage.getToken());
		mtMessage.setTokenId(moMessage.getTokenId());
		indonesiaSmsService.sendMTMessage(mtMessage);
		daoService.saveObject(mtMessage);
		//logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
		//logger.info("handleSubscriptionMoMessage:: moMessage:: "+moMessage);	
		if(moMessage.getActivationKey()!=null
				&&moMessage.getActivationKey().equalsIgnoreCase(TriyakomConstant.ACTIVATION_KEY)&&
				moMessage.getServiceKey().equalsIgnoreCase(selectedTriyakomConfig.getKey())){
		jmsIndonesiaService.saveMTMessage(mtMessage);		
		//logger.info("handleSubscriptionMoMessage::  send mt push message");
		}else{
		//	logger.info("handleSubscriptionMoMessage:: not send mt push message");	
		}
		return true;
	}
	
	
	@Override
	public boolean handleSubscriptionMTPushMessage(MTMessage tmpMtMessage) {
		
		IndonesiaChargingConfig selectedIndonesiaChargingConfig=null;
		for(IndonesiaChargingConfig indonesiaChargingConfig:TriyakomConstant.listIndonesiaChargingConfig){
			if(tmpMtMessage.getOp().equalsIgnoreCase(indonesiaChargingConfig.getOperator())&&
					indonesiaChargingConfig.getType().equalsIgnoreCase(TriyakomConstant.CHARGED)){
				selectedIndonesiaChargingConfig=indonesiaChargingConfig;
				break;
			}
		}		
		
		MTMessage mtMessage=new MTMessage(MConstants.ACT);
		mtMessage.setIndonesiaChargingConfigId(selectedIndonesiaChargingConfig.getId());
		mtMessage.setShortCodeToPrepareMtUrl(selectedIndonesiaChargingConfig.getShortCodeToPrepareMtUrl());
		mtMessage.setShortCode(selectedIndonesiaChargingConfig.getShortCode());
		mtMessage.setDestAddr(tmpMtMessage.getDestAddr());
		mtMessage.setAppId(selectedIndonesiaChargingConfig.getAppId());
		mtMessage.setAppPwd(selectedIndonesiaChargingConfig.getAppPwd());
		mtMessage.setData(selectedIndonesiaChargingConfig.getSubMessage());
		mtMessage.setOp(tmpMtMessage.getOp());
		mtMessage.setRtxId("");
		mtMessage.setService(selectedIndonesiaChargingConfig.getService());
		mtMessage.setAlphabetd("0");
	
		mtMessage.setToken(tmpMtMessage.getToken());
		mtMessage.setTokenId(tmpMtMessage.getTokenId());
		mtMessage.setServiceId(tmpMtMessage.getServiceId());
		mtMessage.setMoId(tmpMtMessage.getMoId());
		
		//if(tmpMtMessage!=null&&tmpMtMessage.getRequestType()!=null&&tmpMtMessage.getRequestType().equalsIgnoreCase("GRACE_BILLING"))
		//	mtMessage.setRequestType(tmpMtMessage.getRequestType());	
		indonesiaSmsService.sendMTMessage(mtMessage);
		daoService.saveObject(mtMessage);
		//logger.info("handleSubscriptionMTPushMessage:::::::::: "+mtMessage);		
		return true;
	}


	
	@Override
	public boolean handleSubscriptionMTPushMessage(SubscriberReg subscriberReg) {
		
		String opName=OperatorIdNameEnum.getOpertorName(subscriberReg.getOperatorId()).getOpName();
		
		IndonesiaChargingConfig selectedIndonesiaChargingConfig=null;
		for(IndonesiaChargingConfig indonesiaChargingConfig:TriyakomConstant.listIndonesiaChargingConfig){
			if(opName.equalsIgnoreCase(indonesiaChargingConfig.getOperator())&&
					indonesiaChargingConfig.getType().equalsIgnoreCase(TriyakomConstant.CHARGED)){
				selectedIndonesiaChargingConfig=indonesiaChargingConfig;
				break;
			}
		}		
		
		MTMessage mtMessage=new MTMessage(MConstants.PARK_TO_ACT);
		mtMessage.setIndonesiaChargingConfigId(selectedIndonesiaChargingConfig.getId());
		mtMessage.setShortCodeToPrepareMtUrl(selectedIndonesiaChargingConfig.getShortCodeToPrepareMtUrl());
		mtMessage.setShortCode(selectedIndonesiaChargingConfig.getShortCode());
		mtMessage.setDestAddr(subscriberReg.getMsisdn());
		mtMessage.setAppId(selectedIndonesiaChargingConfig.getAppId());
		mtMessage.setAppPwd(selectedIndonesiaChargingConfig.getAppPwd());
		mtMessage.setData(selectedIndonesiaChargingConfig.getSubMessage());
		mtMessage.setOp(opName);
		mtMessage.setRtxId("");
		mtMessage.setService(selectedIndonesiaChargingConfig.getService());
		mtMessage.setAlphabetd("0");
		//if(tmpMtMessage!=null&&tmpMtMessage.getRequestType()!=null&&tmpMtMessage.getRequestType().equalsIgnoreCase("GRACE_BILLING"))
		//mtMessage.setRequestType(subscriberReg.getParam2());
		//mtMessage.setMsgType(MConstansts.GRACE_TO_ACT);
		//mtMessage.setToken(tmpMtMessage.getToken());
		//mtMessage.setTokenId(tmpMtMessage.getTokenId());
		mtMessage.setServiceId(subscriberReg.getServiceId());
		//mtMessage.setMoId(tmpMtMessage.getId());		
				
		indonesiaSmsService.sendMTMessage(mtMessage);
		daoService.saveObject(mtMessage);
		//logger.info("handleSubscriptionMTPushMessage:::::::::: "+mtMessage);		
		return true;
	}
	
	
	@Override
	public boolean handleSubscriptionMTRenewalPushMessage(SubscriberReg subscriberReg) {
		
		String opName=OperatorIdNameEnum.getOpertorName(subscriberReg.getOperatorId()).getOpName();
		
		IndonesiaChargingConfig selectedIndonesiaChargingConfig=null;
		for(IndonesiaChargingConfig indonesiaChargingConfig:
			TriyakomConstant.listIndonesiaChargingConfig){
			if(opName.equalsIgnoreCase(indonesiaChargingConfig.getOperator())&&
					indonesiaChargingConfig.getType().equalsIgnoreCase(TriyakomConstant.RENEWAL)){
				selectedIndonesiaChargingConfig=indonesiaChargingConfig;
				break;
			}
		}		
		
		MTMessage mtMessage=new MTMessage(MConstants.RENEW);
		mtMessage.setIndonesiaChargingConfigId(selectedIndonesiaChargingConfig.getId());
		mtMessage.setShortCodeToPrepareMtUrl(selectedIndonesiaChargingConfig.getShortCodeToPrepareMtUrl());
		mtMessage.setShortCode(selectedIndonesiaChargingConfig.getShortCode());
		mtMessage.setDestAddr(subscriberReg.getMsisdn());
		mtMessage.setAppId(selectedIndonesiaChargingConfig.getAppId());
		mtMessage.setAppPwd(selectedIndonesiaChargingConfig.getAppPwd());
		mtMessage.setData("Thanks you");
		mtMessage.setOp(opName);
		mtMessage.setRtxId("");
		mtMessage.setService(selectedIndonesiaChargingConfig.getService());
		mtMessage.setAlphabetd("0");
		
		mtMessage.setServiceId(subscriberReg.getServiceId());
		//mtMessage.setMoId(tmpMtMessage.getId());				
		mtMessage.setSubscriberCurrentState(subscriberReg.getParam1());
		indonesiaSmsService.sendMTMessage(mtMessage);
		daoService.saveObject(mtMessage);
		//logger.info("handleSubscriptionMTRenewalPushMessage:::::::::: "+mtMessage);
		
		return true;
	}
}
