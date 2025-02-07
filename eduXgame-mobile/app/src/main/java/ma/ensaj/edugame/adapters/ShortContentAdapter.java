package ma.ensaj.edugame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.models.ShortContent;

public class ShortContentAdapter extends RecyclerView.Adapter<ShortContentAdapter.ShortContentViewHolder> {

    private List<ShortContent> shortContentList;

    public ShortContentAdapter(List<ShortContent> shortContentList) {
        this.shortContentList = shortContentList;
    }

    @NonNull
    @Override
    public ShortContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_content, parent, false);
        return new ShortContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortContentViewHolder holder, int position) {
        ShortContent shortContent = shortContentList.get(position);
        holder.shortContentTitle.setText(shortContent.getContent());
    }

    @Override
    public int getItemCount() {
        return shortContentList.size();
    }

    static class ShortContentViewHolder extends RecyclerView.ViewHolder {
        TextView shortContentTitle;

        public ShortContentViewHolder(@NonNull View itemView) {
            super(itemView);
            shortContentTitle = itemView.findViewById(R.id.shortContentTitle);
        }
    }
}
