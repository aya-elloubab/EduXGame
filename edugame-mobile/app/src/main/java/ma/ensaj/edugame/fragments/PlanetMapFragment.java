package ma.ensaj.edugame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.api.ApiService;
import ma.ensaj.edugame.api.RetrofitClient;
import ma.ensaj.edugame.models.Planet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanetMapFragment extends Fragment {
    private int completedCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        Log.d("PlanetMap", "PlanetMapFragment loaded"); // Log to check if fragment is loaded

        View rootView = inflater.inflate(R.layout.fragment_planet_map, container, false);

        // Fetch completed courses and update planets
        fetchCompletedCourses(rootView);

        return rootView;
    }

    private void fetchCompletedCourses(View rootView) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        // Fetch completed courses count
        apiService.getCompletedCoursesCount(studentId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    completedCourses = response.body();
                    Log.d("PlanetMap", "Completed Courses: " + completedCourses);

                    fetchPlanetsFromBackend(rootView); // Fetch planets after courses count
                } else {
                    Log.e("PlanetMap", "Failed to fetch completed courses. Response: " + response.code());
                }
            }


            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void fetchPlanetsFromBackend(View rootView) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Long studentId = getUserId();

        apiService.getAllPlanets(studentId).enqueue(new Callback<List<Planet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Planet>> call, @NonNull Response<List<Planet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Planet> planets = response.body();
                    Log.d("PlanetMap", "Fetched Planets: " + planets.size());

                    updatePlanetUI(rootView.findViewById(R.id.planetSun), rootView.findViewById(R.id.sunName), rootView.findViewById(R.id.sunBar), planets.get(8));
                    updatePlanetUI(rootView.findViewById(R.id.planetMercury), rootView.findViewById(R.id.mercuryName), rootView.findViewById(R.id.mercuryBar), planets.get(7));
                    updatePlanetUI(rootView.findViewById(R.id.planetVenus), rootView.findViewById(R.id.venusName), rootView.findViewById(R.id.venusBar), planets.get(6));
                    updatePlanetUI(rootView.findViewById(R.id.planetEarth), rootView.findViewById(R.id.earthName), rootView.findViewById(R.id.earthBar), planets.get(5));
                    updatePlanetUI(rootView.findViewById(R.id.planetMars), rootView.findViewById(R.id.marsName), rootView.findViewById(R.id.marsBar), planets.get(4));
                    updatePlanetUI(rootView.findViewById(R.id.planetJupiter), rootView.findViewById(R.id.jupiterName), rootView.findViewById(R.id.jupiterBar), planets.get(3));
                    updatePlanetUI(rootView.findViewById(R.id.planetSaturn), rootView.findViewById(R.id.saturnName), rootView.findViewById(R.id.saturnBar), planets.get(2));
                    updatePlanetUI(rootView.findViewById(R.id.planetUranus), rootView.findViewById(R.id.uranusName), rootView.findViewById(R.id.uranusBar), planets.get(1));
                    updatePlanetUI(rootView.findViewById(R.id.planetNeptune), rootView.findViewById(R.id.neptuneName), rootView.findViewById(R.id.neptuneBar), planets.get(0));

                } else {
                    Log.e("PlanetMap", "Failed to fetch planets. Response: " + response.code());
                }
            }


            @Override
            public void onFailure(@NonNull Call<List<Planet>> call, @NonNull Throwable t) {
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void updatePlanetUI(ImageView planetImage, TextView planetName, ProgressBar progressBar, Planet planet) {
        // Set planet name
        planetName.setText(planet.getName());

        // Calculate progress based on completed courses and unlocking requirement
        int progress = Math.min((completedCourses * 100) / planet.getUnlockingRequirement(), 100);
        progressBar.setProgress(progress);
        Log.d("PlanetMap", "Fetched Planet: " + planet.getName() + ", Unlocked: " + planet.unlocked());

        // Handle locked/unlocked state
        if (planet.unlocked()) {
            planetImage.setAlpha(1.0f); // Fully visible for unlocked planets
            Log.d("PlanetMap", planet.getName() + " is unlocked: Fully visible");
        } else {
            planetImage.setAlpha(0.5f); // Semi-transparent for locked planets
            Log.d("PlanetMap", planet.getName() + " is locked: Semi-transparent");
        }

    }

    private Long getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }
}
