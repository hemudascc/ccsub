package net.jpa.repository;

import java.util.List;

import net.mycomp.oredoo.kuwait.OredooKuwaitCGNotification;
import net.mycomp.oredoo.kuwait.OredooKuwaitServiceConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAOredooCGNotification extends JpaRepository<OredooKuwaitCGNotification, Long>{

	@Query("select b from OredooKuwaitCGNotification b where b.id=:id")
    OredooKuwaitCGNotification findOredooKuwaitCGNotificationById(@Param("id")Integer  id);

    
}