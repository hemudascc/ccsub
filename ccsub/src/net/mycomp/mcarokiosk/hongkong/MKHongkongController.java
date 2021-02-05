package net.mycomp.mcarokiosk.hongkong;

import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.common.service.RedisCacheService;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.OperatorRequestService;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping("mkhk")
public class MKHongkongController {

	
	private static final Logger logger = Logger
			.getLogger(MKHongkongController.class.getName());

	
	@Autowired
	private MacroKioskHongkongFactoryService macroKioskHongkongFactoryService;
	
	@Value("${macrokiosk.hongkong.mo.lp2Url}")
	private String lp2Url;
	
	@Value("${macrokiosk.hongkong.mo.welcometext}")
	private String welcomeText;
	
	@Value("${macrokiosk.hongkong.mo.countryCode}")
	private String countryCode;
	
	@Autowired
	private JMSHongkongService jmsHongkongService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private OperatorRequestService operatorRequestService;
	
	@RequestMapping("mo")
	@ResponseBody
	public String mo(HttpServletRequest request,ModelAndView modelAndView) {
		logger.info("mo:::::::::: "+request.getQueryString());
		HongkongMOMessage hongkongMOMessage=null;
		VWServiceCampaignDetail vwServiceCampaignDetail=null;
		try{
		logger.info("mo:::::::::::::::::::::::::::::::: "+request.getQueryString());		
//		redisCacheService.getValueString(msisdn)
		hongkongMOMessage=new HongkongMOMessage(true);		
		hongkongMOMessage.setMsisdn(request.getParameter("from"));		
		hongkongMOMessage.setTime(request.getParameter("time"));
		hongkongMOMessage.setReqTime(MKHongkongConstant.convertStringToTimestamp(hongkongMOMessage.getTime()));
		hongkongMOMessage.setShortcode(request.getParameter("shortcode"));
		hongkongMOMessage.setMoid(request.getParameter("moid"));
		hongkongMOMessage.setMsgid(request.getParameter("msgid"));
		hongkongMOMessage.setTelcoid(MUtility.toInt(request.getParameter("telcoid"),0));
		hongkongMOMessage.setPlatform(request.getParameter("platform"));
		String refId =(String)redisCacheService.getObjectCacheValue(MKHongkongConstant.MO_MESSAGE_CAHCHE_PREFIX+hongkongMOMessage.getMsisdn());
		String adNetworkId =(String)redisCacheService.getObjectCacheValue(MKHongkongConstant.HONGKONG_AD_NETWORK_CAHCHE_PREFIX+hongkongMOMessage.getMsisdn());
		
		hongkongMOMessage.setRefId(refId);
		
		logger.info("RefId :::::::::::::::::::::::::::::: "+refId+" adNetworkId: "+adNetworkId);
		hongkongMOMessage.setQueryStr(request.getQueryString());
		hongkongMOMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
		logger.info("CmpIdId :::::::::::::::::::::::::::::: "+new MKHongkongCGToken(hongkongMOMessage.getRefId()).getCampaignId());
		vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail
				.get(new MKHongkongCGToken(hongkongMOMessage.getRefId()).getCampaignId());
		logger.info("adNetworkRequestBean.vwserviceCampaignDetail :::::::::::::::::::::::::::::: "+vwServiceCampaignDetail);
		if(vwServiceCampaignDetail==null){
			vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail
						.entrySet().stream().filter(map ->
						map.getValue().getServiceId()==MConstants.WASTE_SERVICE_ID&&
						map.getValue().getAdNetworkId()==Integer.parseInt(adNetworkId))
						.findFirst().get().getValue();
			}
		logger.info("vwServiceCampaignDetail.getOpId(): "+vwServiceCampaignDetail.getOpId());
		hongkongMOMessage.setOpId(vwServiceCampaignDetail.getOpId());  
		hongkongMOMessage.setText(request.getParameter("text"));
		if(hongkongMOMessage.getText()!=null&&hongkongMOMessage.getText().
				toUpperCase().contains("OFF")){
			hongkongMOMessage.setKeyword(hongkongMOMessage.getText().split(" ")[1]);
		}else{
			hongkongMOMessage.setKeyword(hongkongMOMessage.getText());
		}
		}catch(Exception ex){
			logger.error("Exception",ex);
		}finally{
			jmsHongkongService.saveMOMessage(hongkongMOMessage);	
		}
		return "OK";		
	}
	
