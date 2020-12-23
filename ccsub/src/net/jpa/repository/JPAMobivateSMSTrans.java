package net.jpa.repository;


import net.mycomp.mobivate.MobivateSMSTrans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMobivateSMSTrans extends JpaRepository<MobivateSMSTrans, Long>{

	@Query("select b from MobivateSMSTrans b where b.id=:id")
	MobivateSMSTrans findNumeroMobivateSMSTransById(@Param("id")Integer id);
}