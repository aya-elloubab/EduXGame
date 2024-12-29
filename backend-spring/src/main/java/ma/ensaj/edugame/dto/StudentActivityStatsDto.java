package ma.ensaj.edugame.dto;

public class StudentActivityStatsDto {
    private Long studentId;
    private int completedQuizzes;
    private int completedMatchGames;
    private int completedFlipcards;
    private int completedShortContents;

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
