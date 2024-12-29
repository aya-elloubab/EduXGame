package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.StudentPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentPointsRepository extends JpaRepository<StudentPoints, Long> {
    Optional<StudentPoints> findByStudentIdAndChapterId(Long studentId, Long chapterId);

    List<StudentPoints> findByStudentId(Long studentId);
}
