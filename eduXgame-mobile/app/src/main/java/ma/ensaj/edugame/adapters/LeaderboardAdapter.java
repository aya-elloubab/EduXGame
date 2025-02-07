package ma.ensaj.edugame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.models.LeaderboardEntry;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardEntry> leaderboardEntries;

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardEntry entry = leaderboardEntries.get(position);

        holder.rankTextView.setText(String.valueOf(entry.getRank()));
        holder.nameTextView.setText(entry.getName());
        holder.pointsTextView.setText(entry.getTotalScore() + " Pts");

        Glide.with(holder.avatarImageView.getContext())
                .load(entry.getAvatarUrl())
                .placeholder(R.drawable.unknown)
                .into(holder.avatarImageView);


    }

    @Override
    public int getItemCount() {
        return leaderboardEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView;
        TextView nameTextView;
        TextView pointsTextView;
        ImageView avatarImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rank_text);
            nameTextView = itemView.findViewById(R.id.name_rank);
            pointsTextView = itemView.findViewById(R.id.points_rank);
            avatarImageView = itemView.findViewById(R.id.avatar_rank);
        }
    }
}
