package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.activities.MainActivity;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.databinding.FragmentLoginBinding; // Correct binding class
import ma.ensaj.edugame.models.LoginRequest;
import ma.ensaj.edugame.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding; // Correct binding class

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false); // Correct binding initialization

        // Handle login button click
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.usernameInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                loginStudent(email, password);
            }
        });

        // Handle sign-up link click
        binding.signupPrompt.setOnClickListener(v -> {
            if (getActivity() instanceof TabSwitchListener) {
                ((TabSwitchListener) getActivity()).switchToSignUp();
            }
        });

        return binding.getRoot();
    }

    private void loginStudent(String email, String password) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password, "STUDENT");

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String jwt = response.body().getJwt();
                    Long userId = response.body().getUserId();
                    updateStreakApi(userId);

                    saveUserCredentials(requireContext(), jwt, userId);
                    Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateToStudentDashboard(jwt);
                } else {
                    Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserCredentials(Context context, String token, Long userId) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt_token", token);
        editor.putLong("user_id", userId);
        Log.d("tag",token.toString());
        editor.apply();
    }

    private void navigateToStudentDashboard(String jwt) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("jwt", jwt);
        startActivity(intent);
        requireActivity().finish();
    }

    public interface TabSwitchListener {
        void switchToSignUp();
    }
    private void updateStreakApi(long userId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.updateStreak(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Streak Update", "Streak updated successfully for user: " + userId);
                } else {
                    Log.e("Streak Update", "Failed to update streak. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Streak Update", "Failed to update streak: " + t.getMessage());
            }
        });
    }

}
