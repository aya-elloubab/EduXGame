package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.dto.PlanetDto;
import ma.ensaj.edugame.entity.Planet;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentActivityStats;
import ma.ensaj.edugame.repository.PlanetRepository;
import ma.ensaj.edugame.repository.StudentPlanetProgressRepository;
import ma.ensaj.edugame.service.ChapterProgressService;
import ma.ensaj.edugame.service.PlanetService;
import ma.ensaj.edugame.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @Autowired
    private ChapterProgressService chapterProgressService;
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private StudentPlanetProgressRepository studentPlanetProgressRepository;
    @Autowired
    private StudentPlanetProgressRepository progressRepository;


    @GetMapping
    public List<Planet> getAllPlanets(@RequestParam Long studentId) {
        Student student = chapterProgressService.getStudentById(studentId);
        return planetRepository.findAll().stream()
                .peek(planet -> planet.setUnlocked(
                        studentPlanetProgressRepository.existsByStudentAndPlanet(student, planet)))
                .collect(Collectors.toList());
    }

    @GetMapping("/unlocked/all")
    public ResponseEntity<List<PlanetDto>> getAllUnlockedPlanets(@RequestParam Long studentId) {
        try {
            List<PlanetDto> unlockedPlanets = planetService.getAllUnlockedPlanets(studentId);
            return ResponseEntity.ok(unlockedPlanets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/unlock")
    public ResponseEntity<String> unlockPlanet(@RequestParam Long studentId) {
        Student student = chapterProgressService.getStudentById(studentId);
        Planet unlockedPlanet = planetService.unlockPlanetIfEligible(student);
        if (unlockedPlanet != null) {
            return ResponseEntity.ok("Unlocked planet: " + unlockedPlanet.getName());
        } else {
            return ResponseEntity.ok("No new planets unlocked.");
        }
    }
}
