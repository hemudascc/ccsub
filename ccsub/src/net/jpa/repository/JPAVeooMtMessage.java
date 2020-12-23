package net.jpa.repository;

import net.mycomp.veoo.VeooMtMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAVeooMtMessage extends JpaRepository<VeooMtMessage, Long>{

	@Query("select b from VeooMtMessage b where b.id=:id and b.type=:type")
	VeooMtMessage findVeooMtMessageById(@Param("id")Integer id,
			@Param("type")String type);

    
}