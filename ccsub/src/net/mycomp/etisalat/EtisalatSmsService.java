package net.mycomp.etisalat;

import java.util.HashMap;

import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("etisalatSmsService")
public class EtisalatSmsService {

	private static final Logger logger = Logger
			.getLogger(EtisalatSmsService.class);
	
	private final String smsPushUrl;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	public EtisalatSmsService(@Value("${etisalat.sms.push.url}")String smsPushUrl){
		this.smsPushUrl=smsPushUrl;
	    httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public HTTPResponse sendSubscriptionMTPush(EtisalatChargingCallback etisalatChargingCallback,String msisdn,String senderId,String msg){
		//https://pt1.etisalat.ae/eticms/ContentPush_Test?login=<Username>&
		//pwd=<Password>&ptype=text&senderid=<senderid>&msisdn=<msisdn>&msg=<msg>
		 logger.info("sendSubscriptionMTPush::::: "+msisdn+" , msg: "+msg+" ,smsPushUrl Template:: "+smsPushUrl
				 );
		 String url=smsPushUrl.replaceAll("<msisdn>", msisdn)
				 .replaceAll("<msg>",MUtility.urlEncoding(msg))
				 .replaceAll("<senderid>",MUtility.urlEncoding(senderId));
		 etisalatChargingCallback.setSmsRequest(url);
		 HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpsGet(url);
		 etisalatChargingCallback.setSmsResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		 logger.info("sendSubscriptionMTPush::::: "+msisdn+" , msg: "+msg+" ,smsPushUrl:: "+smsPushUrl
				 +",  response:: "+httpResponse);
		 return httpResponse;
	}
	
}
