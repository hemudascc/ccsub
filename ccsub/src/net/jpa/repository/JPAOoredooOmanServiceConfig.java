package net.jpa.repository;

import java.util.List;
import net.mycomp.comviva.ooredo.oman.OoredooOmanServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAOoredooOmanServiceConfig extends JpaRepository<OoredooOmanServiceConfig, Long>{

	@Query("select b from OoredooOmanServiceConfig b where b.status=:status")
    List<OoredooOmanServiceConfig> findEnableOredooOmanServiceConfig(@Param("status")boolean status);
}