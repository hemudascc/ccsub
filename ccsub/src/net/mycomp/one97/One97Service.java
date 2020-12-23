package net.mycomp.one97;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.jpa.repository.JPAOne97Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("one97Service")
public class One97Service {

	 @Value("${jdbc.db.name}")
		private String dbName;
	 
	 @Autowired
	 private IDaoService daoService;
	
	@Autowired
	private JPAOne97Config jpaOne97Config;
	
	
	@PostConstruct
	public void init(){
		
		List<One97Config> listOne97Config=jpaOne97Config.findEnableOne97Config(true);
		One97Constant.mapIdToOne97Config.putAll(
				listOne97Config.stream().collect(
						Collectors.toMap(p -> p.getId(), p -> p))
						);
		
		 Integer id=daoService.
					findNextAutoIncrementId("tb_one97_otp_send", dbName);		
		 One97Constant.otpPinSendIdAtomicInteger.set(id);
		   
		 id=daoService.
					findNextAutoIncrementId("tb_one97_otp_validation", dbName);		
		 One97Constant.otpValidationIdAtomicInteger.set(id);
	
		
	}
	
}
