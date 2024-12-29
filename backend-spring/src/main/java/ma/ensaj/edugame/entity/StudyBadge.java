package ma.ensaj.edugame.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "study_badges") // Update table name
public class StudyBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Badge name, e.g., "Study Novice"

    @Column(nullable = false)
    private String description; // Description of the badge

    @Column(nullable = false)
    private int requiredHours; // Required hours to earn the badge
}
