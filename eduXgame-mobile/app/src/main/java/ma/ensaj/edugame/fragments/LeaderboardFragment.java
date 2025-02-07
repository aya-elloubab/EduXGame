package ma.ensaj.edugame.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.LeaderboardAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.LeaderboardEntry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {

    private ImageView avatarFirst, avatarSecond, avatarThird;
    private TextView nameFirst, nameSecond, nameThird;
    private TextView scoreFirst, scoreSecond, scoreThird;
    private RecyclerView recyclerView;

    private LeaderboardAdapter adapter;
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

    private static final String TAG = "LeaderboardFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // Initialize views for the top 3 section
        avatarFirst = view.findViewById(R.id.avatar_first);
        avatarSecond = view.findViewById(R.id.avatar_second);
        avatarThird = view.findViewById(R.id.avatar_third);
        nameFirst = view.findViewById(R.id.name_first);
        nameSecond = view.findViewById(R.id.name_second);
        nameThird = view.findViewById(R.id.name_third);
        scoreFirst = view.findViewById(R.id.score_first);
        scoreSecond = view.findViewById(R.id.score_second);
        scoreThird = view.findViewById(R.id.score_third);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter(leaderboardEntries);
        recyclerView.setAdapter(adapter);

        // Fetch leaderboard data
        fetchLeaderboard(3L); // Replace `3L` with the logged-in student's ID

        return view;
    }

    private void fetchLeaderboard(Long studentId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getLeaderboard(studentId).enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LeaderboardEntry> leaderboard = response.body();
                    updateTopThree(leaderboard);
                    updateRecyclerView(leaderboard);
                } else {
                    Toast.makeText(getContext(), "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error in response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<LeaderboardEntry>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error in API call: " + t.getMessage());
            }
        });
    }

    private void updateTopThree(List<LeaderboardEntry> leaderboard) {
        if (leaderboard.size() > 0) {
            LeaderboardEntry first = leaderboard.get(0);
            setTopEntry(first, avatarFirst, nameFirst, scoreFirst);
        }

        if (leaderboard.size() > 1) {
            LeaderboardEntry second = leaderboard.get(1);
            setTopEntry(second, avatarSecond, nameSecond, scoreSecond);
        }

        if (leaderboard.size() > 2) {
            LeaderboardEntry third = leaderboard.get(2);
            setTopEntry(third, avatarThird, nameThird, scoreThird);
        }
    }

    private void updateRecyclerView(List<LeaderboardEntry> leaderboard) {
        if (leaderboard.size() > 3) {
            List<LeaderboardEntry> sublist = leaderboard.subList(3, Math.min(10, leaderboard.size())); // Ranks 4–10
            leaderboardEntries.clear();
            leaderboardEntries.addAll(sublist);
            adapter.notifyDataSetChanged();
        }
    }

    private void setTopEntry(LeaderboardEntry entry, ImageView avatar, TextView name, TextView score) {
        Glide.with(this)
                .load(entry.getAvatarUrl())
                .placeholder(R.drawable.unknown)
                .into(avatar);

        name.setText(entry.getName());
        score.setText(entry.getTotalScore() + " Pts");
    }
}
