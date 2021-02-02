package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mycomp.mcarokiosk.hongkong.MKHongkongConfig;

public interface JPAMKHongkongConfig  extends JpaRepository<MKHongkongConfig, Long>{

	@Query("select b from MKHongkongConfig b where b.status=:status")
    List<MKHongkongConfig> findEnableMKHongkongConfig(@Param("status")boolean status);
}
