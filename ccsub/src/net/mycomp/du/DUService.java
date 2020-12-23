package net.mycomp.du;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.jms.JMSService;
import net.jpa.repository.JPADUConfig;
import net.persist.bean.Otp;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.process.request.AbstractOperatorService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MConstants;
import net.util.MUtility;

@Service
@Qualifier(value="duService")
public class DUService extends AbstractOperatorService {

	private static final Logger logger = Logger
			.getLogger(DUService.class.getName());

	
	@Autowired
	private JPADUConfig jpaDUConfig;
	
	@Autowired
	private JMSService jmsService;
	
	private String requestParamTemplate;
	
     private final String cgUrl;
     private String dbillUrl;
     
     private final String secretEncriptionKeyStorePassword;
     private final String secretEncriptionKeyPassword;
     private final String secretEncriptionKeyAlias;
     private final String unsubTemplate;
     private final String cgCallback;
     //private Mac sha256HMAArabFlah;
     
     private final String jksFileName;
     private SecretFile secretFile;
    // private Cipher cipher;
    // private Key key ;
     
    private HttpURLConnectionUtil httpURLConnectionUtil=new HttpURLConnectionUtil();
	
    private  JAXBContext jaxbContextOCSRequest;  
    private JAXBContext jaxbContextOCSResponse;
    private Random random = new Random();
     
     @Autowired
	public DUService(@Value("${du.cg.url}")String cgUrl,
			@Value("${du.cg.requestparam}")String requestParamTemplate,
			@Value("${du.secret.encription.keystore.password}")String secretEncriptionKeyStorePassword,
			@Value("${du.secret.encription.key.password}")String secretEncriptionKeyPassword,
			@Value("${du.secret.encription.key.alias}")String secretEncriptionKeyAlias,
			@Value("${du.secret.jks.file}")String jksFileName,
			@Value("${du.dbill.url}")String dbillUrl,
			@Value("${du.unsub.msg.template}")String unsubTemplate,
			@Value("${du.cg.callback.url}") String cgCallback){
		this.cgUrl=cgUrl;
		this.requestParamTemplate=requestParamTemplate;
		this.secretEncriptionKeyStorePassword=secretEncriptionKeyStorePassword;
		this.secretEncriptionKeyPassword=secretEncriptionKeyPassword;
		this.secretEncriptionKeyAlias=secretEncriptionKeyAlias;
		this.jksFileName=jksFileName;
		this.dbillUrl=dbillUrl;
		this.unsubTemplate=unsubTemplate;
		this.cgCallback=cgCallback;
	}
	

