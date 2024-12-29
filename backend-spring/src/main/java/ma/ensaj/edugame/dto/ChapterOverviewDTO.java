package ma.ensaj.edugame.dto;

import lombok.Data;

@Data
public class ChapterOverviewDTO {
    private Long id;
    private String chapterName;
    private String description;
    private int flipcardCount;
    private int quizCount;
    private int matchCount;
    private int shortContentCount;

}
