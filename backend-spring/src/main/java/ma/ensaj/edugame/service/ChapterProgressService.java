package ma.ensaj.edugame.service;

import ma.ensaj.edugame.dto.ChapterProgressDto;
import ma.ensaj.edugame.dto.CourseProgressDto;
import ma.ensaj.edugame.dto.SubjectProgressDto;
import ma.ensaj.edugame.entity.*;
import ma.ensaj.edugame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ChapterProgressService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private StudentChapterProgressRepository progressRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentActivityStatsService studentActivityStatsService;

    public void markShortContentCompleted(Student student, Chapter chapter) {
        StudentChapterProgress progress = getOrCreateProgress(student, chapter);

        if (!progress.isShortContentCompleted()) {
            progress.setShortContentCompleted(true);
            updateChapterProgress(progress);

            // Increment the quiz count for the student
            studentActivityStatsService.incrementShortContentCount(student);
        }
    }

    public void markQuizCompleted(Student student, Chapter chapter) {
        StudentChapterProgress progress = getOrCreateProgress(student, chapter);
        if (!progress.isQuizCompleted()) {
            progress.setQuizCompleted(true);
            updateChapterProgress(progress);
            // Increment the quiz count for the student
            studentActivityStatsService.incrementQuizCount(student);
        }
    }

    public void markMatchGameCompleted(Student student, Chapter chapter) {
        StudentChapterProgress progress = getOrCreateProgress(student, chapter);

        if (!progress.isMatchGameCompleted()) {
            progress.setMatchGameCompleted(true);
            updateChapterProgress(progress);

            studentActivityStatsService.incrementMatchGameCount(student);
        }
    }

    public void markFlipcardCompleted(Student student, Chapter chapter) {
        StudentChapterProgress progress = getOrCreateProgress(student, chapter);


        if (!progress.isFlipcardCompleted()) {
            progress.setFlipcardCompleted(true);
            updateChapterProgress(progress);
            // Increment the quiz count for the student
            studentActivityStatsService.incrementFlipcardCount(student);
        }
    }

    private StudentChapterProgress getOrCreateProgress(Student student, Chapter chapter) {
        return progressRepository.findByStudentAndChapter(student, chapter)
                .orElseGet(() -> {
                    StudentChapterProgress newProgress = new StudentChapterProgress();
                    newProgress.setStudent(student);
                    newProgress.setChapter(chapter);
                    return progressRepository.save(newProgress);
                });
    }

    private void updateChapterProgress(StudentChapterProgress progress) {
        int completedActivities = 0;

        if (progress.isShortContentCompleted()) completedActivities++;
        if (progress.isQuizCompleted()) completedActivities++;
        if (progress.isMatchGameCompleted()) completedActivities++;
        if (progress.isFlipcardCompleted()) completedActivities++;

        // Calculate progress as a percentage
        double progressPercentage = (completedActivities / 4.0) * 100.0;
        progress.setProgress(progressPercentage);

        // Save the updated progress
        progressRepository.save(progress);
    }
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    public Chapter getChapterById(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found with ID: " + chapterId));
    }
    public ChapterProgressDto getChapterProgress(Student student, Chapter chapter) {
        StudentChapterProgress progress = progressRepository.findByStudentAndChapter(student, chapter)
                .orElseThrow(() -> new RuntimeException("Progress not found for student " + student.getId() + " and chapter " + chapter.getId()));

        return new ChapterProgressDto(
                student.getId(),
                chapter.getId(),
                progress.getProgress(),
                progress.isShortContentCompleted(),
                progress.isQuizCompleted(),
                progress.isMatchGameCompleted(),
                progress.isFlipcardCompleted()
        );
    }
    public double getProgressValue(Student student, Chapter chapter) {
        StudentChapterProgress progress = progressRepository.findByStudentAndChapter(student, chapter)
                .orElseThrow(() -> new RuntimeException("Progress not found for student " + student.getId() + " and chapter " + chapter.getId()));

        return progress.getProgress(); // Return only the progress percentage
    }
    public double calculateCourseProgress(Student student, Course course) {
        List<Chapter> chapters = course.getChapters(); // Fetch all chapters for the course

        if (chapters.isEmpty()) {
            return 0.0; // No chapters, progress is 0
        }

        double totalProgress = 0.0;

        // Loop through each chapter and get its progress for the student
        for (Chapter chapter : chapters) {
            StudentChapterProgress progress = progressRepository.findByStudentAndChapter(student, chapter)
                    .orElse(new StudentChapterProgress()); // Default progress is 0 if no record exists
            totalProgress += progress.getProgress();
        }

        // Calculate the average progress
        return totalProgress / chapters.size();
    }
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }

    public double calculateSubjectProgress(Student student, Subject subject) {
        List<Course> courses = subject.getCourses(); // Fetch all courses for the subject

        if (courses.isEmpty()) {
            return 0.0; // No courses, progress is 0
        }

        double totalProgress = 0.0;

        // Loop through each course and get its progress for the student
        for (Course course : courses) {
            double courseProgress = calculateCourseProgress(student, course); // Reuse course progress calculation
            totalProgress += courseProgress;
        }

        // Calculate the average progress
        return totalProgress / courses.size();
    }
    public Subject getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));
    }
    public int getCompletedCoursesCount(Student student) {
        List<Course> allCourses = courseRepository.findAll();
        int completedCoursesCount = 0;

        for (Course course : allCourses) {
            double courseProgress = calculateCourseProgress(student, course);
            if (courseProgress == 100.0) { // A course is considered complete at 100% progress
                completedCoursesCount++;
            }
        }

        return completedCoursesCount;
    }
}
