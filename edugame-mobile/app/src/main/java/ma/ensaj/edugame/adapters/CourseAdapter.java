package ma.ensaj.edugame.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Chapter;
import ma.ensaj.edugame.models.Course;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private Map<Long, List<Chapter>> expandedChapters = new HashMap<>();
    private Context context;

    public CourseAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getDescription());
        Long userId = getUserId();

        if (userId != -1) {
            // Fetch progress from the backend
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.getCourseProgress(userId, course.getId()).enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        double progress = response.body();
                        holder.courseProgressBar.setProgress((int) progress);
                        holder.courseProgressText.setText(progress + "%");
                    } else {
                        holder.courseProgressBar.setProgress(0);
                        holder.courseProgressText.setText("N/A");
                    }
                }

                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    holder.courseProgressBar.setProgress(0);
                    holder.courseProgressText.setText("Error");
                }
            });
        } else {
            holder.courseProgressBar.setProgress(0);
            holder.courseProgressText.setText("No ID");
        }

        // Handle expand/collapse logic
        holder.itemView.setOnClickListener(v -> {
            if (expandedChapters.containsKey(course.getId())) {
                expandedChapters.remove(course.getId());
                holder.chaptersRecyclerView.setVisibility(View.GONE);
            } else {
                fetchChapters(course.getId(), chapters -> {
                    expandedChapters.put(course.getId(), chapters);



                    if (userId != -1) {
                        ChapterAdapter chapterAdapter = new ChapterAdapter(chapters, context);
                        holder.chaptersRecyclerView.setAdapter(chapterAdapter);
                        holder.chaptersRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("CourseAdapter", "User ID not found in shared preferences");
                    }
                });
            }
        });

        // Ensure RecyclerView visibility is set correctly
        if (expandedChapters.containsKey(course.getId())) {
            holder.chaptersRecyclerView.setVisibility(View.VISIBLE);

         

            if (userId != -1) {
                ChapterAdapter chapterAdapter = new ChapterAdapter(expandedChapters.get(course.getId()), context);
                holder.chaptersRecyclerView.setAdapter(chapterAdapter);
            } else {
                Log.e("CourseAdapter", "User ID not found in shared preferences");
            }
        } else {
            holder.chaptersRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void fetchChapters(Long courseId, OnChaptersLoadedListener listener) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getChaptersByCourse(courseId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onChaptersLoaded(response.body());
                } else {
                    Log.e("CourseAdapter", "Failed to fetch chapters: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Log.e("CourseAdapter", "Failed to fetch chapters: " + t.getMessage());
            }
        });
    }

    private Long getUserId() {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

    public interface OnChaptersLoadedListener {
        void onChaptersLoaded(List<Chapter> chapters);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDescription, courseProgressText;
        RecyclerView chaptersRecyclerView;
        ProgressBar courseProgressBar;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            courseDescription = itemView.findViewById(R.id.courseDescription);
            courseProgressBar=itemView.findViewById(R.id.courseProgressBar);
            courseProgressText=itemView.findViewById(R.id.courseProgressText);
            chaptersRecyclerView = itemView.findViewById(R.id.chaptersRecyclerView);
            chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
