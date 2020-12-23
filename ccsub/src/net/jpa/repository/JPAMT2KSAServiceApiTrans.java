package net.jpa.repository;

import net.mycomp.mt2.ksa.MT2KSAServiceApiTrans;
import net.persist.bean.AdnetworkToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JPAMT2KSAServiceApiTrans extends JpaRepository<MT2KSAServiceApiTrans, Long>{

	@Query("select b from MT2KSAServiceApiTrans b where b.id=:id")
	MT2KSAServiceApiTrans findEnableMT2KSAServiceApiTrans(@Param("id")Integer id);
}