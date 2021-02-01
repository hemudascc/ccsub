package net.mycomp.mt2.zain.iraq;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.process.bean.CGToken;
import net.util.MConstants;

@Controller
@RequestMapping("mt2zainiraq")
public class Mt2ZainIraqController {

	
	private static final Logger logger = Logger
			.getLogger(Mt2ZainIraqController.class.getName());
	
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSService jmsService;
	
   @Autowired
   private RedisCacheService redisCacheService;
	
	@Autowired
	private JMSMt2ZainIraqService jmsMt2ZainIraqService;
	
	@Autowired
	private Mt2ZainIraqServiceApi mt2ZainIraqServiceApi;
	
	@RequestMapping(value={"success"},method={RequestMethod.GET,RequestMethod.POST})
	
	public ModelAndView success(HttpServletRequest request,ModelAndView modelAndView){
		
		MT2ZainCGCallback mt2ZainCGCallback = new MT2ZainCGCallback(true);
		try{
			logger.info("success:: "+request.getQueryString());
			//uniqid=4e4916633f7838ea949a730b6c640702&success=1&msisdn=9647903190268
			
			mt2ZainCGCallback.setMsisdn(request.getParameter("msisdn"));
			mt2ZainCGCallback.setUniqeId(request.getParameter("uniqid"));
			mt2ZainCGCallback.setSuccess(request.getParameter("success"));
			mt2ZainCGCallback.setQueryStr(request.getQueryString());
					
			
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2ZainIraqService.saveMt2ZainIraqCGCallback(mt2ZainCGCallback);
			modelAndView.setViewName("mt2zainiraq/success");
		}
		return modelAndView;
	}
	
