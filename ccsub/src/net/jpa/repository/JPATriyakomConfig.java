package net.jpa.repository;

import java.util.List;

import net.indonesia.triyakom.TriyakomConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPATriyakomConfig extends JpaRepository<TriyakomConfig, Long>{

	@Query("select b from TriyakomConfig b where b.status=:status")
    List<TriyakomConfig> findEnableTriyakomConfig(@Param("status")boolean status);

    
}
