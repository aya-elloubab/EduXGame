package ma.ensaj.edugame.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.fragments.ChapterOptionsFragment;
import ma.ensaj.edugame.models.Chapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapterList;
    private Context context;

    public ChapterAdapter(List<Chapter> chapterList, Context context) {
        this.chapterList = chapterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.chapterName.setText(chapter.getChapterName());
        holder.chapterDescription.setText(chapter.getDescription());

        // Retrieve user ID
        Long userId = getUserId();

        if (userId != -1) {
            // Fetch progress from the backend
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.getChapterProgress(userId, chapter.getId()).enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        double progress = response.body();
                        holder.progressBar.setProgress((int) progress);
                        holder.progressText.setText(progress + "%");
                    } else {
                        holder.progressBar.setProgress(0);
                        holder.progressText.setText("N/A");
                    }
                }

                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    holder.progressBar.setProgress(0);
                    holder.progressText.setText("Error");
                }
            });
        } else {
            holder.progressBar.setProgress(0);
            holder.progressText.setText("No ID");
        }

        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) context;
            ChapterOptionsFragment fragment = ChapterOptionsFragment.newInstance(chapter.getId(), chapter.getChapterName());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });



    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    private Long getUserId() {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterName, chapterDescription, progressText;
        ProgressBar progressBar;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterName = itemView.findViewById(R.id.chapterName);
            chapterDescription = itemView.findViewById(R.id.chapterDescription);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.progressText);
        }
    }
}
