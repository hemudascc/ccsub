package net.jpa.repository;

import java.util.List;

import net.bizao.BizaoConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPABizaoServiceConfig extends JpaRepository<BizaoConfig, Long>{

	@Query("select b from BizaoConfig b where b.status=:status")
    List<BizaoConfig> findEnableBizaoConfig(@Param("status")boolean status);

    
}