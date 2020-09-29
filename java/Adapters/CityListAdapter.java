package Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import data.DatabaseHelper;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;
    private static final String TAG = "CREATION";

    private ArrayList<String> mCityName = new ArrayList<>();
    private ArrayList<String> mIsCityFavorite = new ArrayList<>();
    private ArrayList<Integer> mFavoriteCityStar = new ArrayList<>();

    private Context context;
    DatabaseHelper mDatabaseHelper;




    //, ArrayList<String> mHourlyImages
    //        this.mHourlyImages = mHourlyImages;
    public CityListAdapter(Context context, ArrayList<String> mCityName, ArrayList<String> mIsCityFavorite, ArrayList<Integer> mFavoriteCityStar) {
        this.mCityName = mCityName;
        this.mIsCityFavorite = mIsCityFavorite;
        this.mFavoriteCityStar = mFavoriteCityStar;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_card_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        //viewHolder.cityName.setText(mCityName.get(i));
        mDatabaseHelper = new DatabaseHelper(context);
        //viewHolder.cityName.setText(mCityName.get(i) + " " + mIsCityFavorite.get(i));
        viewHolder.cityName.setText(mCityName.get(i));
        viewHolder.favImage.setImageResource(mFavoriteCityStar.get(i));


        //viewHolder.favImage.setImageResource(mFavIcon.get(i));
        //viewHolder.moveImage.setImageResource(mMovableIcon.get(i));


        viewHolder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //homeFragment.openHourlyInfoDialog(i);
                String name = mCityName.get(i);
                Toast.makeText(context, "Clicked on " + name, Toast.LENGTH_SHORT).show();

                Cursor data = mDatabaseHelper.getItemID(name);
                //ensures data is returned
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                if (itemID > -1) {
                    homeFragment.geoLocateUserPreference(name);
                    resetActivityScreen();
                } else {
                    Log.d(TAG, "onItemClick: NO ID FOUND");
                }
            }
        });




        viewHolder.moveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mCityName.get(i);
                Toast.makeText(context, "Clicked on " + name, Toast.LENGTH_SHORT).show();

                Cursor data = mDatabaseHelper.getItemID(name);

                //ensures data is returned
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                if (itemID > -1) {

                    buildAlertMessageDelete("Are you sure you want to delete?", "yes", "no", itemID, name);
                } else {
                    Log.d(TAG, "onItemClick: NO ID FOUND");
                }
            }
        });

        viewHolder.favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mCityName.get(i);

                Cursor data = mDatabaseHelper.getItemID(name);
                Cursor favoriteData = mDatabaseHelper.getIsFavorite(name);


                Log.d(TAG, "CITY FAVORITE ARRAY " + mIsCityFavorite);
                Toast.makeText(context, "CLICKED ON " + mCityName.get(i) + " FAVORITE",Toast.LENGTH_SHORT).show();
                if(mIsCityFavorite.get(i).equals("no")) {
                    Toast.makeText(context, name + " favorited",Toast.LENGTH_SHORT).show();
                    viewHolder.favImage.setImageResource(R.drawable.ic_star_gold);
                    mDatabaseHelper.updateFavorite(i, "yes");


                } else if(mIsCityFavorite.get(i).equals("yes")) {
                    Toast.makeText(context, name + " unfavorited",Toast.LENGTH_SHORT).show();
                    viewHolder.favImage.setImageResource(R.drawable.ic_star_border);
                    mDatabaseHelper.updateFavorite(i, "no");

                }

                //ensures data is returned
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }

                if(itemID > -1) {
                    if (null != favoriteData && favoriteData.moveToFirst()) {
                        Log.d(TAG, "listID " + i);
                        //Log.d(TAG, "CITYFAVORITE: " + mIsCityFavorite);
                        mIsCityFavorite.set(i, favoriteData.getString(favoriteData.getColumnIndex("isFavorite")));
                    }
                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return mCityName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        ImageView favImage;
        ImageView moveImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName_textView);
            favImage = itemView.findViewById(R.id.favorite_imageButton);
            moveImage = itemView.findViewById(R.id.movable_imageView);
        }
    }

    public void resetActivityScreen() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private void buildAlertMessageDelete(String message, String positiveButtonText, String negativeButtonText, final int itemID, final String name) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        mDatabaseHelper.deleteName(itemID, name);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
