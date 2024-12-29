package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByBranchId(Long branchId);
}
