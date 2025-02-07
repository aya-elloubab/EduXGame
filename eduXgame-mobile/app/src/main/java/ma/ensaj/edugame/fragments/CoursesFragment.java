package ma.ensaj.edugame.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Course;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ma.ensaj.edugame.adapters.CourseAdapter;

public class CoursesFragment extends Fragment {

    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;

    private Long subjectId;

    public static CoursesFragment newInstance(Long subjectId) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putLong("subjectId", subjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null && getArguments().containsKey("subjectId")) {
            subjectId = getArguments().getLong("subjectId");
            fetchCourses(subjectId);
        } else {
            Log.e("Error", "No subjectId passed");
        }

        return view;
    }

    private void fetchCourses(Long subjectId) {
        if (subjectId == null) {
            Log.e("Error", "Subject ID is null");
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getCoursesBySubject(subjectId).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courses = response.body();
                    courseAdapter = new CourseAdapter(courses, getContext());
                    coursesRecyclerView.setAdapter(courseAdapter);
                } else {
                    Log.e("Error", "Failed to fetch courses");
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

}