	@RequestMapping(value={"failure"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView failure(HttpServletRequest request,ModelAndView modelAndView){
		MT2ZainCGCallback mt2ZainCGCallback=new MT2ZainCGCallback(true);
		try{
			logger.info("failure:: "+request.getQueryString());
			//uniqid=022bb1939fb803e50dbc1a2f0a015e69&fail=1&reason=Block
			
			mt2ZainCGCallback.setMsisdn(request.getParameter("msisdn"));
			mt2ZainCGCallback.setUniqeId(request.getParameter("uniqid"));
			mt2ZainCGCallback.setReason(request.getParameter("reason"));
			mt2ZainCGCallback.setQueryStr(request.getQueryString());
					
			
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2ZainIraqService.saveMt2ZainIraqCGCallback(mt2ZainCGCallback);
			modelAndView.setViewName("mt2zainiraq/failure");
		}
		return modelAndView;
	}
	
	/*
	 * @RequestMapping(value={"notification"},method={RequestMethod.GET,
	 * RequestMethod.POST})
	 * 
	 * @ResponseBody public String notification(HttpServletRequest
	 * request,ModelAndView modelAndView){ Mt2ZainIraqNotification
	 * mt2ZainIraqNotification=new Mt2ZainIraqNotification(true); try{
	 * logger.info("notification:: "+request.getQueryString());
	 * mt2ZainIraqNotification.setTransId(request.getParameter("TransId"));
	 * mt2ZainIraqNotification.setMsisdn(request.getParameter("msisdn"));
	 * mt2ZainIraqNotification.setActionType(request.getParameter("actionType"));
	 * mt2ZainIraqNotification.setServiceId(request.getParameter("serviceId"));
	 * mt2ZainIraqNotification.setDate(request.getParameter("date"));
	 * mt2ZainIraqNotification.setRequestid(request.getParameter("requestid"));
	 * mt2ZainIraqNotification.setSc(request.getParameter("sc"));
	 * mt2ZainIraqNotification.setKey(request.getParameter("key"));
	 * mt2ZainIraqNotification.setQueryStr(request.getQueryString());
	 * 
	 * }catch(Exception ex){ logger.error("Exception" ,ex); }finally{
	 * jmsMt2ZainIraqService.saveMt2ZainIraqNotification(mt2ZainIraqNotification); }
	 * return "1"; }
	 */
	
	
	@RequestMapping(value={"notification"},method={RequestMethod.GET,RequestMethod.POST})	
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView){
		Mt2ZainIraqNotification mt2ZainIraqNotification=new Mt2ZainIraqNotification(true);
		try{
			//Sub , Get request : PartnerURL?Id=<id from MT2 SDP>&Data=S,<serviceid>,<subscriberReferenceID>
			//&MSISDN=<msisdn>&ShortCode=<shortcode>&Date=20201021&Operator= Zain Iraq&ValidityDays=<days>
			
			//Unsub ,Get request : PartnerURL?Id=<id from MT2 SDP>&Data=U, <serviceid>
			//&MSISDN=<msisdn>&ShortCode=<shortcode>&Date=20201021&Operator= Zain Iraq
			
			String action = request.getParameter("Data").split(",")[0].equals("S")?MConstants.ACT:MConstants.DCT;
			logger.info("notification:: "+request.getQueryString());
			mt2ZainIraqNotification.setMt2id(request.getParameter("Id"));
			mt2ZainIraqNotification.setData(request.getParameter("Data"));
			mt2ZainIraqNotification.setMsisdn(request.getParameter("MSISDN"));
			mt2ZainIraqNotification.setShortCode(request.getParameter("ShortCode"));
			mt2ZainIraqNotification.setDate(request.getParameter("Date"));
			mt2ZainIraqNotification.setOperator(request.getParameter("Operator"));
			mt2ZainIraqNotification.setValidityDays(request.getParameter("ValidityDays"));
			mt2ZainIraqNotification.setAction(action);
			mt2ZainIraqNotification.setQueryString(request.getQueryString());
			mt2ZainIraqNotification.setMtStatus(request.getParameter("Data").split(",")[0]);
			mt2ZainIraqNotification.setMt2ServiceId(request.getParameter("Data").split(",")[1]);
			if(request.getParameter("Data").split(",").length>2) {
				mt2ZainIraqNotification.setSubscriberReferenceID(request.getParameter("Data").split(",")[2]);
			}
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2ZainIraqService.saveMt2ZainIraqNotification(mt2ZainIraqNotification);
		}
		return "";
	}
	@RequestMapping(value={"dlr"},method={RequestMethod.GET,RequestMethod.POST})	
	@ResponseBody
	public String dlr(@RequestBody List<Mt2ZainIraqDeliveryNotification> dlrs){
		
		try{
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2ZainIraqService.saveMt2ZainIraqDlr(dlrs);
		}
		return "";
	}
	
	
	@RequestMapping(value={"wifi/error"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView wifiError(HttpServletRequest request,ModelAndView modelAndView){
		
		try{
			modelAndView.setViewName("mt2zainiraq/wifi_error");
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			
		}
		return modelAndView;
	}
	@RequestMapping(value={"lp"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView lp(HttpServletRequest request,ModelAndView modelAndView){
		MT2ZainIraqServiceApiTrans mt2ZainIraqServiceApiTrans=null;
		try{
			
				
				Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=Mt2ZainIraqConstant
						.mapServiceIdToMt2ZainIrqServiceConfig
						.get(83);
				modelAndView.addObject("mt2ZainIraqServiceConfig",mt2ZainIraqServiceConfig);
				String token=request.getParameter("token");
				if(token==null){
					token=Objects.toString(redisCacheService.getObjectCacheValue(
							Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_SOURCE_CACHE_PREFIX
							+request.getRemoteAddr()));
				}
				
				CGToken cgToken=null;
				if(token!=null){
					 cgToken=new CGToken(token);
				}else{
				//http://192.241.253.234/ccsub/cnt/cmp?adid=1&cmpid=194&token=zain
				 cgToken=new CGToken(System.currentTimeMillis(), -1, 194); 
				}
				
				modelAndView.addObject("token",cgToken.getCGToken());
				Enumeration<String> en = request.getHeaderNames();
				Map<String, String> headerMap = new HashMap<String, String>();
				while (en.hasMoreElements()) {
					String key = en.nextElement();				
					headerMap.put(key, request.getHeader(key));
				}
				
				logger.info("lp:: header: "+headerMap);
				 mt2ZainIraqServiceApiTrans=
						mt2ZainIraqServiceApi.getScriptSource(mt2ZainIraqServiceConfig,
								cgToken.getCGToken()
					, headerMap,request.getRemoteAddr(),"http://192.241.253.234/ccsub/cnt/mt2zainiraq/lp");
				
				String uniqid=null;
				if(mt2ZainIraqServiceApiTrans.getResponseToCaller()){
					//Map map=JsonMapper.getJsonToObject(mt2ZainIraqServiceApiTrans.getResponse(), Map.class);
					
					modelAndView.addObject("source",mt2ZainIraqServiceApiTrans.getSource());
					modelAndView.addObject("uniqid",mt2ZainIraqServiceApiTrans.getUniqueId());
					uniqid=mt2ZainIraqServiceApiTrans.getUniqueId();//Objects.toString(map.get("uniqid"));
				}else{
					 uniqid = mt2ZainIraqServiceApi.getSha1Hash( request.getRemoteAddr()
							+"-"+cgToken.getCGToken()
							+"-"+System.currentTimeMillis()); // Unique Key To Use For Block API Call
					String source = "(function(s, o, u, r, k){b = s.URL;v = (b.substr(b.indexOf(r)).replace(r + '=', '')).toString();"
							+ "r = (v.indexOf('&') !== -1) ? v.split('&')[0] : v;"
							+ "a = s.createElement(o),m = s.getElementsByTagName(o)[0];"
							+ "a.async = 1;a.setAttribute('crossorigin', 'anonymous');"
							+ "a.src = u+'script.js?ak='+k+'&lpi='+r+'&lpu='+encodeURIComponent(b)+'&key=$uniqid';"
							+ "m.parentNode.insertBefore(a, m);})"
							+ "(document, 'script', '"+mt2ZainIraqServiceConfig.getApiSnippetUrl()+"', 'token', '"+mt2ZainIraqServiceConfig.getZainServiceKey()+"');";
					modelAndView.addObject("source",source);
					modelAndView.addObject("uniqid",uniqid);
					mt2ZainIraqServiceApiTrans.setUniqueId(uniqid);
					mt2ZainIraqServiceApiTrans.setSource(source);
				}
				
				String cgUrl=mt2ZainIraqServiceConfig.getSubUrl()
						  .replaceAll("<serviceid>", mt2ZainIraqServiceConfig.getZainIraqServiceId())
						  .replaceAll("<spid>", mt2ZainIraqServiceConfig.getSpid())
						  .replaceAll("<shortcode>", mt2ZainIraqServiceConfig.getShortCode())
						  .replaceAll("<uniqid>", uniqid);
				redisCacheService.putObjectCacheValueByEvictionDay(
						Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_UNIQUEID_CACHE_PREFIX+uniqid
						, token,10);
				modelAndView.addObject("cgUrl",cgUrl);
				
				modelAndView.addObject("token",token);				
				modelAndView.setViewName("mt2zainiraq/lp");
				
			}catch(Exception ex){
				logger.error("Exception    ",ex);
			}finally{
				if(mt2ZainIraqServiceApiTrans!=null){
				   daoService.updateObject(mt2ZainIraqServiceApiTrans);
				}
			}
		return modelAndView;
	}
	
	/*
	 * @RequestMapping("tocg") public ModelAndView toCg(HttpServletRequest
	 * request,ModelAndView modelAndView ){ MT2ZainIraqCGTrans mt2ZainIraqCGTrans =
	 * new MT2ZainIraqCGTrans(true); try{
	 * 
	 * mt2ZainIraqCGTrans.setQueryStr(request.getQueryString());
	 * mt2ZainIraqCGTrans.setUniqueId(request.getParameter("uniqid")); String
	 * token=request.getParameter("token"); if(token==null){
	 * token=Objects.toString(redisCacheService.getObjectCacheValue(
	 * Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_SOURCE_CACHE_PREFIX
	 * +request.getRemoteAddr())); }
	 * 
	 * if(Mt2ZainIraqConstant.findMt2ZainIraqOperatorByIp(
	 * request.getRemoteAddr())==null&&!token.equalsIgnoreCase("testingziraq")){
	 * logger.info("toCG:: ipp address not match "+request.getRemoteAddr()
	 * +" , token: "+token+", redirect to: "
	 * +"https://port16.govisibl.com/dlv/c.php?cca=136850&ccz=4398&token="+token);
	 * // modelAndView.setView(new RedirectView //
	 * ("https://port16.govisibl.com/dlv/c.php?cca=136850&ccz=4398&token="+token));
	 * // mt2ZainIraqCGTrans.setCgUrl(
	 * "https://port16.govisibl.com/dlv/c.php?cca=136850&ccz=4398&token="+token);
	 * mt2ZainIraqCGTrans.setCgUrl(
	 * "http://play-mob2.com/ccsub/cnt/mt2zainiraq/wifi/error");
	 * modelAndView.setViewName(
	 * "http://play-mob2.com/ccsub/cnt/mt2zainiraq/wifi/error"); return
	 * modelAndView; }
	 * 
	 * CGToken cgToken=null; if(token!=null){ cgToken=new CGToken(token); }else{
	 * //http://192.241.253.234/ccsub/cnt/cmp?adid=1&cmpid=194&token=zain
	 * cgToken=new CGToken(System.currentTimeMillis(), -1, 194); }
	 * mt2ZainIraqCGTrans.setToken(token); Enumeration<String> en =
	 * request.getHeaderNames(); StringBuilder headersStr = new StringBuilder();
	 * Map<String, String> headerMap = new HashMap<String, String>(); while
	 * (en.hasMoreElements()) { String key = en.nextElement(); headersStr.append(key
	 * + "=" + request.getHeader(key) + " ,"); headerMap.put(key,
	 * request.getHeader(key)); } mt2ZainIraqCGTrans.setIp(request.getRemoteAddr());
	 * mt2ZainIraqCGTrans.setxForwardedFor(headerMap.get("x-forwarded-for"));
	 * Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig=Mt2ZainIraqConstant
	 * .mapServiceIdToMt2ZainIrqServiceConfig .get(83);
	 * 
	 * String cgUrl=mt2ZainIraqServiceConfig.getSubUrl() .replaceAll("<serviceid>",
	 * mt2ZainIraqServiceConfig.getZainIraqServiceId()) .replaceAll("<spid>",
	 * mt2ZainIraqServiceConfig.getSpid()) .replaceAll("<shortcode>",
	 * mt2ZainIraqServiceConfig.getShortCode()) .replaceAll("<uniqid>",
	 * mt2ZainIraqCGTrans.getUniqueId()); mt2ZainIraqCGTrans.setCgUrl(cgUrl);
	 * redisCacheService.putObjectCacheValueByEvictionDay(
	 * Mt2ZainIraqConstant.MT2_ZAIN_IRAQ_UNIQUEID_CACHE_PREFIX+mt2ZainIraqCGTrans.
	 * getUniqueId() , token,10); modelAndView.addObject("cgUrl", cgUrl);
	 * modelAndView.setViewName("mt2zainiraq/cg");
	 * 
	 * }catch(Exception ex){ logger.error("exception :: ",ex); }finally{
	 * jmsService.saveObject(mt2ZainIraqCGTrans); } return modelAndView; }
	 */
}
