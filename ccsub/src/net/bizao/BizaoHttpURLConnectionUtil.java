package net.bizao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import net.util.HTTPResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class BizaoHttpURLConnectionUtil {

	private static final Logger logger = Logger.getLogger(BizaoHttpURLConnectionUtil.class);


	public static void main(String[] args) throws Exception {

		BizaoHttpURLConnectionUtil http = new BizaoHttpURLConnectionUtil();
		String url = "http://www.google.com/search?q=mkyong";
		System.out.println("Testing 1 - Send Http GET request");
		//http.sendGet(url, "test");
		
		
		//System.out.println("httpResponse::::"+httpResponse);
	}
	

	public  BizaoHTTPResponse findAccessToken(String url,String autorization){
	       
	    BizaoHTTPResponse bizaoHTTPResponse=new BizaoHTTPResponse();
        PostMethod post = new PostMethod(url);
        logger.info("sendPostRequest:: "+url +", autorization:: "+autorization);
        String response="";
        try {  
       // RequestEntity entity = new StringRequestEntity(data);
        //post.setRequestEntity(entity);
        post.setRequestHeader("authorization", "Basic "+autorization);
        post.setRequestHeader("cache-control", "no-cache");
        post.setRequestHeader("content-type", "application/x-www-form-urlencoded");
        NameValuePair nameValuePair=new NameValuePair();
        nameValuePair.setName("grant_type");
        nameValuePair.setValue("client_credentials");        
        post.setRequestBody(new NameValuePair[]{nameValuePair});
        
        HttpClient httpclient = new HttpClient();
        httpclient.setConnectionTimeout(20000);
        int responseCode = httpclient.executeMethod(post);
        bizaoHTTPResponse.setResponseCode(responseCode);
            logger.info("Response status code: " + responseCode);
            response=post.getResponseBodyAsString(); 
            bizaoHTTPResponse.setResponseStr(response);
            logger.info("Response body: "+response);
        }catch(Exception ex){
        	logger.error("sendPostRequest:: ", ex);
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
        return bizaoHTTPResponse;
    }
	

	  
	  public  BizaoHTTPResponse sendOTPPostRequest(String url,String data,String accessToken){
	       
		    BizaoHTTPResponse bizaoHTTPResponse=new BizaoHTTPResponse();
	        
	        logger.info("sendPostRequest:: "+url +", data: "+data+", accessToken:: "+accessToken);
	        
	        try {  
	        
	        	CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpPost httpPost = new HttpPost(url);	     
	            httpPost.setHeader("authorization", "Bearer "+accessToken);	 
		        httpPost.setHeader("cache-control", "no-cache");
		     
		        StringEntity  stringEntity = new StringEntity(data);
	           	 
	            stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
	            httpPost.setEntity(stringEntity);	          
	            httpPost.setHeader("content-type", "application/json");
	           
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            String result = EntityUtils.toString(response.getEntity());
	            String locationHeader=response.getHeaders("Location")[0].getValue();
	            bizaoHTTPResponse.setResponseCode(response.getStatusLine().getStatusCode());
	            logger.info("Response status code: " + response.getStatusLine().getStatusCode());
	            
	            bizaoHTTPResponse.setResponseStr(locationHeader);
	            logger.info("Response body: "+result+" ,locationHeader:: "+locationHeader);
	        }catch(Exception ex){
	        	logger.error("sendPostRequest:: ", ex);
	        } finally {
	            // Release current connection to the connection pool once you are done
	           
	        }
	        return bizaoHTTPResponse;
	    }
	  
	  public  BizaoHTTPResponse sendValidateOTPPostRequest(String url,String data,String accessToken){
	       
		    BizaoHTTPResponse bizaoHTTPResponse=new BizaoHTTPResponse();
	        
	        logger.info("sendPostRequest:: "+url +", data: "+data+", accessToken:: "+accessToken);
	        
	        try {  
	        
	        	CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpPost httpPost = new HttpPost(url);	     
	            httpPost.setHeader("authorization", "Bearer "+accessToken);	 
		        httpPost.setHeader("cache-control", "no-cache");
		     
		        StringEntity  stringEntity = new StringEntity(data);
	           	 
	            stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
	            httpPost.setEntity(stringEntity);	          
	            httpPost.setHeader("content-type", "application/json");
	           
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            String result = EntityUtils.toString(response.getEntity());
	            bizaoHTTPResponse.setResponseCode(response.getStatusLine().getStatusCode());
	            logger.info("Response status code: " + response.getStatusLine().getStatusCode());
	            
	            bizaoHTTPResponse.setResponseStr(result);
	            logger.info("Response body: "+result);
	        }catch(Exception ex){
	        	logger.error("sendPostRequest:: ", ex);
	        } finally {
	            // Release current connection to the connection pool once you are done
	           
	        }
	        return bizaoHTTPResponse;
	    }
	
	  
	  
	  public  BizaoHTTPResponse sendPostRequest(String url,String data,Map<String,String> headers){
	       
		    BizaoHTTPResponse bizaoHTTPResponse=new BizaoHTTPResponse();
	        PostMethod post = new PostMethod(url);
	        String response="";
	        logger.info("sendPostRequest:: "+url +", data: "+data+", headers:: "+headers);
	        try {  
	        RequestEntity entity = new StringRequestEntity(data, 
                    "application/json","utf-8");
	        
	        post.setRequestEntity(entity);
	        Iterator<String> itr=headers.keySet().iterator();
	        while(itr.hasNext()){
	        String key=itr.next();	
	        post.setRequestHeader(key,headers.get(key));
	        }
	        
	        HttpClient httpclient = new HttpClient();
	        int responseCode = httpclient.executeMethod(post);
	        bizaoHTTPResponse.setResponseCode(responseCode);
	            logger.info("Response status code: " + responseCode);
	            response=post.getResponseBodyAsString(); 
	            bizaoHTTPResponse.setResponseStr(response);
	            logger.info("Response body: "+response);
	        }catch(Exception ex){
	        	logger.error("sendPostRequest:: ", ex);
	        } finally {
	            // Release current connection to the connection pool once you are done
	            post.releaseConnection();
	        }
	        return bizaoHTTPResponse;
	    }
	  
	  
	  public  BizaoHTTPResponse sendGetRequest(String url,String data,Map<String,String> headers){
	       
		    BizaoHTTPResponse bizaoHTTPResponse=new BizaoHTTPResponse();
	        GetMethod get = new GetMethod(url);
	        String response="";
	        logger.info("sendPostRequest:: "+url +", data: "+data+", headers:: "+headers);
	        try {  
	        RequestEntity entity = new StringRequestEntity(data, 
                  "application/json","utf-8");
	        
	       
	        Iterator<String> itr=headers.keySet().iterator();
	        while(itr.hasNext()){
	        String key=itr.next();	
	        get.setRequestHeader(key,headers.get(key));
	        }
	        
	        HttpClient httpclient = new HttpClient();
	        int responseCode = httpclient.executeMethod(get);
	        bizaoHTTPResponse.setResponseCode(responseCode);
	            logger.info("Response status code: " + responseCode);
	            response=get.getResponseBodyAsString(); 
	            bizaoHTTPResponse.setResponseStr(response);
	            logger.info("Response body: "+response);
	        }catch(Exception ex){
	        	logger.error("sendPostRequest:: ", ex);
	        } finally {
	            // Release current connection to the connection pool once you are done
	            get.releaseConnection();
	        }
	        return bizaoHTTPResponse;
	    }
}
