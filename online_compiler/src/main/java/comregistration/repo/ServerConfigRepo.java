package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comregistration.entity.ServerConfig;
import comregistration.entity.Technology;
import comregistration.entity.User;

@Repository
public interface ServerConfigRepo extends JpaRepository<ServerConfig, Integer> {

//	@Query(value = "SELECT * FROM server_config sc JOIN technology t ON sc.id = t.server_config_id WHERE t.technology ILIKE(:language)", nativeQuery = true)
//	ServerConfig getServerConfig(@Param("language") String language);

	@Query("SELECT t FROM Technology t WHERE t.technology ILIKE :language")
	Technology getServerConfig(@Param("language") String language);

    
}

