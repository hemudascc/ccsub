package net.mycomp.cornet.sudan;

import java.sql.Timestamp;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.activemq.command.IntegerResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;
import net.util.MUtility;


@Controller
@RequestMapping("cornet")
public class CornetController {

	private static final Logger logger = Logger.getLogger(CornetController.class);
  
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private CornetServiceApi cornetServiceApi;
	
	@Autowired
	private CornetJMSService cornetJMSService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping("test")
	@ResponseBody
	public String toCG(ModelAndView modelAndView,HttpServletRequest  request){
		
		
		return "working";	
	}
	
	
	@RequestMapping(value="send-pin", method=RequestMethod.POST)
	public ModelAndView sendPin(HttpServletRequest request, ModelAndView modelAndView) {
		VWServiceCampaignDetail vwserviceCampaignDetail = null;
		CornetConfig cornetConfig=null;
		try {	
			String token = request.getParameter("token");
			String msisdn = request.getParameter("msisdn");  
			String accessToken = request.getParameter("accessToken");
			CGToken cgToken = new CGToken(token);
			vwserviceCampaignDetail= MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			cornetConfig = CornetConstant.mapServiceIdToCornetConfig
					  .get(vwserviceCampaignDetail.getServiceId());
			SubscriberReg subscriberReg =jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, vwserviceCampaignDetail.getProductId());
			if(Objects.nonNull(subscriberReg) && subscriberReg.getStatus()==1) {
				modelAndView.setView(new RedirectView(cornetConfig.getPortalUrl().replaceAll("<subid>", msisdn)));
				return modelAndView;
			}
			modelAndView.addObject("msisdn", msisdn);
			modelAndView.addObject("token", token);
			modelAndView.addObject("accessToken", accessToken);
			modelAndView.addObject("cornetConfig", cornetConfig);
			modelAndView.setViewName("cornetsudan/lp");
			String response = cornetServiceApi.pinSend(token, accessToken,msisdn, "web");
			if(Objects.nonNull(response) && response.equals("0")) {
				modelAndView.addObject("status", Integer.parseInt(response));
				modelAndView.setViewName("cornetsudan/verifyotp");
			}else {
				modelAndView.addObject("status", Integer.parseInt(response));
			}
		} catch (Exception e) {
			modelAndView.addObject("cornetConfig", cornetConfig);
			modelAndView.addObject("status", 3);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="verify-pin", method=RequestMethod.POST)
	public ModelAndView verifyPin(HttpServletRequest request, ModelAndView modelAndView) {
		VWServiceCampaignDetail vwserviceCampaignDetail = null;
		CornetConfig cornetConfig=null;
		try {  
			String token = request.getParameter("token");
			String msisdn = request.getParameter("msisdn");
			String accessToken = request.getParameter("accessToken");
			int pin = MUtility.toInt(request.getParameter("pin"),0);
			CGToken cgToken = new CGToken(token);
			redisCacheService.putObjectCacheValueByEvictionDay(CornetConstant.CORNET_UNIQUE_TOKEN_PREFIX+msisdn,token, 2);
			vwserviceCampaignDetail= MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			cornetConfig = CornetConstant.mapServiceIdToCornetConfig
					  .get(vwserviceCampaignDetail.getServiceId());
			modelAndView.addObject("msisdn", msisdn);
			modelAndView.addObject("token", token);  
			modelAndView.addObject("accessToken", accessToken);
			modelAndView.addObject("cornetConfig", cornetConfig);
			String response = cornetServiceApi.verifyPin(token,accessToken, msisdn, pin, "web");
			if(Objects.nonNull(response) && response.equals("0")) {
				modelAndView.addObject("status", 0);
				modelAndView.addObject("portalURL", cornetConfig.getPortalUrl().replaceAll("<subid>", msisdn));
				modelAndView.setViewName(cornetConfig.getPortalUrl().replaceAll("<subid>", msisdn));
			}else {
				modelAndView.addObject("status", Integer.parseInt(response));
				modelAndView.setViewName("cornetsudan/verifyotp");
			}
		} catch (Exception e) {
			modelAndView.addObject("status", 2);
			modelAndView.setViewName("cornetsudan/verifyotp");
		}
		return modelAndView;
	}
	
	@RequestMapping("unsubscribe/{productId}/{msisdn}")
	public ModelAndView unsubscribe(@PathVariable("productId") Integer productId,@PathVariable("msisdn") String msisdn,
			ModelAndView modelAndView) {
		String status = null;
		modelAndView.setViewName("cornetsudan/unsubscribe");
		status = cornetServiceApi.unsubscribe(msisdn,productId);
		if(Objects.nonNull(status) && "1".equals(status)) {
			modelAndView.addObject("message", "You have been unsubscribe from the service");
		}else {
			modelAndView.addObject("message", "Failed to unsubscribe from the service");
		}
		return modelAndView;
	}
	  
	@RequestMapping(value="callback",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> callback(@RequestBody CornetNotification cornetCallback) {
		try {
			String token =(String)redisCacheService.getObjectCacheValue(CornetConstant.CORNET_UNIQUE_TOKEN_PREFIX+cornetCallback.getMsisdn());
			cornetCallback.setToken(token);	
			cornetCallback.setCreateTime(new Timestamp(System.currentTimeMillis()));
			cornetJMSService.saveCornetCallback(cornetCallback);
		} catch (Exception e) {
			logger.error("error while saving callback altruist "+cornetCallback);
		}  
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@RequestMapping("tc")
	public ModelAndView tc(ModelAndView modelAndView) {
		modelAndView.setViewName("cornetsudan/tc");
		return modelAndView;
	}
}
