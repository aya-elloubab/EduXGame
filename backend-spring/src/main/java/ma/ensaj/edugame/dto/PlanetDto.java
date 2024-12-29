package ma.ensaj.edugame.dto;

import lombok.Data;

@Data
public class PlanetDto {
    private Long id;
    private String name;
    private int unlockingRequirement;
    private String description;

    // Constructor
    public PlanetDto(Long id, String name, int unlockingRequirement, String description) {
        this.id = id;
        this.name = name;
        this.unlockingRequirement = unlockingRequirement;
        this.description = description;
    }

    // Getters and Setters
}
