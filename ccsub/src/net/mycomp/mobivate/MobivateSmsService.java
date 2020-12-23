package net.mycomp.mobivate;

import java.sql.Timestamp;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.jpa.repository.JPAMobivateSMSTrans;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("mobivateSmsService")
public class MobivateSmsService {

	private static final Logger logger = Logger
			.getLogger(MobivateSmsService.class);
	
	
	private final String freeSmsUrlTemplateUrl;
	
	
	private final String billedSmsUrlTemplate;
	
	private final String dlrUrl;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAMobivateSMSTrans jpaMobivateSMSTrans;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	public MobivateSmsService(
			@Value("${mobivate.sms.welcome.template.url}")String freeSmsUrlTemplateUrl,
			@Value("${mobivate.sms.billed.template.url}")String billedSmsUrlTemplate,			
			@Value("${mobivate.sms.dlr.url}")String dlrUrl){
		
		this.freeSmsUrlTemplateUrl=freeSmsUrlTemplateUrl;
		
	
		this.billedSmsUrlTemplate=billedSmsUrlTemplate;
		this.dlrUrl=dlrUrl;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		MobivateConstant.sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
	}
	
	@PostConstruct
	public void init(){
		MobivateConstant.smsTransAtomicInteger.set(
				daoService.findNextAutoIncrementId("tb_mobivate_sms_trans", dbName));
		
	}
	
