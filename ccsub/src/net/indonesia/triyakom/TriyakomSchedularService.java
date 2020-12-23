package net.indonesia.triyakom;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

public class TriyakomSchedularService {


	private static final Logger logger = Logger
			.getLogger(TriyakomSchedularService.class);
	
	@Autowired
	private IDaoService daoService;
	@Autowired
	private JMSIndonesiaService jmsIndonesiaService;
	@Autowired
	private TriyakomOperatorService triyakomOperatorService;
	
//	@Autowired
//	private JPASubscriberReg JPASubscriberReg;
	
	

	// @Scheduled(cron="0 35 17 * * ?")
//	public void xlRenewalCharging() {
//
//		List<SubscriberReg> list = daoService
//				.findSubscriberDetailsByStatusAndValidityAndOpId(100, "");
//
//		logger.debug("xlRenewalCharging:::" + list);
//
//		for (SubscriberReg reg : list) {
//
//			String pattern = "yyyy-MM-dd";
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			Date date1 = null;
//			try {
//
//				date1 = simpleDateFormat.parse(reg.getValidityTo().toString());
//				Date now = new Date();
//				Date today = null;
//				today = simpleDateFormat.parse(simpleDateFormat.format(now));
//
//				if (date1.compareTo(today) < 0 || date1.compareTo(today) == 0) {
//
//					MTMessage mtMessage = null;
//					if (reg.getParam1() != null
//							&& reg.getCurrentActiveType().equalsIgnoreCase(
//									"RENEW")) {
//						mtMessage = daoService.findMTMessageById(Integer
//								.parseInt(reg.getParam1()));
//						triyakomOperatorService
//								.handleSubscriptionMTRenewalPushMessage(mtMessage);
//					}
//				}
//			} catch (Exception ee) {
//				logger.error("xlRenewalCharging::exception ee:" + ee);
//			}
//		}
//	}
//
//	@Scheduled(cron = "0 24 17 * * ?")
//	public void indoSatRenewalCharging() {
//
//		// System.out.println("Method executed at every 60 seconds. Current time is :: "+
//		// new Date());
//		List<SubscriberReg> list = daoService
//				.findSubscriberDetailsByStatusAndValidityAndOpId(102, "");
//
//		logger.debug("indoSatRenewalCharging::" + list);
//
//		for (SubscriberReg reg : list) {
//			
//			String pattern = "yyyy-MM-dd";
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			Date date1 = null;
//			Date today = null;
//			
//			try {
//				date1 = simpleDateFormat.parse(reg.getValidityTo().toString());
//				Date now = new Date();
//				today = simpleDateFormat.parse(simpleDateFormat.format(now));
//				
//				if (date1.compareTo(today) < 0 || date1.compareTo(today) == 0) {
//					MTMessage mtMessage = null;
//					if (reg.getParam1() != null
//							&& reg.getCurrentActiveType().equalsIgnoreCase(
//									"RENEW")) {
//						mtMessage = daoService.findMTMessageById(Integer
//								.parseInt(reg.getParam1()));
//						// logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
//						triyakomOperatorService
//								.handleSubscriptionMTRenewalPushMessage(mtMessage);
//					}
//				}
//			} catch (Exception ee) {
//				logger.error("Indosat RenewalCharging:::" + ee);
//			}
//		}
//
//	}
//
//	@Scheduled(cron = "0 20 8  * * ?")
//	public void indoSatActCharging() {
//
//	
//		List<SubscriberReg> list = daoService
//				.findSubscriberDetailsByStatusAndValidityAndOpId(MConstants.TRIAKOM_INDONESIA_SAT_OPERATOR_ID, "");
//	
//		for (SubscriberReg reg : list) {
//			try {
//				String pattern = "yyyy-MM-dd";
//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//						pattern);
//				Date date1 = null;
//
//				date1 = simpleDateFormat.parse(reg.getValidityTo().toString());
//
//				Date now = new Date();
//				Date today = null;
//
//				today = simpleDateFormat.parse(simpleDateFormat.format(now));
//
//				if (date1.compareTo(today) < 0 || date1.compareTo(today) == 0) {
//
//					MTMessage mtMessage = null;
//					if (reg.getParam1() != null
//							&& reg.getCurrentActiveType().equalsIgnoreCase(
//									"ACT")) {
//						mtMessage = daoService.findMTMessageById(Integer
//								.parseInt(reg.getParam1()));
//						// logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
//						jmsIndonesiaService.saveMTMessage(mtMessage);
//					}
//				}
//			}
//
//			catch (Exception ee) {
//				logger.error("exception ee:" + ee);
//			}
//
//		}
//
//	}

}
