package ma.ensaj.edugame.models;

public class LoginResponse {
    private String jwt;
    private Long userId;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    public Long getUserId() {
        return userId;
    }
}
