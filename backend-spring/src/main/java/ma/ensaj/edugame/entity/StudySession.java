package ma.ensaj.edugame.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Data
@NoArgsConstructor
@Table(name = "study_sessions")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(nullable = false)
    private LocalDateTime startTime; // When the session started

    @Column(nullable = true)
    private LocalDateTime endTime; // When the session ended

    @Column(nullable = true)
    private long durationInMinutes; // Total duration of the session in minutes
}
