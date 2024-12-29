package ma.ensaj.edugame.dto;

import lombok.*;

public class ChapterProgressDto {
    private Long studentId;
    private Long chapterId;
    private double progress; // Progress percentage
    private boolean shortContentCompleted;
    private boolean quizCompleted;
    private boolean matchGameCompleted;
    private boolean flipcardCompleted;

    // Constructors, Getters, and Setters
    public ChapterProgressDto(Long studentId, Long chapterId, double progress,
                              boolean shortContentCompleted, boolean quizCompleted,
                              boolean matchGameCompleted, boolean flipcardCompleted) {
        this.studentId = studentId;
        this.chapterId = chapterId;
        this.progress = progress;
        this.shortContentCompleted = shortContentCompleted;
        this.quizCompleted = quizCompleted;
        this.matchGameCompleted = matchGameCompleted;
        this.flipcardCompleted = flipcardCompleted;
    }

    // Default Constructor
    public ChapterProgressDto() {}
}
