package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.beecel.jordon.BCJordonConfig;


public interface JPABCJordonConfig extends JpaRepository<BCJordonConfig, Long>{

	@Query("select b from BCJordonConfig b where b.status=:status")
    List<BCJordonConfig> findEnableBCJordonConfig(@Param("status")boolean status);


}
