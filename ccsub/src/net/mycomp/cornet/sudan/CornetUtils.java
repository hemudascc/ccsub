package net.mycomp.cornet.sudan;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import net.common.service.RedisCacheService;
import net.process.bean.CGToken;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

public class CornetUtils {
	@Autowired
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	private static final Logger logger = Logger.getLogger(CornetService.class);
	
	public  String GenerateToken(String username,String password) {
		String token =  null;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		try {    
		Map<String,String> headerMap=new HashMap<String,String>();  
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "application/json");

		Map<String, String> requestMap = new HashMap<>();
		requestMap.put("username", username);
		requestMap.put("password", password);
		requestMap.put("remember_me", ""+CornetConstant.REMEMBER_ME);
		
		String request = JsonMapper.getObjectToJson(requestMap);

		logger.info("sending Cornet Login Reuest:  "+CornetConstant.LOGIN_API_URL+" : "+request);

		HTTPResponse  httpResponse = httpURLConnectionUtil.makeHTTPPOSTRequest(CornetConstant.LOGIN_API_URL, request,headerMap);
		Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		boolean status = (boolean)map.get("success");
		if(status) {      
		 token = Objects.toString(map.get("token"));
		}  
		logger.info("Cornet Login Response:  "+JsonMapper.getObjectToJson(httpResponse.getResponseStr()));
		}catch(Exception ex) {
			logger.info("error in generating Acess Token:  "+ex);
		}

		return token;
	}
}
