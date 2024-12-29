package ma.ensaj.edugame.controller;


import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.Matches;
import ma.ensaj.edugame.service.MatchService;
import ma.ensaj.edugame.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ChapterService chapterService;
    @GetMapping("/chapter/{chapterId}")
    public List<Matches> getMatchesByChapterId(@PathVariable Long chapterId) {
        return matchService.getMatchesByChapterId(chapterId);
    }

    // Créer un Match
    @PostMapping
    public ResponseEntity<Matches> createMatch(@Valid @RequestBody Matches match) {
        if (match.getChapter() != null && match.getChapter().getId() != null) {
            Optional<Chapter> chapterOpt = chapterService.getChapterById(match.getChapter().getId());
            if (chapterOpt.isPresent()) {
                match.setChapter(chapterOpt.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        Matches savedMatch = matchService.saveMatch(match);
        return ResponseEntity.ok(savedMatch);
    }

    // Récupérer tous les Matches
    @GetMapping
    public List<Matches> getAllMatches() {
        return matchService.getAllMatches();
    }

    // Récupérer un Match par ID
    @GetMapping("/{id}")
    public ResponseEntity<Matches> getMatchById(@PathVariable Long id) {
        Optional<Matches> match = matchService.getMatchById(id);
        return match.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un Match
    @PutMapping("/{id}")
    public ResponseEntity<Matches> updateMatch(@PathVariable Long id, @Valid @RequestBody Matches matchDetails) {
        Optional<Matches> optionalMatch = matchService.getMatchById(id);
        if (optionalMatch.isPresent()) {
            Matches match = optionalMatch.get();
            match.setElement(matchDetails.getElement());
            match.setMatchText(matchDetails.getMatchText());
            // Mettre à jour la relation avec le chapitre si nécessaire
            if (matchDetails.getChapter() != null && matchDetails.getChapter().getId() != null) {
                Optional<Chapter> chapterOpt = chapterService.getChapterById(matchDetails.getChapter().getId());
                chapterOpt.ifPresent(match::setChapter);
            }
            Matches updatedMatch = matchService.saveMatch(match);
            return ResponseEntity.ok(updatedMatch);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un Match
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        Optional<Matches> match = matchService.getMatchById(id);
        if (match.isPresent()) {
            matchService.deleteMatch(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
