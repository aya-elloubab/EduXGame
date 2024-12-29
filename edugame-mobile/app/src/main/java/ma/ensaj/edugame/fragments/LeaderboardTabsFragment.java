package ma.ensaj.edugame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import ma.ensaj.edugame.R;

public class LeaderboardTabsFragment extends Fragment {

    private static final String TAG_STATS = "StudentStatsFragment";
    private static final String TAG_LEADERBOARD = "LeaderboardFragment";

    private Fragment activeFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard_tabs, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        // Load the default tab (Stats)
        if (savedInstanceState == null) {
            loadFragment(new StudentStatsFragment(), TAG_STATS);
        } else {
            // Restore the active fragment
            activeFragment = getChildFragmentManager().findFragmentById(R.id.fragment_container_leaderboard_tag);
        }

        // Add tab options
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));       // Tab 1
        tabLayout.addTab(tabLayout.newTab().setText("Leaderboard")); // Tab 2

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadFragment(new StudentStatsFragment(), TAG_STATS);
                } else if (tab.getPosition() == 1) {
                    loadFragment(new LeaderboardFragment(), TAG_LEADERBOARD);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void loadFragment(Fragment fragment, String tag) {
        if (activeFragment != null && activeFragment.getTag() != null && activeFragment.getTag().equals(tag)) {
            // Avoid reloading the same fragment
            return;
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_leaderboard_tag, fragment, tag);
        transaction.commit();

        activeFragment = fragment;
    }
}
