package com.example.sneek.outdoorindex;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import Support.Utils;
import model.Weather;

public class TimeMachineFragment extends Fragment {

    private static final String TAG = "CREATION";
    Weather weather = new Weather();
    Utils utils = new Utils();
    CalendarView dateCalendar;
    EditText dateTimeTravel;
    TextView attributionText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.time_machine_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        attributionText = (TextView) view.findViewById(R.id.attribution_textView);
        dateCalendar = (CalendarView)view.findViewById(R.id.calendarView);
        dateTimeTravel = (EditText)view.findViewById(R.id.enterDate_editText);


        attributionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nhc.noaa.gov/gtwo.php"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    Log.d(TAG, "onClick: inTryBrowser");
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.e(TAG, "onClick: in inCatchBrowser", ex );
                    intent.setPackage(null);
                    startActivity(intent.createChooser(intent, "Select Browser"));
                }
            }
        });
    }





}
