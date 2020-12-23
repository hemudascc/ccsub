package net.scheduler;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.common.service.CommonService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.Operator;
import net.util.MConstants;





import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class CommonScheduler {

	private static final Logger logger = Logger.getLogger(CommonScheduler.class);

	@Autowired
	private IDaoService daoService;	
	
	@Scheduled(cron="0 0 * ? * *")	
	public void resetDailyValues(){
		try{
		RedisCacheService.dateddMMYYYY=MConstants.sdfDDMMyyyy.format(
				new Timestamp(System.currentTimeMillis()).toLocalDateTime());
		RedisCacheService.dateddMMYYYYHH=MConstants.sdfDDMMyyyyHH.format(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
		}catch(Exception ex){
			logger.error("resetDailyValues ",ex);
		}
	}
}
