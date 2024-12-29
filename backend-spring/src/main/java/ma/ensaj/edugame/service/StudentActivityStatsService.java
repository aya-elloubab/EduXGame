package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentActivityStats;
import ma.ensaj.edugame.repository.StudentActivityStatsRepository;
import ma.ensaj.edugame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentActivityStatsService {

    @Autowired
    private StudentActivityStatsRepository statsRepository;
    @Autowired
    private StudentRepository studentRepository;

    public void incrementQuizCount(Student student) {
        StudentActivityStats stats = getOrCreateStats(student);
        stats.setCompletedQuizzes(stats.getCompletedQuizzes() + 1);
        statsRepository.save(stats);
    }

    public void incrementMatchGameCount(Student student) {
        StudentActivityStats stats = getOrCreateStats(student);
        stats.setCompletedMatchGames(stats.getCompletedMatchGames() + 1);
        statsRepository.save(stats);
    }

    public void incrementFlipcardCount(Student student) {
        StudentActivityStats stats = getOrCreateStats(student);
        stats.setCompletedFlipcards(stats.getCompletedFlipcards() + 1);
        statsRepository.save(stats);
    }

    public void incrementShortContentCount(Student student) {
        StudentActivityStats stats = getOrCreateStats(student);
        stats.setCompletedShortContents(stats.getCompletedShortContents() + 1);
        statsRepository.save(stats);
    }

    private StudentActivityStats getOrCreateStats(Student student) {
        return statsRepository.findByStudent(student)
                .orElseGet(() -> statsRepository.save(new StudentActivityStats(student)));
    }
    public StudentActivityStats getStats(Student student) {
        return statsRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Stats not found for student " + student.getId()));
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

}
