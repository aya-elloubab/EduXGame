package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.Flipcard;
import ma.ensaj.edugame.service.FlipcardService;
import ma.ensaj.edugame.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flipcards")
public class FlipcardController {

    @Autowired
    private FlipcardService flipcardService;

    @Autowired
    private ChapterService chapterService;
    @GetMapping("/chapter/{chapterId}")
    public List<Flipcard> getFlipcardsByChapter(@PathVariable Long chapterId) {
        return flipcardService.getFlipcardsByChapterId(chapterId);
    }

    // Créer une Flipcard
    @PostMapping
    public ResponseEntity<Flipcard> createFlipcard(@Valid @RequestBody Flipcard flipcard) {
        if (flipcard.getChapter() != null && flipcard.getChapter().getId() != null) {
            Optional<Chapter> chapterOpt = chapterService.getChapterById(flipcard.getChapter().getId());
            if (chapterOpt.isPresent()) {
                flipcard.setChapter(chapterOpt.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        Flipcard savedFlipcard = flipcardService.saveFlipcard(flipcard);
        return ResponseEntity.ok(savedFlipcard);
    }

    // Récupérer toutes les Flipcards
    @GetMapping
    public List<Flipcard> getAllFlipcards() {
        return flipcardService.getAllFlipcards();
    }

    // Récupérer une Flipcard par ID
    @GetMapping("/{id}")
    public ResponseEntity<Flipcard> getFlipcardById(@PathVariable Long id) {
        Optional<Flipcard> flipcard = flipcardService.getFlipcardById(id);
        return flipcard.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour une Flipcard
    @PutMapping("/{id}")
    public ResponseEntity<Flipcard> updateFlipcard(@PathVariable Long id, @Valid @RequestBody Flipcard flipcardDetails) {
        Optional<Flipcard> optionalFlipcard = flipcardService.getFlipcardById(id);
        if (optionalFlipcard.isPresent()) {
            Flipcard flipcard = optionalFlipcard.get();
            flipcard.setFront(flipcardDetails.getFront());
            flipcard.setBack(flipcardDetails.getBack());
            // Mettre à jour la relation avec le chapitre si nécessaire
            if (flipcardDetails.getChapter() != null && flipcardDetails.getChapter().getId() != null) {
                Optional<Chapter> chapterOpt = chapterService.getChapterById(flipcardDetails.getChapter().getId());
                chapterOpt.ifPresent(flipcard::setChapter);
            }
            Flipcard updatedFlipcard = flipcardService.saveFlipcard(flipcard);
            return ResponseEntity.ok(updatedFlipcard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une Flipcard
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlipcard(@PathVariable Long id) {
        Optional<Flipcard> flipcard = flipcardService.getFlipcardById(id);
        if (flipcard.isPresent()) {
            flipcardService.deleteFlipcard(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
