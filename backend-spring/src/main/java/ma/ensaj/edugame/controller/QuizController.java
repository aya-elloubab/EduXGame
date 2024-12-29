package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.Quiz;
import ma.ensaj.edugame.service.QuizService;
import ma.ensaj.edugame.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private ChapterService chapterService;

    // Créer un Quiz
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz) {
        if (quiz.getChapter() != null && quiz.getChapter().getId() != null) {
            Optional<Chapter> chapterOpt = chapterService.getChapterById(quiz.getChapter().getId());
            if (chapterOpt.isPresent()) {
                quiz.setChapter(chapterOpt.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    // Récupérer tous les Quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    // Récupérer un Quiz par ID
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        return quiz.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un Quiz
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @Valid @RequestBody Quiz quizDetails) {
        Optional<Quiz> optionalQuiz = quizService.getQuizById(id);
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            quiz.setQuestion(quizDetails.getQuestion());
            quiz.setAnswers(quizDetails.getAnswers());
            quiz.setCorrectAnswers(quizDetails.getCorrectAnswers());
            // Mettre à jour la relation avec le chapitre si nécessaire
            if (quizDetails.getChapter() != null && quizDetails.getChapter().getId() != null) {
                Optional<Chapter> chapterOpt = chapterService.getChapterById(quizDetails.getChapter().getId());
                chapterOpt.ifPresent(quiz::setChapter);
            }
            Quiz updatedQuiz = quizService.saveQuiz(quiz);
            return ResponseEntity.ok(updatedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un Quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<Quiz>> getQuizzesByChapter(@PathVariable Long chapterId) {
        List<Quiz> quizzes = quizService.getQuizzesByChapterId(chapterId);
        return ResponseEntity.ok(quizzes);
    }
}
