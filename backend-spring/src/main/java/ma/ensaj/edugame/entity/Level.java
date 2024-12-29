package ma.ensaj.edugame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Branch> branches = new ArrayList<>();

    // Constructor
    public Level(String name) {
        this.name = name;
    }

    // Default constructor (required by JPA)
    public Level() {}

    // Helper method to add a branch
    public void addBranch(Branch branch) {
        branches.add(branch);
        branch.setLevel(this);
    }

    // Helper method to remove a branch
    public void removeBranch(Branch branch) {
        branches.remove(branch);
        branch.setLevel(null);
    }
}
