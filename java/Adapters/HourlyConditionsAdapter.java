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

public class HourlyConditionsAdapter extends RecyclerView.Adapter<HourlyConditionsAdapter.ViewHolder> {

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;
    private static final String TAG = "CREATION";

    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mTemp = new ArrayList<>();
    private ArrayList<String> mPrecip = new ArrayList<>();
    private ArrayList<Integer> mHourlyImages = new ArrayList<>();
    private Context context;


    //, ArrayList<String> mHourlyImages
    //        this.mHourlyImages = mHourlyImages;
    public HourlyConditionsAdapter(Context context, ArrayList<String> mTime, ArrayList<String> mTemp, ArrayList<String> mPrecip, ArrayList<Integer> mHourlyImages) {
        this.mTime = mTime;
        this.mTemp = mTemp;
        this.mPrecip = mPrecip;
        this.mHourlyImages = mHourlyImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hourly_outlook_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.time.setText(mTime.get(i));
        viewHolder.temp.setText(mTemp.get(i));
        viewHolder.precip.setText(mPrecip.get(i));
        viewHolder.image.setImageResource(mHourlyImages.get(i));

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.openHourlyInfoDialog(i,false);
                //Toast.makeText(context, "Clicked on " + mTime.get(i), Toast.LENGTH_SHORT).show();

            }
        });
        viewHolder.temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.openHourlyInfoDialog(i,false);

            }
        });

    }
    @Override
    public int getItemCount() {
        return mTime.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView temp;
        TextView precip;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.cardTime_textView);
            temp = itemView.findViewById(R.id.cardTemp_textView);
            precip = itemView.findViewById(R.id.cardPrecip_textView);
            image = itemView.findViewById(R.id.cardHourlyIcon_imageView);
        }
    }

}
