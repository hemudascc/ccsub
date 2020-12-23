package net.mycomp.mcarokiosk.malaysia;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.common.service.BlockSeriesRedisCacheService;
import net.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("malaysiaSmsService")
public class MalaysiaSmsService {

	private static final Logger logger = Logger.getLogger(MalaysiaSmsService.class);

	@Autowired
	private BlockSeriesRedisCacheService  blockSeriesRedisCacheService;
	
	private HttpURLConnectionUtil httpURLConnectionUtil; 
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	
	public HTTPResponse sendMOSMS(String url,MalasiyaMOMessage malasiyaMOMessage){
		//http://mis.etracker.cc/THwap/WAPMORequest.aspx?
		//Telcoid=<telcoid>&Shortcode=<shortcode>&Keyword=<keyword>&refid=<refid>
		try{
		 url=url.replaceAll("<telcoid>",URLEncoder.encode(String.valueOf(malasiyaMOMessage.getTelcoid()),"utf-8"))
				.replaceAll("<shortcode>", URLEncoder.encode(malasiyaMOMessage.getShortcode(),"utf-8"))
				.replaceAll("<keyword>", URLEncoder.encode(malasiyaMOMessage.getKeyword(),"utf-8"))
				.replaceAll("<refid>", URLEncoder.encode(malasiyaMOMessage.getRefId(),"utf-8"));
		return  httpURLConnectionUtil.sendGet(url);
		}catch(Exception ex){
			logger.error("sendMOSMS:: ",ex);
		}
		return null;
	}
	
	
	
	
	public HTTPResponse sendMTSMS(String url,MalasiyaMTMessage malasiyaMTMessage){
		//User=<user>&Pass=<pass>&Type=<type>&To=<to>&From=<from>&Text=<text>
		//&Price=<price>&Telcoid=<telcoid>&Cat=<cat>&Keyword=<keyword>&Senderid=<senderid>&Linkid=<linkid>
		try{
//		       Map<String ,String> map=new HashMap<String,String>();		
//     			map.put("user",malasiyaMTMessage.getUser());
//		        map.put("pass", malasiyaMTMessage.getPass());
//				map.put("type", malasiyaMTMessage.getType());
//				map.put("to",malasiyaMTMessage.getMsisdn());
//				map.put("from", malasiyaMTMessage.getFromStr());
//				map.put("text",malasiyaMTMessage.getTextMsg());
//				map.put("price",String.valueOf(malasiyaMTMessage.getPrice().intValue()));
//				map.put("telcoid",String.valueOf(malasiyaMTMessage.getTelcoId()));
//				map.put("cat",String.valueOf(malasiyaMTMessage.getCat()));
//				map.put("keyword", malasiyaMTMessage.getKeyword());
//			    //Uncoment
//				map.put("senderid", malasiyaMTMessage.getSenderid());
//				map.put("linkid",malasiyaMTMessage.getLinkId());
			
			if(blockSeriesRedisCacheService.isBlockSeries(Arrays.asList(malasiyaMTMessage.getMsisdn()))){
				malasiyaMTMessage.setRequestUrl("Numberin block list..so not sent any message");
				return null;
			}
		
		url=url+"?senderid="+MUtility.urlEncoding(malasiyaMTMessage.getSenderid())
				//+"&linkid="+MUtility.urlEncoding(malasiyaMTMessage.getLinkId())
				+"&pass="+MUtility.urlEncoding(malasiyaMTMessage.getPass())
				+"&price="+MUtility.urlEncoding(""+malasiyaMTMessage.getPrice().intValue())
				//+"&cat="+MUtility.urlEncoding(""+malasiyaMTMessage.getCat())
				+"&from="+MUtility.urlEncoding(malasiyaMTMessage.getFromStr())
				+"&to="+MUtility.urlEncoding(malasiyaMTMessage.getMsisdn())
				+"&text="+MUtility.urlEncoding(malasiyaMTMessage.getTextMsg())
				+"&type="+MUtility.urlEncoding(malasiyaMTMessage.getType())
				+"&keyword="+MUtility.urlEncoding(malasiyaMTMessage.getKeyword())
				+"&user="+MUtility.urlEncoding(malasiyaMTMessage.getUser())
				+"&telcoid="+MUtility.urlEncoding(""+malasiyaMTMessage.getTelcoId())
				//+"&moid="+MUtility.urlEncoding(""+malasiyaMTMessage.getMoMessageIdStr())
				+"&charge="+MUtility.urlEncoding(""+malasiyaMTMessage.getCharge())
				;
				
		logger.info("sendMTSMS::::calling url:: "+url);
		//HTTPResponse response=httpURLConnectionUtil.sendPostRequest(url, map); 
		HTTPResponse response=httpURLConnectionUtil.sendGet(url); 
		malasiyaMTMessage.setRequestUrl(url);
	    logger.info("sendMTSMS::::calling url::response: "+url+", response: "+response);
		 malasiyaMTMessage.setResponse(response.getResponseCode()+":" +response.getResponseStr());
		 if(response.getResponseStr()!=null){
			 //{mobileno}, {msgid}, {status}
			// malasiyaMTMessage.setResponse(response);
			 String arr[]=response.getResponseStr().split(",");
			 if(arr.length==3&&arr[2].equalsIgnoreCase("200")){
			   malasiyaMTMessage.setMsgId(arr[1]);
			 }
		 }		  
		}catch(Exception ex){
			logger.error("sendMTSMS:: ",ex);
		}
		return null;
	}

}
