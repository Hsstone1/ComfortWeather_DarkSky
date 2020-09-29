package com.example.sneek.outdoorindex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import Support.Utils;
import model.MapCreator;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Creation";

    Utils utils = new Utils();
    MapCreator mapCreator = new MapCreator();

    public final static Fragment homeFragment = new HomeFragment();
    public final static Fragment mapviewFragment = new MapviewFragment();
    public final static Fragment tropicalFragment = new TropicalFragment();
    public final static Fragment severeFragment = new SevereFragment();
    //public final static Fragment radarFragment = new RadarFragment();
    public final static Fragment timeFragment = new TimeMachineFragment();
    public final static Fragment warningFragment = new WarningsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    static Fragment active = homeFragment;

    String DEFAULT_LATITUDE = utils.DEFAULT_LATITUDE;
    String DEFAULT_LONGITUDE = utils.DEFAULT_LONGITUDE;

    Boolean mapInit = true;
    Boolean timeInit = false;
    Boolean severeInit = true;
    Boolean tropicalInit = true;
    Boolean warningsInit = true;

    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);


        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav_bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


//        mapCreator.radar.setRadarLatitude(Float.parseFloat(DEFAULT_LATITUDE));
//        mapCreator.radar.setRadarLongitude(Float.parseFloat(DEFAULT_LONGITUDE));
//        Log.d(TAG, "RADAR Coords: " + mapCreator.radar.getRadarLatitude() + "," + mapCreator.radar.getRadarLongitude());
//
//        Log.d(TAG, "Default Radar Generated");
        fm.beginTransaction().remove(warningFragment).commit();
        //fm.beginTransaction().remove(timeFragment).commit();
        //fm.beginTransaction().remove(radarFragment).commit();
        fm.beginTransaction().remove(severeFragment).commit();
        fm.beginTransaction().remove(tropicalFragment).commit();
        fm.beginTransaction().remove(mapviewFragment).commit();
        fm.beginTransaction().remove(homeFragment).commit();

        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();
        //fm.beginTransaction().add(R.id.fragment_container, timeFragment, "time").hide(timeFragment).commit();
        //fm.beginTransaction().add(R.id.radarView_frameLayout, radarFragment, "radar").hide(radarFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, warningFragment, "warnings").hide(warningFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, tropicalFragment, "tropical").hide(tropicalFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, severeFragment, "severe").hide(severeFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, mapviewFragment, "mapview").hide(mapviewFragment).commit();

    }

    //FOR FRAGMENT NAVIGATION
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    Log.d(TAG, "Home Selected");
                    return true;

                case R.id.nav_mapview:
                    if (mapInit) {
                        fm.beginTransaction().hide(active).show(mapviewFragment).commit();
                        active = mapviewFragment;
                    } else {
                        fm.beginTransaction().add(R.id.fragment_container, mapviewFragment, "mapview").hide(mapviewFragment).commit();
                        fm.beginTransaction().hide(active).show(mapviewFragment).commit();
                        active = mapviewFragment;
                        mapInit = true;
                        Log.d(TAG, "MAP CREATED");
                    }
                    Log.d(TAG, "Map Selected");
                    return true;


                case R.id.nav_severe:
                    if (severeInit) {
                        fm.beginTransaction().hide(active).show(severeFragment).commit();
                        active = severeFragment;
                    } else {
                        fm.beginTransaction().add(R.id.fragment_container, severeFragment, "severe").hide(severeFragment).commit();
                        fm.beginTransaction().hide(active).show(severeFragment).commit();
                        active = severeFragment;
                        severeInit = true;
                        Log.d(TAG, "SEVERE CREATED");

                    }
                    Log.d(TAG, "Severe Selected");
                    return true;

                case R.id.nav_tropical:
                    if (tropicalInit) {
                        fm.beginTransaction().hide(active).show(tropicalFragment).commit();
                        active = tropicalFragment;
                    } else {
                        fm.beginTransaction().add(R.id.fragment_container, tropicalFragment, "severe").hide(tropicalFragment).commit();
                        fm.beginTransaction().hide(active).show(tropicalFragment).commit();
                        active = tropicalFragment;
                        tropicalInit = true;
                        Log.d(TAG, "TROPICAL CREATED");

                    }
                    Log.d(TAG, "Tropical Selected");
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        //MainActivity.super.onBackPressed();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", null);
        final AlertDialog alert = builder.create();
        alert.show();


    }
}
