package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sneek.outdoorindex.HomeFragment;
import com.example.sneek.outdoorindex.MainActivity;
import com.example.sneek.outdoorindex.R;

import java.util.ArrayList;

public class ExtendHourlyAdapter extends RecyclerView.Adapter<ExtendHourlyAdapter.ViewHolder> {

    private static final String TAG = "CREATION";

    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mConditionDesc = new ArrayList<>();
    private ArrayList<String> mTemp = new ArrayList<>();
    private ArrayList<String> mApparent = new ArrayList<>();
    private ArrayList<String> mDew = new ArrayList<>();
    private ArrayList<String> mPrecip = new ArrayList<>();
    private ArrayList<String> mWind = new ArrayList<>();
    private ArrayList<String> mUVNum = new ArrayList<>();
    private ArrayList<String> mUVCondition = new ArrayList<>();
    private ArrayList<String> mHumid = new ArrayList<>();
    private ArrayList<String> mComfort = new ArrayList<>();

    private ArrayList<Integer> mConditionImage = new ArrayList<>();
    private ArrayList<Integer> mWindImage = new ArrayList<>();
    private Context context;

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;



    //, ArrayList<String> mHourlyImages
    //        this.mHourlyImages = mHourlyImages;
    public ExtendHourlyAdapter(Context context, ArrayList<String> mTime, ArrayList<String> mConditionDesc,
                                   ArrayList<String> mTemp, ArrayList<String> mApparent,
                                   ArrayList<String> mDew, ArrayList<String> mPrecip,
                                   ArrayList<String>mWind, ArrayList<String> mUVNum,
                                   ArrayList<String> mUVCondition, ArrayList<String> mHumid,
                                   ArrayList<String> mComfort, ArrayList<Integer> mConditionImage, ArrayList<Integer> mWindImage) {
        this.mTime = mTime;
        this.mConditionDesc = mConditionDesc;
        this.mTemp = mTemp;
        this.mApparent = mApparent;
        this.mDew = mDew;
        this.mPrecip = mPrecip;
        this.mWind = mWind;
        this.mUVNum = mUVNum;
        this.mUVCondition = mUVCondition;
        this.mHumid = mHumid;
        this.mComfort = mComfort;
        this.mWindImage = mWindImage;
        this.mConditionImage = mConditionImage;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.extended_hourly_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.time.setText(mTime.get(i));
        viewHolder.desc.setText(mConditionDesc.get(i));
        viewHolder.temp.setText(mTemp.get(i));
        viewHolder.apparent.setText(mApparent.get(i));
        viewHolder.dew.setText(mDew.get(i));
        viewHolder.precip.setText(mPrecip.get(i));
        viewHolder.wind.setText(mWind.get(i));
        viewHolder.uvNum.setText(mUVNum.get(i));
        viewHolder.uvCondition.setText(mUVCondition.get(i));
        viewHolder.humid.setText(mHumid.get(i));
        viewHolder.comfort.setText(mComfort.get(i));
        viewHolder.windImage.setImageResource(mWindImage.get(i));
        viewHolder.conditionImage.setImageResource(mConditionImage.get(i));

        //LOADS ALERT DIALOG BEHIND ACTIVITY
//        viewHolder.conditionImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                homeFragment.openHourlyInfoDialog(i);
//
//            }
//        });



    }
    @Override
    public int getItemCount() {
        return mTime.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView desc;
        TextView temp;
        TextView apparent;
        TextView dew;
        TextView precip;
        TextView wind;
        TextView uvNum;
        TextView uvCondition;
        TextView humid;
        TextView comfort;
        ImageView windImage;
        ImageView conditionImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.extendTime_textView);
            desc = itemView.findViewById(R.id.extendIconDesc_textView);
            temp = itemView.findViewById(R.id.extendTemp_textView);
            apparent = itemView.findViewById(R.id.extendApparent_textView);
            dew = itemView.findViewById(R.id.extendDew_textView);
            precip = itemView.findViewById(R.id.extendPrecip_textView);
            wind = itemView.findViewById(R.id.extendWind_textView);
            uvNum = itemView.findViewById(R.id.extendUVNum_textView);
            uvCondition = itemView.findViewById(R.id.extendUVCondition_textView);
            humid = itemView.findViewById(R.id.extendHumid_textView);
            comfort = itemView.findViewById(R.id.extendComfort_textView);
            windImage = itemView.findViewById(R.id.extendWindImage_imageView);
            conditionImage = itemView.findViewById(R.id.extendIcon_imageView);

        }
    }

}

