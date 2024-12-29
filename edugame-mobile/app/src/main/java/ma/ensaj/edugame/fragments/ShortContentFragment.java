package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.adapters.ShortContentAdapter;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.ShortContent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShortContentFragment extends Fragment {

    private static final String ARG_CHAPTER_ID = "chapterId";
    private Long chapterId;
    private ViewPager2 shortContentViewPager;
    private ShortContentAdapter adapter;
    private View completionButton;

    public static ShortContentFragment newInstance(Long chapterId) {
        ShortContentFragment fragment = new ShortContentFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_short_content, container, false);

        shortContentViewPager = view.findViewById(R.id.shortContentViewPager);
        completionButton = view.findViewById(R.id.completionButton);

        completionButton.setOnClickListener(v -> showCompletionPopup());

        fetchShortContents();

        return view;
    }

    private void fetchShortContents() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getShortContentsByChapter(chapterId).enqueue(new Callback<List<ShortContent>>() {
            @Override
            public void onResponse(Call<List<ShortContent>> call, Response<List<ShortContent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ShortContent> shortContents = response.body();
                    adapter = new ShortContentAdapter(shortContents);
                    shortContentViewPager.setAdapter(adapter);

                    // Sound effect on swiping
                    final MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.swipe_sound);
                    shortContentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);

                            // Play sound on swipe
                            if (position < shortContents.size() - 1) {
                                mediaPlayer.start();
                            }

                            // Show completion button on the last page
                            completionButton.setVisibility(position == shortContents.size() - 1 ? View.VISIBLE : View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ShortContent>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showCompletionPopup() {
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_completion, null);
        playCompletionSound();
        markAllShortContentsAsComplete();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(popupView);

        bottomSheetDialog.show();
    }
    private void markAllShortContentsAsComplete() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Replace with the actual studentId (e.g., fetched from shared preferences or session)
        Long studentId = getUserId();

        apiService.markShortContentAsComplete(studentId, chapterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "All short contents marked as complete!", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed(); // Navigate back
                } else {
                    Toast.makeText(requireContext(), "Failed to complete short contents.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