	///srs/api/sendsms?
	//USER_NAME=<username>&PASSWORD=<password>&ORIGINATOR=<shortcode>&RECIPIENT=<msisdn>&PROVIDER=<provider>&MESSAGE_TEXT=<msg>
	public void sendMessage(String msisdn,String msgTemplate,
				MobivateServiceConfig mobivateServiceConfig,String action,String formateMoDate){
		//Thank you for subscribing <servicename> for <billingfrequency>/<validitydesc>.
		MobivateSMSTrans numeroMobivateSMSTrans=null;
		try{
		if(msgTemplate==null){
			return;
		}
		numeroMobivateSMSTrans=new MobivateSMSTrans(true);
		numeroMobivateSMSTrans.setAction(action);
		numeroMobivateSMSTrans.setMsisdn(msisdn);
		
		numeroMobivateSMSTrans.setServiceId(mobivateServiceConfig.getServiceId());
		String msg=msgTemplate.replaceAll("<servicename>", mobivateServiceConfig.getServiceName())
				     .replaceAll("<validitydesc>", mobivateServiceConfig.getValidityDesc())
				     .replaceAll("<shortcode>", mobivateServiceConfig.getShortcode())
				     .replaceAll("<keyword>", mobivateServiceConfig.getKeyword())
				     
				     ;
		//https://gateway.mobivate.com/srs/api/sendsms?USER_NAME=<username>&
		//PASSWORD=<password>&ORIGINATOR=<shortcode>&RECIPIENT=<msisdn>&
		//PROVIDER=default&MESSAGE_TEXT=<msg>&VALUE=0
		String url=freeSmsUrlTemplateUrl.replaceAll("<username>", MUtility.urlEncoding(mobivateServiceConfig.getSmsUserName()))
		              .replaceAll("<password>",MUtility.urlEncoding(mobivateServiceConfig.getSmsPassword()))
		              .replaceAll("<shortcode>", MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
		              .replaceAll("<msisdn>", MUtility.urlEncoding(msisdn))
		              .replaceAll("<keyword>", MUtility.urlEncoding(mobivateServiceConfig.getSmsKeyword()))
		              .replaceAll("<started>",MUtility.urlEncoding(formateMoDate))
		              .replaceAll("<msg>",MUtility.urlEncoding(msg));
		
		logger.info("sendMessage::: url:: "+url);
		numeroMobivateSMSTrans.setRequestStr(url);
		numeroMobivateSMSTrans.setMtMessageType(MobivateConstant.FREE_SMS);
		HTTPResponse response=httpURLConnectionUtil.sendHttpsGet(url);
		numeroMobivateSMSTrans.setResponse(response.getResponseCode()+":"+response.getResponseStr());
		logger.info("sendMessage::: numeroMobivateSMSTrans:: "+numeroMobivateSMSTrans);
		}catch(Exception ex){
			logger.error("sendWelcomeMessage::: ",ex);
		}finally{
			if(numeroMobivateSMSTrans!=null){
			daoService.updateObject(numeroMobivateSMSTrans);
			}
		}
	}
	
	public void sendBilledMessage(MobivateServiceConfig mobivateServiceConfig,
			String token,String action,String msisdn,String provider,String formatedDate){
		//Thank you for subscribing <servicename> for <billingfrequency>/<validitydesc>.
				MobivateSMSTrans numeroMobivateSMSTrans=null;
				try{
					if(mobivateServiceConfig.getBilledMessageTemplate()==null){
						return ;
					}
				numeroMobivateSMSTrans=new MobivateSMSTrans(true);
				numeroMobivateSMSTrans.setServiceId(mobivateServiceConfig.getServiceId());
				numeroMobivateSMSTrans.setAction(action);
				numeroMobivateSMSTrans.setToken(token);
				
				String msg=mobivateServiceConfig.getBilledMessageTemplate().replaceAll("<servicename>",
						mobivateServiceConfig.getServiceName())
						     .replaceAll("<validitydesc>", mobivateServiceConfig.getValidityDesc())
						     .replaceAll("<portalurl>", mobivateServiceConfig.getPortalUrl()+msisdn);
				/////srs/api/sendsms?USER_NAME=<username>&PASSWORD=<password>&
				//ORIGINATOR=<shortcode>&RECIPIENT=<msisdn>&PROVIDER=<provider>&
				//MESSAGE_TEXT=<msg>&PROVIDER=<provider>&VALUE=<value>&REFERENCE=<reference>
				//&URL=<portalurl>&STARTED=<started>&KEYWORD=<keyword>&DR_ENDPOINT=<dlr>
				
				String url=billedSmsUrlTemplate.replaceAll("<username>", MUtility.urlEncoding(mobivateServiceConfig.getSmsUserName()))
				              .replaceAll("<password>",MUtility.urlEncoding(mobivateServiceConfig.getSmsPassword()))
				              .replaceAll("<shortcode>", MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
				              .replaceAll("<msisdn>", MUtility.urlEncoding(msisdn))
				              .replaceAll("<provider>", MUtility.urlEncoding(provider))
				              .replaceAll("<msg>",MUtility.urlEncoding(msg))
				              .replaceAll("<value>",MUtility.urlEncoding(mobivateServiceConfig.getSmsValue()))
				              .replaceAll("<reference>",MUtility.urlEncoding(""+numeroMobivateSMSTrans.getId()))
				              .replaceAll("<portalurl>",MUtility.urlEncoding(mobivateServiceConfig.getPortalUrl()+"&msisdn="+msisdn))
				              .replaceAll("<started>",MUtility.urlEncoding(formatedDate))
				              .replaceAll("<keyword>",MUtility.urlEncoding(mobivateServiceConfig.getSmsKeyword()))
				               .replaceAll("<dlr>",MUtility.urlEncoding(dlrUrl))
				               ;
				logger.info("send billed message::: url:: "+url);
				numeroMobivateSMSTrans.setRequestStr(url);
				numeroMobivateSMSTrans.setMtMessageType(MobivateConstant.BILLED_SMS);
				HTTPResponse response=httpURLConnectionUtil.sendHttpsGet(url);
				numeroMobivateSMSTrans.setResponse(response.getResponseCode()+":"+response.getResponseStr());
				logger.info("send billed message::: numeroMobivateSMSTrans:: "+numeroMobivateSMSTrans);
				}catch(Exception ex){
					logger.error("sendBilledMessage::: ",ex);
				}finally{
					if(numeroMobivateSMSTrans!=null){
					jpaMobivateSMSTrans.save(numeroMobivateSMSTrans);
					}
				}
		
	}
}
