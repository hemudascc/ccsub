package net.mycomp.mobipay;

import java.sql.Timestamp;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.DaoServiceImpl;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;

@Service("mobiPayApiService")
public class MobiPayApiService {
	
	private static final Logger logger = Logger.getLogger(MobiPayApiService.class);
	@Autowired
	private DaoServiceImpl daoService;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public boolean sendSms(MobiPayCGCallback mobiPayCGCallback, MobiPayServiceConfig mobiPayServiceConfig) {
		MobiPayTrans mobiPayTrans = new MobiPayTrans(Boolean.TRUE);
		//https://gateway.mobivate.com/srs/api/sendsms?
		//USER_NAME=2a74245f27da4377b30e1d7954026cd8&
		//PASSWORD=08a3958640f64a7db4e1a12f4bcb367b&ORIGINATOR=30053&
		//RECIPIENT=<msisdn>&PROVIDER=DEFAULT&MESSAGE_TEXT=<message>&
		//VALUE=<value>&KEYWORD=v_glamworld.1000.1.GlamourWorld&STARTED=<date>
		try {
			mobiPayTrans.setMsisdn(mobiPayCGCallback.getUserId());
			String requestURL = mobiPayServiceConfig.getSmsURL()
			.replaceAll("<msisdn>", mobiPayCGCallback.getUserId())
			.replaceAll("<message>", mobiPayServiceConfig.getWelcomeMessage()
					.replaceAll("<portalurl>", mobiPayCGCallback.getPortalURL()))
			.replaceAll("<date>", Objects.toString(new Timestamp(System.currentTimeMillis())));
			mobiPayTrans.setRequest(requestURL);
			HTTPResponse  httpResponse = httpURLConnectionUtil.makeHTTPGETRequest(requestURL, null);
			mobiPayTrans.setResponse(httpResponse.getResponseStr());
		} catch (Exception e) {
			logger.error("error while sending message"+mobiPayTrans, e);
		}finally {
			daoService.saveObject(mobiPayTrans);
		}
		return false;	
	}
	
}
