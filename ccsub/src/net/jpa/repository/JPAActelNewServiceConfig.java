package net.jpa.repository;

import java.util.List;

import net.mycomp.actel.ActelNewServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAActelNewServiceConfig extends JpaRepository<ActelNewServiceConfig, Long>{

	@Query("select b from ActelNewServiceConfig b where b.status=:status")
    List<ActelNewServiceConfig> findEnableActelNewServiceConfig(@Param("status")boolean status);
}