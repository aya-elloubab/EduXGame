package ma.ensaj.edugame.models;

import com.google.gson.annotations.SerializedName;

public class StudentActivityStatsDto {

    @SerializedName("studentId")
    private Long studentId;

    @SerializedName("completedQuizzes")
    private int completedQuizzes;

    @SerializedName("completedMatchGames")
    private int completedMatchGames;

    @SerializedName("completedFlipcards")
    private int completedFlipcards;

    @SerializedName("completedShortContents")
    private int completedShortContents;

    // Constructor
    public StudentActivityStatsDto(Long studentId, int completedQuizzes, int completedMatchGames, int completedFlipcards, int completedShortContents) {
        this.studentId = studentId;
        this.completedQuizzes = completedQuizzes;
        this.completedMatchGames = completedMatchGames;
        this.completedFlipcards = completedFlipcards;
        this.completedShortContents = completedShortContents;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public int getCompletedQuizzes() {
        return completedQuizzes;
    }

    public void setCompletedQuizzes(int completedQuizzes) {
        this.completedQuizzes = completedQuizzes;
    }

    public int getCompletedMatchGames() {
        return completedMatchGames;
    }

    public void setCompletedMatchGames(int completedMatchGames) {
        this.completedMatchGames = completedMatchGames;
    }

    public int getCompletedFlipcards() {
        return completedFlipcards;
    }

    public void setCompletedFlipcards(int completedFlipcards) {
        this.completedFlipcards = completedFlipcards;
    }

    public int getCompletedShortContents() {
        return completedShortContents;
    }

    public void setCompletedShortContents(int completedShortContents) {
        this.completedShortContents = completedShortContents;
    }
}
