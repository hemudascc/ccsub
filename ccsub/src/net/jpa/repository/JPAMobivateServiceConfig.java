package net.jpa.repository;

import java.util.List;

import net.mycomp.mobivate.MobivateServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMobivateServiceConfig extends JpaRepository<MobivateServiceConfig, Long>{

	@Query("select b from MobivateServiceConfig b where b.status=:status")
    List<MobivateServiceConfig> findEnableMobivateServiceConfig(@Param("status")boolean status);

    
}