package net.jpa.repository;

import java.util.List;

import net.mycomp.ksa.KsaServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAKsaServiceConfig extends JpaRepository<KsaServiceConfig, Long>{

	@Query("select b from KsaServiceConfig b where b.status=:status")
    List<KsaServiceConfig> findEnableKsaServiceConfig(@Param("status")boolean status);

    
}