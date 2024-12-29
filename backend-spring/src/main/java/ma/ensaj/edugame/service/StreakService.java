package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Streak;
import ma.ensaj.edugame.entity.Student;
import ma.ensaj.edugame.repository.StreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StreakService {

    private final StreakRepository streakRepository;

    public StreakService(StreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    public Streak updateStreak(Student student) {
        LocalDate today = LocalDate.now();

        // Find existing streak for the student
        Streak streak = streakRepository.findByStudent(student).orElse(new Streak());
        streak.setStudent(student);

        if (streak.getLastActivityDate() == null || !streak.getLastActivityDate().isEqual(today)) {
            if (streak.getLastActivityDate() != null && streak.getLastActivityDate().plusDays(1).isEqual(today)) {
                // Increment streak if the student is active on consecutive days
                streak.setDailyStreak(streak.getDailyStreak() + 1);
            } else {
                // Reset streak if there's a gap in activity
                streak.setDailyStreak(1);
            }
            // Update the last activity date
            streak.setLastActivityDate(today);
            return streakRepository.save(streak);
        }

        return streak; // No update needed if already active today
    }

    public int getStreak(Student student) {
        return streakRepository.findByStudent(student)
                .map(Streak::getDailyStreak)
                .orElse(0); // Default to 0 if no streak exists
    }
}
