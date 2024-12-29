package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.ChapterOptionAdapter;
import ma.ensaj.edugame.models.ChapterOption;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterOptionsFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapterId";
    private static final String ARG_CHAPTER_NAME = "chapterName";

    private Long chapterId;
    private String chapterName;
    private TextView chapterTitle,progressText;
    private ProgressBar progressBar;
    private RecyclerView optionsRecyclerView;
    private ChapterOptionAdapter optionsAdapter;

    public static ChapterOptionsFragment newInstance(Long chapterId, String chapterName) {
        ChapterOptionsFragment fragment = new ChapterOptionsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHAPTER_ID, chapterId);
        args.putString(ARG_CHAPTER_NAME, chapterName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getLong(ARG_CHAPTER_ID);
            chapterName = getArguments().getString(ARG_CHAPTER_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_options, container, false);

        chapterTitle = view.findViewById(R.id.chapterTitle);
        progressText = view.findViewById(R.id.progressText);
        progressBar = view.findViewById(R.id.chapProgressBar);
        optionsRecyclerView = view.findViewById(R.id.optionsRecyclerView);

        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chapterTitle.setText(chapterName); // Set the chapter name
        fetchProgress(); // Fetch the progress
        setupOptions(); // Initialize the options

        return view;
    }


    private void fetchProgress() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long userId = getUserId();

        if (userId != -1) {
            apiService.getChapterProgress(userId, chapterId).enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        double progress = response.body();
                        progressBar.setProgress((int) progress);
                        progressText.setText(progress+"%");
                    } else {
                        progressBar.setProgress(0);
                    }
                }

                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    progressBar.setProgress(0); // Fallback to 0 on failure
                }
            });
        } else {
            progressBar.setProgress(0); // Fallback to 0 if user ID is not available
        }
    }




    private void setupOptions() {
        List<ChapterOption> options = new ArrayList<>();
        options.add(new ChapterOption("Course", R.drawable.quizz));
        options.add(new ChapterOption("Quiz", R.drawable.quizz));
        options.add(new ChapterOption("Match Game", R.drawable.match));
        options.add(new ChapterOption("Flash Cards", R.drawable.flash_card));

        // Pass chapterId to the adapter
        optionsAdapter = new ChapterOptionAdapter(options, chapterId);
        optionsRecyclerView.setAdapter(optionsAdapter);
    }


    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
}
