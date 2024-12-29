package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {

}

