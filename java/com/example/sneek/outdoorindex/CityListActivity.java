package com.example.sneek.outdoorindex;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import Adapters.CityListAdapter;
import Support.Utils;
import data.DatabaseHelper;





public class CityListActivity extends AppCompatActivity {

    Utils utils = new Utils();
    DatabaseHelper mDatabaseHelper;
    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;

    private ArrayList<String> mCityName = new ArrayList<>();
    private ArrayList<String> mIsCityFavorite = new ArrayList<>();
    private ArrayList<Integer> mFavoriteCityStar = new ArrayList<>();




    private static final String TAG = "CREATION";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity_layout);

        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
        Slidr.attach(this);
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView");

        Cursor data = mDatabaseHelper.getData();

        //gets data from city column
        while (data.moveToNext()) {
            mCityName.add(data.getString(1));
            mIsCityFavorite.add(data.getString(2));
            if(data.getString(2).equals("no")){
                mFavoriteCityStar.add(R.drawable.ic_star_border);
            } else if(data.getString(2).equals("yes")){
                mFavoriteCityStar.add(R.drawable.ic_star_gold);
            }
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = this.findViewById(R.id.cityList_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        CityListAdapter adapter = new CityListAdapter(this, mCityName, mIsCityFavorite, mFavoriteCityStar);
        recyclerView.setAdapter(adapter);



    }

    public void resetActivityScreen() {
        Intent intent = new Intent(CityListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
