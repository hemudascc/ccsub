package net.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.indonesia.triyakom.MOMessage;


public interface JPAIndonesiaMOMessage extends JpaRepository<MOMessage, Long>{

	@Query("select b from MOMessage b where b.txId=:txId order by b.id desc")	
	 public MOMessage findIndonesiaMOMessageTransByTXId(@Param("txId")String txId);
	
	@Query("select b from MOMessage b where b.msisdn=:msisdn and b.activationKey=:activationKey order by b.id desc")	
	 public MOMessage findIndonesiaMOMessageTransByMsisdnAndActivationKey(@Param("msisdn")String msisdn,@Param("activationKey")String activationKey);
}
