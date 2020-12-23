package net.mycomp.common.inapp;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.jpa.repository.JPAInappTmtConfig;
import net.mycomp.common.inapp.tmt.InappTmtConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("inappService")
public class InappService {

	
	 @Autowired
	 private IDaoService daoService;
	
	@Autowired
	private JPAInappTmtConfig jpaInappConfig;
	
	@Value("${jdbc.db.name}")
	private String dbName;
	
	
	@PostConstruct
	public void init(){
		
		 Integer id=daoService.
					findNextAutoIncrementId("tb_inapp_process_request", dbName);		
		 InappConstant.inappProcessRequestId.set(id);
		 
	}
	
}
