package ma.ensaj.edugame.service;

import ma.ensaj.edugame.dto.StudentAvatarDTO;
import ma.ensaj.edugame.entity.Chapter;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.entity.StudentAvatar;
import ma.ensaj.edugame.entity.StudentPoints;
import ma.ensaj.edugame.repository.ChapterRepository;
import ma.ensaj.edugame.repository.StudentAvatarRepository;
import ma.ensaj.edugame.repository.StudentPointsRepository;
import ma.ensaj.edugame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PointsService {

    @Autowired
    private StudentPointsRepository studentPointsRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private StudentAvatarRepository studentAvatarRepository;

    public StudentPoints getStudentPoints(Long studentId, Long chapterId) {
        return studentPointsRepository.findByStudentIdAndChapterId(studentId, chapterId)
                .orElseGet(() -> createNewStudentPoints(studentId, chapterId));
    }

    public int getTotalScoreForStudent(Long studentId) {
        List<StudentPoints> pointsList = studentPointsRepository.findByStudentId(studentId);

        return pointsList.stream()
                .mapToInt(points -> points.getQuizPoints() + points.getMatchPoints())
                .sum();
    }
    public List<Map<String, Object>> getLeaderboardWithRank(Long studentId) {
        // Fetch all students and their total scores
        List<Map<String, Object>> leaderboard = studentRepository.findAll().stream()
                .map(student -> {
                    int totalScore = getTotalScoreForStudent(student.getId());
                    String avatarImageUrl = getMostCollectedAvatar(student.getId()) != null
                            ? getMostCollectedAvatar(student.getId()).getAvatarImageUrl()
                            : null;

                    Map<String, Object> entry = new HashMap<>();
                    entry.put("studentId", student.getId());
                    entry.put("name", student.getFirstName() + " " + student.getLastName());
                    entry.put("totalScore", totalScore);
                    entry.put("avatarImageUrl", avatarImageUrl);
                    return entry;
                })
                .sorted((a, b) -> Integer.compare((int) b.get("totalScore"), (int) a.get("totalScore"))) // Sort by totalScore descending
                .collect(Collectors.toList());

        // Assign ranks to all students
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).put("rank", i + 1);
        }

        // Extract top 10
        List<Map<String, Object>> top10 = leaderboard.stream().limit(10).collect(Collectors.toList());

        // Find the logged-in student
        Map<String, Object> loggedInStudent = leaderboard.stream()
                .filter(entry -> entry.get("studentId").equals(studentId))
                .findFirst()
                .orElse(null);

        // If the logged-in student is not in the top 10, add their rank and total score to the response
        if (loggedInStudent != null && !top10.contains(loggedInStudent)) {
            Map<String, Object> loggedInStudentWithRank = new HashMap<>(loggedInStudent);
            top10.add(loggedInStudentWithRank); // Add the logged-in student at the end of the top 10
        }

        return top10;
    }





    public StudentPoints updateQuizPoints(Long studentId, Long chapterId, int points) {
        StudentPoints studentPoints = studentPointsRepository.findByStudentIdAndChapterId(studentId, chapterId)
                .orElseGet(() -> createNewStudentPoints(studentId, chapterId));

        studentPoints.setQuizPoints(studentPoints.getQuizPoints() + points);
        return studentPointsRepository.save(studentPoints);
    }

    public StudentPoints updateMatchPoints(Long studentId, Long chapterId, int points) {
        StudentPoints studentPoints = studentPointsRepository.findByStudentIdAndChapterId(studentId, chapterId)
                .orElseGet(() -> createNewStudentPoints(studentId, chapterId));

        studentPoints.setMatchPoints(studentPoints.getMatchPoints() + points);
        return studentPointsRepository.save(studentPoints);
    }

    private StudentPoints createNewStudentPoints(Long studentId, Long chapterId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + chapterId));

        StudentPoints studentPoints = new StudentPoints();
        studentPoints.setStudent(student);
        studentPoints.setChapter(chapter);
        studentPoints.setQuizPoints(0);
        studentPoints.setMatchPoints(0);

        return studentPoints;
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
