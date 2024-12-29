package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Level;
import ma.ensaj.edugame.entity.Branch;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.repository.LevelRepository;
import ma.ensaj.edugame.repository.BranchRepository;
import ma.ensaj.edugame.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final LevelRepository levelRepository;
    private final BranchRepository branchRepository;

    public StudentService(StudentRepository studentRepository, LevelRepository levelRepository, BranchRepository branchRepository) {
        this.studentRepository = studentRepository;
        this.levelRepository = levelRepository;
        this.branchRepository = branchRepository;
    }

    public void updateStudentLevelAndBranch(Long studentId, Long levelId, Long branchId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid level ID"));

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));

        // Ensure the branch belongs to the selected level
        if (!level.getBranches().contains(branch)) {
            throw new IllegalArgumentException("Branch does not belong to the selected level");
        }

        student.setLevel(level);
        student.setBranch(branch);
        studentRepository.save(student);
    }
    public Map<String, Long> getStudentLevelAndBranch(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Map<String, Long> levelAndBranch = new HashMap<>();
        levelAndBranch.put("levelId", student.getLevel() != null ? student.getLevel().getId() : null);
        levelAndBranch.put("branchId", student.getBranch() != null ? student.getBranch().getId() : null);

        return levelAndBranch;
    }

    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    public List<Branch> getBranchesByLevel(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid level ID"));
        return level.getBranches();
    }
    public Student getStudentProfile(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

}