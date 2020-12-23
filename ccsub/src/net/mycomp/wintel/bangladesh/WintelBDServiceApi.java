package net.mycomp.wintel.bangladesh;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.mycomp.veoo.VeooController;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wintelBDServiceApi")
public class WintelBDServiceApi {

	private static final Logger logger = Logger
			.getLogger(WintelBDServiceApi.class.getName());
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public WintelBDApiTrans sendSMS(String msg,String transId
			,WintelBDServiceConfig wintelBDServiceConfig,String msisdn,String action){
		//http://xxx.xxx.xxx.xxx?clientid=<clientid>&Keyword=<Keyword>&unique_id=<uniqueid>&amount=<am
		//ount>
		WintelBDApiTrans wintelBDApiTrans=null;
		try{
			 wintelBDApiTrans=new WintelBDApiTrans(true,action);
			wintelBDApiTrans.setMsisdn(msisdn);
//			Boolean charge=(Boolean)redisCacheService.getObjectCacheValue(WintelBDConstant.WINTEL_BD_BILLING_PREFIX
//					+msisdn);
//			if(charge!=null&&charge){
//				wintelBDApiTrans.setRequest("Blocked");				
//				return wintelBDApiTrans;
//			}
			
			String url=wintelBDServiceConfig.getMtUrl()
					.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
					.replaceAll("<clientid>", MUtility.urlEncoding(wintelBDServiceConfig.getClientId()))
					.replaceAll("<keyword>", MUtility.urlEncoding(wintelBDServiceConfig.getKeyword()))
					.replaceAll("<shortcode>", MUtility.urlEncoding(wintelBDServiceConfig.getShortCode()))
					.replaceAll("<transid>", MUtility.urlEncoding(transId))
					.replaceAll("<msg>",MUtility.urlEncoding( msg))
					.replaceAll("<serviceid>", MUtility.urlEncoding(wintelBDServiceConfig.getBdServiceId()))
					.replaceAll("<opt>", MUtility.urlEncoding(wintelBDServiceConfig.getOperator()))
					;
			
			wintelBDApiTrans.setRequest(url);
			HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url, null);
			wintelBDApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
	        if(httpResponse.getResponseCode()==200&&httpResponse.getResponseStr().equalsIgnoreCase("OK")){
	        	wintelBDApiTrans.setSuccess(true);
//	        	redisCacheService.putObjectCacheValueByEvictionDay(WintelBDConstant.WINTEL_BD_BILLING_PREFIX+msisdn
//	        			, true, wintelBDServiceConfig.getValidity());
	        	
	        }
	        
		}catch(Exception ex){
			logger.error("Exception  ",ex);
		}finally{
			daoService.saveObject(wintelBDApiTrans);
		}
		return wintelBDApiTrans;
	}
}
