package net.jpa.repository;

import java.util.List;

import net.mycomp.mt2.uae.Mt2UAEServiceConfig;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMt2UAEServiceConfig extends JpaRepository<Mt2UAEServiceConfig, Long>{

	@Query("select b from Mt2UAEServiceConfig b where b.status=:status")
    List<Mt2UAEServiceConfig> findEnableMt2UAEServiceConfig(@Param("status")boolean status);
}