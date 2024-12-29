package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.*;
import ma.ensaj.edugame.repository.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final String jwtSecret = "6yY#7N%pD@e1$k9A!xT&4G#qW@mZ2aK$E!dF3rQ2pX8cR6oT&L";

    public AuthService(StudentRepository studentRepository, AdminRepository adminRepository,
                       EmailVerificationTokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void registerStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setIsVerified(false);
        studentRepository.save(student);
        sendVerificationEmail(student.getId(), "STUDENT", student.getEmail());
    }

    public void registerAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setIsVerified(false);
        adminRepository.save(admin);
        sendVerificationEmail(admin.getId(), "ADMIN", admin.getEmail());
    }

    private void sendVerificationEmail(Long userId, String userType, String email) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(userId);
        verificationToken.setUserType(userType);
        verificationToken.setExpirationTime(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify Your Email");
        message.setText("Click the link to verify: http://localhost:8088/api/auth/verify?token=" + token);
        mailSender.send(message);
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        if ("STUDENT".equals(verificationToken.getUserType())) {
            Student student = studentRepository.findById(verificationToken.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            student.setIsVerified(true);
            studentRepository.save(student);
        } else if ("ADMIN".equals(verificationToken.getUserType())) {
            Admin admin = adminRepository.findById(verificationToken.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            admin.setIsVerified(true);
            adminRepository.save(admin);
        }
    }

    public String login(String email, String password, String userType) {
        if ("STUDENT".equals(userType)) {
            Student student = studentRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            if (!passwordEncoder.matches(password, student.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            if (!student.getIsVerified()) {
                throw new IllegalArgumentException("Email not verified");
            }
            return generateToken(student.getId(), userType);
        } else if ("ADMIN".equals(userType)) {
            Admin admin = adminRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
            if (!passwordEncoder.matches(password, admin.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            if (!admin.getIsVerified()) {
                throw new IllegalArgumentException("Email not verified");
            }
            return generateToken(admin.getId(), userType);
        }
        throw new IllegalArgumentException("Invalid user type"+userType);
    }

    private String generateToken(Long id, String userType) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
    }

}
