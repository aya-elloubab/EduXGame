package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudySession;
import ma.ensaj.edugame.service.StudentService;
import ma.ensaj.edugame.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private StudentService studentService;

    public StudySessionController(StudySessionService studySessionService, StudentService studentService) {
        this.studySessionService = studySessionService;
        this.studentService = studentService;
    }

    // Start a new study session
    @PostMapping("/{studentId}/start")
    public ResponseEntity<StudySession> startSession(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        StudySession session = studySessionService.startSession(student);
        return ResponseEntity.ok(session);
    }

    // End the last active study session for a student
    @PostMapping("/{studentId}/end")
    public ResponseEntity<StudySession> endLastSession(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        StudySession session = studySessionService.endLastActiveSession(student);
        return ResponseEntity.ok(session);
    }

    // Get total hours studied by a student
    @GetMapping("/{studentId}/total-hours")
    public ResponseEntity<Long> getTotalHoursStudied(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        long totalHours = studySessionService.getTotalHoursStudied(student);
        return ResponseEntity.ok(totalHours);
    }
}
