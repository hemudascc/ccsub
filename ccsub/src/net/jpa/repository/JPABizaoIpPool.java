package net.jpa.repository;

import java.util.List;

import net.bizao.BizaoIpPool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPABizaoIpPool extends JpaRepository<BizaoIpPool, Long>{

	@Query("select b from BizaoIpPool b where b.status=:status")
    List<BizaoIpPool> findEnableBizaoIpPool(@Param("status")boolean status);

    
}