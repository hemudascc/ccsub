package net.mycomp.beecel.jordon;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

public class JMSJordonMTListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSJordonMTListener.class);


	@Autowired
	private IDaoService daoService;
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Override
	public void onMessage(Message message) {
		BCJordonMTMessage bcJordonMTMessage = null;
		
		long time = System.currentTimeMillis();
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			bcJordonMTMessage = (BCJordonMTMessage)objectMessage.getObject();
			logger.info("bcJordonNotification::::: "+bcJordonMTMessage);
			daoService.saveObject(bcJordonMTMessage);
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}
	}
}
