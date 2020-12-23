package net.jpa.repository;

import java.util.List;

import net.mycomp.mt2.zain.iraq.Mt2ZainIraqIpPool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMt2ZainIraqIpPool extends JpaRepository<Mt2ZainIraqIpPool, Long>{

	@Query("select b from Mt2ZainIraqIpPool b where b.status=:status")
    List<Mt2ZainIraqIpPool> findEnableMt2ZainIraqIpPoolIpPool(@Param("status")boolean status);

    
}