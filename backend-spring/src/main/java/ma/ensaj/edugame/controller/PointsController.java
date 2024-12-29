package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.StudentPoints;
import ma.ensaj.edugame.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/points")
public class PointsController {

    @Autowired
    private PointsService pointsService;

    // Update Quiz Points
    @PostMapping("/{studentId}/{chapterId}/quiz")
    public ResponseEntity<StudentPoints> updateQuizPoints(
            @PathVariable Long studentId,
            @PathVariable Long chapterId,
            @RequestBody int points) {
        StudentPoints updatedPoints = pointsService.updateQuizPoints(studentId, chapterId, points);
        return ResponseEntity.ok(updatedPoints);
    }

    // Update Match Points
    @PostMapping("/{studentId}/{chapterId}/match")
    public ResponseEntity<StudentPoints> updateMatchPoints(
            @PathVariable Long studentId,
            @PathVariable Long chapterId,
            @RequestBody int points) {
        StudentPoints updatedPoints = pointsService.updateMatchPoints(studentId, chapterId, points);
        return ResponseEntity.ok(updatedPoints);
    }

    // Get Total Score for a Student
    @GetMapping("/{studentId}/totalScore")
    public ResponseEntity<Integer> getTotalScoreForStudent(@PathVariable Long studentId) {
        int totalScore = pointsService.getTotalScoreForStudent(studentId);
        return ResponseEntity.ok(totalScore);
    }

    // Get Points for a Specific Chapter
    @GetMapping("/{studentId}/{chapterId}")
    public ResponseEntity<StudentPoints> getPointsForChapter(
            @PathVariable Long studentId,
            @PathVariable Long chapterId) {
        StudentPoints studentPoints = pointsService.getStudentPoints(studentId, chapterId);
        return ResponseEntity.ok(studentPoints);
    }

    @GetMapping("/leaderboard/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(@PathVariable Long studentId) {
        List<Map<String, Object>> leaderboard = pointsService.getLeaderboardWithRank(studentId);
        return ResponseEntity.ok(leaderboard);
    }
}
