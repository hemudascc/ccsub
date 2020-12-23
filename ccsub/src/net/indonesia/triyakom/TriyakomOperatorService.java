package net.indonesia.triyakom;

import net.persist.bean.SubscriberReg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("triyakomOperatorService")
public class TriyakomOperatorService implements IOpeartorService{

	
	@Autowired
	private XLOperatorService xlOperatorService;
	
	@Autowired
	private SmaftrenOperatorService smaftrenOperatorService;
	
	@Autowired
	private HT1OperatorService ht1OperatorService;
	
	public IOpeartorService findOperatorService(String operatorName){
		IOpeartorService opeartorService=null;
		switch(operatorName){
		case TriyakomConstant.XL_TRIYAKON_OPERATOR_NAME:{
			opeartorService=xlOperatorService;
			break;
		}
		case TriyakomConstant.SMAFTREN_TRIYAKON_OPERATOR_NAME:{
			opeartorService=smaftrenOperatorService;
			break;
		}
		case TriyakomConstant.TELKOMSEL_TRIYAKON_OPERATOR_NAME:{
			break;
		}	
		case TriyakomConstant.HT1_TRIYAKON_OPERATOR_NAME:{
			opeartorService=ht1OperatorService;
			break;
		}	
		default :{
			opeartorService=xlOperatorService;		
			break;
		}	
		}
		return opeartorService;		
	}
	
	
	@Override
	public boolean handleSubscriptionMoMessage(MOMessage moMessage) {		
		return findOperatorService(moMessage.getOp()).handleSubscriptionMoMessage(moMessage);
	}


	@Override
	public boolean handleSubscriptionMTPushMessage(MTMessage mtMessage) {
		return findOperatorService(mtMessage.getOp()).handleSubscriptionMTPushMessage(mtMessage);
	}


//	@Override
//	public boolean handleSubscriptionMTRenewalPushMessage(MTMessage mtMessage) {
//		return findOperatorService(mtMessage.getOp()).handleSubscriptionMTRenewalPushMessage(mtMessage);
//	}


	@Override
	public boolean handleSubscriptionMTRenewalPushMessage(SubscriberReg subscriberReg) {
		OperatorIdNameEnum operatorIdNameEnum=OperatorIdNameEnum.getOpertorName(subscriberReg.getOperatorId());		
		return findOperatorService(operatorIdNameEnum.getOpName()).handleSubscriptionMTRenewalPushMessage(subscriberReg);
	}


	@Override
	public boolean handleSubscriptionMTPushMessage(SubscriberReg subscriberReg) {
		OperatorIdNameEnum operatorIdNameEnum=OperatorIdNameEnum.getOpertorName(subscriberReg.getOperatorId());
		return findOperatorService(operatorIdNameEnum.getOpName()).handleSubscriptionMTPushMessage(subscriberReg);
		}
}
