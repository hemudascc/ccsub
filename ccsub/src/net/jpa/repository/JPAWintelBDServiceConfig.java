package net.jpa.repository;

import java.util.List;

import net.mycomp.wintel.bangladesh.WintelBDServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAWintelBDServiceConfig extends JpaRepository<WintelBDServiceConfig, Long>{

	@Query("select b from WintelBDServiceConfig b where b.status=:status")
    List<WintelBDServiceConfig> findEnableWintelBDServiceConfig(@Param("status")boolean status);

    
}