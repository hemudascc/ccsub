package net.jpa.repository;

import java.util.List;

import net.mycomp.du.DUConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPADUConfig extends JpaRepository<DUConfig, Long>{

	@Query("select b from DUConfig b where b.status=:status")
    List<DUConfig> findEnableDUConfig(@Param("status")boolean status);

    
}