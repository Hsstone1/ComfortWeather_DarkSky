package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sneek.outdoorindex.HomeFragment;
import com.example.sneek.outdoorindex.MainActivity;
import com.example.sneek.outdoorindex.R;

import java.util.ArrayList;

public class WeatherConditionsAdapter extends RecyclerView.Adapter<WeatherConditionsAdapter.ViewHolder> {

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;

    private static final String TAG = "CREATION";

    private ArrayList<String> mHeaders = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context context;


    //, ArrayList<String> mImages
    //        this.mImages = mImages;
    public WeatherConditionsAdapter(Context context, ArrayList<String> mHeaders, ArrayList<String> mInfo, ArrayList<Integer> mImages) {
        this.mHeaders = mHeaders;
        this.mInfo = mInfo;
        this.mImages = mImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_weather_conditions_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.header.setText(mHeaders.get(i));
        viewHolder.info.setText(mInfo.get(i));
        viewHolder.image.setImageResource(mImages.get(i));

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.openHourlyInfoDialog(i, false);

                //Toast.makeText(context, "Clicked on " + mHeaders.get(i), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.openHourlyInfoDialog(i, false);
            }
        });
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.openHourlyInfoDialog(i, false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHeaders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView info;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.cardHeader_textView);
            info = itemView.findViewById(R.id.cardInformation_textView);
            image = itemView.findViewById(R.id.cardIcon_imageView);
        }
    }


}
