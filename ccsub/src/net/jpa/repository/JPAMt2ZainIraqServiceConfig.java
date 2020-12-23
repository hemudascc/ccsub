package net.jpa.repository;

import java.util.List;

import net.mycomp.mt2.ksa.Mt2KSAServiceConfig;
import net.mycomp.mt2.zain.iraq.Mt2ZainIraqServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMt2ZainIraqServiceConfig extends JpaRepository<Mt2ZainIraqServiceConfig, Long>{

	@Query("select b from Mt2ZainIraqServiceConfig b where b.status=:status")
    List<Mt2ZainIraqServiceConfig> findEnableMt2ZainIraqServiceConfig(@Param("status")boolean status);
}