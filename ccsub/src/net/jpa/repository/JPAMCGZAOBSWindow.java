package net.jpa.repository;


import net.mycomp.messagecloud.gateway.za.MCGZAOBSWindowTrans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMCGZAOBSWindow extends JpaRepository<MCGZAOBSWindowTrans, Long>{

	@Query("select b from MCGZAOBSWindowTrans b where b.responseId=:obsWindowId")
	MCGZAOBSWindowTrans findMCGZAOBSWindowTransBYOBSWindowId(@Param("obsWindowId")String obsWindowId);
     
}