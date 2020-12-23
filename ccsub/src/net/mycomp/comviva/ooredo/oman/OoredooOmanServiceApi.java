package net.mycomp.comviva.ooredo.oman;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.common.service.BlockSeriesRedisCacheService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service("ooredooOmanServiceApi")
public class OoredooOmanServiceApi {

	private static final Logger logger = Logger
			.getLogger(OoredooOmanServiceApi.class.getName());

	
	
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
   private  JAXBContext jaxbContextOCSRequest;  
   private JAXBContext jaxbContextOCSResponse;
	
   @Autowired
   private BlockSeriesRedisCacheService blockSeriesRedisCacheService;
   
	@Autowired
	private IDaoService daoService;
   
	String heDecryptionKey ="ga5qpkd3g72";// "099094kfjfdhjfi"; //PSK to be shared during integration
	
  // @Autowired
	public OoredooOmanServiceApi(){
	   
	   httpURLConnectionUtil=new HttpURLConnectionUtil();
		
		try{
		 jaxbContextOCSRequest = JAXBContext.newInstance(OCSRequest.class);   
	     jaxbContextOCSResponse = JAXBContext.newInstance(OCSResponse.class);  
		}catch(Exception ex){
			logger.error("ooredooOmanServiceApi",ex);
		}
	}
	
   
   
   
   public OoredooOmanOCSLogDetail sendPinApi(String msisdn,String token,
			OoredooOmanServiceConfig ooredooOmanServiceConfig){
		
	   OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		try{
		
		ooredooOmanOCSLogDetail=new OoredooOmanOCSLogDetail(true,OoredooOmanConstant.SNED_PIN);
		ooredooOmanOCSLogDetail.setMsisdn(msisdn);
		ooredooOmanOCSLogDetail.setToken(token);
		ooredooOmanOCSLogDetail.setConfigId(ooredooOmanServiceConfig.getId());
		if(checkBlocking(msisdn)){
			ooredooOmanOCSLogDetail.setResponse("Blocked");
			return ooredooOmanOCSLogDetail;
		}
		
		
		OCSRequest ocsRequest=new OCSRequest();
		ocsRequest.setServiceId(""+ooredooOmanServiceConfig.getServiceId());
		ocsRequest.setServiceNode(ooredooOmanServiceConfig.getServiceNode());
		ocsRequest.setCallingParty(msisdn);
		ocsRequest.setBearerId(ooredooOmanServiceConfig.getBearerId());
		ocsRequest.setPlanId(ooredooOmanServiceConfig.getPlanId());
		ocsRequest.setSequenceNo(Objects.toString(ooredooOmanOCSLogDetail.getId()));
		ocsRequest.setShortcode(ooredooOmanServiceConfig.getShortCode());
		ocsRequest.setKeyword(ooredooOmanServiceConfig.getSubKeyword());
		 Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	
	     ooredooOmanOCSLogDetail.setRequet(sw.toString());
	     Map<String,String> headerMap=new HashMap<String,String>();
	     headerMap.put("Content-Type", "application/xml");
	     headerMap.put("apiKey", ooredooOmanServiceConfig.getAuthApiKey());
	     headerMap.put("authentication", generateKey(ooredooOmanServiceConfig.getAuthPresharedApiKey(),
	    		 ooredooOmanServiceConfig.getAuthServiceId(),ooredooOmanOCSLogDetail.getCreateDate().getTime()));
	     ooredooOmanOCSLogDetail.setRequet(
	    		 ooredooOmanServiceConfig.getSendPinUrl()
	    		 +sw.toString()+headerMap);
	     HTTPResponse  httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequest(
	    		 ooredooOmanServiceConfig.getSendPinUrl()
	    		 , sw.toString(),headerMap);
	     ooredooOmanOCSLogDetail.setResponseCode(""+httpResponse.getResponseCode());
	     ooredooOmanOCSLogDetail.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			 StringReader sr = new StringReader(httpResponse.getResponseStr());	
		     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse.createUnmarshaller().unmarshal(sr);
		    if(ocsResponse!=null&&ocsResponse.getCode().equalsIgnoreCase("SUCCESS")&&
		    		ocsResponse.getErrorCode().equalsIgnoreCase("OPTIN_PREACTIVE_WAIT_CONF")){
		    	ooredooOmanOCSLogDetail.setSuccess(true);
		    }
		   logger.info("sendPinApi:ocsResponse:::::::::::: "+ocsResponse);
		}
	}catch(Exception ex){
		logger.error("sendPinApi:: ",ex);
	}finally{
		daoService.updateObject(ooredooOmanOCSLogDetail);
	}
		return ooredooOmanOCSLogDetail;
	}
   	
   

   public OoredooOmanOCSLogDetail pinValidation(String msisdn,String token,
			OoredooOmanServiceConfig ooredooOmanServiceConfig,String otp){
		
	   OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		try{
		
		ooredooOmanOCSLogDetail=new OoredooOmanOCSLogDetail(true,OoredooOmanConstant.PIN_VALIDATION);
		ooredooOmanOCSLogDetail.setMsisdn(msisdn);
		ooredooOmanOCSLogDetail.setToken(token);
		ooredooOmanOCSLogDetail.setConfigId(ooredooOmanServiceConfig.getId());
		if(checkBlocking(msisdn)){
			ooredooOmanOCSLogDetail.setResponse("Blocked");
			return ooredooOmanOCSLogDetail;
		}
		
		OCSRequest ocsRequest=new OCSRequest();
		ocsRequest.setSequenceNo(Objects.toString(ooredooOmanOCSLogDetail.getId()));
		ocsRequest.setServiceId(""+ooredooOmanServiceConfig.getServiceId());
		ocsRequest.setServiceNode(ooredooOmanServiceConfig.getServiceNode());
		ocsRequest.setCallingParty(msisdn);
		ocsRequest.setBearerId(ooredooOmanServiceConfig.getBearerId());
		ocsRequest.setPlanId(ooredooOmanServiceConfig.getPlanId());
		ocsRequest.setOtp(otp);
		 Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	
	     ooredooOmanOCSLogDetail.setRequet(sw.toString());
	     Map<String,String> headerMap=new HashMap<String,String>();
	     headerMap.put("Content-Type", "application/xml");
	     headerMap.put("apiKey", ooredooOmanServiceConfig.getAuthApiKey());
	     headerMap.put("authentication", generateKey(ooredooOmanServiceConfig.getAuthPresharedApiKey()
	    		 ,ooredooOmanServiceConfig.getAuthServiceId()
	    		 ,ooredooOmanOCSLogDetail.getCreateDate().getTime()));
	     
	     ooredooOmanOCSLogDetail.setRequet(
	    		 ooredooOmanServiceConfig.getPinValidationUrl()
	    		 +sw.toString()+headerMap);
	     HTTPResponse  httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequest(
	    		 ooredooOmanServiceConfig.getPinValidationUrl()
	    		 , sw.toString(),headerMap);
	     ooredooOmanOCSLogDetail.setResponseCode(""+httpResponse.getResponseCode());
	     ooredooOmanOCSLogDetail.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			 StringReader sr = new StringReader(httpResponse.getResponseStr());	
		     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse
		    		 .createUnmarshaller().unmarshal(sr);
		    if(ocsResponse.getErrorCode().equalsIgnoreCase("OPTIN_ACTIVE_WAIT_CHARGING")||
		    		ocsResponse.getErrorCode().equalsIgnoreCase("OPTIN_ALREADY_ACTIVE")){
		    	ooredooOmanOCSLogDetail.setSuccess(true);
		    }
		     logger.info("pinValidation::::::::::::ocsResponse:: "+ocsResponse);
		}
	}catch(Exception ex){
		logger.error("pinValidation:: ",ex);
	}finally{
		daoService.updateObject(ooredooOmanOCSLogDetail);
	}
		return ooredooOmanOCSLogDetail;
	}
   
   public OoredooOmanOCSLogDetail unsubSubscribe(String msisdn,String token,
			OoredooOmanServiceConfig ooredooOmanServiceConfig){
		
	   OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		try{
		
		ooredooOmanOCSLogDetail=new OoredooOmanOCSLogDetail(true,OoredooOmanConstant.UNSUB);
		ooredooOmanOCSLogDetail.setMsisdn(msisdn);
		ooredooOmanOCSLogDetail.setToken(token);
		ooredooOmanOCSLogDetail.setConfigId(ooredooOmanServiceConfig.getId());
		
		
		OCSRequest ocsRequest=new OCSRequest();
		ocsRequest.setSequenceNo(Objects.toString(ooredooOmanOCSLogDetail.getId()));
		ocsRequest.setServiceId(""+ooredooOmanServiceConfig.getServiceId());
		ocsRequest.setServiceNode(ooredooOmanServiceConfig.getServiceNode());
		ocsRequest.setCallingParty(msisdn);
		ocsRequest.setBearerId(ooredooOmanServiceConfig.getBearerId());
		ocsRequest.setPlanId(ooredooOmanServiceConfig.getPlanId());
		ocsRequest.setShortcode(ooredooOmanServiceConfig.getShortCode());
		ocsRequest.setKeyword(ooredooOmanServiceConfig.getUnsubKeyword());
		 Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	
	     ooredooOmanOCSLogDetail.setRequet(sw.toString());
	     Map<String,String> headerMap=new HashMap<String,String>();
	     headerMap.put("Content-Type", "application/xml");
	     headerMap.put("apiKey", ooredooOmanServiceConfig.getAuthApiKey());
	     headerMap.put("authentication", generateKey(ooredooOmanServiceConfig.getAuthPresharedApiKey(),
	    		 ooredooOmanServiceConfig.getAuthServiceId(),ooredooOmanOCSLogDetail.getCreateDate().getTime()));
	     ooredooOmanOCSLogDetail.setRequet(
	    		 ooredooOmanServiceConfig.getUnsubUrl()
	    		 +sw.toString()+headerMap);
	     HTTPResponse  httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequest(
	    		 ooredooOmanServiceConfig.getUnsubUrl()
	    		 , sw.toString(),headerMap);
	     ooredooOmanOCSLogDetail.setResponseCode(""+httpResponse.getResponseCode());
	     ooredooOmanOCSLogDetail.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			 StringReader sr = new StringReader(httpResponse.getResponseStr());	
		     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse.createUnmarshaller().unmarshal(sr);
		    if(ocsResponse.getErrorCode().equalsIgnoreCase("OPTOUT_CANCELED_OK")||
		    		ocsResponse.getErrorCode().equalsIgnoreCase("OPTOUT_NO_SUB")){
		    	ooredooOmanOCSLogDetail.setSuccess(true);
		    }
		     logger.info("unsubSubscribe::::::::::::ocsResponse:: "+ocsResponse);
		}
	}catch(Exception ex){
		ooredooOmanOCSLogDetail.setResponse(ex.toString());
		logger.error("unsubSubscribe:: ",ex);
	}finally{
		daoService.updateObject(ooredooOmanOCSLogDetail);
	}
		return ooredooOmanOCSLogDetail;
	}
   
   
   public OoredooOmanOCSLogDetail sendMT(String msisdn,String token,
			OoredooOmanServiceConfig ooredooOmanServiceConfig,String msg){
		
	   OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=null;
		try{
		
		ooredooOmanOCSLogDetail=new OoredooOmanOCSLogDetail(true,OoredooOmanConstant.SEND_MT);
		ooredooOmanOCSLogDetail.setMsisdn(msisdn);
		ooredooOmanOCSLogDetail.setToken(token);
		ooredooOmanOCSLogDetail.setConfigId(ooredooOmanServiceConfig.getId());
		if(checkBlocking(msisdn)){
			ooredooOmanOCSLogDetail.setResponse("Blocked");
			return ooredooOmanOCSLogDetail;
		}
		
		OCSRequest ocsRequest=new OCSRequest();
		
		ocsRequest.setServiceNode(ooredooOmanServiceConfig.getServiceNode());
		
		ocsRequest.setSequenceNo(Objects.toString(ooredooOmanOCSLogDetail.getId()));
		ocsRequest.setServiceId(""+ooredooOmanServiceConfig.getServiceId());
		ocsRequest.setCallingParty(msisdn);
		
		
		ocsRequest.setStartTime(OoredooOmanConstant.sdfDDMMyyyyHHmmss.format(ooredooOmanOCSLogDetail.getCreateDate()));
		ocsRequest.setPlanId(ooredooOmanServiceConfig.getPlanId());
		ocsRequest.setShortcode(ooredooOmanServiceConfig.getShortCode());
		ocsRequest.setContentName(msg);
		
		 Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	
	     ooredooOmanOCSLogDetail.setRequet(sw.toString());
	     Map<String,String> headerMap=new HashMap<String,String>();
	     headerMap.put("Content-Type", "application/xml");
	     headerMap.put("apiKey", ooredooOmanServiceConfig.getAuthSmsApiKey());
	     headerMap.put("authentication",
	    		 generateKey(ooredooOmanServiceConfig.getAuthPresharedSmsKey(),
	    		 ooredooOmanServiceConfig.getAuthServiceId(),ooredooOmanOCSLogDetail.getCreateDate().getTime()));
	     ooredooOmanOCSLogDetail.setRequet(
	    		 ooredooOmanServiceConfig.getMtUrl()
	    		 +sw.toString()+headerMap);
	     HTTPResponse  httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequest(
	    		 ooredooOmanServiceConfig.getMtUrl()
	    		 , sw.toString(),headerMap);
	     ooredooOmanOCSLogDetail.setResponseCode(""+httpResponse.getResponseCode());
	     ooredooOmanOCSLogDetail.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			 StringReader sr = new StringReader(httpResponse.getResponseStr());	
		     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse.createUnmarshaller().unmarshal(sr);
		    if(ocsResponse.getErrorCode().equalsIgnoreCase("6000")){
		    	ooredooOmanOCSLogDetail.setSuccess(true);
		    }
		     logger.info("unsubSubscribe::::::::::::ocsResponse:: "+ocsResponse);
		}
	}catch(Exception ex){
		logger.error("unsubSubscribe:: ",ex);
	}finally{
		daoService.updateObject(ooredooOmanOCSLogDetail);
	}
		return ooredooOmanOCSLogDetail;
	}
   
	 public static String parseSoapResponse(String str) {
		   String response="";
	        String rootPath = "/Envelope/Body/CheckSubs_typeResponse/";
	         try {
	            //Create DocumentBuilderFactory for reading xml file
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            InputStream stream = new ByteArrayInputStream(str.getBytes());
	            Document doc = builder.parse(stream);
	            
	            // Create XPathFactory for creating XPath Object
	            XPathFactory xPathFactory = XPathFactory.newInstance();

	            // Create XPath object from XPathFactory
	            XPath xpath = xPathFactory.newXPath();

	            XPathExpression xPathExpr = xpath.compile(rootPath + "CheckSubs_typeResult/text()");
	            Object result = xPathExpr.evaluate(doc, XPathConstants.STRING);
	            response=result.toString();
	            
	        } catch (Exception e) {
	            logger.error("parseSoapResponse::  :: Exception " , e);
	        }finally{
	      
	        //activationCallback.setCallbackResp(str);
	        
	        }
	        return response;
	    }

	
	 
	 public String decyptMsisdn(String msisdn){
			try{
			
				 MessageDigest md = MessageDigest.getInstance("SHA-256");
			      md.update("ga5qpkd3g72".getBytes("UTF-8"));
			      byte[] digest =Base64.decodeBase64("ga5qpkd3g72");
			      
				   byte[] bytes = Base64.decodeBase64(msisdn.getBytes());
			      
			        byte[] ivBytes = Arrays.copyOfRange(bytes, 0,16);
			        byte[] contentBytes = Arrays.copyOfRange(bytes, 16, bytes.length);
	    	            Cipher ciper = Cipher.getInstance("AES/CBC/NOPADDING");
	                    SecretKeySpec keySpec = new SecretKeySpec(digest,"AES");
			            IvParameterSpec iv = new IvParameterSpec(ivBytes,0, ciper.getBlockSize());
			            ciper.init(Cipher.DECRYPT_MODE, keySpec,iv);
			            return new String(ciper.doFinal(contentBytes));
			}catch(Exception ex){
				logger.error("decyptMsisdn:: ",ex);
				ex.printStackTrace();
			}
			return null;		
		}
	 
 public  String decryptByHttp(String encrypted ) { 
		 String msisdn=null;
		 try { 
			  String url="http://13.232.180.113/timwe/2/decryptpmsisdn.php?encmsisdn="+MUtility.urlEncoding(encrypted);
					   HTTPResponse  httpResponse= httpURLConnectionUtil.sendGet(url);
				return httpResponse.getResponseStr();		
			 } catch (Exception ex) {
				logger.error("decrypt::: ",ex);
			  } 
	    	 return null;
		 }
 
	 public  String decrypt(String encrypted ) { 
		 
		 try { 
			 IvParameterSpec iv = new IvParameterSpec(new byte[16]);
			 SecretKeySpec skeySpec = new SecretKeySpec("ga5qpkd3g72xygwk".getBytes("UTF-8"), "AES");
			 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			 cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			 byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted)); 
			 return new String(original);			 
			 } catch (Exception ex) {
				logger.error("decrypt::: ",ex);
			  } 
	    	 return null;
		 }
	 
	 private  String generateKey(String keyStr,String serviceId,long time) { 
		 
		 
		 String phrasetoEncrypt = serviceId + "#" +time; // 1 Service Id will be shared during integration 
		 String encryptionAlgorithm = "AES/ECB/PKCS5Padding";
		 String encrypted = "";
		 try {
			 Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
			 SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
			 cipher.init(Cipher.ENCRYPT_MODE, key); 
			 final byte[] crypted = cipher.doFinal(phrasetoEncrypt.getBytes()); 
			 encrypted = Base64.encodeBase64String(crypted); 
			 System.out.println("ENCRYPTED API KEY--" + encrypted); 
			 System.out.println("encrypt END \n"); 
			 }catch(Exception ex) {
				logger.error("generateKey exception ",ex);
			}
		 return encrypted;
		 }
	 
	 public boolean checkBlocking(String msisdn) {
			try{
				msisdn=OoredooOmanConstant.formatMsisdn(msisdn);
			 List<String> keys=Arrays.asList(new String[]{msisdn,
					  MUtility.find7DigitMobileNo(msisdn),
					  MUtility.find11DigitMobileNo(msisdn)					
					  });
			 boolean block= blockSeriesRedisCacheService.isBlockSeries(keys);
			 if(block){
				 return block;
			 }
			 
			}catch(Exception ex){
				logger.error("Excepion ",ex);
			}
			 return false;		
			
		}
 public static void main(String arg[]){
		 
		 OoredooOmanServiceApi ooredooOmanServiceApi=new OoredooOmanServiceApi();
		//System.out.println("ooredooOmanServiceApi:: "+ooredooOmanServiceApi.decyptMsisdn("MM+LDZ49Pw6WIeMEUusXzA=="));	
	  // System.out.println(MUtility.urlEncoding("MM+LDZ49Pw6WIeMEUusXzA=="));
	   System.out.println(ooredooOmanServiceApi.decrypt("FXRKZrIY%2BUNd4NzUSSbNlA%3D%3D"));
	 
	 }
 
	
}


