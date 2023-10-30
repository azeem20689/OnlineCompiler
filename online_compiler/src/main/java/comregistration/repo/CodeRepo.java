package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

import comregistration.entity.Code;
import comregistration.entity.OcDivConfig;

@Repository
public interface CodeRepo extends JpaRepository<Code, Integer> {

	@Query(value = "select * from code where name = :name", nativeQuery = true)
	List<Code> getCodeByUsername(@Param("name") String name);

//	@Query(value = "SELECT * FROM code WHERE file_name = :file_name AND name = :name", nativeQuery = true)
//	Code getCodeByFileNameAndName(@Param("file_name") String file_name, @Param("name") String name);

	@Query(value = "SELECT * FROM code WHERE  file_name = :file_name and name = :name ", nativeQuery = true)
	Code getCodeByFileNameAndName(@Param("file_name") String fileName, @Param("name") String name);
	
	@Query(value = "Delete  FROM code WHERE  file_name = :file_name and name = :name ", nativeQuery = true)
	Code deleteFromCodeRepo(@Param("file_name") String fileName, @Param("name") String name);
	
	
	@Query(value = "select * from code c where split_part(file_name,'.',2) = :substr and \"name\" = :name ", nativeQuery = true)
	List<Code> bashCodeList(@Param("substr") String substr, @Param("name") String name);
	

	
	
}
