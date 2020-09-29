package Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

import data.DatabaseHelper;

public class WarningsAdapter extends RecyclerView.Adapter<WarningsAdapter.ViewHolder>{

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;
    private static final String TAG = "CREATION";

    private List<String> mWarningTitle = new ArrayList<>();
    private List<String> mState = new ArrayList<>();
    private List<String> mCertainty = new ArrayList<>();
    private List<String> mExpires = new ArrayList<>();
    private List<Integer> mExpiresUNIX = new ArrayList<>();
    private List<String> mDesc = new ArrayList<>();
    private List<String> mAreaDesc = new ArrayList<>();
    private List<Integer> mAlertIcon = new ArrayList<>();
    private List<String> mGeofence = new ArrayList<>();
    private List<String> mColor = new ArrayList<>();
    private List<String> mID = new ArrayList<>();
    private List<Integer> mAlertBackground = new ArrayList<>();

    private Context context;
    DatabaseHelper mDatabaseHelper;


    public WarningsAdapter(Context context, List<String> mWarningTitle, List<String> mState, List<String> mCertainty, List<String> mExpires, List<Integer> mExpiresUNIX, List<String> mDesc, List<String> mAreaDesc, List<Integer> mAlertIcon, List<String> mGeofence, List<String> mColor, List<String> mID) {
        this.mWarningTitle = mWarningTitle;
        this.mState = mState;
        this.mCertainty = mCertainty;
        this.mExpires = mExpires;
        this.mExpiresUNIX = mExpiresUNIX;
        this.mDesc = mDesc;
        this.mAreaDesc = mAreaDesc;
        this.mAlertIcon = mAlertIcon;
        this.mGeofence = mGeofence;
        this.mColor = mColor;
        this.mID = mID;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warnings_fragment_card_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        mDatabaseHelper = new DatabaseHelper(context);

        viewHolder.warningTitleText.setText(mWarningTitle.get(i));
        viewHolder.stateText.setText(mState.get(i));
        viewHolder.stateText.append(" (" + mAreaDesc.get(i) + ")");
        viewHolder.certaintyText.setText("Certainty: " + mCertainty.get(i));
        viewHolder.expireText.setText("Expires: " + mExpires.get(i));
        viewHolder.warningIcon.setImageResource(mAlertIcon.get(i));


        viewHolder.warningTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d(TAG, "ALERT TITLE " + mWarningTitle.get(i));
//                Log.d(TAG, "ALERT UNIX " + mExpiresUNIX.get(i));
//                Log.d(TAG, "ALERT DESC " + mDesc.get(i));
//                Log.d(TAG, "ALERT ICON " + mAlertIcon.get(i));
//                Log.d(TAG, "ALERT GEOFENCE " + mGeofence.get(i));
//                Log.d(TAG, "ALERT COLOR " + mColor.get(i));
                //Log.d(TAG, "ID: " + mID.get(i));
                homeFragment.openWeatherAlertDialog(mWarningTitle.get(i),mExpires.get(i), mExpiresUNIX.get(i), mDesc.get(i), mAlertIcon.get(i), mGeofence.get(i), mColor.get(i), mID.get(i));

            }
        });

    }
    @Override
    public int getItemCount() {
        return mWarningTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView warningTitleText;
        TextView stateText;
        TextView certaintyText;
        TextView expireText;

        ImageView warningIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            warningTitleText = itemView.findViewById(R.id.natWarningTitle_textView);
            stateText = itemView.findViewById(R.id.natWarningState_textView);
            certaintyText = itemView.findViewById(R.id.natWarningCertainty_textView);
            expireText = itemView.findViewById(R.id.natWarningExpire_textView);
            warningIcon = itemView.findViewById(R.id.natWarningAlert_imageView);
        }
    }

    public void resetActivityScreen() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
