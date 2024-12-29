package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.dto.StudentAvatarDTO;
import ma.ensaj.edugame.entity.Avatar;
import ma.ensaj.edugame.entity.StudentAvatar;
import ma.ensaj.edugame.service.AvatarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    // CRUD Endpoints for Avatars
    @GetMapping
    public ResponseEntity<List<Avatar>> getAllAvatars() {
        return ResponseEntity.ok(avatarService.getAllAvatars());
    }

    @GetMapping("/{avatarId}")
    public ResponseEntity<Avatar> getAvatarById(@PathVariable Long avatarId) {
        return ResponseEntity.ok(avatarService.getAvatarById(avatarId));
    }

    @PostMapping
    public ResponseEntity<Avatar> createAvatar(@RequestBody Avatar avatar) {
        return ResponseEntity.ok(avatarService.createAvatar(avatar));
    }

    @PutMapping("/{avatarId}")
    public ResponseEntity<Avatar> updateAvatar(@PathVariable Long avatarId, @RequestBody Avatar updatedAvatar) {
        return ResponseEntity.ok(avatarService.updateAvatar(avatarId, updatedAvatar));
    }

    @DeleteMapping("/{avatarId}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long avatarId) {
        avatarService.deleteAvatar(avatarId);
        return ResponseEntity.noContent().build();
    }

    // Student Avatar Management
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<StudentAvatarDTO>> getStudentAvatars(@PathVariable Long studentId) {
        return ResponseEntity.ok(avatarService.getStudentAvatars(studentId));
    }

    @PostMapping("/students/{studentId}/claim/{avatarId}")
    public ResponseEntity<Void> collectAvatar(@PathVariable Long studentId, @PathVariable Long avatarId) {
        avatarService.collectAvatar(studentId, avatarId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/{studentId}/unlocked")
    public ResponseEntity<List<Avatar>> getUnlockedAvatars(@PathVariable Long studentId) {
        return ResponseEntity.ok(avatarService.getUnlockedAvatars(studentId));
    }
    @PostMapping("/initialize/{studentId}")
    public ResponseEntity<Void> initializeStudentAvatars(@PathVariable Long studentId) {
        avatarService.initializeStudentAvatars(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/{studentId}/most-collected")
    public ResponseEntity<StudentAvatarDTO> getMostCollectedAvatar(@PathVariable Long studentId) {
        StudentAvatarDTO mostCollectedAvatar = avatarService.getMostCollectedAvatar(studentId);
        if (mostCollectedAvatar != null) {
            return ResponseEntity.ok(mostCollectedAvatar);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
