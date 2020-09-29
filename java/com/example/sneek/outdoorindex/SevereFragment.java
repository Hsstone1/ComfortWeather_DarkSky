package com.example.sneek.outdoorindex;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class SevereFragment extends Fragment {

    Button warnings_button;
    ScrollView severeScrollView;
    WebView severeWeather_webView;
    WebView severeWind_webView;
    WebView severeHail_webView;
    WebView severeTornado_webView;

    RadioButton severeWeather_radioButton;
    RadioButton severeWind_radioButton;
    RadioButton severeHail_radioButton;
    RadioButton severeTornadoes_radioButton;

    TextView expandTitle;
    TextView severeDescText;
    ImageView expandImage;
    TextView attributionText;

    LinearLayout expandable_linearLayout;

    Boolean severeExpanded = false;
    private HomeFragment homeFragment = (HomeFragment) MainActivity.homeFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.severe_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        warnings_button = view.findViewById(R.id.warnings_button);

        severeScrollView = view.findViewById(R.id.severeScrollView_scrollView);

        severeWeather_webView = view.findViewById(R.id.severeWeather_webView);
        severeWind_webView = view.findViewById(R.id.severeWind_webView);
        severeHail_webView = view.findViewById(R.id.severeHail_webView);
        severeTornado_webView = view.findViewById(R.id.severeTornadoes_webView);

        severeWeather_radioButton = view.findViewById(R.id.severeWeather_radioButton);
        severeWind_radioButton = view.findViewById(R.id.severeWind_radioButton);
        severeHail_radioButton = view.findViewById(R.id.severeHail_radioButton);
        severeTornadoes_radioButton = view.findViewById(R.id.severeTornadoes_radioButton);

        expandTitle = view.findViewById(R.id.expandableTitleText_textView);
        attributionText = view.findViewById(R.id.severeAttribution_textView);
        severeDescText = view.findViewById(R.id.severeDetails_textView);
        expandImage = view.findViewById(R.id.expandableImage_imageView);
        expandable_linearLayout = view.findViewById(R.id.expandable_linearLayout);

        final String severeWeatherText = "The TSTM area implies a 10% or higher probability of thunderstorms in the next 24 hours. These storms are not likely to contain severe elements.\n\n A 1-MRGL-dark green risk area implies scattered severe storms, which likely are not very intense or organized.\n\n 2-SLGT-yellow risk area implies more organized, and more numerous severe thunderstorms with varying intensity.\n\n A 3-ENH-orange risk area implies a greater concentration of organized severe thunderstorms with varying levels of intensity.\n\n A 4-MDT-red risk indicates potential for widespread severe weather with several tornadoes and/or numerous severe thunderstorms, some of which may be intense. Damaging hail and dangerous winds are possible.\n\n A 5-HIGH-magenta risk area suggests a severe weather outbreak is expected from either numerous intense and long-track tornadoes, or a long-lived system with hurricane-force wind gusts producing widespread damage. Large damaging hail is possible.";
        final String severeWindText = "Probability of damaging thunderstorm winds or wind gusts of 50 knots or higher within 25 miles of a given location. Storms that produce these winds are categorized as severe.\n\n Hatched Area: 10% or greater probability of wind gusts 65 knots or greater within 25 miles of a given location. Storms that produce these winds are categorized as a significant severe thunderstorm.";
        final String severeHailText = "Probability of one inch diameter hail or larger within 25 miles of a given location. Storms that produce these winds are categorized as severe.\n\nHatched Area: 10% or greater probability of two inch diameter hail or larger within 25 miles of a given location. Storms that produce these winds are categorized as a significant severe thunderstorm.";
        final String severeTornadoesText = "Probability of a tornado within 25 miles of a given location.\n A higher percentage doesn't always mean higher intensity, but there is usually a correlation.\n\nHatched Area: 10% or greater probability of EF2 - EF5 tornadoes within 25 miles of a given location.";


        if(homeFragment.isNetworkAvailable()) {
            severeScrollView.setVisibility(View.VISIBLE);
            createSevereMaps();
        } else {
            severeScrollView.setVisibility(View.GONE);
        }

        severeDescText.setText(severeWeatherText);

        expandable_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandAction();
            }
        });

        expandTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandAction();
            }
        });


        severeWeather_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRadioAction(View.VISIBLE,View.GONE,View.GONE,View.GONE,severeWeatherText);
            }
        });
        severeWind_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRadioAction(View.GONE,View.VISIBLE,View.GONE,View.GONE,severeWindText);
            }
        });
        severeHail_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRadioAction(View.GONE,View.GONE,View.VISIBLE,View.GONE,severeHailText);
            }
        });
        severeTornadoes_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRadioAction(View.GONE,View.GONE,View.GONE,View.VISIBLE,severeTornadoesText);
            }
        });

        warnings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().hide(MainActivity.active).show(MainActivity.warningFragment).commit();
                MainActivity.active = MainActivity.warningFragment;
            }
        });

        attributionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spc.noaa.gov/products/outlook/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    intent.setPackage(null);
                    startActivity(intent.createChooser(intent, "Select Browser"));
                }
            }
        });
    }

    private void getExpandAction() {
        if (!severeExpanded) {
            severeDescText.setMaxLines(Integer.MAX_VALUE);
            expandImage.setImageResource(R.drawable.ic_collapse);
            expandTitle.setText("Less Details");
            severeExpanded = true;
        } else {
            severeDescText.setMaxLines(2);
            expandImage.setImageResource(R.drawable.ic_expand);
            expandTitle.setText("More Details");
            severeExpanded = false;
        }
    }

    private void getRadioAction(int severeVis, int windVis, int hailVis, int tornVis, String text){
        severeWeather_webView.setVisibility(severeVis);
        severeWind_webView.setVisibility(windVis);
        severeHail_webView.setVisibility(hailVis);
        severeTornado_webView.setVisibility(tornVis);
        severeDescText.setText(text);
    }

    private void createSevereMaps() {
        WebHelper(severeWeather_webView, "https://www.spc.noaa.gov/products/outlook/day1otlk_2000.gif");
        WebHelper(severeWind_webView, "https://www.spc.noaa.gov/products/outlook/day1probotlk_2000_wind.gif");
        WebHelper(severeHail_webView, "https://www.spc.noaa.gov/products/outlook/day1probotlk_2000_hail.gif");
        WebHelper(severeTornado_webView, "https://www.spc.noaa.gov/products/outlook/day1probotlk_2000_torn.gif");
    }

    public void WebHelper(WebView resource, String url) {
        //resource.getSettings().setJavaScriptEnabled(true);
        resource.loadUrl(url);
        resource.getSettings().setSupportZoom(true);
        resource.getSettings().setLoadWithOverviewMode(true);
        resource.getSettings().setUseWideViewPort(true);
        resource.getSettings().setBuiltInZoomControls(true);
        resource.getSettings().setDisplayZoomControls(false);

        resource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                severeScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

}
