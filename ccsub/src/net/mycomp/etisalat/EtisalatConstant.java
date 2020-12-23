package net.mycomp.etisalat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;


public interface EtisalatConstant {

	 static final Logger logger = Logger
			.getLogger(EtisalatController.class.getName());
	
	public static final List<EtisalatServiceConfig> listEtisalatServiceConfig=new ArrayList<EtisalatServiceConfig>();
	public static final Map<Integer,EtisalatServiceConfig> mapServiceIdToEtisalatServiceConfig=new HashMap<Integer,EtisalatServiceConfig>();
	public static final  Map<String,EtisalatServiceConfig> mapPackageIdToEtisalatServiceConfig=new HashMap<String,EtisalatServiceConfig>();
   
	public static final String SUB="SUB";
	public static final String REN="REN";
	public static final String UNSUB="UNSUB";
	public final int ETISALAT_SERVICE_ID=5;
	
	
	
	public static String getSoapRequestToString(ServletInputStream sis){
		StringBuilder sb = new StringBuilder();
		try {
			
	        java.io.BufferedReader bis = new java.io.BufferedReader(new java.io.InputStreamReader(sis));
	        String line = "";
	        while ((line = bis.readLine()) != null) {
	            sb.append(line);
	        }
	        bis.close();
	    } catch (Exception e) {
	    	logger.error("getSoapRequestToString:: exception::::: ",e);
	       
	    }finally{
	    	if(sis!=null){
	    		try {
					sis.close();
				} catch (IOException e) {
					logger.error("getSoapRequestToString::finnally:: exception:::::",e);
				}
	    	}
	    }
		return sb.toString();
	}

	public static EtisalatChargingCallback parseXMLToJava(JAXBContext jaxbContext,String xml){
		EtisalatChargingCallback etisalatChargingCallback=null;
		try {
		
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			 etisalatChargingCallback = (EtisalatChargingCallback) jaxbUnmarshaller.unmarshal(new StreamSource(new java.io.StringReader(xml)));

		      } catch (Exception e) {
			logger.error("parseXMLToJava" ,e);
		      }
		return etisalatChargingCallback;
	}
	
	
	public static Double convertToAED(double amount){
	   try {
		   return amount/100;
		 } catch (Exception e) {
			logger.error("parseXMLToJava" ,e);
		      }
		return 0d;
	}
	
}
