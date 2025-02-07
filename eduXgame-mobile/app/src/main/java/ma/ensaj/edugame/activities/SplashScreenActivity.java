package ma.ensaj.edugame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ma.ensaj.edugame.R;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.logo);
        TextView tagline = findViewById(R.id.tagline);

        // Load Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply Animations
        logo.startAnimation(fadeIn);
        tagline.startAnimation(slideUp);

        // Transition to Main Activity after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
            startActivity(intent);
            finish(); // Close the Splash Screen Activity
        }, 3000);
    }
}
