package net.indonesia.triyakom;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;

public class JMSMOListener implements MessageListener {

	private static final Logger logger = Logger.getLogger(JMSMOListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private TriyakomOperatorService triyakomOperatorService;

	@Override
	public void onMessage(Message m) {
		
		long time = System.currentTimeMillis();
		//logger.info("onMessage::::::::::::::::: " + m);
		ObjectMessage objectMessage = (ObjectMessage) m;
		MOMessage moMessage=null;
		try {
			moMessage=(MOMessage)objectMessage.getObject();
			
//			IndonesiaChargingConfig selectedIndonesiaChargingConfig=null;
//			for(IndonesiaChargingConfig indonesiaChargingConfig:TriyakomConstant.listIndonesiaChargingConfig){
//				if(moMessage.getOp().equalsIgnoreCase(indonesiaChargingConfig.getOperator())&&
//						indonesiaChargingConfig.getType().equalsIgnoreCase(TriyakomConstant.PUSH)){
//					selectedIndonesiaChargingConfig=indonesiaChargingConfig;
//					break;
//				}
//			}
//			
//			MTMessage mtMessage=new MTMessage();
//			mtMessage.setDestAddr(moMessage.getMsisdn());
//			mtMessage.setAppId(selectedIndonesiaChargingConfig.getAppId());
//			mtMessage.setAppPwd(selectedIndonesiaChargingConfig.getAppPwd());
//			mtMessage.setData("Thanks you");
//			mtMessage.setOp(selectedIndonesiaChargingConfig.getOperator());
//			mtMessage.setRtxId(moMessage.getTxId());
//			mtMessage.setService(selectedIndonesiaChargingConfig.getService());
//			mtMessage.setAlphabetd("0");
//			indonesiaSmsService.sendMTMessage(mtMessage);
//			daoService.saveObject(mtMessage);
//			logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
			
			
			
			triyakomOperatorService.handleSubscriptionMoMessage(moMessage);	
			
		} catch (Exception e) {
			logger.error("onMessage::::::::::::::::: Exception  " + e);
		}finally{
			daoService.saveObject(moMessage);
		} 	
		//logger.info("onMessage::::::::::::::::: :: total time:: " + (System.currentTimeMillis() - time));
	}	
}



