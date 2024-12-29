package ma.ensaj.edugame.models;

public class LoginRequest {
    private String email;
    private String password;
    private String userType;

    public LoginRequest(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // Getters and setters
}
