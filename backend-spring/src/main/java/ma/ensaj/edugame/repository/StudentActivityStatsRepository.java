package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentActivityStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentActivityStatsRepository extends JpaRepository<StudentActivityStats, Long> {
    Optional<StudentActivityStats> findByStudent(Student student);
}
