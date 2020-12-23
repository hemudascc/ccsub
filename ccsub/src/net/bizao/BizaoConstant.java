package net.bizao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletInputStream;

import net.persist.bean.SubscriberReg;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;

public interface BizaoConstant {

	 static final Logger logger = Logger
			.getLogger(BizaoConstant.class.getName());
	
	// public static  String SENDER_NAME="GAMEPAD";
	 
	 public static  String BIZAO_PAYMENT_TRX="BIZAO_PAYMENT_TRX";
	 
	 public static  String STOP="stop";
	 public static final String DAILY="daily";
	 
	 public static final String REQUEST_MODE_USSD="USSD";
	 public static final String REQUEST_MODE_WAP="WAP";
	 public static final String RENEW_ALERT="RENEW_ALERT";
	 
	public static List<Integer> dailyNoOfDaysPattern=new ArrayList<Integer>(); 
	public static List<Integer> weeklyNoOfDaysPattern=new ArrayList<Integer>();
	public static List<Integer> monthlyNoOfDaysPattern=new ArrayList<Integer>();
	
	public static List<BizaoConfig> listBizaoConfig=new ArrayList<BizaoConfig>();
	
	public static final Map<Integer,BizaoConfig> mapIdToBizaoConfig=new HashMap<Integer,BizaoConfig>();
	public static final Map<Integer,BizaoConfig> mapServiceIdToBizaoConfig=new HashMap<Integer,BizaoConfig>();
	//public static final Map<Integer,BizaoConfig> mapBizaoOperatorIdToBizaoConfig=new HashMap<Integer,BizaoConfig>();
	public static final Map<String,BizaoConfig> mapBizaoMsisdnPrefixServiceTypeToBizaoConfig=new HashMap<String,BizaoConfig>();
	//public static final Map<String,BizaoConfig> mapKeywordToBizaoConfig=new HashMap<String,BizaoConfig>();
	
	public static final List<Integer> operatorIds=new ArrayList<Integer>(){{
	add(MConstants.ORANGE_CAMERON_OPERATOR_ID);
	add(MConstants.ORANGE_IV_OPERATOR_ID);
	add(MConstants.ORANGE_RDCONGO_OPERATOR_ID);
	add(MConstants.ORANGE_SENEGAL_OPERATOR_ID);
	//add(MConstants.ORANGE_TUNISIA_OPERATOR_ID);
	}};
	
	public static final Map<Integer,BizaoConfig> mapUssdServiceIdToBizaoConfig=new HashMap<Integer,BizaoConfig>();
	
	
	public static final List<BizaoIpPool> listIPPool=new ArrayList<BizaoIpPool>();
	public static final List<String> listMsisdnPrefix=new ArrayList<String>();
	public static final String BIZAO_CHALENGE_ID_CACHE_PREFIX="BIZAO_CHALENGE_ID_CACHE_PREFIX";
	public static AtomicInteger bizaoPaymentIdAtomicInteger=new AtomicInteger();
	public static AtomicInteger bizaoSmsAtomicInteger=new AtomicInteger();
	
	public static BizaoIpPool findBizaoOperatorByIp(String ip) {
		BizaoIpPool ippool=null;
		try{
		 ippool=  BizaoConstant.listIPPool.stream().filter(p->p!=null&&
				p.getSubnetUtils()!=null&&p.getSubnetUtils().getInfo().
     			isInRange(ip)).findFirst().orElse(null);	
		}catch(Exception ex){
			logger.error("findBizaoOperatorByIp:: ",ex);
		}
		return ippool;		
	}
	
