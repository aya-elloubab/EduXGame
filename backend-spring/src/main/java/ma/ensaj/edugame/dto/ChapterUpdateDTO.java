package ma.ensaj.edugame.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterUpdateDTO {
    private String chapterName;
    private String description;
    private List<FlipcardDTO> flipcards;
    private List<MatchDTO> match;
    private List<QuizDTO> quiz;
    private List<String> shortContent;
}
