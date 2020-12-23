
package net.mycomp.common.inapp;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.common.service.BlockSeriesRedisCacheService;
import net.common.service.CommonService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.BsnlMisReport;
import net.persist.bean.LiveReport;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.Service;
import net.persist.bean.VWAdnetworkOperatorConfig;
import net.process.bean.AdnetworkCampaignReport;
import net.process.bean.AdnetworkCompaignReportWrapper;
import net.process.bean.AggReport;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping(value = "inapp")
public class InappMisController {

	private static  Logger logger = Logger.getLogger(InappMisController.class);

	@Autowired
	private IDaoService daoService;


	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BlockSeriesRedisCacheService blockSeriesRedisCacheService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@InitBinder
	public void binder(WebDataBinder binder) {binder.registerCustomEditor(Timestamp.class,
	    new PropertyEditorSupport() {
	        public void setAsText(String value) {
	            try {
	                Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(value);
	                setValue(new Timestamp(parsedDate.getTime()));
	            } catch (ParseException e) {
	                setValue(null);
	            }
	        }
	    });
	}
	

	
	
	
	@RequestMapping("/aggstats")	
	public ModelAndView aggReport(@ModelAttribute(value="AggReport") AggReport aggReport,BindingResult result) {
		
		ModelAndView modelAndView=new ModelAndView("inapp/agg_report");
		
		//modelAndView.addObject("mapAggregator",MData.mapIdToAggregator);
		modelAndView.addObject("mapAggregator", MData.mapIdToAggregator.entrySet().stream()
                .filter(map -> map.getValue().getType().equalsIgnoreCase(InappConstant.INAPP))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		
		
		modelAndView.addObject("mapOperator",MData.mapIdToOperator);
		modelAndView.addObject("mapProduct",MData.mapIdToProduct);
		modelAndView.addObject("productId",aggReport.getProductId());
		
		modelAndView.addObject("listAggregator",MData.mapIdToAggregator.values().stream()
				.collect(Collectors.toList()));
		
		if(aggReport.getAggregatorId()!=null){
			modelAndView.addObject("operatorList", MData.mapIdToOperator.values().stream()
			.filter(p -> p.getAggregatorId().intValue()==aggReport.getAggregatorId())
			.collect(Collectors.toList()));
		}
		
		if(aggReport.getOpid()!=null){
			Map<Integer,Product> mapProduct=new HashMap<Integer,Product>();
			
			for(Service service:MData.mapServiceIdToService.values()){
				if(aggReport.getOpid().intValue()==service.getOpId()){
				  mapProduct.put(service.getProductId(),MData.mapIdToProduct.get(service.getProductId()));
				}
			}
			modelAndView.addObject("productList", mapProduct.values().stream()
					.collect(Collectors.toList()));
		}
		
		modelAndView.addObject("adnetworksList", MData.mapAdnetworks.values().stream()
				.collect(Collectors.toList()));
		
		List<InappLiveReport> list = daoService.findInappLiveReportAggReport(aggReport);
		modelAndView.addObject("report",list);
		Map<Integer,List<InappLiveReport>> map=null;
	   if(list!=null){
		 map=list.stream()
	        .collect(Collectors.groupingBy(p->p.getOperatorId(),Collectors.toList()
	        ));
	   }		
	   
		modelAndView.addObject("reportMap",map);
		Map<Integer,Integer> mapActiveBase=daoService.findSubscriberActiveBase(aggReport);
		modelAndView.addObject("mapActiveBase",mapActiveBase);
		return modelAndView;
	}
	
	
	

	
}
