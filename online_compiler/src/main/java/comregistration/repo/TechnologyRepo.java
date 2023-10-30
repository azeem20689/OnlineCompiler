package comregistration.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import comregistration.entity.Technology;

public interface TechnologyRepo extends JpaRepository<Technology, Integer> {

}
