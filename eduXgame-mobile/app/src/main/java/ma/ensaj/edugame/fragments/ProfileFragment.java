package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.AvatarAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.StudentAvatar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView highestCollectedAvatar;
    private TextView highestCollectedAvatarName;
    private RecyclerView avatarRecyclerView;

    private ApiService apiService;
    private Long studentId;
    private List<StudentAvatar> studentAvatars;
    private int currentPoints; // Mocked points (replace with backend fetch if needed)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        highestCollectedAvatar = view.findViewById(R.id.highest_collected_avatar);
        highestCollectedAvatarName = view.findViewById(R.id.highest_collected_avatar_name);
        avatarRecyclerView = view.findViewById(R.id.avatar_recycler_view);

        avatarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = RetrofitClient.getInstance().create(ApiService.class);
        studentId = getUserId();
        fetchAndDisplayTotalPoints();
        loadAvatars();

        return view;
    }

    private void loadAvatars() {
        apiService.getStudentAvatars(studentId).enqueue(new Callback<List<StudentAvatar>>() {
            @Override
            public void onResponse(Call<List<StudentAvatar>> call, Response<List<StudentAvatar>> response) {
                if (response.isSuccessful() && response.body() != null) {
                     studentAvatars = response.body();
                    for (StudentAvatar studentAvatar : studentAvatars) {
                        Log.d("ProfileFragment", "Checking avatar: " + studentAvatar.getAvatarName() +
                                ", Collected: " + studentAvatar.isCollected() +
                                ", TargetPoints: " + studentAvatar.getTargetPoints());
                    }
                    displayHighestCollectedAvatar(studentAvatars);
                    avatarRecyclerView.setAdapter(new AvatarAdapter(getContext(), studentAvatars, currentPoints));
                } else {
                    Toast.makeText(getContext(), "Failed to load avatars.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StudentAvatar>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayHighestCollectedAvatar(List<StudentAvatar> studentAvatars) {
        if (studentAvatars == null || studentAvatars.isEmpty()) {
            highestCollectedAvatar.setImageResource(R.drawable.profile);
            highestCollectedAvatarName.setText("No avatar collected yet.");
            return;
        }

        StudentAvatar highestCollected = null;

        // Log the list size
        Log.d("ProfileFragment", "Number of avatars : " + studentAvatars.size()+" id use"+getUserId());

        // Iterate through the avatars to find the highest collected one
        for (StudentAvatar studentAvatar : studentAvatars) {
            Log.d("ProfileFragment", "Checking avatar: " + studentAvatar.getAvatarName() +
                    ", Collected: " + studentAvatar.isCollected() +
                    ", TargetPoints: " + studentAvatar.getTargetPoints());

            if (studentAvatar.isCollected()) {
                if (highestCollected == null || studentAvatar.getTargetPoints() > highestCollected.getTargetPoints()) {
                    highestCollected = studentAvatar;
                    Log.d("ProfileFragment", "New highest collected: " + highestCollected.getAvatarName());
                }
            }
        }

        // Display the highest collected avatar or a placeholder if none are collected
        if (highestCollected != null) {
            Glide.with(this)
                    .load(highestCollected.getAvatarImageUrl())
                    .placeholder(R.drawable.profile)
                    .into(highestCollectedAvatar);

            highestCollectedAvatarName.setText(highestCollected.getAvatarName());
        } else {
            highestCollectedAvatar.setImageResource(R.drawable.profile);
            highestCollectedAvatarName.setText("No avatar collected yet.");
        }
    }



    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id",3L);
    }
    private void fetchAndDisplayTotalPoints() {
        apiService.getTotalScore(studentId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentPoints = response.body();
                    // You can update the UI here if you need to show total points in a TextView
                    Toast.makeText(getContext(), "Total Points: " + currentPoints, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch total points.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching total points: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
