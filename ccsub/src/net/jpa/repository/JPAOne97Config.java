package net.jpa.repository;

import java.util.List;

import net.mycomp.one97.One97Config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAOne97Config extends JpaRepository<One97Config, Long>{

	@Query("select b from One97Config b where b.status=:status")
    List<One97Config> findEnableOne97Config(@Param("status")boolean status);

    
}