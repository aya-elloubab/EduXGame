package ma.ensaj.edugame.models;


import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("id")
    private Long id;

    @SerializedName("element")
    private String element;

    @SerializedName("matchText")
    private String matchText;

    // Constructor
    public Match(Long id, String element, String matchText) {
        this.id = id;
        this.element = element;
        this.matchText = matchText;
    }

    // Default Constructor
    public Match() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getMatchText() {
        return matchText;
    }

    public void setMatchText(String matchText) {
        this.matchText = matchText;
    }

    // Utility Method for UI
    public String getElementOrMatchText() {
        return element != null ? element : matchText;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", element='" + element + '\'' +
                ", matchText='" + matchText + '\'' +
                '}';
    }
}
