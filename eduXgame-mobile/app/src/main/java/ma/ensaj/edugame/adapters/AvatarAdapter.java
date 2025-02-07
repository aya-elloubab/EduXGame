package ma.ensaj.edugame.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.StudentAvatar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private final Context context;
    private final List<StudentAvatar> avatars;
    private final int currentPoints;

    public AvatarAdapter(Context context, List<StudentAvatar> avatars, int currentPoints) {
        this.context = context;
        this.avatars = avatars;
        this.currentPoints = currentPoints;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        StudentAvatar avatar = avatars.get(position);
        holder.bind(avatar);

        if (avatar.getTargetPoints() <= currentPoints && !avatar.isCollected()) {
            holder.avatarImage.setOnClickListener(v -> {
                claimAvatar(avatar, holder);
            });
        } else {
            holder.avatarImage.setOnClickListener(null);
        }
    }

    private void claimAvatar(StudentAvatar avatar, AvatarViewHolder holder) {
        holder.avatarImage.clearColorFilter(); // Reset for collected avatars
        playCompletionSound();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Replace with the appropriate student ID
        long studentId = getStudentIdFromPreferences();

        apiService.collectAvatar(studentId, avatar.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    avatar.setCollected(true);
                    notifyItemChanged(holder.getAdapterPosition());

                    // Show animation
                    holder.avatarImage.animate().scaleX(1.2f).scaleY(1.2f)
                            .setDuration(300)
                            .withEndAction(() -> holder.avatarImage.animate().scaleX(1f).scaleY(1f).start())
                            .start();

                    // Show message
                    Toast.makeText(context, "Avatar claimed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to claim avatar. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getStudentIdFromPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    class AvatarViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarImage;
        private final TextView avatarName;
        private final ProgressBar progressBar;
        private final TextView progressText;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            avatarName = itemView.findViewById(R.id.avatar_name);
            progressBar = itemView.findViewById(R.id.avatar_progress_bar);
            progressText = itemView.findViewById(R.id.avatar_progress_text);
        }

        public void bind(StudentAvatar studentAvatar) {
            avatarName.setText(studentAvatar.getAvatarName());
            Glide.with(context)
                    .load(studentAvatar.getAvatarImageUrl())
                    .placeholder(R.drawable.unknown)
                    .into(avatarImage);

            if (!studentAvatar.isCollected()) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0); // Grayscale for uncollected avatars
                avatarImage.setColorFilter(new ColorMatrixColorFilter(matrix));
            } else {
                avatarImage.clearColorFilter(); // Reset for collected avatars
            }

            int targetPoints = studentAvatar.getTargetPoints();
            int progressPoints = Math.min(currentPoints, targetPoints);
            progressBar.setMax(targetPoints);
            progressBar.setProgress(progressPoints);
            progressText.setText(progressPoints + "/" + targetPoints + " Points");
        }
    }
    private void playCompletionSound() {
        MediaPlayer completionSound = MediaPlayer.create(context, R.raw.level_up);
        completionSound.setOnCompletionListener(MediaPlayer::release); // Release resources when done
        completionSound.start();
    }

}
