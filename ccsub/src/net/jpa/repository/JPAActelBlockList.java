package net.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mycomp.actel.ActelQatarBlockList;

public interface JPAActelBlockList extends JpaRepository<ActelQatarBlockList, Long>{
	
	@Query("select b from ActelQatarBlockList b")
    List<ActelQatarBlockList> findAllActelQatarBlockList();
	
}
