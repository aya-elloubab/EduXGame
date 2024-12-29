package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.AnswerAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Quiz;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    private TextView questionTextView, scoreTextView;
    private RecyclerView answersRecyclerView;
    private MaterialButton nextButton;

    private List<Quiz> quizList = new ArrayList<>();
    private int currentQuizIndex = 0;
    private int score = 0;
    private Long chapterId;
    private Long studentId;
    private ApiService apiService;
    private AnswerAdapter answerAdapter;
    private MediaPlayer correctSound, incorrectSound;

    public static QuizFragment newInstance(Long chapterId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putLong("chapterId", chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getLong("chapterId");
        }
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        studentId = getUserId();
        // Initialize sound effects
        correctSound = MediaPlayer.create(requireContext(), R.raw.correct_sound);
        incorrectSound = MediaPlayer.create(requireContext(), R.raw.incorrect_sound);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        initializeViews(view);
        fetchQuizData();
        return view;
    }

    private void initializeViews(View view) {
        questionTextView = view.findViewById(R.id.quiz_questionTextView);
        scoreTextView = view.findViewById(R.id.quiz_scoreTextView);
        answersRecyclerView = view.findViewById(R.id.quiz_answersRecyclerView);
        nextButton = view.findViewById(R.id.quiz_nextButton);

        answersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        answerAdapter = new AnswerAdapter();
        answersRecyclerView.setAdapter(answerAdapter);

        nextButton.setOnClickListener(v -> handleNextButtonClick());
    }


    private void fetchQuizData() {
        apiService.getQuizzesByChapter(chapterId).enqueue(new Callback<List<Quiz>>() {
            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizList.clear();
                    quizList.addAll(response.body());
                    displayQuiz();
                }
            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                showExplanationPopup(false, "Failed to load quizzes");
            }
        });
    }

    private void displayQuiz() {
        if (quizList.isEmpty() || currentQuizIndex >= quizList.size()) {
            submitScore();
            return;
        }

        Quiz currentQuiz = quizList.get(currentQuizIndex);
        questionTextView.setText(currentQuiz.getQuestion());
        answerAdapter.setAnswers(currentQuiz.getAnswers());
    }

    private void handleNextButtonClick() {
        if (currentQuizIndex >= quizList.size()) return;

        Quiz currentQuiz = quizList.get(currentQuizIndex);
        List<String> selectedAnswers = answerAdapter.getSelectedAnswers();

        boolean isCorrect = new HashSet<>(selectedAnswers).equals(new HashSet<>(currentQuiz.getCorrectAnswers()));
        updateScore(isCorrect);
        showExplanationPopup(isCorrect, currentQuiz.getExplanation());

        currentQuizIndex++;
        if (currentQuizIndex < quizList.size()) {
            displayQuiz();
        } else {
            submitScore();
        }
    }

    private void updateScore(boolean isCorrect) {
        score += isCorrect ? 10 : -5;
        scoreTextView.setText(String.format("Score: %d", score));
    }

    private void showExplanationPopup(boolean isCorrect, String explanation) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_explanation, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);

        LottieAnimationView animationView = bottomSheetView.findViewById(R.id.statusAnimation);
        TextView statusText = bottomSheetView.findViewById(R.id.statusText);
        TextView explanationText = bottomSheetView.findViewById(R.id.explanationText);

        if (isCorrect) {
            correctSound.start(); // Play correct sound
            animationView.setAnimation(R.raw.correct_animation);
            statusText.setText("Correct!");
            statusText.setTextColor(requireContext().getColor(R.color.correct));
        } else {
            incorrectSound.start(); // Play incorrect sound
            animationView.setAnimation(R.raw.incorrect_animation);
            statusText.setText("Incorrect!");
            statusText.setTextColor(requireContext().getColor(R.color.incorrect));
        }

        explanationText.setText(explanation);
        animationView.playAnimation();

        bottomSheetDialog.show();
    }

    private void submitScore() {
        apiService.submitQuizResults(studentId, chapterId, score).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showExplanationPopup(true, "Quiz completed! Final Score: " + score);
                markQuizAsComplete();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showExplanationPopup(false, "Failed to submit score");
                requireActivity().onBackPressed(); // Navigate back

            }
        });
    }
    private void markQuizAsComplete() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long userId = getUserId(); // Method to retrieve user ID
        apiService.markQuizComplete(userId, chapterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    requireActivity().onBackPressed(); // Navigate back on success
                } else {
                    Toast.makeText(getContext(), "Failed to complete quiz.", Toast.LENGTH_SHORT).show();
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
}
