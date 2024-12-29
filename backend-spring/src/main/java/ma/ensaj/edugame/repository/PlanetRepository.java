package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    List<Planet> findAllByOrderByIdAsc(); // Order by ID instead of unlockOrder

}


