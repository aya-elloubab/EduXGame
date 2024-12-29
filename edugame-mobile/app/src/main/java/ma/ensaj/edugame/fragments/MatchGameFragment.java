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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.MatchAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Match;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchGameFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapterId";

    private Long chapterId;
    private RecyclerView elementsRecyclerView, matchTextsRecyclerView;
    private MatchAdapter elementsAdapter, matchTextsAdapter;
    private TextView scoreTextView;
    private int score = 0;
    private Match selectedElement = null;
    private Match selectedMatchText = null;
    private Button completeButton;

    private MediaPlayer correctSound, incorrectSound;

    public static MatchGameFragment newInstance(Long chapterId) {
        MatchGameFragment fragment = new MatchGameFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getLong(ARG_CHAPTER_ID);
        }

        // Initialize sound effects
        correctSound = MediaPlayer.create(requireContext(), R.raw.correct_sound);
        incorrectSound = MediaPlayer.create(requireContext(), R.raw.incorrect_sound);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (correctSound != null) correctSound.release();
        if (incorrectSound != null) incorrectSound.release();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_game, container, false);

        elementsRecyclerView = view.findViewById(R.id.elementsRecyclerView);
        matchTextsRecyclerView = view.findViewById(R.id.matchTextsRecyclerView);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        completeButton = view.findViewById(R.id.completeButtonMatch);
        completeButton.setVisibility(View.GONE); // Initially hide the button

        completeButton.setOnClickListener(v -> showCompletionPopup());

        elementsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matchTextsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchMatches();
        return view;
    }

    private void fetchMatches() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getMatchesByChapter(chapterId).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Match> matches = response.body();
                    setupRecyclerViews(matches);
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                Log.e("MatchGameFragment", "Error fetching matches: " + t.getMessage());
            }
        });
    }

    private void setupRecyclerViews(List<Match> matches) {
        List<Match> elements = new ArrayList<>(matches);
        List<Match> matchTexts = new ArrayList<>(matches);

        // Shuffle matchTexts to randomize order
        Collections.shuffle(matchTexts);

        elementsAdapter = new MatchAdapter(elements, match -> {
            selectedElement = match;
            checkMatch();
        }, true);

        matchTextsAdapter = new MatchAdapter(matchTexts, match -> {
            selectedMatchText = match;
            checkMatch();
        }, false);

        elementsRecyclerView.setAdapter(elementsAdapter);
        matchTextsRecyclerView.setAdapter(matchTextsAdapter);
    }

    private void checkMatch() {
        if (selectedElement != null && selectedMatchText != null) {
            if (selectedElement.getId().equals(selectedMatchText.getId())) {
                score += 10;
                correctSound.start(); // Play correct sound
                // Clear selection for incorrect match
                elementsAdapter.clearSelection();
                matchTextsAdapter.clearSelection();
                elementsAdapter.removeMatch(selectedElement);
                matchTextsAdapter.removeMatch(selectedMatchText);
            } else {
                score = Math.max(0, score - 5);
                incorrectSound.start(); // Play incorrect sound

                // Clear selection for incorrect match
                elementsAdapter.clearSelection();
                matchTextsAdapter.clearSelection();
            }

            updateScore();
            selectedElement = null;
            selectedMatchText = null;

            if (elementsAdapter.isEmpty()) {
                completeButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void showCompletionPopup() {
        playCompletionSound();

        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_completion, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(popupView);

        LottieAnimationView completionAnimation = popupView.findViewById(R.id.completionAnimation);
        completionAnimation.playAnimation();
        markQuizAsComplete();
        bottomSheetDialog.show();
    }

    private void markQuizAsComplete() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long userId = getUserId();
        apiService.submitMatchScore(userId, chapterId, score).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Quiz marked as complete!", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Failed to mark quiz as complete.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
    private void playCompletionSound() {
        MediaPlayer completionSound = MediaPlayer.create(requireContext(), R.raw.completed);
        completionSound.setOnCompletionListener(MediaPlayer::release); // Release resources when done
        completionSound.start();
    }

}
