package ma.ensaj.edugame.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
public class StudentPlanetProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planet_id", nullable = false)
    private Planet planet;

    private LocalDateTime unlockedAt;

    public StudentPlanetProgress(Student student, Planet planet) {
        this.student = student;
        this.planet = planet;
        this.unlockedAt = LocalDateTime.now();
    }
}
