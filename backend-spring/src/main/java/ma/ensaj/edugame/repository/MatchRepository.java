package ma.ensaj.edugame.repository;


import ma.ensaj.edugame.entity.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long> {
    List<Matches> findByChapterId(Long chapterId);

}
