    package ma.ensaj.edugame.repository;

    import ma.ensaj.edugame.entity.Student;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.Optional;

    public interface StudentRepository extends JpaRepository<Student, Long> {
        Optional<Student> findByEmail(String email);
    }
