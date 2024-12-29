package ma.ensaj.edugame.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.fragments.DashboardFragment;
import ma.ensaj.edugame.fragments.LeaderboardTabsFragment;
import ma.ensaj.edugame.fragments.ProfileFragment;
import ma.ensaj.edugame.fragments.SettingsFragment;
import ma.ensaj.edugame.models.StudentAvatar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Fragment activeFragment;
    private StudentAvatar studentAvatar;
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();
    private Long studentId = 3L;
    private CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchHighestCollectedAvatar(studentId);
        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        profileImage = findViewById(R.id.profile_image);



        // Initialize fragments
        fragmentMap.put(R.id.nav_dashboard, new DashboardFragment());
        fragmentMap.put(R.id.nav_profile, new ProfileFragment());
        fragmentMap.put(R.id.nav_settings, new SettingsFragment());
        fragmentMap.put(R.id.nav_leaderboard, new LeaderboardTabsFragment());
        // Set default fragment
        activeFragment = fragmentMap.get(R.id.nav_dashboard);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, activeFragment)
                .commit();

        // Handle bottom navigation selection
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment targetFragment = fragmentMap.get(item.getItemId());
            if (targetFragment != null && targetFragment != activeFragment) {
                switchFragment(targetFragment);
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit);
        transaction.replace(R.id.fragment_container, targetFragment);
        transaction.commit();
        activeFragment = targetFragment;
    }
    private void fetchHighestCollectedAvatar(Long studentId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getMostCollectedAvatar(studentId).enqueue(new Callback<StudentAvatar>() {
            @Override
            public void onResponse(Call<StudentAvatar> call, Response<StudentAvatar> response) {
                if (response.isSuccessful() && response.body() != null) {
                    studentAvatar  = response.body();
                    showAvatar();
                } else {
                    Toast.makeText(getApplicationContext(), "No collected avatar found for this student", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentAvatar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showAvatar(){
        String imageUrl = studentAvatar.getAvatarImageUrl();

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.unknown)
                        .error(R.drawable.unknown))
                .into(profileImage);

        profileImage.setOnClickListener(v -> {

        });
    }

}