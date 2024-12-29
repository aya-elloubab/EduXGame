package ma.ensaj.edugame.service;

import ma.ensaj.edugame.dto.PlanetDto;
import ma.ensaj.edugame.entity.Planet;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentPlanetProgress;
import ma.ensaj.edugame.repository.PlanetRepository;
import ma.ensaj.edugame.repository.StudentPlanetProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private StudentPlanetProgressRepository studentPlanetProgressRepository;

    @Autowired
    private ChapterProgressService chapterProgressService;

    public List<Planet> getAllPlanetsWithStatus(Student student) {
        List<Planet> allPlanets = planetRepository.findAll();
        List<StudentPlanetProgress> unlockedPlanets = studentPlanetProgressRepository.findByStudent(student);

        return allPlanets.stream()
                .map(planet -> {
                    planet.setUnlocked(unlockedPlanets.stream()
                            .anyMatch(progress -> progress.getPlanet().equals(planet)));
                    return planet;
                })
                .collect(Collectors.toList());
    }

    public Planet unlockPlanetIfEligible(Student student) {
        int completedCourses = chapterProgressService.getCompletedCoursesCount(student);
        int requiredPlanets = completedCourses / 3; // One planet per 3 completed courses

        List<StudentPlanetProgress> unlockedPlanets = studentPlanetProgressRepository.findByStudent(student);

        if (unlockedPlanets.size() < requiredPlanets) {
            // Fetch the next planet to unlock
            long nextPlanetId = unlockedPlanets.size() + 1; // Next unlockable planet's ID
            Planet nextPlanet = planetRepository.findById(nextPlanetId)
                    .orElseThrow(() -> new RuntimeException("Planet not found with ID: " + nextPlanetId));
            studentPlanetProgressRepository.save(new StudentPlanetProgress(student, nextPlanet));
            return nextPlanet;
        }

        return null; // No new planets to unlock
    }
    public List<PlanetDto> getAllUnlockedPlanets(Long studentId) {
        List<StudentPlanetProgress> progressList = studentPlanetProgressRepository.findByStudentId(studentId);

        // Map to DTOs
        return progressList.stream()
                .map(progress -> {
                    Planet planet = progress.getPlanet();
                    return new PlanetDto(
                            planet.getId(),
                            planet.getName(),
                            planet.getUnlockingRequirement(),
                            planet.getDescription()
                    );
                })
                .collect(Collectors.toList());
    }
}
