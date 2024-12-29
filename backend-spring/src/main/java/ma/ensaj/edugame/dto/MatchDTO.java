package ma.ensaj.edugame.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MatchDTO {
    @NotBlank(message = "Element cannot be blank")
    private String element;

    @NotBlank(message = "Match text cannot be blank")
    private String matchText;
}
