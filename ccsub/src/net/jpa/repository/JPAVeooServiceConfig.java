package net.jpa.repository;

import java.util.List;

import net.mycomp.veoo.VeooServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAVeooServiceConfig extends JpaRepository<VeooServiceConfig, Long>{

	@Query("select b from VeooServiceConfig b where b.status=:status")
    List<VeooServiceConfig> findEnableVeooServiceConfig(@Param("status")boolean status);

    
}