package ma.ensaj.edugame.adapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.models.Flipcard;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flipcard> flashcards;
    private Context context;

    public FlashcardAdapter(List<Flipcard> flashcards) {
        this.flashcards = flashcards;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flipcard flashcard = flashcards.get(position);

        // Set the content for the front and back
        holder.flashcardFront.setText(flashcard.getFront());
        holder.flashcardBack.setText(flashcard.getBack());

        // Ensure initial visibility
        if (holder.isFrontVisible) {
            holder.flashcardFront.setVisibility(View.VISIBLE);
            holder.flashcardBack.setVisibility(View.GONE);
        } else {
            holder.flashcardFront.setVisibility(View.GONE);
            holder.flashcardBack.setVisibility(View.VISIBLE);
        }

        // Add click listener to flip the card
        holder.itemView.setOnClickListener(v -> flipCard(holder));
    }


    private void flipCard(FlashcardViewHolder holder) {
        AnimatorSet frontAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_in);
        AnimatorSet backAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_out);
        flipSound();

        if (holder.isFrontVisible) {
            // Flip to back
            frontAnimation.setTarget(holder.flashcardFront);
            backAnimation.setTarget(holder.flashcardBack);

            frontAnimation.start();
            backAnimation.start();

            // Hide front and show back after the animation
            frontAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    holder.flashcardFront.setVisibility(View.GONE);
                    holder.flashcardBack.setVisibility(View.VISIBLE);
                }

                @Override public void onAnimationStart(Animator animation) {}
                @Override public void onAnimationCancel(Animator animation) {}
                @Override public void onAnimationRepeat(Animator animation) {}
            });
        } else {
            // Flip to front
            frontAnimation.setTarget(holder.flashcardBack);
            backAnimation.setTarget(holder.flashcardFront);

            frontAnimation.start();
            backAnimation.start();

            // Hide back and show front after the animation
            frontAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    holder.flashcardBack.setVisibility(View.GONE);
                    holder.flashcardFront.setVisibility(View.VISIBLE);
                }

                @Override public void onAnimationStart(Animator animation) {}
                @Override public void onAnimationCancel(Animator animation) {}
                @Override public void onAnimationRepeat(Animator animation) {}
            });
        }

        holder.isFrontVisible = !holder.isFrontVisible;
    }


    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView flashcardFront, flashcardBack;
        boolean isFrontVisible = true;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            flashcardFront = itemView.findViewById(R.id.flashcardFront);
            flashcardBack = itemView.findViewById(R.id.flashcardBack);
        }
    }
    private void flipSound() {
        MediaPlayer completionSound = MediaPlayer.create(context, R.raw.flipcard_sound);
        completionSound.setOnCompletionListener(MediaPlayer::release); // Release resources when done
        completionSound.start();
    }
}
