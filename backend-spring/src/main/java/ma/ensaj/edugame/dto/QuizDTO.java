package ma.ensaj.edugame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuizDTO {
    @NotBlank(message = "Question cannot be blank")
    private String question;

    @NotBlank(message = "Explanation cannot be blank")
    private String explanation;

    @Size(min = 1, message = "At least one answer is required")
    private List<@NotBlank(message = "Answer cannot be blank") String> answers;

    @Size(min = 1, message = "At least one correct answer is required")
    private List<@NotBlank(message = "Correct answer cannot be blank") String> correctAnswer;
}
