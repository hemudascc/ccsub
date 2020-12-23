package net.jpa.repository;

import java.util.List;

import net.mycomp.messagecloud.MessageCloudServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMessageCloudServiceConfig extends JpaRepository<MessageCloudServiceConfig, Long>{

	@Query("select b from MessageCloudServiceConfig b where b.status=:status")
    List<MessageCloudServiceConfig> findEnableMessageCloudServiceConfig(@Param("status")boolean status);

    
}