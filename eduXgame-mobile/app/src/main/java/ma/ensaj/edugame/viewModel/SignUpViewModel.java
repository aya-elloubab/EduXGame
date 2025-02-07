package ma.ensaj.edugame.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.models.Branch;
import ma.ensaj.edugame.models.Level;
import ma.ensaj.edugame.models.RegisterRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ma.ensaj.edugame.api.RetrofitClient;

public class SignUpViewModel extends ViewModel {
    private final ApiService apiService;

    private final MutableLiveData<List<Level>> levels = new MutableLiveData<>();
    private final MutableLiveData<List<Branch>> branches = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    // Constructor accepting dependencies

    // Constructor now requires a Context to initialize RetrofitClient
    public SignUpViewModel(Context context) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<Level>> getLevels() {
        return levels;
    }

    public LiveData<List<Branch>> getBranches() {
        return branches;
    }

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    // Fetch levels data
    public void fetchLevels() {
        loadingState.setValue(true);
        apiService.getLevels().enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(Call<List<Level>> call, Response<List<Level>> response) {
                loadingState.setValue(false);
                if (response.isSuccessful()) {
                    levels.setValue(response.body());
                } else {
                    logError("fetchLevels", response);
                }
            }

            @Override
            public void onFailure(Call<List<Level>> call, Throwable t) {
                loadingState.setValue(false);
                logFailure("fetchLevels", t);
            }
        });
    }

    // Fetch branches by level
    public void fetchBranches(Long levelId) {
        loadingState.setValue(true);
        apiService.getBranchesByLevel(levelId).enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                loadingState.setValue(false);
                if (response.isSuccessful()) {
                    branches.setValue(response.body());
                } else {
                    logError("fetchBranches", response);
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                loadingState.setValue(false);
                logFailure("fetchBranches", t);
            }
        });
    }

    // Register user
    public LiveData<Boolean> registerUser(RegisterRequest request) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        loadingState.setValue(true);

        apiService.registerUser(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingState.setValue(false);
                if (response.isSuccessful()) {
                    success.setValue(true);
                    Log.d("SignUpViewModel", "Registration successful.");
                } else {
                    logError("registerUser", response);
                    success.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingState.setValue(false);
                logFailure("registerUser", t);
                success.setValue(false);
            }
        });

        return success;
    }

    // Logging helpers
    private void logError(String method, Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
            Log.e("SignUpViewModel", method + " failed: " + errorBody);
        } catch (IOException e) {
            Log.e("SignUpViewModel", method + " failed. Error reading error body.", e);
        }
    }

    private void logFailure(String method, Throwable t) {
        Log.e("SignUpViewModel", method + " failed: " + t.getMessage(), t);
    }
}
