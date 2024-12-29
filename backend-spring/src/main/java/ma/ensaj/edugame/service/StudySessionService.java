package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudySession;
import ma.ensaj.edugame.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    public StudySession startSession(Student student) {
        // Create a new session
        StudySession session = new StudySession();
        session.setStudent(student);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(null); // Not ended yet
        return studySessionRepository.save(session);
    }

    public StudySession endLastActiveSession(Student student) {
        // Fetch the last active session
        StudySession lastSession = studySessionRepository.findTopByStudentAndEndTimeIsNullOrderByStartTimeDesc(student);
        if (lastSession == null) {
            throw new IllegalStateException("No active study session found for the student.");
        }

        // End the session
        lastSession.setEndTime(LocalDateTime.now());
        return studySessionRepository.save(lastSession);
    }

    public long getTotalHoursStudied(Student student) {
        // Fetch all completed sessions
        List<StudySession> sessions = studySessionRepository.findByStudentAndEndTimeIsNotNull(student);

        // Calculate total hours
        return sessions.stream()
                .mapToLong(session -> Duration.between(session.getStartTime(), session.getEndTime()).toHours())
                .sum();
    }
}
