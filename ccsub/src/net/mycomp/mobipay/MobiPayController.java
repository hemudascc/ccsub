package net.mycomp.mobipay;

import java.sql.Timestamp;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;

@Controller
@RequestMapping("mobipay")
public class MobiPayController {

	private static final Logger logger = Logger.getLogger(MobiPayController.class);
	@Autowired
	private SubscriberRegService subscriberRegService; 
	@Autowired
	private JMSMobiPayService jmsMobiPayService;

	@RequestMapping("cgcallback/{token}")
	public ModelAndView cgCallback(HttpServletRequest request, 
			ModelAndView modelAndView, @PathVariable(value="token")String token) {
		logger.info("callback::::::::: "+request.getQueryString());
		VWServiceCampaignDetail vwServiceCampaignDetail=null;
		LiveReport liveReport=null;
		CGToken cgToken=null;
		Integer subId=0;
		SubscriberReg subscriberReg=null;
		MobiPayServiceConfig mobiPayServiceConfig=null;
		MobiPayCGCallback mobiPayCGCallback = new MobiPayCGCallback(Boolean.TRUE);
		try {
			mobiPayCGCallback.setCgStatus(request.getParameter("status"));
			mobiPayCGCallback.setUserId(request.getParameter("userid"));
			mobiPayCGCallback.setNetwork(request.getParameter("network"));
			mobiPayCGCallback.setToken(token);
			if(Objects.nonNull(mobiPayCGCallback.getCgStatus()) && MobiPayConstant.SUBSCRIBED.equals(mobiPayCGCallback.getCgStatus())) {
				cgToken=new CGToken(token);
				vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail
						.get(cgToken.getCampaignId());
				mobiPayServiceConfig = MobiPayConstant
						.mapServiceIdToMobiPayServiceConfig.get(vwServiceCampaignDetail.getServiceId());
				liveReport = new LiveReport(mobiPayServiceConfig.getOperatorId(),
						new Timestamp(System.currentTimeMillis()),
						-1, mobiPayServiceConfig.getServiceId(), mobiPayServiceConfig.getProductId());

				subscriberReg = subscriberRegService.findOrCreateSubscriberByAct(mobiPayCGCallback.getUserId()
						, null, liveReport);
				if(Objects.nonNull(subscriberReg)) {
					subId = subscriberReg.getSubscriberId();
				}
			}
			String portalURL = MobiPayConstant
					.getPortal(mobiPayServiceConfig.getPortalURL(), mobiPayCGCallback.getUserId(), subId);
			mobiPayCGCallback.setPortalURL(portalURL);
			modelAndView.setView(new RedirectView(portalURL));
		} catch (Exception e) {
			logger.error("error while processing cg callback"+request.getQueryString());
		}finally {
			jmsMobiPayService.saveMobiPayCGCallback(mobiPayCGCallback);
		}
		return modelAndView;
	}

	
	@RequestMapping(value={"dlr"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String dlr(HttpServletRequest request,ModelAndView modelAndVie){
		MobiPayDlr mobiPayDlr = new MobiPayDlr(Boolean.TRUE);
		//ORIGINATOR=65065&REFERENCE=1596716925096c82554931c253&
		//MESSAGE_TEXT=PFI+sID%3D6204+type%3DSUB+tID%3D109282625+%28origTID%3D109282625%29%3A+GYKEER99&CHANGED=False&
		//VALUE=1.0&RCPT=2020-08-06T18%3A27%3A18.466314&RECIPIENT=447400000000&RECEIPTED=2020-08-06T18%3A27%3A18.466314&
		//PROVIDER=pfi&ID=MT-90aed2c7514a4c0b813fee7c8259f4c2&SENT=2020-08-06T18%3A27%3A18.461176&RESULT=503
		try{
		logger.info("dlr::: "+request.getQueryString());	
		mobiPayDlr=new MobiPayDlr(true);
		mobiPayDlr.setMsisdn(request.getParameter("RECIPIENT"));
		mobiPayDlr.setMessageText(request.getParameter("MESSAGE_TEXT"));
		mobiPayDlr.setProvider(request.getParameter("PROVIDER"));
		mobiPayDlr.setReference(request.getParameter("REFERENCE"));
		mobiPayDlr.setDlrId(request.getParameter("ID"));
		mobiPayDlr.setResult(request.getParameter("RESULT"));
		mobiPayDlr.setQueryStr(request.getQueryString());
		}catch(Exception ex){
			logger.error("dlr ",ex);
		}finally{
			jmsMobiPayService.saveMobiPayDlrJMSTemplate(mobiPayDlr);
		}
		return "OK";
	}
}
