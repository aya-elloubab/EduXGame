package ma.ensaj.edugame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseProgressDto {
    private String courseName;
    private double progress;
    private boolean completed;
}
