package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.StudentStudyBadge;
import ma.ensaj.edugame.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentStudyBadgeRepository extends JpaRepository<StudentStudyBadge, Long> {
    List<StudentStudyBadge> findByStudent(Student student);
}
