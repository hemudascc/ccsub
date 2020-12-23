package net.jpa.repository;

import java.util.List;

import net.indonesia.triyakom.IndonesiaChargingConfig;
import net.indonesia.triyakom.TriyakomConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPAIndonesiaChargingConfig extends JpaRepository<IndonesiaChargingConfig, Long>{

	@Query("select b from IndonesiaChargingConfig b where b.status=:status")
    List<IndonesiaChargingConfig> findEnableIndonesiaChargingConfig(@Param("status")boolean status);

    
}