	@RequestMapping("dn")
	@ResponseBody
	public String dn(HttpServletRequest request,ModelAndView modelAndView) {
		logger.info("dn:::::::::: "+request.getQueryString());
		
		HongkongDeliveryNotification hongkongDeliveryNotification=null;
		try{
		logger.info("deliveryNotification:::::::::::::::::::::::::::::::: "+request.getQueryString());
		//// http://www.yourdomainDNurl/receive.aspx?mtid=123296707&moid=1234567&
		// msisdn=66874111222&shortcode=4541889&telcoid=1&countryid=3&datetime=2010-06-15
		// 10:10:10&status=OK
		hongkongDeliveryNotification=new HongkongDeliveryNotification(true);
		hongkongDeliveryNotification.setMtid(request.getParameter("mtid"));
		hongkongDeliveryNotification.setMoid(request.getParameter("moid"));		
		hongkongDeliveryNotification.setMsisdn(request.getParameter("msisdn"));
		hongkongDeliveryNotification.setDatetime(MKHongkongConstant.convertStringToTimestamp(request.getParameter("datetime")));
		hongkongDeliveryNotification.setShortcode(request.getParameter("shortcode"));	
		hongkongDeliveryNotification.setTelcoId(MUtility.toInt(request.getParameter("telcoid"),0));
		hongkongDeliveryNotification.setCountryId(MUtility.toInt(request.getParameter("countryid"),0));		
		hongkongDeliveryNotification.setQueryStr(request.getQueryString());
		hongkongDeliveryNotification.setCreateTime(new Timestamp(System.currentTimeMillis()));
		hongkongDeliveryNotification.setStatus(request.getParameter("status"));		
		hongkongDeliveryNotification
		.setOpId(HongkongOperatorTelcoidMap.getOperatorId(hongkongDeliveryNotification.getTelcoId()));
		
		}catch(Exception ex){
			logger.error("exception",ex);
		}finally{
			jmsHongkongService.saveHongkongDeliveryNotification(hongkongDeliveryNotification);
		}		
		return "ok";
	}
	

	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest  request){
		
		String token=request.getParameter("token");
		logger.info("toMo:: url: "+lp2Url);
		modelAndView.setViewName(lp2Url);
		return modelAndView;	
	}
	

	@RequestMapping("lp-2")
	public ModelAndView freeMt(ModelAndView modelAndView,HttpServletRequest  request){		
		String opid=request.getParameter("opid");
		String msisdn=request.getParameter("msisdn");
		String token = request.getParameter("token");
		String telcoid = request.getParameter("telcoid");
		String adNetworkId = request.getParameter("adNetworkId");
		redisCacheService.putObjectCacheValueByEvictionDay(MKHongkongConstant.MO_MESSAGE_CAHCHE_PREFIX+msisdn,
				token, 1);
		redisCacheService.putObjectCacheValueByEvictionDay(MKHongkongConstant.HONGKONG_AD_NETWORK_CAHCHE_PREFIX+msisdn,
				adNetworkId, 1);
		logger.info("opid: "+opid+"  telcoid : "+telcoid+"  msisdn: "+msisdn+"  token: "+token+"  adNetworkId: "+adNetworkId);
		HongkongMOMessage hongkongMOMessage = new HongkongMOMessage();
		try {	
		String tokenStr[] = token.split(MConstants.TOKEN_SEPERATOR);
		hongkongMOMessage.setTokenId(Integer.parseInt(tokenStr[0]));
		hongkongMOMessage.setCampaignId(Integer.parseInt(tokenStr[1]));
		hongkongMOMessage.setMsisdn((msisdn.startsWith(countryCode))?msisdn:countryCode+msisdn);
		hongkongMOMessage.setToken(token);
		hongkongMOMessage.setTelcoid(MUtility.toInt(telcoid,0));
		hongkongMOMessage.setText(welcomeText);
		hongkongMOMessage.setOpId(Integer.parseInt(opid));
		hongkongMOMessage.setIsFreeMt(true);
		}catch(Exception ex){
			logger.error("exception",ex);
		}finally {
			boolean response=  macroKioskHongkongFactoryService.handleSubscriptionMOMessage(hongkongMOMessage);
			logger.info("response: "+response + "  msisdn: "+ hongkongMOMessage.getMsisdn());
			modelAndView.setViewName("mkhongkong/lp2");
		}
		
		return modelAndView;	
	}  

}
