package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Flipcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlipcardRepository extends JpaRepository<Flipcard, Long> {
    List<Flipcard> findByChapter_Id(Long chapterId);

}