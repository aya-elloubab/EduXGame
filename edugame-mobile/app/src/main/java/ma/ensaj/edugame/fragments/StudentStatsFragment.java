package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.custom.CircularProgressView;
import ma.ensaj.edugame.models.StudentStats;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentStatsFragment extends Fragment {

    private BarChart barChart;
    private CircularProgressView circularProgress;
    private ApiService apiService;
    private Long studentId;
    private TextView streak_count, xp_count;
    private int currentPoints;
    private static final String TAG = "StudentStatsFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_stats, container, false);
        Log.d(TAG, "onCreateView called");
        barChart = view.findViewById(R.id.bar_chart);
        circularProgress = view.findViewById(R.id.circular_progress);
        streak_count=view.findViewById(R.id.streak_count);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        studentId = getUserId();
        xp_count=view.findViewById(R.id.xp_count);
        fetchStudentStats();
        fetchStudyHours();
        fetchStreak();
        fetchAndDisplayTotalPoints();

        return view;
    }

    private void fetchStudentStats() {
        apiService.getStudentStats(studentId).enqueue(new Callback<StudentStats>() {
            @Override
            public void onResponse(Call<StudentStats> call, Response<StudentStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StudentStats stats = response.body();
                    populateBarChart(stats);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch stats.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentStats> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        streak_count.setText(String.valueOf(streak));
        streak_count.setAlpha(0f);
        streak_count.animate().alpha(1f).setDuration(500).start();
    }
    private void fetchAndDisplayTotalPoints() {
        apiService.getTotalScore(studentId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentPoints = response.body();
                    // You can update the UI here if you need to show total points in a TextView
                    xp_count.setText(String.valueOf(currentPoints));
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
    private void fetchStudyHours() {
        apiService.getTotalStudyHours(studentId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalHours = response.body();
                    updateCircularProgress(totalHours);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch study hours.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCircularProgress(int totalHours) {
        int target = calculateDynamicTarget(totalHours);

        circularProgress.setMax(target);
        circularProgress.setProgressWithAnimation(totalHours);
        circularProgress.setTextColor(Color.BLUE); // Optional customization
        circularProgress.setTextSize(40f); // Optional customization
    }

    private int calculateDynamicTarget(int totalHours) {
        return ((totalHours / 10) + 1) * 10; // Next nearest multiple of 10
    }

    private void populateBarChart(StudentStats stats) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, stats.getCompletedQuizzes()));
        entries.add(new BarEntry(1, stats.getCompletedMatchGames()));
        entries.add(new BarEntry(2, stats.getCompletedFlipcards()));
        entries.add(new BarEntry(3, stats.getCompletedShortContents()));

        BarDataSet dataSet = new BarDataSet(entries, "Student Stats");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        dataSet.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);
        barChart.setData(barData);

        String[] labels = {"Quizzes", "Match Games", "Flipcards", "Short Contents"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setTextColor(Color.BLACK);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true);
        barChart.animateY(1500);
        barChart.invalidate();
    }

    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

}
