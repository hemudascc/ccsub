package net.bizao;


import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSBizaoRenewalListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSBizaoRenewalListener.class);
	
	@Autowired
	private IDaoService daoService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private BizaoApiService bizaoApiService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private JMSBizaoService jmsBizaoService;
	
	@Override
	public void onMessage(Message m) {

		BizaoSuscriberIdMsg bizaoSuscriberIdMsg = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		BizaoConfig bizaoConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			bizaoSuscriberIdMsg = (BizaoSuscriberIdMsg) objectMessage.getObject();
			logger.info("bizaoSuscriberIdMsg:::::::::: "+bizaoSuscriberIdMsg);
			
			boolean isEligibleToRenewToday=false;
			SubscriberReg subscriberReg=null;
			if(bizaoSuscriberIdMsg.getAction().equalsIgnoreCase(MConstants.RENEW)){				
			 subscriberReg=
					jpaSubscriberReg.findSubscriberRegById(bizaoSuscriberIdMsg.getSubscriberId());		
			
			if(subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				bizaoConfig=BizaoConstant.mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
				isEligibleToRenewToday=BizaoConstant.isEligibleToRenewToday(subscriberReg, bizaoConfig);
			     }
			}
			
			logger.info("isEligibleToRenewToday:: "+isEligibleToRenewToday+"  ,bizaoConfig:: "+
			bizaoConfig+", bizaoSuscriberIdMsg:: "+bizaoSuscriberIdMsg);
			
			if(isEligibleToRenewToday){
				
				BizaoCGToken bizaoCGToken=new BizaoCGToken(time, 0,
						MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID,
						0, bizaoConfig.getId());
				
				BizaoPayment bizaoPayment=bizaoApiService.makePayment(MConstants.RENEW,
						null, bizaoConfig, bizaoCGToken.getBizaoCGToken(),
						subscriberReg.getParam1(),subscriberReg.getMsisdn());
				
				subscriberReg.setLastRenewalRetryDate(new Timestamp(System.currentTimeMillis()));
				daoService.updateObject(subscriberReg);
				//jmsBizaoService.saveBizaoPayment(bizaoPayment);	
			}
			
				
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
