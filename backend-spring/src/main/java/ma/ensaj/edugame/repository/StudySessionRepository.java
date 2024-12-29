package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    // Find the last active session for a student
    StudySession findTopByStudentAndEndTimeIsNullOrderByStartTimeDesc(Student student);

    // Find all completed sessions for a student
    List<StudySession> findByStudentAndEndTimeIsNotNull(Student student);
}
