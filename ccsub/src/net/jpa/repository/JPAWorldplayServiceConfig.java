package net.jpa.repository;

import java.util.List;

import net.mycomp.worldplay.WorldplayServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAWorldplayServiceConfig extends JpaRepository<WorldplayServiceConfig, Long>{

	@Query("select b from WorldplayServiceConfig b where b.status=:status")
    List<WorldplayServiceConfig> findEnableWorldplayServiceConfig(@Param("status")boolean status);

    
}
