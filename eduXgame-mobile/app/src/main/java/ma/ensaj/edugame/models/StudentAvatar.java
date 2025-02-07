package ma.ensaj.edugame.models;

public class StudentAvatar {
    private Long id;
    private String avatarName;
    private String avatarDescription;
    private String avatarImageUrl;
    private int targetPoints;
    private boolean collected;

    // Constructor
    public StudentAvatar(Long id, String avatarName, String avatarDescription, String avatarImageUrl, int targetPoints, boolean isCollected) {
        this.id = id;
        this.avatarName = avatarName;
        this.avatarDescription = avatarDescription;
        this.avatarImageUrl = avatarImageUrl;
        this.targetPoints = targetPoints;
        this.collected = isCollected;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarDescription() {
        return avatarDescription;
    }

    public void setAvatarDescription(String avatarDescription) {
        this.avatarDescription = avatarDescription;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public int getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(int targetPoints) {
        this.targetPoints = targetPoints;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        collected = collected;
    }
}
