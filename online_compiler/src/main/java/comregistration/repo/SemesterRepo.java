package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comregistration.entity.Semester;

@Repository
public interface SemesterRepo extends JpaRepository<Semester, Integer>{
	
	
	@Query(value="select * from semester where is_active = (:status)",nativeQuery = true)
	Semester getSemester(@Param("status") int status);

}
