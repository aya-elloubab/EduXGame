package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudyBadge;
import ma.ensaj.edugame.service.StudentService;
import ma.ensaj.edugame.service.StudyBadgeService;
import ma.ensaj.edugame.service.StudySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-badges")
public class StudyBadgeController {

    private final StudyBadgeService studyBadgeService;
    private final StudySessionService studySessionService;
    private final StudentService studentService;

    public StudyBadgeController(StudyBadgeService studyBadgeService, StudySessionService studySessionService, StudentService studentService) {
        this.studyBadgeService = studyBadgeService;
        this.studySessionService = studySessionService;
        this.studentService = studentService;
    }

    // Get all badges earned by a student
    @GetMapping("/{studentId}/earned")
    public ResponseEntity<List<StudyBadge>> getEarnedBadges(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        List<StudyBadge> badges = studyBadgeService.getEarnedBadges(student);
        return ResponseEntity.ok(badges);
    }

    // Award badges to a student based on their total hours studied
    @PostMapping("/{studentId}/award")
    public ResponseEntity<List<StudyBadge>> awardBadges(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        int totalHoursStudied = (int) studySessionService.getTotalHoursStudied(student);
        List<StudyBadge> newBadges = studyBadgeService.awardBadges(student, totalHoursStudied);
        return ResponseEntity.ok(newBadges);
    }
}
