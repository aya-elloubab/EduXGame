package ma.ensaj.edugame.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import ma.ensaj.edugame.databinding.FragmentSignUpBinding;
import ma.ensaj.edugame.models.Branch;
import ma.ensaj.edugame.models.Level;
import ma.ensaj.edugame.models.RegisterRequest;
import ma.ensaj.edugame.viewModel.SignUpViewModel;
import ma.ensaj.edugame.viewModel.SignUpViewModelFactory;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        // Create the ViewModel with the custom factory
        SignUpViewModelFactory factory = new SignUpViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(SignUpViewModel.class);

        observeViewModel();
        viewModel.fetchLevels();

        // Handle Level selection
        binding.spinnerLevels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Level selectedLevel = (Level) binding.spinnerLevels.getSelectedItem();
                if (selectedLevel != null) {
                    viewModel.fetchBranches(selectedLevel.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No-op
            }
        });

        // Handle Register button
        binding.registerButton.setOnClickListener(v -> registerUser());

        return binding.getRoot();
    }

    private void observeViewModel() {
        viewModel.getLevels().observe(getViewLifecycleOwner(), this::populateLevelSpinner);
        viewModel.getBranches().observe(getViewLifecycleOwner(), this::populateBranchSpinner);
        viewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void populateLevelSpinner(List<Level> levels) {
        ArrayAdapter<Level> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLevels.setAdapter(adapter);
    }

    private void populateBranchSpinner(List<Branch> branches) {
        ArrayAdapter<Branch> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, branches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBranches.setAdapter(adapter);
    }

    private void registerUser() {
        String firstName = binding.firstNameInput.getText().toString();
        String lastName = binding.lastNameInput.getText().toString();
        String email = binding.emailInput.getText().toString();
        String phone = binding.phoneNumberInput.getText().toString();
        String city = binding.cityInput.getText().toString();
        String school = binding.schoolInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        Level selectedLevel = (Level) binding.spinnerLevels.getSelectedItem();
        Branch selectedBranch = (Branch) binding.spinnerBranches.getSelectedItem();

        if (selectedLevel == null || selectedBranch == null) {
            Toast.makeText(getContext(), "Please select a level and branch", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(firstName, lastName, email, phone, city, school, password,
                selectedLevel.getId(), selectedBranch.getId());

        viewModel.registerUser(request).observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Registration successful! Check your email for verification.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
