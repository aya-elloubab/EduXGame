package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.StudyBadge;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentStudyBadge;
import ma.ensaj.edugame.repository.StudyBadgeRepository;
import ma.ensaj.edugame.repository.StudentStudyBadgeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyBadgeService {

    private final StudyBadgeRepository studyBadgeRepository;
    private final StudentStudyBadgeRepository studentStudyBadgeRepository;

    public StudyBadgeService(StudyBadgeRepository studyBadgeRepository, StudentStudyBadgeRepository studentStudyBadgeRepository) {
        this.studyBadgeRepository = studyBadgeRepository;
        this.studentStudyBadgeRepository = studentStudyBadgeRepository;
    }

    // Award badges based on total hours studied
    public List<StudyBadge> awardBadges(Student student, int totalHoursStudied) {
        // Fetch all eligible badges
        List<StudyBadge> eligibleBadges = studyBadgeRepository.findAllByRequiredHoursLessThanEqual(totalHoursStudied);

        // Find badges already earned by the student
        List<StudyBadge> alreadyEarned = studentStudyBadgeRepository.findByStudent(student)
                .stream()
                .map(StudentStudyBadge::getStudyBadge)
                .collect(Collectors.toList());

        // Determine new badges to award
        List<StudyBadge> newBadges = eligibleBadges.stream()
                .filter(badge -> !alreadyEarned.contains(badge))
                .collect(Collectors.toList());

        // Save new badges for the student
        for (StudyBadge badge : newBadges) {
            StudentStudyBadge studentStudyBadge = new StudentStudyBadge();
            studentStudyBadge.setStudent(student);
            studentStudyBadge.setStudyBadge(badge);
            studentStudyBadge.setEarnedAt(LocalDateTime.now());
            studentStudyBadgeRepository.save(studentStudyBadge);
        }

        return newBadges;
    }

    // Get all badges earned by a student
    public List<StudyBadge> getEarnedBadges(Student student) {
        return studentStudyBadgeRepository.findByStudent(student)
                .stream()
                .map(StudentStudyBadge::getStudyBadge)
                .collect(Collectors.toList());
    }
}
