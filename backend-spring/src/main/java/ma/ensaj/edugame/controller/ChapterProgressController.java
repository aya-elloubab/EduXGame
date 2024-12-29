package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.dto.*;
import ma.ensaj.edugame.entity.*;
import ma.ensaj.edugame.repository.ChapterRepository;
import ma.ensaj.edugame.repository.StudentRepository;
import ma.ensaj.edugame.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/chapter-progress")
public class ChapterProgressController {

    @Autowired
    private ChapterProgressService chapterProgressService;



    @PostMapping("/complete/short-content")
    public void completeShortContent(@RequestParam Long studentId, @RequestParam Long chapterId) {
        Student student = chapterProgressService.getStudentById(studentId); // Retrieve Student entity
        Chapter chapter = chapterProgressService.getChapterById(chapterId); // Retrieve Chapter entity
        chapterProgressService.markShortContentCompleted(student, chapter); // Mark ShortContent as completed
    }

    @PostMapping("/complete/quiz")
    public void completeQuiz(@RequestParam Long studentId, @RequestParam Long chapterId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Chapter chapter = chapterProgressService.getChapterById(chapterId);
        chapterProgressService.markQuizCompleted(student, chapter);
    }

    @PostMapping("/complete/match-game")
    public void completeMatchGame(@RequestParam Long studentId, @RequestParam Long chapterId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Chapter chapter = chapterProgressService.getChapterById(chapterId);
        chapterProgressService.markMatchGameCompleted(student, chapter);
    }

    @PostMapping("/complete/flipcard")
    public void completeFlipcard(@RequestParam Long studentId, @RequestParam Long chapterId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Chapter chapter = chapterProgressService.getChapterById(chapterId);
        chapterProgressService.markFlipcardCompleted(student, chapter);
    }
    @GetMapping("/progress")
    public double getChapterProgress(@RequestParam Long studentId, @RequestParam Long chapterId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Chapter chapter = chapterProgressService.getChapterById(chapterId);
        return chapterProgressService.getProgressValue(student, chapter);
    }
    @GetMapping("/course-progress")
    public double getCourseProgress(@RequestParam Long studentId, @RequestParam Long courseId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Course course = chapterProgressService.getCourseById(courseId);
        return chapterProgressService.calculateCourseProgress(student, course);
    }
    @GetMapping("/subject-progress")
    public double getSubjectProgress(@RequestParam Long studentId, @RequestParam Long subjectId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Subject subject = chapterProgressService.getSubjectById(subjectId);
        return chapterProgressService.calculateSubjectProgress(student, subject);
    }

    @GetMapping("/completed-courses-count")
    public ResponseEntity<Integer> getCompletedCoursesCount(@RequestParam Long studentId) {
        try {
            Student student = chapterProgressService.getStudentById(studentId);
            int completedCoursesCount = chapterProgressService.getCompletedCoursesCount(student);
            return ResponseEntity.ok(completedCoursesCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(0); // Return 0 in case of an error
        }
    }

}
