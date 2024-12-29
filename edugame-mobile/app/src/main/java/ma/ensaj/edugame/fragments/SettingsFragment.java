package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.activities.AuthActivity;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Branch;
import ma.ensaj.edugame.models.Level;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    private ImageView profileAvatar;
    private TextView profileName, profileEmail, profilePhone, profileSchool;
    private Spinner levelSpinner, branchSpinner;
    private Button updateClassButton, logoutButton, changeAvatarButton;

    private ApiService apiService;
    private Long currentLevelId;
    private Long currentBranchId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize API Service
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Initialize Views
        profileAvatar = view.findViewById(R.id.profileAvatar);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);

        levelSpinner = view.findViewById(R.id.levelSpinner);
        branchSpinner = view.findViewById(R.id.branchSpinner);
        updateClassButton = view.findViewById(R.id.updateClassButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        changeAvatarButton = view.findViewById(R.id.changeAvatarButton);

        fetchStudentProfile();
        fetchStudentData();

        // Handle Update Class
        updateClassButton.setOnClickListener(v -> updateClassInfo());

        // Handle Logout
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        });

        return view;
    }

    private void fetchStudentProfile() {
        Long userId = getUserId();

        if (userId == -1L) {
            Toast.makeText(getContext(), "Error: User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch student profile details
        apiService.getStudentProfile(userId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("SettingsFragment", "Profile Response: " + response.body().toString());

                    Map<String, String> profileData = response.body();
                    String firstName = profileData.get("firstName");
                    String lastName = profileData.get("lastName");
                    String email = profileData.get("email");
                    String phone = profileData.get("phone");
                    String school = profileData.get("school");

                    // Set dynamic profile data
                    profileName.setText(firstName + " " + lastName);
                    profileEmail.setText(email);
                    //profilePhone.setText(phone);
                    //profileSchool.setText(school);
                } else {
                    try {
                        // Read and log the error response
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("SettingsFragment", "Response Error Body: " + errorBody);
                        Toast.makeText(getContext(), "Failed to load profile details: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("SettingsFragment", "Error reading error body", e);
                        Toast.makeText(getContext(), "Failed to load profile details.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("SettingsFragment", "Request Failed: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Error fetching profile details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void fetchStudentData() {
        Long userId = getUserId();

        if (userId == -1L) {
            Toast.makeText(getContext(), "Error: User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getStudentLevelAndBranch(userId).enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentLevelId = response.body().get("levelId");
                    currentBranchId = response.body().get("branchId");

                    fetchLevels(); // Fetch levels after retrieving student data
                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch student data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchLevels() {
        apiService.getLevels().enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(Call<List<Level>> call, Response<List<Level>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateLevelSpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Level>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch levels", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateLevelSpinner(List<Level> levels) {
        ArrayAdapter<Level> levelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, levels);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);

        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getId().equals(currentLevelId)) {
                levelSpinner.setSelection(i);
                fetchBranches(currentLevelId);
                break;
            }
        }

        levelSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Level selectedLevel = (Level) levelSpinner.getSelectedItem();
                fetchBranches(selectedLevel.getId());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void fetchBranches(Long levelId) {
        apiService.getBranchesByLevel(levelId).enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateBranchSpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch branches", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateBranchSpinner(List<Branch> branches) {
        ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getId().equals(currentBranchId)) {
                branchSpinner.setSelection(i);
                break;
            }
        }
    }

    private void updateClassInfo() {
        Level selectedLevel = (Level) levelSpinner.getSelectedItem();
        Branch selectedBranch = (Branch) branchSpinner.getSelectedItem();

        Long userId = getUserId();

        if (userId == -1L) {
            Toast.makeText(getContext(), "Error: User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLevel != null && selectedBranch != null) {
            Map<String, Long> payload = new HashMap<>();
            payload.put("levelId", selectedLevel.getId());
            payload.put("branchId", selectedBranch.getId());

            apiService.updateLevelAndBranch(userId, payload).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Class updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update class", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error updating class", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select valid level and branch!", Toast.LENGTH_SHORT).show();
        }
    }

    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
}
