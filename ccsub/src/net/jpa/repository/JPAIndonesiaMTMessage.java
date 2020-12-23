package net.jpa.repository;

import net.indonesia.triyakom.MTMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAIndonesiaMTMessage extends JpaRepository<MTMessage, Long>{

	
	@Query("SELECT b FROM MTMessage b where b.id=:id")	
	 public MTMessage findMTMessageById(@Param("id")Integer id);
	
	@Query("select b from MTMessage b where b.responseTid=:txId order by b.id desc")	
	 public MTMessage findIndonesiaSmsTransByTXId(@Param("txId")String txId);
    
}
