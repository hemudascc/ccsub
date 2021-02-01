package net.mycomp.mt2.uae;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;

import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.process.bean.CGToken;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2UAECGCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2UAECGCallbackListener.class);

	

	@Autowired
	private IDaoService daoService;
	

	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private Mt2UAEServiceApi mt2UAEServiceApi;

	@Override
	public void onMessage(Message m) {

		Mt2UAECGCallback mt2UAECGCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		String msg=null;
		Mt2UAEServiceConfig mt2UAEServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2UAECGCallback = (Mt2UAECGCallback) objectMessage
					.getObject();
			
			logger.info("mt2UAECGCallback::::: "+mt2UAECGCallback);
			
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2UAECGCallback:: "
						+ mt2UAECGCallback);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2UAECGCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			
		}
	}
}
