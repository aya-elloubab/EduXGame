package ma.ensaj.edugame.models;

import com.google.gson.annotations.SerializedName;

public class Chapter {

    @SerializedName("id")
    private Long id;

    @SerializedName("chapterName")
    private String chapterName;

    @SerializedName("description")
    private String description;

    @SerializedName("course")
    private Course course;

    // Constructor
    public Chapter(Long id, String chapterName, String description, Course course) {
        this.id = id;
        this.chapterName = chapterName;
        this.description = description;
        this.course = course;
    }

    // Default Constructor
    public Chapter() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // toString Method (Optional, for debugging)
    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", chapterName='" + chapterName + '\'' +
                ", description='" + description + '\'' +
                ", course=" + (course != null ? course.getCourseName() : "null") +
                '}';
    }
}
