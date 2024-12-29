package ma.ensaj.edugame.models;

public class LeaderboardEntry {
    private Long studentId;
    private String name;
    private int totalScore;
    private int rank;
    private String avatarImageUrl;
    private int rankChange; // Positive for upward, negative for downward, 0 for no change

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getAvatarUrl() {
        return avatarImageUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarImageUrl = avatarUrl;
    }

    public int getRankChange() {
        return rankChange;
    }

    public void setRankChange(int rankChange) {
        this.rankChange = rankChange;
    }
}