	public static String extractData(ServletInputStream sis){
		StringBuilder sb = new StringBuilder();
		try {
			
	        java.io.BufferedReader bis = new java.io.BufferedReader(new java.io.InputStreamReader(sis));
	        String line = "";
	        while ((line = bis.readLine()) != null) {
	            sb.append(line);
	        }
	        bis.close();
	    } catch (Exception e) {
	    	logger.error("extractData:: exception::::: ",e);
	       
	    }finally{
	    	if(sis!=null){
	    		try {
					sis.close();
				} catch (IOException e) {
					logger.error("extractData::finnally:: exception:::::",e);
				}
	    	}
	    }
		return sb.toString();
	}
	
	
	public static String getPortalUrl(String templateUrl,Integer subId,String msisdn){
		try{
		return templateUrl.replaceAll("<subid>",String.valueOf(subId))+"&msisdn="+msisdn;
		}catch(Exception ex){
			
		}
		return templateUrl;
	}
	
	public static String getMsg(String msg,BizaoConfig bizaoConfig){
		if(msg!=null&&bizaoConfig!=null){
		return msg.replaceAll("<countryname>",bizaoConfig.getCountryName())
				.replaceAll("<servicename>", bizaoConfig.getServiceName())
				.replaceAll("<keyword>", Objects.toString(bizaoConfig.getKeyword()))
				.replaceAll("<shortcode>", Objects.toString(bizaoConfig.getShortCode()))
				.replaceAll("<campaignurl>",Objects.toString(bizaoConfig.getCampaignUrl()));
		}
		return msg;
	}
	
	public static String getMsg(String msgTemplate,BizaoConfig bizaoConfig,
			Double amount,Integer subId){
		
		try{
		if(msgTemplate==null){
			return null;
		}
		
		return msgTemplate.replaceAll("<currency>", Objects.toString(bizaoConfig.getCurrencyDesc()))
		 .replaceAll("<servicename>", Objects.toString(bizaoConfig.getServiceName()))
		 .replaceAll("<amount>", Objects.toString(amount))
		 .replaceAll("<keyword>", Objects.toString(bizaoConfig.getKeyword()))
		 .replaceAll("<amountdesc>", Objects.toString(bizaoConfig.getPricePointDesc()))
		 .replaceAll("<validity>", Objects.toString(bizaoConfig.getValidity()))
		 .replaceAll("<shortcode>", Objects.toString(bizaoConfig.getShortCode()))
		 .replaceAll("<moshortcode>", Objects.toString(bizaoConfig.getMoShortCode()))
         .replaceAll("<portalurl>",Objects.toString(bizaoConfig.getPortalUrl()))
         .replaceAll("<subid>",Objects.toString(subId))
         .replaceAll("<campaignurl>",Objects.toString(bizaoConfig.getCampaignUrl()));
		
		}catch(Exception ex){
			logger.error("getMsg::: ",ex);
		}
		return msgTemplate;
	}
	
	public static boolean isEligibleToRenewToday(SubscriberReg subscriberReg,BizaoConfig bizaoConfig){
		
		if(subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
			return false;
		}
		Timestamp ts=new Timestamp(System.currentTimeMillis());
		int daysDiff=MUtility.getDayDiffrence(subscriberReg.getValidityTo(),ts);
		logger.info("isEligibleToRenewToday:: daysDiff:: "+daysDiff);
		boolean eligibleToRenew=false;
		switch (bizaoConfig.getValidity().intValue()){
		case 1:{
			logger.info("isEligibleToRenewToday:: dailyNoOfDaysPattern:: "+dailyNoOfDaysPattern);
			eligibleToRenew=true;// dailyNoOfDaysPattern.contains(daysDiff);
			break;
		}
		case 7:{
				logger.info("isEligibleToRenewToday:: weeklyNoOfDaysPattern:: "+weeklyNoOfDaysPattern);
				eligibleToRenew= weeklyNoOfDaysPattern.contains(daysDiff);
				break;
			}
			case 30:{
				logger.info("isEligibleToRenewToday:: monthlyNoOfDaysPattern:: "+monthlyNoOfDaysPattern);
				eligibleToRenew= monthlyNoOfDaysPattern.contains(daysDiff);
				break;
			}
		default:{
			eligibleToRenew=false;
		}	
		}
		return eligibleToRenew;
	}
	
 }