	@PostConstruct
	public void init() throws Exception{
		try {
			List<DUConfig> listDUConfig=jpaDUConfig.findEnableDUConfig(true);
			logger.info("listDUConfig::: "+listDUConfig+",  img:: ");
			
			DUConstant.mapServiceIdToDuConfig.putAll(
					listDUConfig.stream().collect(
							Collectors.toMap(p -> p.getServiceId(), p -> p)));
//			DUConstant.mapProductIdToDuConfig.putAll(
//					listDUConfig.stream().collect(
//							Collectors.toMap(p -> p.getProductId(), p -> p)));
			DUConstant.mapPlanIdToDuConfig.putAll(
					listDUConfig.stream().collect(
							Collectors.toMap(p -> p.getPlanId(), p -> p)));
			
//			Mac sha256HMAArabFlah sha256HMAArabFlah = Mac.getInstance("HmacSHA256");
//			SecretKeySpec secret_key = new SecretKeySpec
//					("COLLECTCENT_01".getBytes(), "HmacSHA256");
//			sha256HMAArabFlah.init(secret_key);
			
//			File file = new File(jksFileName);
//			InputStream keystoreStream = new FileInputStream(file); 
//			KeyStore keystore = KeyStore.getInstance("JCEKS");
//			keystore.load(keystoreStream, secretEncriptionKeyStorePassword.toCharArray()); 
//		    key = keystore.getKey(secretEncriptionKeyAlias, secretEncriptionKeyPassword.toCharArray());
//		    keystoreStream.close();
		    
		    jaxbContextOCSRequest = JAXBContext.newInstance(OCSRequest.class);   
		     jaxbContextOCSResponse = JAXBContext.newInstance(OCSResponse.class);   
		     
		} catch (Exception ex) {
			logger.error("init ", ex);
			 throw ex;
		}
	}
	
//	@Override
//	public boolean processBilling(ModelAndView modelAndView,
//			AdNetworkRequestBean adNetworkRequestBean)  {
//
//		try {
//			
//			//MSISDN=<msisdn>&productID=<productid>&pName=<pname>&pPrice=<pprice>&pVal=<pval>&CpId=<cpid>&
//			//CpPwd=<cppwd>&CpName=<cpname>&reqMode=<reqmode>&reqType=<reqtype>&ismID=<ismid>&transID=<transid>&
//			//sRenewalPrice=<srenewalprice>&sRenewalValidity=<srenewalvalidity>&
//			//request_locale=<requestlocale>&serviceType=<servicetype>&planId=<planid>&Wap_mdata=<wapmdata>
//			
//			DUConfig duConfig=DUConstant.mapServiceIdToDuConfig.
//					get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
//			String requestParam=this.requestParamTemplate
//					.replace("<msisdn>","")
//					.replace("<productid>",duConfig.getProductId())
//					.replaceAll("<pname>",MUtility.urlEncoding(duConfig.getProductName()))
//					.replaceAll("<pprice>",String.valueOf(duConfig.getProductPrice()))
//					.replaceAll("<pval>",String.valueOf(duConfig.getProductVal()))
//					.replaceAll("<cpid>", MUtility.urlEncoding(duConfig.getCpId()))
//					.replaceAll("<cppwd>",MUtility.urlEncoding(duConfig.getCpPwd()))
//					.replaceAll("<cpname>", MUtility.urlEncoding(duConfig.getCpName()))
//					.replaceAll("<reqmode>", MUtility.urlEncoding(duConfig.getReqMode()))
//					.replaceAll("<reqtype>", MUtility.urlEncoding(duConfig.getReqType()))
//					.replaceAll("<ismid>", duConfig.getImsId())
//					.replaceAll("<transid>",MUtility.urlEncoding(adNetworkRequestBean.adnetworkToken.getTokenToCg()))
//					.replaceAll("<srenewalprice>", String.valueOf(duConfig.getsRenewalPrice()))
//					.replaceAll("<srenewalvalidity>", duConfig.getRenewalPlanValidity())
//					.replaceAll("<requestlocale>", duConfig.getRequestLocale())
//					.replaceAll("<servicetype>", duConfig.getServiceType())
//					.replaceAll("<planid>", duConfig.getPlanId())					
//					//.replaceAll("<pvalunit>",MUtility.urlEncoding(duConfig.getProductValUnit()))
//					//.replaceAll("<pdesc>", MUtility.urlEncoding(duConfig.getProductDesc()))
//					.replaceAll("<wapmdata>",MUtility.urlEncoding(duConfig.
//							getWapMdataImgs().get((int)MUtility.randomNumber(0, duConfig.
//							getWapMdataImgs().size()))));
//			logger.info("requestParam:: "+requestParam);
//			Mac  sha256HMAArabFlah = Mac.getInstance("HmacSHA256");
//			SecretKeySpec secret_key = new SecretKeySpec
//					("COLLECTCENT_01".getBytes(), "HmacSHA256");
//			sha256HMAArabFlah.init(secret_key);
//			String checksum ="";
//			
//			checksum=URLEncoder.encode(Base64.encodeBase64String(sha256HMAArabFlah.
//					doFinal(requestParam.getBytes())),"utf-8");
//			
//			File file = new File(jksFileName);
//			InputStream keystoreStream = new FileInputStream(file); 
//			KeyStore keystore = KeyStore.getInstance("JCEKS");
//			keystore.load(keystoreStream, secretEncriptionKeyStorePassword.toCharArray()); 
//			Key key = keystore.getKey(secretEncriptionKeyAlias, secretEncriptionKeyPassword.toCharArray());
//		    keystoreStream.close();
//		    
//			IvParameterSpec iv = new IvParameterSpec(new byte[16]);
//		   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
//		   cipher.init(Cipher.ENCRYPT_MODE, key,iv); 		     
//		    byte[] encryptedMessageInBytes = cipher.doFinal(requestParam.getBytes()); 
//		    String encriptedRequestParam=Base64.encodeBase64String(encryptedMessageInBytes);
//			 
//			String url=cgUrl+"?CpId="+MUtility.urlEncoding(duConfig.getCpId())+"&requestParam="+encriptedRequestParam
//					+"&checksum="+checksum+"&request_locale="+duConfig.getRequestLocale();
//			
//			logger.info("processBilling::: redirect to url::  "+url);   
//			modelAndView.setView(new RedirectView(url));
//			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
//		} catch (Exception ex) {			
//			logger.error("campaign:: exception " ,ex);
//			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_ERROR);	
//			modelAndView.setViewName("error");
//			 return false;
//		}
//		return true;
//	}
	
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean)  {

		try {
			
			//MSISDN=<msisdn>&CpName=<cpname>&productID=<productid>
			//&pName=<pname>&planId=<planid>&pPrice=<pprice>
			//&pVal=<pval>&serviceType=<servicetype>&sRenewalPrice=<srenewalprice>
			//&sRenewalValidity=<srenewalvalidity>&reqMode=<reqmode>&transID=<transid>
			//&request_locale=<requestlocale>&Songname=<songname>&Opt3=<cgcallback>
			
			DUConfig duConfig=DUConstant.mapServiceIdToDuConfig.
					get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			String requestParam=this.requestParamTemplate
					.replace("<msisdn>","")
					.replace("<productid>",duConfig.getProductId())
					.replaceAll("<pname>",MUtility.urlEncoding(duConfig.getProductName()))
					.replaceAll("<pprice>",String.valueOf(duConfig.getProductPrice()))
					.replaceAll("<pval>",String.valueOf(duConfig.getProductVal()))
					.replaceAll("<cpid>", MUtility.urlEncoding(duConfig.getCpId()))
					.replaceAll("<cppwd>",MUtility.urlEncoding(duConfig.getCpPwd()))
					.replaceAll("<cpname>", MUtility.urlEncoding(duConfig.getCpName()))
					.replaceAll("<reqmode>", MUtility.urlEncoding(duConfig.getReqMode()))
					.replaceAll("<reqtype>", MUtility.urlEncoding(duConfig.getReqType()))
					.replaceAll("<ismid>", duConfig.getImsId())
					.replaceAll("<transid>",MUtility.urlEncoding(adNetworkRequestBean.adnetworkToken.getTokenToCg()))
					.replaceAll("<srenewalprice>", String.valueOf(duConfig.getsRenewalPrice()))
					.replaceAll("<srenewalvalidity>", duConfig.getRenewalPlanValidity())
					.replaceAll("<requestlocale>", duConfig.getRequestLocale())
					.replaceAll("<servicetype>", duConfig.getServiceType())
					.replaceAll("<planid>", duConfig.getPlanId())
					.replaceAll("<cgcallback>",MUtility.urlEncoding(cgCallback))
					//.replaceAll("<songname>",MUtility.urlEncoding(duConfig.getProductName()))
					.replaceAll("<songname>",MUtility.urlEncoding(adNetworkRequestBean.getPubId()))
					//.replaceAll("<pvalunit>",MUtility.urlEncoding(duConfig.getProductValUnit()))
					//.replaceAll("<pdesc>", MUtility.urlEncoding(duConfig.getProductDesc()))
//					.replaceAll("<wapmdata>",MUtility.urlEncoding(duConfig.
//							getWapMdataImgs().get((int)MUtility.randomNumber(0, duConfig.
//							getWapMdataImgs().size()))))
					;
			logger.info("requestParam:: "+requestParam);			 
			String url=cgUrl+"?"+requestParam;			
			logger.info("processBilling::: redirect to url::  "+url);   
			modelAndView.setView(new RedirectView(url));
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			adNetworkRequestBean.adnetworkToken.setRedirectToUrl(url);
		} catch (Exception ex) {			
			logger.error("campaign:: exception " ,ex);
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_ERROR);	
			modelAndView.setViewName("error");
			 return false;
		}
		return true;
	}
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		
//		boolean isBlock=super.checkBlocking(adNetworkRequestBean);
//		if(isBlock){
//			return isBlock;
//		}

		return false;
	}
	
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
		if(super.isSubscribed(adNetworkRequestBean)){
		    return true;
		 }
		 return false;
	}
	
	
	
	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,SubscriberReg subscriberReg) {
		DUOCSLogDetail duOCSLogDetail=null;
		try { 
		 
		 DUConfig duConfig=DUConstant.mapServiceIdToDuConfig.get(subscriberReg.getServiceId());
		 OCSRequest ocsRequest=DUFactory.createDeactivationRequest(subscriberReg,duConfig);
	     Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	    
	     String requestXml=sw.toString();
	      duOCSLogDetail=new DUOCSLogDetail();
	     duOCSLogDetail.setMsisdn(subscriberReg.getMsisdn());
	     duOCSLogDetail.setAction(MConstants.DCT);
	     duOCSLogDetail.setRequet(requestXml);
	     duOCSLogDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));
	     duOCSLogDetail.setStatus(true);	     
	     HTTPResponse  httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequestWithXML(dbillUrl, requestXml);
	     duOCSLogDetail.setResponse(httpResponse.getResponseStr());
	     
	     StringReader sr = new StringReader(httpResponse.getResponseStr());	
	     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse.createUnmarshaller().unmarshal(sr);
	     logger.info("ocsResponse:::::::::::: "+ocsResponse);


		} catch (Exception ex) {
			logger.error("deactivation::: ",ex);
		}finally{
			jmsService.saveObject(duOCSLogDetail);
		}
	     
		 return null;
	}
	
