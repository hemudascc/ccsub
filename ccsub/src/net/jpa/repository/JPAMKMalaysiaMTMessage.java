package net.jpa.repository;

import java.util.List;

import net.mycomp.mcarokiosk.malaysia.MalasiyaMTMessage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMKMalaysiaMTMessage extends JpaRepository<MalasiyaMTMessage, Long>{

	@Query("select b from MalasiyaMTMessage b where b.id=:id")
	MalasiyaMTMessage findMTMessage(@Param("id")Integer id);
	
	@Query("select b from MalasiyaMTMessage b where b.msgId=:msgId order by b.id desc")
	List<MalasiyaMTMessage> findMTMessageByMessageId(@Param("msgId")String msgId);

    
}