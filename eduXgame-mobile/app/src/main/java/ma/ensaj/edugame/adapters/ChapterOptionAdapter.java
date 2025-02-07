package ma.ensaj.edugame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.fragments.FlashcardFragment;
import ma.ensaj.edugame.fragments.MatchGameFragment;
import ma.ensaj.edugame.fragments.ShortContentFragment;
import ma.ensaj.edugame.fragments.QuizFragment;
import ma.ensaj.edugame.models.ChapterOption;

public class ChapterOptionAdapter extends RecyclerView.Adapter<ChapterOptionAdapter.ChapterOptionViewHolder> {

    private List<ChapterOption> chapterOptionList;
    private Long chapterId; // Added chapterId as a member variable
    public ChapterOptionAdapter(List<ChapterOption> chapterOptionList, Long chapterId) {
        this.chapterOptionList = chapterOptionList;
        this.chapterId = chapterId;
    }


    @NonNull
    @Override
    public ChapterOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter_option, parent, false);
        return new ChapterOptionViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ChapterOptionViewHolder holder, int position) {
        ChapterOption option = chapterOptionList.get(position);
        holder.optionTitle.setText(option.getTitle());
        holder.optionIcon.setImageResource(option.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();

            if ("Match Game".equals(option.getTitle())) {
                // Navigate to MatchGameFragment
                MatchGameFragment fragment = MatchGameFragment.newInstance(chapterId);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else if ("Course".equals(option.getTitle())) {
                // Navigate to ShortContentFragment
                ShortContentFragment fragment = ShortContentFragment.newInstance(chapterId);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }else if ("Flash Cards".equals(option.getTitle())) {
                FlashcardFragment fragment = FlashcardFragment.newInstance(chapterId);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
            if ("Quiz".equals(option.getTitle())) {
                activity = (FragmentActivity) holder.itemView.getContext();
                QuizFragment fragment = QuizFragment.newInstance(chapterId);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }



        });
    }

    @Override
    public int getItemCount() {
        return chapterOptionList.size();
    }

    public static class ChapterOptionViewHolder extends RecyclerView.ViewHolder {
        TextView optionTitle;
        ImageView optionIcon;

        public ChapterOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionTitle = itemView.findViewById(R.id.optionTitle);
            optionIcon = itemView.findViewById(R.id.optionIcon);
        }
    }
}
