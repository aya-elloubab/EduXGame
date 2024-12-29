package ma.ensaj.edugame.repository;

import ma.ensaj.edugame.entity.StudentAvatar;
import ma.ensaj.edugame.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAvatarRepository extends JpaRepository<StudentAvatar, Long> {
    List<StudentAvatar> findByStudentId(Long studentId);
    List<StudentAvatar> findByStudent(Student student);
    Optional<StudentAvatar> findByStudentAndAvatarId(Student student, Long avatarId);
}
