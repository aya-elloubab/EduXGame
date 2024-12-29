package ma.ensaj.edugame.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("branch")
    private List<Subject> subjects;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    @JsonBackReference // Empêche la sérialisation dans le sens "Branch -> Level"
    private Level level;

    // Constructor
    public Branch(String name) {
        this.name = name;
    }

    // Default constructor (required by JPA)
    public Branch() {}
}
