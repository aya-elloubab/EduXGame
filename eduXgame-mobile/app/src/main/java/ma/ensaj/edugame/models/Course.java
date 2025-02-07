package ma.ensaj.edugame.models;

import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("id")
    private Long id;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("description")
    private String description;

    @SerializedName("subject")
    private Subject subject;

    // Constructor
    public Course(Long id, String courseName, String description, Subject subject) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.subject = subject;
    }

    // Default Constructor
    public Course() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    // toString Method (Optional, for debugging)
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", subject=" + (subject != null ? subject.getName() : "null") +
                '}';
    }
}
