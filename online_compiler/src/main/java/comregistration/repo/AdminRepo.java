package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import comregistration.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer>{

	@Query(value="select * from admin  where user_name = :user_name ",nativeQuery=true)
	Admin getAdmin(@Param("user_name") String userName);
}
