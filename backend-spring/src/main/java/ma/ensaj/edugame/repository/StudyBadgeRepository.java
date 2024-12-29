package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.StudyBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyBadgeRepository extends JpaRepository<StudyBadge, Long> {
    List<StudyBadge> findAllByRequiredHoursLessThanEqual(int hours);
}
