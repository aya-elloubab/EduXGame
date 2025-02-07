package ma.ensaj.edugame.models;

public class StudentStats {
    private int studentId;
    private int completedQuizzes;
    private int completedMatchGames;
    private int completedFlipcards;
    private int completedShortContents;

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
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
