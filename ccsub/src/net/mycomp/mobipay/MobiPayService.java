package net.mycomp.mobipay;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.jpa.repository.JPAMobiPayServiceConfig;
import net.process.bean.AdNetworkRequestBean;
import net.process.request.AbstractOperatorService;
import net.util.MConstants;

@Service("mobiPayService")
public class MobiPayService extends AbstractOperatorService{

	@Autowired
	private JPAMobiPayServiceConfig jpaMobiPayServiceConfig;
	
	private static final Logger logger = Logger.getLogger(MobiPayService.class);
	
	@PostConstruct
	void init() {
		List<MobiPayServiceConfig> mobiPayServiceConfigList= jpaMobiPayServiceConfig.findAll();
		MobiPayConstant.mapServiceIdToMobiPayServiceConfig.putAll(mobiPayServiceConfigList.stream().collect(
				Collectors.toMap(p -> p.getServiceId(), p ->p)));
	}
	
	
	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		return false;
	}

	@Override
	public boolean processBilling(ModelAndView modelAndView, AdNetworkRequestBean adNetworkRequestBean) {
		MobiPayServiceConfig mobiPayServiceConfig;
		String cgURL;
		try {
			//https://content.mobivate.com/lookup/za/?shortcode=30053&service=v_glamworld.1000.1.GlamourWorld=v_glamworld.1000.1.GlamourWorld&freq=1&campaign=GlamourWorld&amount=10&bl=http%3A%2F%2F192.241.253.234%2Fccsub%2Fresources%2Fmobivate%2Fglamour_world_1.jpg&bc=fffff&tc=#ddddd&return=http%3A%2F%2F192.241.253.234%2Fccsub%2Fcnt%2Fmobipay%2Fcgcallback%2F<token>
			
			mobiPayServiceConfig = MobiPayConstant
					.mapServiceIdToMobiPayServiceConfig
					.get(adNetworkRequestBean.vwserviceCampaignDetail.getServiceId());
			cgURL = mobiPayServiceConfig.getCgURL()
					.replaceAll("<token>", adNetworkRequestBean.adnetworkToken.getTokenToCg());
			adNetworkRequestBean.adnetworkToken.setAction(MConstants.REDIRECT_TO_CG);
			modelAndView.setView(new RedirectView(cgURL));
		} catch (Exception e) {
			logger.error("error while processing mobipay request");
		}
		return Boolean.TRUE;
	}
		
	
}
