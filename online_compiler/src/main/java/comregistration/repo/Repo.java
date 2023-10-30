package comregistration.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Repository;
import java.util.*;
import comregistration.entity.Code;
import comregistration.entity.User;

@Repository
public interface Repo extends JpaRepository<User, Integer> {

	@Query(value = "select * from online_compiler  where username = :name", nativeQuery = true)
	User getIdByName(@Param("name") String name);

	@Query(value = "select * from online_compiler  where username = :name", nativeQuery = true)
	Optional<User> duplicateUser(@Param("name") String name);
	
	@Query(value="select * from online_compiler oc where is_instructor = 1 and username = :userName",nativeQuery = true)
	User getInstructor(@Param("userName") String name);
}
