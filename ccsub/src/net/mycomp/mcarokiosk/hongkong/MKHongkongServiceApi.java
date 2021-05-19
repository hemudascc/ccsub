package net.mycomp.mcarokiosk.hongkong;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

@Service("mkhongkongServiceApi")
public class MKHongkongServiceApi {

	private static final Logger logger = Logger
			.getLogger(MKHongkongServiceApi.class.getName());
	
	@Value("${macrokiosk.hongkong.access.token}")
	private String accessToken;
	
	@Value("${macrokiosk.hongkong.access.token.url}")
	private String accessTokenUrl;
	
	private HttpURLConnectionUtil  httpURLConnectionUtil;
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
public String getToken(MKHongkongConfig mkhongkongConfig,String token){
		
		try{
			
		String dateTime=MKHongkongConstant.getFormatUTC8TokenTime();
		String accessToken=MKHongkongConstant.md5(mkhongkongConfig.getUser().toUpperCase()
				+mkhongkongConfig.getKeyword().toUpperCase()
				+mkhongkongConfig.getShortcode().toUpperCase()
				+dateTime
				+MKHongkongConstant.md5(mkhongkongConfig.getPassword()).toUpperCase());
		
		String url=accessTokenUrl
		.replaceAll("<accesstoken>",MUtility.urlEncoding(accessToken))
		.replaceAll("<keyword>",MUtility.urlEncoding(mkhongkongConfig.getKeyword()))
		.replaceAll("<shortcode>",MUtility.urlEncoding(mkhongkongConfig.getShortcode()))
		.replaceAll("<datetime>",MUtility.urlEncoding(dateTime));
		
		
		HTTPResponse  httpResponse=httpURLConnectionUtil.invokeGetURL(url);
		logger.info("getToken::: token:: "+token+",user: "+mkhongkongConfig.getUser().toUpperCase()
				+", password:: "+mkhongkongConfig.getPassword().toUpperCase()+", dateTime:: "+dateTime
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
