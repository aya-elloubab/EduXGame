package ma.ensaj.edugame.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
public class StudentActivityStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private int completedQuizzes = 0;
    private int completedMatchGames = 0;
    private int completedFlipcards = 0;
    private int completedShortContents = 0;

    // Constructors
    public StudentActivityStats(Student student) {
        this.student = student;
    }
}
