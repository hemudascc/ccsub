package net.mycomp.mcarokiosk.malaysia;

import javax.annotation.PostConstruct;

import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("mkmalaysiaServiceApi")
public class MKMalaysiaServiceApi {


	private static final Logger logger = Logger
			.getLogger(MKMalaysiaServiceApi.class.getName());
	
	@Value("${macrokiosk.malaysia.access.token}")
	private String accessToken;
	
	@Value("${macrokiosk.malaysia.access.token.url}")
	private String accessTokenUrl;
	
	private HttpURLConnectionUtil  httpURLConnectionUtil;
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
public String getToken(MKMalaysiaConfig mkmalaysiaConfig,String token){
		
		try{
			
//		String dateTime=MKMalaysiaConstant.yyyyMMddHHmmssAccessToken.
//		format(MKMalaysiaConstant.getFormatUTC8Date());
		String dateTime=MKMalaysiaConstant.getFormatUTC8TokenTime();
		
//		String accessToken=ThiaConstant.md5(mkmalaysiaConfig.getUser().toUpperCase()
//				+mkmalaysiaConfig.getKeyword().toUpperCase()+mkmalaysiaConfig.getShortcode()
//				+dateTime+ThiaConstant.md5(mkmalaysiaConfig.getPassword()).toUpperCase());
		

		String accessToken=MKMalaysiaConstant.md5(mkmalaysiaConfig.getUser().toUpperCase()
				+mkmalaysiaConfig.getKeyword().toUpperCase()
				+mkmalaysiaConfig.getShortcode().toUpperCase()
				+dateTime
				+MKMalaysiaConstant.md5(mkmalaysiaConfig.getPassword()).toUpperCase());
		
		String url=accessTokenUrl
		.replaceAll("<accesstoken>",MUtility.urlEncoding(accessToken))
		.replaceAll("<keyword>",MUtility.urlEncoding(mkmalaysiaConfig.getKeyword()))
		.replaceAll("<shortcode>",MUtility.urlEncoding(mkmalaysiaConfig.getShortcode()))
		.replaceAll("<datetime>",MUtility.urlEncoding(dateTime));
		
		
		HTTPResponse  httpResponse=httpURLConnectionUtil.invokeGetURL(url);
		logger.info("getToken::: token:: "+token+",user: "+mkmalaysiaConfig.getUser().toUpperCase()
				+", password:: "+mkmalaysiaConfig.getPassword().toUpperCase()+", dateTime:: "+dateTime
				+" ,url::::::::: "+url+" , httpResponse: "+httpResponse);
		
		if(httpResponse.getResponseCode()==200){
			return httpResponse.getResponseStr().split(",")[0];
		}
	}catch(Exception ex){
		logger.error("Exception:: ",ex);
	}
		return null;
	}
	
}
