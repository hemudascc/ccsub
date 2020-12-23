package net.jpa.repository;


import java.util.List;

import net.mycom.nxt.vas.NxtVasConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPANxtVasConfig extends JpaRepository<NxtVasConfig, Long>{

	@Query("select b from NxtVasConfig b where  b.status=:status")
    List<NxtVasConfig> findAllEnableNxtVasConfig(
    		@Param("status")boolean status);

}