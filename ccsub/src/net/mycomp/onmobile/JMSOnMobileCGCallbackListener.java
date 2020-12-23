package net.mycomp.onmobile;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.jpa.repository.JPAMT2KSAServiceApiTrans;
import net.jpa.repository.JPAOoredooOmanOCSLogDetail;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSOnMobileCGCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSOnMobileCGCallbackListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private JPAOoredooOmanOCSLogDetail jpaOoredooOmanOCSLogDetail;
	
	@Override
	public void onMessage(Message m) {

		OnMobileCGCallback onMobileCGCallback = null;
		
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			onMobileCGCallback = (OnMobileCGCallback) objectMessage
					.getObject();
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
			} catch (Exception ex) {			
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				daoService.saveObject(onMobileCGCallback);
			}
			logger.info("onMessage::::::::::::::::: ::  "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			}
	}
}
