package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Streak;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.service.StreakService;
import ma.ensaj.edugame.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {
    @Autowired
    private StreakService streakService;
    @Autowired
    private StudentService studentService;


    // Endpoint to get the current streak for a student
    @GetMapping("/{studentId}")
    public ResponseEntity<Integer> getStreak(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);
        int streak = streakService.getStreak(student);
        return ResponseEntity.ok(streak);
    }

    // Endpoint to update the streak for a student
    @PostMapping("/{studentId}/update")
    public ResponseEntity<Streak> updateStreak(@PathVariable Long studentId) {
        Student student = studentService.getStudentProfile(studentId);

        // Update the streak and return the updated streak object
        Streak updatedStreak = streakService.updateStreak(student);
        return ResponseEntity.ok(updatedStreak);
    }
}
