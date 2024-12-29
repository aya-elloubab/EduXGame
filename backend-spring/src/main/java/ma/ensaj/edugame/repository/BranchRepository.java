package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Branch;
import ma.ensaj.edugame.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByLevel(Level level);
    @Query("SELECT b FROM Branch b JOIN FETCH b.level")
    List<Branch> findAllWithLevels();

}
