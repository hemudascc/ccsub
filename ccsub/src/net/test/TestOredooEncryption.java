package net.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;

import net.mycomp.comviva.ooredo.oman.OCSResponse;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.commons.codec.binary.Base64;
import org.mortbay.util.UrlEncoded;

public class TestOredooEncryption {

	
	public static void main(String[] args) throws Exception{
		JAXBContext jaxbContextOCSResponse = JAXBContext.newInstance(OCSResponse.class);  
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><OcsRequest><code>1000</code><inError>false</inError><requestId /><message>Successfully Processed</message></OcsRequest>";
		 StringReader sr = new StringReader(xml);	
		 if(xml.contains("<inError>false</inError>")){
			 System.out.print("Trueeeeeeeeeee");
		 }
		 
		OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse
		    		 .createUnmarshaller().unmarshal(sr);
		System.out.print(ocsResponse.toString());
		  
		  
	}
	public static void main2(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String checksum ="";
		String secretEncriptionKeyAlias="com";
		String secretEncriptionKeyStorePassword="com@123";
		String secretEncriptionKeyPassword="com@123";
		String requestParam="";

	
		
		//requestParam=JsonMapper.getObjectToJson(map);
		requestParam="MSISDN=66893767&productID=S-ArabViEwMY2&pName=ArabVibes&pPrice=750.0&pVal=7&CpId=Collectcent"
				+ "&CpPwd=Collect@123&CpName=Collectcent&ismID=457&transID="+System.currentTimeMillis()+"&reqMode=WAP&reqType=Subscription&cpBgColor=&sRenewalPrice=750.0&sRenewalValidity=7&Wap_mdata=http%253A%252F%252Fmob.ccd2c.com%252Fccsub%252Fresources%252Fooredoo%252Flp.jpg";
		requestParam="MSISDN=66893767&productID=S-ArabViEwMY2&pName=ArabVibes&pPrice=750.0&pVal=7&CpId=Collectcent&CpPwd=Collect@123&CpName=Collectcent&sRenewalPrice=750.0&sRenewalValidity=7&reqMode=WAP&reqType=Subscription&ismID=457&transID="+System.currentTimeMillis()+"&tncFontFamily=times&cpBgColor=silver&Wap_mdata=http%253a%252f%252fmob.ccd2c.com%252fccsub%252fresources%252fooredoo%252flp.jpg";
		System.out.println("Non Encrypt Data:::::: "+requestParam);
		 Mac sha256HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretkey = new SecretKeySpec
					("Collectcent".getBytes(), "HmacSHA256");
			
			sha256HMAC.init(secretkey);
			String check=Base64.encodeBase64String(sha256HMAC.
					doFinal(requestParam.getBytes()));
			
			
			//System.out.println("check:::::: "+check);
			checksum=URLEncoder.encode(Base64.encodeBase64String(sha256HMAC.
					doFinal(requestParam.getBytes())),"utf-8");
			
			
			File file = new File("C:\\Users\\mobitize\\Desktop\\collectcent\\ooredoo  kuwait\\encyption\\keyFile.jks");
			InputStream keystoreStream = new FileInputStream(file); 
			KeyStore keystore = KeyStore.getInstance("JCEKS");
			keystore.load(keystoreStream, secretEncriptionKeyStorePassword.toCharArray()); 
		    Key key = keystore.getKey(secretEncriptionKeyAlias, secretEncriptionKeyPassword.toCharArray());
		    keystoreStream.close();
		    
			IvParameterSpec iv = new IvParameterSpec(new byte[16]);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			 cipher.init(Cipher.ENCRYPT_MODE, key,iv); 		     
		    byte[] encryptedMessageInBytes = cipher.doFinal(requestParam.getBytes()); 
		    String encriptedRequestParam=Base64.encodeBase64String(encryptedMessageInBytes);
			 
			String url="http://testconsent.ooredoo.com.kw:8280/API/CCG?CpId="+MUtility.urlEncoding("Collectcent")
					+"&requestParam="+encriptedRequestParam
					+"&checksum="+checksum;
			
			System.out.println("Encrypt Data:::::: "+url);
		    
		    
		    
	}

}
