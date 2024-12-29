package ma.ensaj.edugame.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.PlanetAdapter;
import ma.ensaj.edugame.adapters.SubjectAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Branch;
import ma.ensaj.edugame.models.Level;
import ma.ensaj.edugame.models.Planet;
import ma.ensaj.edugame.models.StudentActivityStatsDto;
import ma.ensaj.edugame.models.Subject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView classInfoTextView;
    private RecyclerView subjectsRecyclerView;
    private SubjectAdapter subjectAdapter;
    private Long currentLevelId;
    private Long currentBranchId;
    private Button btnViewPlanetMap;


    // New UI components for student stats
    private ProgressBar quizzesProgress, matchGamesProgress, flashcardsProgress, shortContentsProgress;
    private TextView streakTextView;
    private RecyclerView unlockedPlanetsRecyclerView;
    private PlanetAdapter planetAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize existing UI components
        classInfoTextView = view.findViewById(R.id.classInfo);
        subjectsRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize new UI components
        quizzesProgress = view.findViewById(R.id.quizzesProgress);
        matchGamesProgress = view.findViewById(R.id.matchGamesProgress);
        flashcardsProgress = view.findViewById(R.id.flashcardsProgress);
        shortContentsProgress = view.findViewById(R.id.shortContentsProgress);
        streakTextView = view.findViewById(R.id.streakTextView);
        // Initialize RecyclerView for unlocked planets
        unlockedPlanetsRecyclerView = view.findViewById(R.id.unlockedPlanetsRecyclerView);
        unlockedPlanetsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize the button
        btnViewPlanetMap = view.findViewById(R.id.btnViewPlanetMap);
        streakTextView = view.findViewById(R.id.streakTextView);

        // Set click listener for the button
        btnViewPlanetMap.setOnClickListener(v -> navigateToPlanetMapFragment());

        // Fetch unlocked planets
        fetchUnlockedPlanets();
        // Fetch and display existing data
        fetchStudentLevelAndBranch();

        // Fetch and display student stats
        fetchStats();
        fetchStreak();

        return view;
    }
    private void navigateToPlanetMapFragment() {
        PlanetMapFragment planetMapFragment = new PlanetMapFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, planetMapFragment) // Replace `fragment_container` with your container ID
                .addToBackStack(null)
                .commit();
    }
    private void fetchStudentLevelAndBranch() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        apiService.getStudentLevelAndBranch(studentId).enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Long>> call, @NonNull Response<Map<String, Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Long> data = response.body();
                    currentLevelId = data.get("levelId");
                    currentBranchId = data.get("branchId");

                    fetchLevelAndBranchNames();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Long>> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log error
            }
        });
    }

    private void fetchLevelAndBranchNames() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Fetch level name
        apiService.getLevels().enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(@NonNull Call<List<Level>> call, @NonNull Response<List<Level>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String levelName = null;
                    for (Level level : response.body()) {
                        if (level.getId().equals(currentLevelId)) {
                            levelName = level.getName();
                            break;
                        }
                    }

                    fetchBranchName(levelName);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Level>> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log error
            }
        });
    }

    private void fetchBranchName(String levelName) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getBranchesByLevel(currentLevelId).enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(@NonNull Call<List<Branch>> call, @NonNull Response<List<Branch>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String branchName = null;
                    for (Branch branch : response.body()) {
                        if (branch.getId().equals(currentBranchId)) {
                            branchName = branch.getName();
                            break;
                        }
                    }

                    // Update the class info text view
                    classInfoTextView.setText(levelName + " - " + branchName);

                    // Fetch and display subjects
                    fetchSubjectsByBranch(currentBranchId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Branch>> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log error
            }
        });
    }

    private void fetchSubjectsByBranch(Long branchId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getSubjectsByBranch(branchId).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(@NonNull Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Subject> subjects = response.body();

                    // Initialize and set adapter
                    subjectAdapter = new SubjectAdapter(getContext(),subjects, subjectId -> {
                        // Handle subject click here (e.g., navigate to ChaptersFragment)
                        navigateToCoursesFragment(subjectId);
                    });
                    subjectsRecyclerView.setAdapter(subjectAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Subject>> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log error
            }
        });
    }

    private void fetchStats() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        apiService.getStats(studentId).enqueue(new Callback<StudentActivityStatsDto>() {
            @Override
            public void onResponse(Call<StudentActivityStatsDto> call, Response<StudentActivityStatsDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StudentActivityStatsDto stats = response.body();
                    updateStatsUI(stats);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch stats.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentActivityStatsDto> call, Throwable t) {
                Log.e("DashboardFragment", "Error fetching stats: " + t.getMessage());
                Toast.makeText(requireContext(), "Error fetching stats.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchStreak() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        apiService.getStreak(studentId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int streak = response.body();
                    updateStreakUI(streak);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch streak.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.e("DashboardFragment", "Error fetching streak: " + t.getMessage());
                Toast.makeText(requireContext(), "Error fetching streak.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateStreakUI(int streak) {
        streakTextView.setText("Current Streak: " + streak + " days");
        streakTextView.setAlpha(0f);
        streakTextView.animate().alpha(1f).setDuration(500).start();
    }

    private void updateStatsUI(StudentActivityStatsDto stats) {
        animateProgress(quizzesProgress, stats.getCompletedQuizzes());
        animateProgress(matchGamesProgress, stats.getCompletedMatchGames());
        animateProgress(flashcardsProgress, stats.getCompletedFlipcards());
        animateProgress(shortContentsProgress, stats.getCompletedShortContents());

    }

    private void animateProgress(ProgressBar progressBar, int targetProgress) {
        ObjectAnimator.ofInt(progressBar, "progress", targetProgress)
                .setDuration(1000)
                .start();
    }

    private void navigateToCoursesFragment(Long subjectId) {
        if (subjectId == null) {
            Log.e("Error", "Subject ID is null");
            return;
        }

        CoursesFragment coursesFragment = CoursesFragment.newInstance(subjectId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, coursesFragment)
                .addToBackStack(null)
                .commit();
    }
    private void fetchUnlockedPlanets() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        apiService.getAllUnlockedPlanets(studentId).enqueue(new Callback<List<Planet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Planet>> call, @NonNull Response<List<Planet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Planet> unlockedPlanets = response.body();

                    // Set up adapter with the fetched data
                    planetAdapter = new PlanetAdapter(requireContext(), unlockedPlanets);
                    unlockedPlanetsRecyclerView.setAdapter(planetAdapter);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch unlocked planets.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Planet>> call, @NonNull Throwable t) {
                Log.e("DashboardFragment", "Error fetching unlocked planets: " + t.getMessage());
                Toast.makeText(requireContext(), "Error fetching unlocked planets.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
}
