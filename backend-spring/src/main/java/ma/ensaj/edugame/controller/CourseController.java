package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Course;
import ma.ensaj.edugame.entity.Subject;
import ma.ensaj.edugame.service.CourseService;
import ma.ensaj.edugame.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final SubjectService subjectService;

    public CourseController(CourseService courseService, SubjectService subjectService) {
        this.courseService = courseService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        if (updatedCourse.getSubject() == null || updatedCourse.getSubject().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course existingCourse = courseService.getCourseById(id);
        if (existingCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Subject subject = subjectService.getSubjectById(updatedCourse.getSubject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Subject ID"));
        if (subject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setSubject(subject);

        Course savedCourse = courseService.saveCourse(existingCourse);
        return ResponseEntity.ok(savedCourse);
    }

    // Delete a course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Course existingCourse = courseService.getCourseById(id);
        if (existingCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Course>> getCoursesBySubject(@PathVariable Long subjectId) {
        List<Course> courses = courseService.getCoursesBySubject(subjectId);
        return ResponseEntity.ok(courses);
    }

}
