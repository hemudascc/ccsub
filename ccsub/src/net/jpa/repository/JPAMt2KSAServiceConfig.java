package net.jpa.repository;

import java.util.List;

import net.mycomp.mt2.ksa.Mt2KSAServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMt2KSAServiceConfig extends JpaRepository<Mt2KSAServiceConfig, Long>{

	@Query("select b from Mt2KSAServiceConfig b where b.status=:status")
    List<Mt2KSAServiceConfig> findEnableMt2KSAServiceConfig(@Param("status")boolean status);
}