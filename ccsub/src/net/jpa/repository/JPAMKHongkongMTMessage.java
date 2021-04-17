package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.mcarokiosk.hongkong.HongkongMTMessage;


public interface JPAMKHongkongMTMessage  extends JpaRepository<HongkongMTMessage, Long>{

	@Query("select b from HongkongMTMessage b where b.id=:id")
	HongkongMTMessage findMTMessage(@Param("id")Integer id);
	
	@Query("select b from HongkongMTMessage b where b.msgId=:msgId order by b.id desc")
	List<HongkongMTMessage> findMTMessageByMessageId(@Param("msgId")String msgId);
	
	@Query("select b from HongkongMTMessage b where b.messageType=:messageType order by b.id desc")
	List<HongkongMTMessage> findMTMessageByMessageType(@Param("messageType")String messageType);

}
