package ma.ensaj.edugame.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentAvatarDTO {
    private Long id; // ID of the StudentAvatar
    private String avatarName; // Name of the Avatar
    private String avatarDescription; // Description of the Avatar
    private String avatarImageUrl; // URL of the Avatar image
    private int targetPoints; // Target points required to claim the Avatar
    private boolean isCollected; // Indicates if the Avatar is collected
}
