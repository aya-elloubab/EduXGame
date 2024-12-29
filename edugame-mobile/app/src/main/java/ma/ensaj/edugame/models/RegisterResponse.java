package ma.ensaj.edugame.models;

public class RegisterResponse {
    private Long id; // Match the "id" field from the backend
    private String message; // Match the "message" field from the backend

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
