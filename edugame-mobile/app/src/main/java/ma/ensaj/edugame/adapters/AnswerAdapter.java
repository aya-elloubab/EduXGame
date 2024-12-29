package ma.ensaj.edugame.adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.edugame.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<String> answers = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void setAnswers(List<String> answers) {
        this.answers = answers;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<String> getSelectedAnswers() {
        List<String> selectedAnswers = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                selectedAnswers.add(answers.get(selectedItems.keyAt(i)));
            }
        }
        return selectedAnswers;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        String answer = answers.get(position);
        holder.responseText.setText(answer);

        boolean isSelected = selectedItems.get(position, false);
        holder.cardView.setCardBackgroundColor(isSelected
                ? holder.itemView.getContext().getColor(R.color.selected_answer)
                : holder.itemView.getContext().getColor(R.color.unselected_answer));

        holder.cardView.setElevation(isSelected ? 8f : 4f);

        holder.itemView.setOnClickListener(v -> toggleSelection(position));
    }

    private void toggleSelection(int position) {
        selectedItems.put(position, !selectedItems.get(position, false));
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView responseText;
        CardView cardView;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            responseText = itemView.findViewById(R.id.quiz_responseText);
            cardView = itemView.findViewById(R.id.quiz_responseCard);
        }
    }
}
