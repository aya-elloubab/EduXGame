package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentChapterProgressRepository extends JpaRepository<StudentChapterProgress, Long> {
    Optional<StudentChapterProgress> findByStudentAndChapter(Student student, Chapter chapter);
}
