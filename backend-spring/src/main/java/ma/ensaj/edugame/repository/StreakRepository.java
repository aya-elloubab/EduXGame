package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Streak;
import ma.ensaj.edugame.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreakRepository extends JpaRepository<Streak, Long> {
    Optional<Streak> findByStudent(Student student);
}
