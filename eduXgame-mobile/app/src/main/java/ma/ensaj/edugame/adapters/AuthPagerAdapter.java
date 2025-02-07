package ma.ensaj.edugame.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ma.ensaj.edugame.fragments.LoginFragment;
import ma.ensaj.edugame.fragments.SignUpFragment;

public class AuthPagerAdapter extends FragmentStateAdapter {

    public AuthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return new LoginFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs: Login and Sign Up
    }
}
