package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.dto.StudentActivityStatsDto;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentActivityStats;
import ma.ensaj.edugame.service.StudentActivityStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-stats")
public class StudentActivityStatsController {

    @Autowired
    private StudentActivityStatsService statsService;

    @GetMapping
    public StudentActivityStatsDto getStats(@RequestParam Long studentId) {
        Student student = statsService.getStudentById(studentId);
        StudentActivityStats stats = statsService.getStats(student);
        return new StudentActivityStatsDto(
                student.getId(),
                stats.getCompletedQuizzes(),
                stats.getCompletedMatchGames(),
                stats.getCompletedFlipcards(),
                stats.getCompletedShortContents()
        );
    }
}
