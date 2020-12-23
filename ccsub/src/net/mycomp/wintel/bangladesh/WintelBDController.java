package net.mycomp.wintel.bangladesh;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("wbd")
public class WintelBDController {

	private static final Logger logger = Logger
			.getLogger(WintelBDController.class.getName());
	
	@Autowired
	private JMSWintelBDService jmsWintelBDService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private WintelBDServiceApi wintelBDServiceApi;
	
	@Value("${wintelbd.portal.url}")
	private String portalUrl;
	
	@Value("${wintelbd.campaign.url}")
	private String campaignUrl;
	
	
	@RequestMapping("mo")
	@ResponseBody
	public String mo(HttpServletRequest request){
		WintelBDMO wintelBDMO=null;
		try{
			//http://xxxxxxxxx:xxxx/xxxx?client_id=clcnt&shortcode=shortcode&msisdn=msisdn
			//&message=message&transid=transid&operator=operator&serviceid=serviceid
			//client_id=clcnt&shortcode=16466&msisdn=576829965374122770&message=cs%20c
			//&transid=3040211171571306553504156&operator=grameenphone&serviceid=PPU00027603111
			wintelBDMO=new WintelBDMO(true);
			wintelBDMO.setQuery_str(request.getQueryString());
			wintelBDMO.setMsisdn(request.getParameter("msisdn"));
			wintelBDMO.setClientId(request.getParameter("client_id"));
			wintelBDMO.setShortcode(request.getParameter("shortcode"));
			wintelBDMO.setTransid(request.getParameter("transid"));
			wintelBDMO.setOperator(request.getParameter("operator"));
			wintelBDMO.setServiceid(request.getParameter("serviceid"));		
			wintelBDMO.setMessage(request.getParameter("message"));
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			jmsWintelBDService.saveMO(wintelBDMO);
		}
		return "ok";
	}
	
	//Wintel BD Notification url

//http://192.241.253.234/ccsub/cnt/wbd/charge/notification
	@RequestMapping("charge/notification")
	@ResponseBody
	public String notification(HttpServletRequest request){
		
		logger.info("notification::::::::: "+request.getQueryString());
		try{
			//http://xxxxxxxxx:xxxx/xxxx?client_id=clcnt&shortcode=shortcode&msisdn=msisdn
			//&message=message&transid=transid&operator=operator&serviceid=serviceid
			
		
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			
		}
		return "ok";
	}
	
	@RequestMapping("renewal")	
	public ModelAndView renewal(HttpServletRequest request,ModelAndView modelAndView){
		
		logger.info("renewal::::::::: "+request.getQueryString());
		WintelBDMO wintelBDMO=null;
		try{
			String msisdn=request.getParameter("msisdn");
			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, 8);
			if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
				modelAndView.setView(new RedirectView(campaignUrl));
				return modelAndView;
			}
			
			WintelBDServiceConfig wintelBDServiceConfig=
					 WintelBDConstant.mapServiceIdToWintelBDServiceConfig.get(subscriberReg.getServiceId());
			logger.info("renewal::::::::subscriberReg::::::  "+subscriberReg);
			logger.info("renewal::::::::day diffrence::::::  "
			        +MUtility.timeDiffrence(new Timestamp(System.currentTimeMillis()),
					subscriberReg.getValidityTo()));
			
			
			if(MUtility.timeDiffrence(new Timestamp(System.currentTimeMillis()),
					subscriberReg.getValidityTo())<=0){	
				modelAndView.setView(new RedirectView(campaignUrl));
				return modelAndView;
				
//				wintelBDMO=new WintelBDMO(true);
//				wintelBDMO.setAction(MConstants.RENEW);
//				wintelBDMO.setQuery_str(request.getQueryString());
//				wintelBDMO.setMsisdn(request.getParameter("msisdn"));
//				wintelBDMO.setOperator(wintelBDServiceConfig.getOperator());
//				wintelBDMO.setTransid(subscriberReg.getParam1());

		
			}
			String portalUrl=wintelBDServiceConfig.getPortalUrl().replaceAll("<msisdn>", msisdn);			
			modelAndView.setView(new RedirectView(portalUrl));
		}catch(Exception ex){
			logger.error("renewal:: Exception ",ex);
			modelAndView.setView(new RedirectView(portalUrl));
		}finally{
		jmsWintelBDService.saveMO(wintelBDMO);	
		}
		return modelAndView;
	}
	
}
