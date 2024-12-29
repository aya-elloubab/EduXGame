package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.FlashcardAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Flipcard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapterId";

    private Long chapterId;
    private RecyclerView flashcardRecyclerView;
    private Button completeButton;

    public static FlashcardFragment newInstance(Long chapterId) {
        FlashcardFragment fragment = new FlashcardFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_CHAPTER_ID)) {
            chapterId = getArguments().getLong(ARG_CHAPTER_ID);
        } else {
            throw new IllegalStateException("Chapter ID is required but missing.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcard, container, false);

        flashcardRecyclerView = view.findViewById(R.id.flashcardRecyclerView);
        flashcardRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        completeButton = view.findViewById(R.id.completeButton);

        completeButton.setOnClickListener(v -> markAllFlashcardsAsComplete());

        fetchFlashcards();

        return view;
    }

    private void fetchFlashcards() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getFlashcardsByChapter(chapterId).enqueue(new Callback<List<Flipcard>>() {
            @Override
            public void onResponse(Call<List<Flipcard>> call, Response<List<Flipcard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Flipcard> flashcards = response.body();
                    FlashcardAdapter adapter = new FlashcardAdapter(flashcards);
                    flashcardRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Flipcard>> call, Throwable t) {
                Log.e("FlashcardFragment", "Error fetching flashcards: " + t.getMessage());
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAllFlashcardsAsComplete() {
        playCompletionSound();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Replace this with actual logic to get the logged-in student ID
        Long studentId = getStudentId();

        apiService.markFlipcardsAsComplete(studentId, chapterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "All flashcards marked as complete!", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed(); // Navigate back
                } else {
                    Toast.makeText(requireContext(), "Failed to complete flashcards.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FlashcardFragment", "Error marking flashcards as complete: " + t.getMessage());
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Replace this with actual logic to retrieve the student ID
    private Long getStudentId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
    private void playCompletionSound() {
        MediaPlayer completionSound = MediaPlayer.create(requireContext(), R.raw.completed);
        completionSound.setOnCompletionListener(MediaPlayer::release); // Release resources when done
        completionSound.start();
    }
}
