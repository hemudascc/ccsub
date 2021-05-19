package net.indonesia.triyakom;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.jpa.repository.JPAIndonesiaMOMessage;
import net.jpa.repository.JPAIndonesiaMTMessage;
import net.persist.bean.LiveReport;

import net.process.bean.CGToken;
import net.util.MConstants;

public class JMSDLRNotification implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSDLRNotification.class);

	@Autowired
	private IDaoService daoService;
	
	
	@Autowired
	private JPAIndonesiaMTMessage jpaIndonesiaMTMessage;
	
	@Autowired
	private JPAIndonesiaMOMessage jpaIndonesiaMOMessage;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	

	@Override
	public void onMessage(Message m) {

		//logger.debug("onMessage::::::::::::::::: " + m);
		ObjectMessage objectMessage = (ObjectMessage) m;
		DLRNotification dlrNotification=null;
		LiveReport liveReport=null;
		try {
			 dlrNotification=(DLRNotification)objectMessage.getObject();
			 logger.error("dlrNotification:::"+dlrNotification);
			 
			MTMessage mtMessage=jpaIndonesiaMTMessage.findIndonesiaSmsTransByTXId
					(dlrNotification.getTid());
			
			if(mtMessage!=null){
				
				
				IndonesiaChargingConfig indonesiaChargingConfig=
						TriyakomConstant.listIndonesiaChargingConfig.stream().
						filter(a->a.getId().intValue()==mtMessage.getIndonesiaChargingConfigId()).findFirst().get();
				
				TriyakomConfig triyakomConfig=TriyakomConstant.listTriyakomConfig.stream().
				filter(a->a.getOp().equalsIgnoreCase(mtMessage.getOp())).findFirst().get();
				CGToken cgToken=new CGToken(mtMessage.getToken());
				
				 liveReport=new LiveReport(OperatorIdNameEnum.getOpertorId(mtMessage.getOp())
						 ,new Timestamp(System.currentTimeMillis()),
				 cgToken.getCampaignId(),triyakomConfig.getServiceId(),triyakomConfig.getProductId());
			
				liveReport.setMsisdn(mtMessage.getDestAddr());
				liveReport.setType(String.valueOf(triyakomConfig.getServiceId()));
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setTokenId(cgToken.getTokenId());
				
				if(dlrNotification!=null&&
						dlrNotification.getStatusId()!=null&&
								(dlrNotification.getStatusId().
								equalsIgnoreCase(TriyakomConstant.DLR_DELIVERED))&&
										mtMessage.getAction()
										.equalsIgnoreCase(MConstants.ACT)
								){
				  liveReport.setAction(MConstants.ACT);
				  liveReport.setAmount((double)indonesiaChargingConfig.getCharge());
				  liveReport.setNoOfDays(indonesiaChargingConfig.getValidity());
				  liveReport.setLcId(mtMessage.getId().toString());
				}else if(dlrNotification!=null&&
						dlrNotification.getStatusId()!=null&&
						(dlrNotification.getStatusId().
						equalsIgnoreCase(TriyakomConstant.DLR_DELIVERED))&&
								mtMessage.getAction()
								.equalsIgnoreCase(MConstants.PARK_TO_ACT)
						){
		  liveReport.setAction(MConstants.RENEW);
		  liveReport.setAmount((double)indonesiaChargingConfig.getCharge());
		  liveReport.setParkingToActivationCount(1);
		  liveReport.setParkToActivationAmount((double)indonesiaChargingConfig.getCharge());
		  liveReport.setNoOfDays(indonesiaChargingConfig.getValidity());
		  liveReport.setResponse(dlrNotification.toString());
		}else if(dlrNotification!=null&&
						dlrNotification.getStatusId()!=null&&
									dlrNotification.getStatusId().
								equalsIgnoreCase(TriyakomConstant.DLR_INSUFFICIENT)&&
										mtMessage.getAction()
										.equalsIgnoreCase(MConstants.ACT)){
					  liveReport.setAction(MConstants.GRACE);
					  liveReport.setAmount(0.0);
					  liveReport.setNoOfDays(1);
					  liveReport.setLcId(mtMessage.getId().toString());
					  liveReport.setParam1(MConstants.GRACE);
				}else if(mtMessage.getAction()
						.equalsIgnoreCase(MConstants.DCT)){
					 liveReport.setAction(MConstants.DCT);
					 liveReport.setDctCount(1);
				}else if(dlrNotification!=null&&
						dlrNotification.getStatusId()!=null&&
						(dlrNotification.getStatusId().
						equalsIgnoreCase(TriyakomConstant.DLR_DELIVERED))&&
						mtMessage.getAction().equalsIgnoreCase(MConstants.RENEW)){				
					 liveReport.setAction(MConstants.RENEW);
					 liveReport.setNoOfDays(indonesiaChargingConfig.getValidity());
					 liveReport.setRenewalAmount((double)indonesiaChargingConfig.getCharge());
					 liveReport.setRenewalCount(1);
				}
			}
		
		} catch (Exception e) {
			logger.error("onMessage::::::::mainException  " , e);
		} finally {
			try{
				if(liveReport!=null&&liveReport.getAction()!=null){
					  liveReportFactoryService.process(liveReport);
				}
			}catch (Exception e) {
				logger.error("finally:: onMessage::::::::  " ,e);
			}
			daoService.saveObject(dlrNotification);
		}
	}
}
