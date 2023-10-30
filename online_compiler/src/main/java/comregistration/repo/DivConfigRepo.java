package comregistration.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comregistration.entity.OcDivConfig;

@Repository
public interface DivConfigRepo extends JpaRepository<OcDivConfig, Integer> {

	@Query(value = "SELECT * FROM oc_div_config odc where option = :choose", nativeQuery = true)
	List<OcDivConfig> divConf(@Param("choose") String choose);

}
