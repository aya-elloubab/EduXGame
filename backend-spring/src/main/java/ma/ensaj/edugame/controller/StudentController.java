package ma.ensaj.edugame.controller;


import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PutMapping("/{id}/level-branch")
    public ResponseEntity<?> updateLevelAndBranch(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        Long levelId = payload.get("levelId");
        Long branchId = payload.get("branchId");
        studentService.updateStudentLevelAndBranch(id, levelId, branchId);
        return ResponseEntity.ok("Level and branch updated successfully.");
    }
    @GetMapping("/{id}/level-branch")
    public ResponseEntity<?> getStudentLevelAndBranch(@PathVariable Long id) {
        Map<String, Long> levelAndBranch = studentService.getStudentLevelAndBranch(id);
        return ResponseEntity.ok(levelAndBranch);
    }


    @GetMapping("/levels")
    public ResponseEntity<?> getAllLevels() {
        return ResponseEntity.ok(studentService.getAllLevels());
    }

    @GetMapping("/branches/{levelId}")
    public ResponseEntity<?> getBranchesByLevel(@PathVariable Long levelId) {
        return ResponseEntity.ok(studentService.getBranchesByLevel(levelId));
    }
    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getStudentProfile(@PathVariable Long id) {
        Student student = studentService.getStudentProfile(id);
        return ResponseEntity.ok(Map.of(
                "firstName", student.getFirstName(),
                "lastName", student.getLastName(),
                "email", student.getEmail(),
                "phone", student.getPhone(),
                "school", student.getSchool()
        ));
    }

}