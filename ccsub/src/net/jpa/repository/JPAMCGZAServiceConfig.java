package net.jpa.repository;

import java.util.List;



import net.mycomp.messagecloud.gateway.za.MCGZAServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMCGZAServiceConfig extends JpaRepository<MCGZAServiceConfig, Long>{

	@Query("select b from MCGZAServiceConfig b where b.status=:status")
    List<MCGZAServiceConfig> findEnableMCGZAServiceConfig(@Param("status")boolean status);

    
}