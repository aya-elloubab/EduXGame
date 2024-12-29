package ma.ensaj.edugame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.models.Match;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matches;
    private OnMatchClickListener listener;
    private boolean showElement; // Determines which attribute to display
    private int selectedPosition = -1; // To track the selected item

    public MatchAdapter(List<Match> matches, OnMatchClickListener listener, boolean showElement) {
        this.matches = matches;
        this.listener = listener;
        this.showElement = showElement;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.matchTextView.setText(showElement ? match.getElement() : match.getMatchText());

        // Highlight selected item
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.selected_answer));
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.unselected_answer));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            listener.onMatchClick(match);
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void removeMatch(Match match) {
        matches.remove(match);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return matches.isEmpty();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView matchTextView;
        CardView cardView;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            matchTextView = itemView.findViewById(R.id.matchTextView);
            cardView = itemView.findViewById(R.id.matchCard);
        }
    }

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }
}
