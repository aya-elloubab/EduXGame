package ma.ensaj.edugame.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectProgressDto {
    private String subjectName;
    private double progress;
    private boolean completed;
}
