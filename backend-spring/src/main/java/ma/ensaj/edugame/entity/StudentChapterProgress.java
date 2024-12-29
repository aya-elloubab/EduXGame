package ma.ensaj.edugame.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class StudentChapterProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    private double progress = 0.0; // Progress percentage (0-100%)

    private boolean shortContentCompleted = false;
    private boolean quizCompleted = false;
    private boolean matchGameCompleted = false;
    private boolean flipcardCompleted = false;
}
