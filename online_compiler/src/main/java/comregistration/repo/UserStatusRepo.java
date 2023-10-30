package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comregistration.entity.User;
import comregistration.entity.UserStatus;

@Repository
public interface UserStatusRepo extends JpaRepository<UserStatus, Integer>{
	
//	@Query(value= "select * from user_status us where user_name = :name",nativeQuery = true)
//	User checkUserStatus(@Param("name")String name);

}
