package ma.ensaj.edugame.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.edugame.R;
import ma.ensaj.edugame.models.Planet;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

    private final List<Planet> planetList;
    private final Context context;

    public PlanetAdapter(Context context, List<Planet> planetList) {
        this.context = context;
        this.planetList = planetList;
    }

    @NonNull
    @Override
    public PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_planet, parent, false);
        return new PlanetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {
        Planet planet = planetList.get(position);

        // Set the planet name
        holder.planetName.setText(planet.getName());

        // Map planet name to drawable resource
        int drawableId = getPlanetDrawable(planet.getName());
        holder.planetImage.setImageResource(drawableId);
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder {
        TextView planetName;
        ImageView planetImage;

        public PlanetViewHolder(@NonNull View itemView) {
            super(itemView);
            planetName = itemView.findViewById(R.id.planetName);
            planetImage = itemView.findViewById(R.id.planetImage);
        }
    }

    // Helper method to map planet names to drawable resource IDs
    private int getPlanetDrawable(String planetName) {
        switch (planetName.toLowerCase()) {
            case "sun":
                return R.drawable.sun;
            case "mercury":
                return R.drawable.mercury;
            case "venus":
                return R.drawable.venus;
            case "earth":
                return R.drawable.earth;
            case "mars":
                return R.drawable.mars;
            case "jupiter":
                return R.drawable.jupiter;
            case "saturn":
                return R.drawable.saturn;
            case "uranus":
                return R.drawable.uranus;
            case "neptune":
                return R.drawable.neptune;
            default:
                return R.drawable.space_background; // Default image if no match is found
        }
    }
}
