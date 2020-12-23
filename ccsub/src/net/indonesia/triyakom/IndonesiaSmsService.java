package net.indonesia.triyakom;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;

@Service("indonesiaSmsService")
public class IndonesiaSmsService {

	private static final Logger logger = Logger.getLogger(IndonesiaSmsService.class);

	@Value("${sms.send.url}")
	private  String smsUrl;
	
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	private JAXBContext jaxbContext;
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		try {
			 jaxbContext = JAXBContext.newInstance(PushResponse.class);
		} catch (JAXBException e) {
			logger.error("init ",e);
		}
	}
	
	public boolean sendMTMessage(MTMessage mtMessage){
//		String url="http://gateway.9386.me:3230/api/99386/handle.aspx?dest_addr=<dest_addr>&app_id=<app_id>&"
//				+ "app_id=<app_pwd>&data=<data>&op=<op>&rtx_id=<rtx_id>&service=<service>&alphabet_id=0";
		try{
		
		String url=smsUrl.replaceAll("<dest_addr>", URLEncoder.encode(mtMessage.getDestAddr(),"utf-8"))				
				.replaceAll("<app_id>", URLEncoder.encode(mtMessage.getAppId(),"utf-8"))
				.replaceAll("<app_pwd>", URLEncoder.encode(mtMessage.getAppPwd(),"utf-8"))
				.replaceAll("<data>", URLEncoder.encode(mtMessage.getData(),"utf-8"))
				.replaceAll("<op>", URLEncoder.encode(mtMessage.getOp(),"utf-8"))
				.replaceAll("<rtx_id>", URLEncoder.encode(mtMessage.getRtxId(),"utf-8"))
				.replaceAll("<service>", URLEncoder.encode(mtMessage.getService(),"utf-8"))
				.replaceAll("<short_code>", URLEncoder.encode(mtMessage.getShortCodeToPrepareMtUrl(),"utf-8"));
		
		mtMessage.setRequestUrl(url);
		logger.info("sendMTMessage:: url: "+url);
		HTTPResponse  httpResponse=httpURLConnectionUtil.sendGet(url);
		logger.info("sendMTMessage:: httpResponse: "+httpResponse);
		mtMessage.setResponse(httpResponse.getResponseStr());
		if(httpResponse!=null&&httpResponse.getResponseCode()==200){
			PushResponse pushResponse=(PushResponse)jaxbContext.createUnmarshaller().unmarshal(
					new ByteArrayInputStream(httpResponse.getResponseStr().getBytes(StandardCharsets.UTF_8.name())));
			logger.info("sendMTMessage:::: pushResponse:: "+pushResponse);
			mtMessage.setResponseTid(pushResponse.getTid());
			mtMessage.setStatusId(pushResponse.getStatusId());
			if(pushResponse.getStatusId().equalsIgnoreCase("0")){
				mtMessage.setSendSuccess(true);
				return true;
			}
			
		}
		
		}catch(Exception ex){
			logger.error("sendSMS:: "+ex);
		}
		return false;
	}
	
	
	}
