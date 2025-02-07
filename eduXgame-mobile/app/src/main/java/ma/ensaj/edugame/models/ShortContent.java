package ma.ensaj.edugame.models;

import com.google.gson.annotations.SerializedName;
public class ShortContent {

    @SerializedName("id")
    private Long id;

    @SerializedName("content") // Matches the backend field name
    private String content;

    private Chapter chapter;

    // Constructors, Getters, and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
