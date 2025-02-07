package ma.ensaj.edugame.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Paint shadowPaint;

    private RectF rectF;

    private int progress = 0;
    private int max = 100;

    private float strokeWidth = 20f;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Background circle paint
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(0xFFE0E0E0); // Light gray
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        // Shadow paint for depth
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(0x55000000); // Semi-transparent black
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(strokeWidth + 5);

        // Progress arc paint
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND); // Rounded edges

        // Center text paint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF000000); // Black
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = (int) strokeWidth / 2;
        rectF.set(padding, padding, w - padding, h - padding);

        // Gradient for progress arc
        LinearGradient gradient = new LinearGradient(0, 0, w, h,
                new int[]{0xFF76FF03, 0xFFFFC107, 0xFFF44336}, // Green to Yellow to Red
                null, Shader.TileMode.CLAMP);
        progressPaint.setShader(gradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw shadow for depth
        canvas.drawArc(rectF, 0, 360, false, shadowPaint);

        // Draw background circle
        canvas.drawArc(rectF, 0, 360, false, backgroundPaint);

        // Draw progress arc
        float sweepAngle = (360f * progress) / max;
        canvas.drawArc(rectF, -90, sweepAngle, false, progressPaint);

        // Draw center text
        String progressText = progress + "/" + max;
        canvas.drawText(progressText, getWidth() / 2f, getHeight() / 2f + 20, textPaint);
    }

    /**
     * Sets the progress with animation.
     *
     * @param targetProgress The new progress value.
     */
    public void setProgressWithAnimation(int targetProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetProgress);
        animator.setDuration(1000); // Animation duration in milliseconds
        animator.addUpdateListener(animation -> {
            this.progress = (int) animation.getAnimatedValue();
            invalidate(); // Redraw the view
        });
        animator.start();
    }

    /**
     * Sets the progress value directly.
     *
     * @param progress The progress value.
     */
    public void setProgress(int progress) {
        this.progress = Math.min(progress, max);
        invalidate(); // Redraw the view
    }

    /**
     * Sets the maximum value for the progress.
     *
     * @param max The maximum value.
     */
    public void setMax(int max) {
        this.max = max;
        invalidate(); // Redraw the view
    }

    /**
     * Sets the stroke width for the progress arc and background.
     *
     * @param strokeWidth The stroke width in pixels.
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        backgroundPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeWidth(strokeWidth);
        shadowPaint.setStrokeWidth(strokeWidth + 5);
        invalidate(); // Redraw the view
    }

    /**
     * Sets the color of the center text.
     *
     * @param color The text color.
     */
    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate(); // Redraw the view
    }

    /**
     * Sets the size of the center text.
     *
     * @param textSize The text size in pixels.
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        invalidate(); // Redraw the view
    }
}
