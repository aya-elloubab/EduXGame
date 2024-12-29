package ma.ensaj.edugame.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "student_study_badges") // Update table name
public class StudentStudyBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_badge_id", nullable = false) // Updated foreign key
    private StudyBadge studyBadge;

    @Column(nullable = false)
    private LocalDateTime earnedAt; // When the badge was earned
}
