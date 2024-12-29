package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.dto.ChapterOverviewDTO;
import ma.ensaj.edugame.dto.ChapterUpdateDTO;
import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.Flipcard;
import ma.ensaj.edugame.entity.Matches;
import ma.ensaj.edugame.entity.Quiz;
import ma.ensaj.edugame.entity.ShortContent;
import ma.ensaj.edugame.repository.ChapterRepository;
import ma.ensaj.edugame.service.ChapterService;
import ma.ensaj.edugame.service.FlipcardService;
import ma.ensaj.edugame.service.MatchService;
import ma.ensaj.edugame.service.QuizService;
import ma.ensaj.edugame.service.ShortContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chapters")
@CrossOrigin(origins = "http://localhost:3000")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FlipcardService flipcardService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ShortContentService shortContentService;
    @Autowired
    private ChapterRepository chapterRepository;

    // Créer un nouveau chapitre
    @PostMapping
    public Chapter createChapter(@Valid @RequestBody Chapter chapter) {
        return chapterService.saveChapter(chapter);
    }

    // Récupérer tous les chapitres
    @GetMapping
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters();
    }

    // Récupérer un chapitre par ID
    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapterById(@PathVariable Long id) {
        Optional<Chapter> chapter = chapterService.getChapterById(id);
        return chapter.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un chapitre
    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long id, @Valid @RequestBody Chapter chapterDetails) {
        Optional<Chapter> optionalChapter = chapterService.getChapterById(id);
        if (optionalChapter.isPresent()) {
            Chapter chapter = optionalChapter.get();
            chapter.setChapterName(chapterDetails.getChapterName());
            chapter.setDescription(chapterDetails.getDescription());
            // Vous pouvez mettre à jour les relations si nécessaire
            Chapter updatedChapter = chapterService.saveChapter(chapter);
            return ResponseEntity.ok(updatedChapter);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un chapitre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        Optional<Chapter> chapter = chapterService.getChapterById(id);
        if (chapter.isPresent()) {
            chapterService.deleteChapter(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Ajouter une Flipcard à un chapitre
    @PostMapping("/{chapterId}/flipcards")
    public ResponseEntity<Flipcard> addFlipcardToChapter(@PathVariable Long chapterId, @Valid @RequestBody Flipcard flipcard) {
        Optional<Chapter> chapterOpt = chapterService.getChapterById(chapterId);
        if (chapterOpt.isPresent()) {
            Chapter chapter = chapterOpt.get();
            flipcard.setChapter(chapter);
            Flipcard savedFlipcard = flipcardService.saveFlipcard(flipcard);
            return ResponseEntity.ok(savedFlipcard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter un Match à un chapitre
    @PostMapping("/{chapterId}/matches")
    public ResponseEntity<Matches> addMatchToChapter(@PathVariable Long chapterId, @Valid @RequestBody Matches match) {
        Optional<Chapter> chapterOpt = chapterService.getChapterById(chapterId);
        if (chapterOpt.isPresent()) {
            Chapter chapter = chapterOpt.get();
            match.setChapter(chapter);
            Matches savedMatch = matchService.saveMatch(match);
            return ResponseEntity.ok(savedMatch);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter un Quiz à un chapitre
    @PostMapping("/{chapterId}/quizzes")
    public ResponseEntity<Quiz> addQuizToChapter(@PathVariable Long chapterId, @Valid @RequestBody Quiz quiz) {
        Optional<Chapter> chapterOpt = chapterService.getChapterById(chapterId);
        if (chapterOpt.isPresent()) {
            Chapter chapter = chapterOpt.get();
            quiz.setChapter(chapter);
            Quiz savedQuiz = quizService.saveQuiz(quiz);
            return ResponseEntity.ok(savedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter un ShortContent à un chapitre
    @PostMapping("/{chapterId}/shortContents")
    public ResponseEntity<ShortContent> addShortContentToChapter(@PathVariable Long chapterId, @Valid @RequestBody ShortContent shortContent) {
        Optional<Chapter> chapterOpt = chapterService.getChapterById(chapterId);
        if (chapterOpt.isPresent()) {
            Chapter chapter = chapterOpt.get();
            shortContent.setChapter(chapter);
            ShortContent savedShortContent = shortContentService.saveShortContent(shortContent);
            return ResponseEntity.ok(savedShortContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}/data")
    public ResponseEntity<Chapter> updateChapterData(
            @PathVariable("id") Long chapterId,
            @Validated @RequestBody ChapterUpdateDTO chapterUpdateDTO) {
        Chapter updatedChapter = chapterService.updateChapter(chapterId, chapterUpdateDTO);
        return new ResponseEntity<>(updatedChapter, HttpStatus.OK);
    }
    @GetMapping("/{id}/overview")
    public ResponseEntity<ChapterOverviewDTO> getChapter(@PathVariable Long id) {
        Optional<Chapter> chapterOpt = chapterRepository.findById(id);
        if (chapterOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Chapter chapter = chapterOpt.get();
        ChapterOverviewDTO dto = new ChapterOverviewDTO();
        dto.setId(chapter.getId());
        dto.setChapterName(chapter.getChapterName());
        dto.setDescription(chapter.getDescription());
        dto.setFlipcardCount(chapter.getFlipcards().size());
        dto.setQuizCount(chapter.getQuizzes().size());
        dto.setMatchCount(chapter.getMatches().size());
        dto.setShortContentCount(chapter.getShortContents().size());
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Chapter>> getChaptersByCourse(@PathVariable Long courseId) {
        List<Chapter> chapters = chapterService.getChaptersByCourse(courseId);
        return ResponseEntity.ok(chapters);
    }

}