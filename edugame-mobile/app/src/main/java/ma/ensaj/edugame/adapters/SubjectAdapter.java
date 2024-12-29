package ma.ensaj.edugame.adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Subject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;
    private OnSubjectClickListener listener;
    private Context context;

    public interface OnSubjectClickListener {
        void onSubjectClick(Long subjectId);
    }

    public SubjectAdapter(Context context,List<Subject> subjectList, OnSubjectClickListener listener) {
        this.subjectList = subjectList;
        this.listener = listener;
        this.context = context;
    }

    public void updateSubjects(List<Subject> newSubjects) {
        this.subjectList = newSubjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("sub","set 1" );

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }
    private Long getUserId() {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Log.d("sub","set 2" );

        Subject subject = subjectList.get(position);
        holder.nameTextView.setText(subject.getName());
        Long userId = getUserId();
        Log.d("id", ""+userId);

        if (userId != -1) {
            // Fetch progress from the backend
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.getSubjectProgress(userId, subject.getId()).enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        double progress = response.body();
                        double roundedProgress = Math.round(progress * 100.0) / 100.0;

                        // Convert the rounded progress to an integer for ProgressBar (0 to 100)
                        int progressInt = (int) (roundedProgress);

                        // Set the progress in the ProgressBar and the text
                        holder.subjectProgressBar.setProgress(progressInt);
                        holder.subjectProgressText.setText(String.format("%.2f%%", roundedProgress));
                    } else {
                        holder.subjectProgressBar.setProgress(0);
                        holder.subjectProgressText.setText("N/A");
                    }
                }


                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    holder.subjectProgressBar.setProgress(0);
                    holder.subjectProgressText.setText("Error");
                }
            });
        } else {
            holder.subjectProgressBar.setProgress(0);
            holder.subjectProgressText.setText("No ID");
        }
        // Existing click listener for navigating to a subject
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSubjectClick(subject.getId());
            }
        });

        // Retrieve CardView and apply animation logic
        CardView cardView = holder.itemView.findViewById(R.id.subjectCardView);
        Log.d("sub","set animation 1" );

        cardView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cardView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    cardView.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    break;
            }
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,subjectProgressText;
        ProgressBar subjectProgressBar;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.subjectTitle);
            subjectProgressBar=itemView.findViewById(R.id.subjectProgressBar);
            subjectProgressText=itemView.findViewById(R.id.subjectProgressText);
        }
    }

}
