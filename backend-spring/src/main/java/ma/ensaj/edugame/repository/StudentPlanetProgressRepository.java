package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Planet;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentPlanetProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPlanetProgressRepository extends JpaRepository<StudentPlanetProgress, Long> {
    List<StudentPlanetProgress> findByStudent(Student student);
    boolean existsByStudentAndPlanet(Student student, Planet planet);
    List<StudentPlanetProgress> findByStudentId(Long studentId);

}
