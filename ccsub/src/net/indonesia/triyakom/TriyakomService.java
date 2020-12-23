package net.indonesia.triyakom;

import java.net.URLEncoder;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.IDaoService;
import net.jpa.repository.JPAIndonesiaChargingConfig;
import net.jpa.repository.JPATriyakomConfig;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;


@Service("triyakomService")
public class TriyakomService extends AbstractOperatorService {

	private static final Logger logger = Logger.getLogger(TriyakomService.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPATriyakomConfig jpaTriyakomConfig;
	@Autowired
	private JPAIndonesiaChargingConfig jpaIndonesiaChargingConfig;
	
	@PostConstruct
	public void init() {
		reloadConfig();	
	}

	
	public void reloadConfig(){
		
		try{
		TriyakomConstant.listTriyakomConfig.clear();
		TriyakomConstant.listTriyakomConfig.addAll(jpaTriyakomConfig.findEnableTriyakomConfig(true));
		TriyakomConstant.mapServiceIdToTriyakomConfig.putAll(TriyakomConstant.listTriyakomConfig.stream().
				collect(Collectors.toMap(p -> p.getServiceId(), p -> p)));
		
		TriyakomConstant.listIndonesiaChargingConfig.addAll(jpaIndonesiaChargingConfig.findEnableIndonesiaChargingConfig(true));
				}catch(Exception ex){
			logger.error("reloadConfig:: ",ex);
		}
		}
	
	@Override
	public boolean processBilling(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
	    try{
		
	    	TriyakomConfig triyakomConfig=TriyakomConstant.mapServiceIdToTriyakomConfig.
	    			get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());			
			String url=triyakomConfig.getUrl().replaceAll("<service_id>",
					URLEncoder.encode(triyakomConfig.getSid(),"utf-8"))
					.replaceAll("<sms_keyword>", URLEncoder.encode(triyakomConfig.getKey()+" "+adNetworkRequestBean.adnetworkToken.getTokenToCg(),"utf-8"))
					.replaceAll("<callback_url>", "");			
			logger.error("url: redirect to :: "+url);
			modelAndView.setView(new RedirectView(url));
	    }catch(Exception ex){
	    	logger.error("processBilling:: ",ex);
	    }
		return true;
	}
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		return false;
	}
	
	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		
		
		   return false;
	}
	
}
