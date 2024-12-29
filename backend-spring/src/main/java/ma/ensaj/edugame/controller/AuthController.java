package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Admin;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        authService.registerStudent(student);
        return ResponseEntity.ok("Student registered successfully. Verify your email.");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        authService.registerAdmin(admin);
        return ResponseEntity.ok("Admin registered successfully. Verify your email.");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        String userType = credentials.get("userType"); // STUDENT or ADMIN

        String jwt = authService.login(email, password, userType);

        Long userId = null;
        if ("STUDENT".equals(userType)) {
            Student student = authService.getStudentByEmail(email);
            userId = student.getId();
        } else if ("ADMIN".equals(userType)) {
            Admin admin = authService.getAdminByEmail(email);
            userId = admin.getId();
        }

        assert userId != null;
        return ResponseEntity.ok(Map.of(
                "jwt", jwt,
                "userId", userId
        ));
    }

}
