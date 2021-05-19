package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.cornet.sudan.CornetConfig;

public interface JPACornetConfig extends JpaRepository<CornetConfig, Long> {

	@Query("select b from CornetConfig b where b.status=:status")
	List<CornetConfig> findEnableCornetConfig(@Param("status")boolean status);
}
