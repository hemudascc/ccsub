package net.indonesia.triyakom;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.CommonService;
import net.common.service.IDaoService;
import net.common.service.IpCheckService;
import net.factory.RequestFactory;
import net.jpa.repository.JPAIndonesiaMTMessage;
import net.persist.bean.Adnetworks;
import net.persist.bean.ErrorInfo;
import net.persist.bean.IPPool;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.CGToken;
import net.util.MUtility;

@Controller("indonesia")
@RequestMapping("indonesia")
public class TriyakomController {

	private static final Logger logger = Logger.getLogger(TriyakomController.class);

	@Autowired
	private IDaoService daoService;
	
	
	@Autowired
	private TriyakomService triyakomService;
	
	@Autowired
	private SchedularConfig schedularConfig;
	

	@Autowired
	private TriyakomOperatorService triyakomOperatorService;
	
	@Autowired
	private JPAIndonesiaMTMessage jpaIndonesiaMTMessage;
	
	@Autowired
	private JMSIndonesiaService jmsIndonesiaService;
	
	@Autowired
	private RequestFactory requestFactory;
	@Autowired
	private CommonService commonService;
	@Autowired
	private IpCheckService ipCheckService;

	@PostConstruct
	public void init() {
				
	}

	
	@RequestMapping("mo/callback")
	@ResponseBody
	public String receivedSMS(
			@RequestParam(value="X-Source-Addr",required=false) String msisdn,
			@RequestParam(value="X-Dest-Addr",required=false)String destAddress,
			@RequestParam(value="_sc",defaultValue="",required=false)String text,
			@RequestParam(value="_tid",required=false)String txId,
			@RequestParam(value="op",defaultValue="",required=false)String op,
			HttpServletRequest request){
		
		MOMessage moMessage=new MOMessage();
		moMessage.setMsisdn(msisdn);
		moMessage.setOp(op);
		moMessage.setShortCode(destAddress);
		moMessage.setText(text);
		moMessage.setTxId(txId);
		moMessage.setCreateDate(new Timestamp(System.currentTimeMillis()));
		moMessage.setQueryStr(request.getQueryString());
		if(text!=null){
		String str[]=text.split(" ");
		if(str!=null&&str.length>=2){
			moMessage.setActivationKey(str[0]);
			moMessage.setServiceKey(str[1]);
		}
		
		if(str!=null&&str.length>=3){
			CGToken cgToken=new CGToken(str[2]); 
			moMessage.setTokenId(cgToken.getTokenId());
			moMessage.setToken(cgToken.getCGToken());
			moMessage.setCampaignId(cgToken.getCampaignId());
		}
		}
		//logger.info("indonesiaSmsReceived:::::::::: "+moMessage+", query String:: "+request.getQueryString());
		jmsIndonesiaService.saveMOMessage(moMessage);
		return "200";		
	}
	
	
	@RequestMapping("dn/callback")
	@ResponseBody
	public String dlr(@RequestParam(value="_tid") String tid,
			@RequestParam(value="status_id")String statusId,
			@RequestParam(value="dtdone",defaultValue="")String dtdone,@RequestParam(value="op")String op,
			HttpServletRequest request){	
		//logger.info("dlr:::::::::::: query String:: "+request.getQueryString());
		//logger.info("dlr:: tid: "+tid+", statusId:: "+statusId+", dtdone: "+dtdone+", op: "+op);
		DLRNotification dlrNotification=new DLRNotification();
		dlrNotification.setTid(tid);
		dlrNotification.setStatusId(statusId);
		dlrNotification.setDtdone(dtdone);
		dlrNotification.setOp(op);
		dlrNotification.setQueryStr(request.getQueryString());
		dlrNotification.setReceivedTime(new Timestamp(System.currentTimeMillis()));		
		jmsIndonesiaService.saveDLR(dlrNotification);
		return "200";		
	}
	
	
	
	@RequestMapping("mt")
	@ResponseBody
	public String mt(@RequestParam(value="dest_addr") String msisdn,
			@RequestParam(value="app_id")String appId,
			@RequestParam(value="app_pwd",defaultValue="")String appPwd,
			@RequestParam(value="data")String data,
			@RequestParam(value="op",defaultValue="")String op,
			@RequestParam(value="rtx_id",defaultValue="")String rtxId,
			@RequestParam(value="service",defaultValue="")String service,
			@RequestParam(value="alphabet",required=false,defaultValue="")String alphabet
			,HttpServletRequest request){
		
		//logger.info("mt:::::::::::: query String:: "+request.getQueryString());
		MTMessage mtMessage=new MTMessage(TriyakomConstant.MT_NEW_REQUEST);
		mtMessage.setDestAddr(msisdn);
		mtMessage.setAppId(appId);
		mtMessage.setAppPwd(appPwd);
		mtMessage.setData(data);
		mtMessage.setOp(op);
		mtMessage.setRtxId(rtxId);
		mtMessage.setService(service);
		mtMessage.setAlphabetd(alphabet);
		//mtMessage.setAction(TriyakomConstant.MT_NEW_REQUEST);
		//logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
		jmsIndonesiaService.saveMTMessage(mtMessage);
		return "200";		
	}
	
	
	@RequestMapping("sendmt")
	@ResponseBody
	public String sendMT(@RequestParam(value="mtid") Integer mtId){
		
		MTMessage mtMessage=jpaIndonesiaMTMessage.findMTMessageById(mtId);
		 mtMessage.setAction(TriyakomConstant.MT_GRACE_BILLING);
		logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
		jmsIndonesiaService.saveMTMessage(mtMessage);
		return "200";		
	}
	
	
	@RequestMapping("sendmt/renewal")
	@ResponseBody
	public String sendMTRenewal(@RequestParam(value="mtid") Integer mtId){
		
		MTMessage mtMessage=jpaIndonesiaMTMessage.findMTMessageById(mtId);
		
		//logger.info("indonesiaSmsReceived:::::::::: "+mtMessage);
		//triyakomOperatorService.handleSubscriptionMTRenewalPushMessage(mtMessage);
		return "200";		
	}
	
	
	@RequestMapping("send/renewal")
	@ResponseBody
	public String sendRenewal(){
		schedularConfig.sendRenewalBilled();		  
		return "200";		
	}
	
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String error(HttpServletRequest request,Exception ex){
		logger.error("error:: query  string: "+request.getQueryString()+", Exception:: "+ex);
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
		errorInfo.setQueryStr(" query str ="+request.getQueryString());
		errorInfo.setErrorDesc(ex.toString());
		daoService.saveObject(errorInfo);
		return "ERROR";
	}
	
}
