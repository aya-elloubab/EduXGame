package ma.ensaj.edugame.service;

import ma.ensaj.edugame.dto.StudentAvatarDTO;
import ma.ensaj.edugame.entity.Avatar;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentAvatar;
import ma.ensaj.edugame.repository.AvatarRepository;
import ma.ensaj.edugame.repository.StudentAvatarRepository;
import ma.ensaj.edugame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentAvatarRepository studentAvatarRepository;
    private final StudentRepository studentRepository;

    @Autowired
    private PointsService pointsService;

    public AvatarService(AvatarRepository avatarRepository, StudentAvatarRepository studentAvatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentAvatarRepository = studentAvatarRepository;
        this.studentRepository = studentRepository;
    }

    // CRUD Methods for Avatars
    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }

    public Avatar getAvatarById(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not found."));
    }

    public Avatar createAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    public Avatar updateAvatar(Long avatarId, Avatar updatedAvatar) {
        Avatar avatar = getAvatarById(avatarId);
        avatar.setName(updatedAvatar.getName());
        avatar.setDescription(updatedAvatar.getDescription());
        avatar.setImageUrl(updatedAvatar.getImageUrl());
        avatar.setTargetPoints(updatedAvatar.getTargetPoints());
        return avatarRepository.save(avatar);
    }

    public void deleteAvatar(Long avatarId) {
        Avatar avatar = getAvatarById(avatarId);
        avatarRepository.delete(avatar);
    }

    // Claim Avatar
    public void collectAvatar(Long studentId, Long avatarId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));

        StudentAvatar studentAvatar = studentAvatarRepository.findByStudentAndAvatarId(student, avatarId)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not associated with student."));

        if (pointsService.getTotalScoreForStudent(studentId) >= studentAvatar.getAvatar().getTargetPoints() && !studentAvatar.isCollected()) {
            studentAvatar.setCollected(true);
            studentAvatarRepository.save(studentAvatar);
        } else {
            throw new IllegalStateException("Not enough points to claim this avatar.");
        }
    }

    // Fetch Student Avatars
    public List<StudentAvatarDTO> getStudentAvatars(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));

        List<StudentAvatar> studentAvatars = studentAvatarRepository.findByStudent(student);

        if (studentAvatars.isEmpty()) {
            this.initializeStudentAvatars(studentId); // Initialize avatars
            studentAvatars = studentAvatarRepository.findByStudent(student); // Re-fetch after initialization
        }

        return studentAvatars.stream()
                .map(sa -> new StudentAvatarDTO(
                        sa.getAvatar().getId(),
                        sa.getAvatar().getName(),
                        sa.getAvatar().getDescription(),
                        sa.getAvatar().getImageUrl(),
                        sa.getAvatar().getTargetPoints(),
                        sa.isCollected()
                ))
                .collect(Collectors.toList());
    }


    // Fetch Unlocked Avatars for Student
    public List<Avatar> getUnlockedAvatars(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));

        return avatarRepository.findAll().stream()
                .filter(avatar -> pointsService.getTotalScoreForStudent(studentId) >= avatar.getTargetPoints())
                .collect(Collectors.toList());
    }
    public void initializeStudentAvatars(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));

        List<Avatar> avatars = avatarRepository.findAll();
        List<StudentAvatar> studentAvatars = avatars.stream()
                .map(avatar -> {
                    StudentAvatar sa = new StudentAvatar();
                    sa.setStudent(student);
                    sa.setAvatar(avatar);
                    sa.setCollected(false);
                    return sa;
                })
                .collect(Collectors.toList());

        studentAvatarRepository.saveAll(studentAvatars);
    }
    public StudentAvatarDTO getMostCollectedAvatar(Long studentId) {
        List<StudentAvatar> studentAvatars = studentAvatarRepository.findByStudentId(studentId);

        return studentAvatars.stream()
                .filter(StudentAvatar::isCollected)
                .max((a1, a2) -> Integer.compare(a1.getAvatar().getTargetPoints(), a2.getAvatar().getTargetPoints()))
                .map(this::toStudentAvatarDTO)
                .orElse(null);
    }

    private StudentAvatarDTO toStudentAvatarDTO(StudentAvatar studentAvatar) {
        StudentAvatarDTO dto = new StudentAvatarDTO();
        dto.setId(studentAvatar.getStudent().getId());
        dto.setAvatarName(studentAvatar.getAvatar().getName());
        dto.setAvatarDescription(studentAvatar.getAvatar().getDescription());
        dto.setAvatarImageUrl(studentAvatar.getAvatar().getImageUrl());
        dto.setTargetPoints(studentAvatar.getAvatar().getTargetPoints());
        dto.setCollected(studentAvatar.isCollected());
        return dto;
    }
}
