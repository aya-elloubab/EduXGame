package ma.ensaj.edugame.models;

import com.google.gson.annotations.SerializedName;

public class Flipcard {

    @SerializedName("id")
    private Long id;

    @SerializedName("front")
    private String front;

    @SerializedName("back")
    private String back;

    @SerializedName("chapter")
    private Chapter chapter;

    // Constructors
    public Flipcard(Long id, String front, String back, Chapter chapter) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.chapter = chapter;
    }

    public Flipcard() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public String toString() {
        return "Flipcard{" +
                "id=" + id +
                ", front='" + front + '\'' +
                ", back='" + back + '\'' +
                ", chapter=" + (chapter != null ? chapter.getId() : "null") +
                '}';
    }
}
