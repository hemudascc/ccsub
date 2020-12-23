package net.jpa.repository;

import java.util.List;

import net.mycomp.etisalat.EtisalatServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAEtisalatServiceConfig extends JpaRepository<EtisalatServiceConfig, Long>{

	@Query("select b from EtisalatServiceConfig b where b.status=:status")
    List<EtisalatServiceConfig> findEnableEtisalatServiceConfig(@Param("status")boolean status);

    
}