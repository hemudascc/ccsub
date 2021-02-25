package net.mycomp.beecel.jordon;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;


@Controller
@RequestMapping("beecell")
public class BCJordonController {
	
	private static final Logger logger = Logger
			.getLogger(BCJordonController.class.getName());

	
	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest  request){
		
		String token=request.getParameter("token");
		BCJordonCGToken bcJordonCGToken=new BCJordonCGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(bcJordonCGToken.getCampaignId());
		
	BCJordonConfig bcJordonConfig=BCJordonConstant
			.mapServiceIdToBCJordonConfig   
			.get(vwServiceCampaignDetail.getServiceId());
		String url=BCJordonConstant.CG_URL.replaceAll("<cid>", token).replaceAll("<t>",bcJordonConfig.getT() ).replaceAll("<pub>",bcJordonConfig.getPub());	
		logger.info("toMo:: url: "+url);
		modelAndView.setView(new RedirectView(url));
		return modelAndView;	
	}

	@RequestMapping("cgcallback")
	public String cgcallback(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("cgCallBackUrl:::::::::: "+request.getQueryString());
		return "Ok";	
	}
	
	@RequestMapping("notification")
	public String notification(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("notification:::::::::: "+request.getQueryString());
		return "Ok";	
	}  
	
	@RequestMapping("mt/dlr")
	public String dlr(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("mt/dlr:::::::::: "+request.getQueryString());
		return "Ok";	
	}
}
