package net.jpa.repository;

import java.util.List;

import net.mycomp.mcarokiosk.malaysia.MKMalaysiaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMKMalaysiaConfig extends JpaRepository<MKMalaysiaConfig, Long>{

	@Query("select b from MKMalaysiaConfig b where b.status=:status")
    List<MKMalaysiaConfig> findEnableMKMalaysiaConfig(@Param("status")boolean status);

    
}