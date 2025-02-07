package ma.ensaj.edugame.models;
public class Planet {
    private String name;
    private boolean unlocked;

    public Planet(String name, int progress, boolean isUnlocked) {
        this.name = name;
        this.unlocked = isUnlocked;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    private int unlockingRequirement;

    public int getUnlockingRequirement() {
        return unlockingRequirement;
    }

    public void setUnlockingRequirement(int unlockingRequirement) {
        this.unlockingRequirement = unlockingRequirement;
    }

    public boolean unlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { unlocked = unlocked; }
}
