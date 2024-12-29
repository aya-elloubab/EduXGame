package ma.ensaj.edugame.repository;


import ma.ensaj.edugame.entity.ShortContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortContentRepository extends JpaRepository<ShortContent, Long> {
    List<ShortContent> findByChapterId(Long chapterId);

}
