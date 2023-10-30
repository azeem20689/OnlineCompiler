package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comregistration.entity.Courses;

@Repository
public interface CoursesRepo extends JpaRepository<Courses, Integer>{

	@Query(value="select * from courses c where course_code = (:code)",nativeQuery = true)
	Courses getCourseByName(@Param("code")String code);
	
	@Query(value= "select * from courses where course_code  = (:courseName) and section = (:section);",nativeQuery = true)
	Courses getCourseBySecionAndCourseCode(@Param("courseName")String courseCode , @Param("section") int section);
	
}