//	@Override
	public Otp sendOtp(ModelAndView modelAndView,String msisdn,Integer operatorId,Integer serviceId) {

		Otp otp=new Otp();
		DUOCSLogDetail duOCSLogDetail=null;
		try{
		otp.setMsisdn(msisdn);
		otp.setOtp(String.format("%04d", random.nextInt(10000)));
		otp.setCreateTime(new Timestamp(System.currentTimeMillis()));
		otp.setStatus(true);	
		
		 otp.setMsg("your OTP is "+otp.getOtp() +". Enter this pin to access portal service.");
		 DUConfig duConfig=DUConstant.mapServiceIdToDuConfig.get(serviceId);
		 OCSRequest ocsRequest=DUFactory.createOTPRequest(otp,msisdn, duConfig);
	     Marshaller jaxbMarshaller = jaxbContextOCSRequest.createMarshaller();
	     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     StringWriter sw = new StringWriter();
	     jaxbMarshaller.marshal(ocsRequest, sw);	    
	     String requestXml=sw.toString();
	      duOCSLogDetail=new DUOCSLogDetail();
	     duOCSLogDetail.setMsisdn(msisdn);
	     duOCSLogDetail.setAction(DUConstant.OTP_MT);
	     duOCSLogDetail.setRequet(requestXml);
	     duOCSLogDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));
	     duOCSLogDetail.setStatus(true);	     
	     HTTPResponse httpResponse= httpURLConnectionUtil.makeHTTPPOSTRequestWithXML(dbillUrl, requestXml);
	     duOCSLogDetail.setResponse(httpResponse.getResponseStr());
	    
	     StringReader sr = new StringReader(httpResponse.getResponseStr());	
	     OCSResponse  ocsResponse= (OCSResponse)jaxbContextOCSResponse.createUnmarshaller().unmarshal(sr);
	     logger.info("ocsResponse:::::::::::: "+ocsResponse);
	    
	     if(ocsResponse!=null&&ocsResponse.getResult().toUpperCase().contains("OK")){
	    	
	    	 otp.setSend(true);	
	    	
		  }
		} catch (Exception ex) {
			logger.error("deactivation::: ",ex);
		}finally{
			jmsService.saveObject(duOCSLogDetail);
			jmsService.saveObject(otp);
		}
	     
		 return otp;
		
	}
}
