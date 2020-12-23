package net.jpa.repository;

import java.util.List;

import net.mycomp.onmobile.OnMobileServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAOnmobileServiceConfig extends JpaRepository<OnMobileServiceConfig, Long>{

	@Query("select b from OnMobileServiceConfig b where b.status=:status")
    List<OnMobileServiceConfig> findEnableOnMobileServiceConfig(@Param("status")boolean status);
}