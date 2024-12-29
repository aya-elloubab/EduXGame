package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.ShortContent;
import ma.ensaj.edugame.service.ShortContentService;
import ma.ensaj.edugame.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shortContents")
public class ShortContentController {

    @Autowired
    private ShortContentService shortContentService;

    @Autowired
    private ChapterService chapterService;


    @GetMapping("/chapter/{chapterId}")
    public List<ShortContent> getShortContentsByChapterId(@PathVariable Long chapterId) {
        return shortContentService.getShortContentsByChapterId(chapterId);
    }
    // Créer un ShortContent
    @PostMapping
    public ResponseEntity<ShortContent> createShortContent(@Valid @RequestBody ShortContent shortContent) {
        if (shortContent.getChapter() != null && shortContent.getChapter().getId() != null) {
            Optional<Chapter> chapterOpt = chapterService.getChapterById(shortContent.getChapter().getId());
            if (chapterOpt.isPresent()) {
                shortContent.setChapter(chapterOpt.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        ShortContent savedShortContent = shortContentService.saveShortContent(shortContent);
        return ResponseEntity.ok(savedShortContent);
    }

    // Récupérer tous les ShortContents
    @GetMapping
    public List<ShortContent> getAllShortContents() {
        return shortContentService.getAllShortContents();
    }

    // Récupérer un ShortContent par ID
    @GetMapping("/{id}")
    public ResponseEntity<ShortContent> getShortContentById(@PathVariable Long id) {
        Optional<ShortContent> shortContent = shortContentService.getShortContentById(id);
        return shortContent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un ShortContent
    @PutMapping("/{id}")
    public ResponseEntity<ShortContent> updateShortContent(@PathVariable Long id, @Valid @RequestBody ShortContent shortContentDetails) {
        Optional<ShortContent> optionalShortContent = shortContentService.getShortContentById(id);
        if (optionalShortContent.isPresent()) {
            ShortContent shortContent = optionalShortContent.get();
            shortContent.setContent(shortContentDetails.getContent());
            // Mettre à jour la relation avec le chapitre si nécessaire
            if (shortContentDetails.getChapter() != null && shortContentDetails.getChapter().getId() != null) {
                Optional<Chapter> chapterOpt = chapterService.getChapterById(shortContentDetails.getChapter().getId());
                chapterOpt.ifPresent(shortContent::setChapter);
            }
            ShortContent updatedShortContent = shortContentService.saveShortContent(shortContent);
            return ResponseEntity.ok(updatedShortContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un ShortContent
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortContent(@PathVariable Long id) {
        Optional<ShortContent> shortContent = shortContentService.getShortContentById(id);
        if (shortContent.isPresent()) {
            shortContentService.deleteShortContent(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
