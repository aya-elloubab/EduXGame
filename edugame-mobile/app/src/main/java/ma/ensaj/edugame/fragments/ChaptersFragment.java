package ma.ensaj.edugame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.ChapterAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Chapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaptersFragment extends Fragment {

    private static final String ARG_SUBJECT_ID = "subjectId";
    private Long subjectId;
    private RecyclerView chaptersRecyclerView;
    private ChapterAdapter chapterAdapter;

    public static ChaptersFragment newInstance(Long subjectId) {
        ChaptersFragment fragment = new ChaptersFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SUBJECT_ID, subjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapters, container, false);
        chaptersRecyclerView = view.findViewById(R.id.chaptersRecyclerView);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            subjectId = getArguments().getLong(ARG_SUBJECT_ID);
            fetchChapters();
        }

        return view;
    }

    private void fetchChapters() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getChaptersBySubject(subjectId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chapterAdapter = new ChapterAdapter(response.body(), getContext());
                    chaptersRecyclerView.setAdapter(chapterAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                t.printStackTrace(); // Log error
            }
        });
    }
}
