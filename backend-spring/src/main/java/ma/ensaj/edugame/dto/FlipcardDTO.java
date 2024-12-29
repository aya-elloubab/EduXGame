package ma.ensaj.edugame.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlipcardDTO {
    @NotBlank(message = "Front text cannot be blank")
    private String front;

    @NotBlank(message = "Back text cannot be blank")
    private String back;
}
