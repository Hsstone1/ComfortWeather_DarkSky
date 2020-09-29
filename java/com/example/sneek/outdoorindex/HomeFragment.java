package com.example.sneek.outdoorindex;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapters.HourlyConditionsAdapter;
import Adapters.WeatherConditionsAdapter;
import Support.Utils;
import data.AutocompleteDatabaseHelper;
import data.DatabaseHelper;
import data.JSONTimeWeatherParser;
import data.JSONWeatherParser;
import data.ReadCityExcel;
import data.TimeWeatherHttpClient;
import data.WeatherDataDatabaseHelper;
import data.WeatherHttpClient;
import model.MapCreator;
import model.TimeWeather;
import model.Weather;

import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.max;

public class HomeFragment extends Fragment {


    LineGraphSeries<DataPoint> series;

    public Weather weather = new Weather();
    public TimeWeather timeWeather = new TimeWeather();
    public Utils utils = new Utils();
    ReadCityExcel readCityExcel = new ReadCityExcel();
    DatabaseHelper mDatabaseHelper;
    AutocompleteDatabaseHelper mAutocompleteDatabaseHelper;
    public WeatherDataDatabaseHelper mWeatherDataDatabaseHelper;
    private MapviewFragment mapviewFragment = (MapviewFragment) MainActivity.mapviewFragment;

    private Handler mainHandler = new Handler();
    private volatile boolean stopThread = false;
    private static final String TAG = "CREATION";


    String DEG_UNITS = utils.DEG_UNITS;
    String SPEED_UNITS = utils.SPEED_UNITS;
    String DIST_UNITS = utils.DIST_UNITS;
    String RAIN_UNITS = utils.RAIN_UNITS;
    String PRECIP_ACCUM_UNITS = utils.PRECIP_ACCUM_UNITS;
    String DEFAULT_LATITUDE = utils.DEFAULT_LATITUDE;
    String DEFAULT_LONGITUDE = utils.DEFAULT_LONGITUDE;
    String DEFAULT_CITY_NAME = utils.DEFAULT_CITY_NAME;
    final int tempWeight = utils.tempWeight;
    final int feelLikeWeight = utils.feelLikeWeight;
    final int windSpeedWeight = utils.windSpeedWeight;
    final int uvWeight = utils.uvWeight;
    final int cloudCoverWeight = utils.cloudCoverWeight;
    final int precipWeight = utils.precipWeight;
    int dewWeight = utils.dewWeight;
    final int precipIntensityWeight = utils.precipIntensityWeight;
    int detailReducer = utils.detailReducer;
    final int CURRENT_TIMEZONE_OFFSET = 4 * 60 * 60;            //THIS IS FOR EAST COAST TIME (UTC - 4)\
    final int WEEKLY_HOURS = 168;

    final String WINDY_API_KEY = "gGoQKRqTjBJlTHqFMcxK3bcS9oL29wJx";
    final int ZOOM = 5;

    public final DecimalFormat d4f = new DecimalFormat("#.####");
    public final DecimalFormat d3f = new DecimalFormat("#.###");
    public final DecimalFormat d2f = new DecimalFormat("#.##");
    public final DecimalFormat d1f = new DecimalFormat("#.#");
    public final DecimalFormat d0f = new DecimalFormat("#");
    //32.941931, -80.157641 LADSON
    //38.9072, -77.0369        WASH DC

    private ArrayList<String> mHeaders = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();

    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mTemp = new ArrayList<>();
    private ArrayList<String> mPrecip = new ArrayList<>();
    private ArrayList<Integer> mHourlyImages = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener datePickerSetListener;

    boolean day1Selected = false;
    boolean day2Selected = false;
    boolean day3Selected = false;
    boolean day4Selected = false;
    boolean day5Selected = false;
    boolean day6Selected = false;
    boolean day7Selected = false;

    boolean cityHasQueried = false;


    TextView currentTemperature_textView;
    TextView feelLike_textView;
    TextView humidity_textView;
    TextView coordText_textView;
    TextView timeLabels_textView;
    TextView sunrise_textView;
    TextView sunset_textView;
    TextView updated_textView;
    TextView description_textView;
    TextView minTemperature_textView;
    TextView maxTemperature_textView;
    TextView tomTempOutlook_textView;
    TextView daylightHours_textView;
    TextView moonPhase_textView;
    TextView nextFullMoon_textView;
    TextView nextNewMoon_textView;
    TextView moonIllumination_textView;

    TextView dayDescription_textView;
    TextView dailySummaryHigh_textView;
    TextView dailySummaryLow_textView;
    TextView dailySummaryDewpoint_textView;
    TextView dailySummaryPrecip_textView;
    TextView dailySummaryAccum_textView;
    TextView summaryDate_textView;
    TextView daySummarySunrise_textView;
    TextView daySummarySunset_textView;
    TextView daySummaryMoonphase_textView;
    TextView dailyCondition_textView;
    TextView weatherAlert_textView;
    TextView weatherAlertType;
    TextView hourlyExpectedAccum_textView;
    TextView minutelyGraphTimeLabels_textView;
    TextView forecastGraphViewingDate_textView;
    TextView daylightLabel_textView;
    TextView sunriseDifference_textView;
    TextView daylightDifference_textView;
    TextView sunsetDifference_textView;
    TextView hourlyDates_textView;
    TextView currentIconDesc_textView;
    TextView comfortConditon_textView;


    ImageView currentConditionIcon_imageView;
    ImageView dailyCondition_imageView;
    ImageView moonPhaseIcon_imageView;

    ImageView day0RadioIcon_imageView;
    ImageView day1RadioIcon_imageView;
    ImageView day2RadioIcon_imageView;
    ImageView day3RadioIcon_imageView;
    ImageView day4RadioIcon_imageView;
    ImageView day5RadioIcon_imageView;
    ImageView day6RadioIcon_imageView;
    ImageView windIcon_imageView;


    ImageButton clearSearch_imageButton;
    ImageButton location_imageButton;
    ImageButton uvIndexInfo_imageButton;
    ImageButton cityList_imageButton;
    ImageButton poweredDarkSky_imageButton;
    Button showGraphs_button;
    Button extendHourlyButton;
    Button timeMachine_button;
    RadioButton hourlySummary_radioButton;
    RadioButton daily_radioButton;
    RadioButton sunPosition_radioButton;
    RadioButton uvIndex_radioButton;
    RadioButton moonPhase_radioButton;
    RadioGroup daySummary_radioGroup;
    RadioGroup daySummaryDates_radioGroup;
    RadioButton radioButton;
    RadioButton day0_radioButton;
    RadioButton day1_radioButton;
    RadioButton day2_radioButton;
    RadioButton day3_radioButton;
    RadioButton day4_radioButton;
    RadioButton day5_radioButton;
    RadioButton day6_radioButton;
    RadioButton summaryDay0_radioButton;
    RadioButton summaryDay1_radioButton;
    RadioButton summaryDay2_radioButton;
    RadioButton summaryDay3_radioButton;
    RadioButton summaryDay4_radioButton;
    RadioButton summaryDay5_radioButton;
    RadioButton summaryDay6_radioButton;
    RadioButton uv3Day_radioButton;
    RadioButton uv7Day_radioButton;


    AutoCompleteTextView citySearch_editText;


    GraphView minutely_graph;
    CheckBox apparentTemp_checkBox;
    CheckBox dewPoint_checkBox;
    CheckBox humidity_checkBox;
    CheckBox comfortIndex_checkBox;
    CheckBox cloudCover_checkBox;
    CheckBox windSpeed_checkBox;


    LinearLayout hourlySummary_linearLayout;
    LinearLayout minutelyGraph_linearLayout;
    LinearLayout dailyGraph_linearLayout;
    LinearLayout dayButtons_linearLayout;
    LinearLayout dailyOutlook_linearLayout;
    LinearLayout sunPosition_linearLayout;
    LinearLayout uvIndexGraph_linearLayout;
    LinearLayout moonPhase_linearLayout;
    LinearLayout summaryDailyButtons_linearLayout;
    LinearLayout weatherAlertScrollable;
    LinearLayout dailyGraphLegend_linearLayout;
    RelativeLayout citySearch_relativeLayout;
    HorizontalScrollView dailyGraphLegend_horizontalScrollView;
    ScrollView mainScreen_scrollView;
    HorizontalScrollView horizontalGraphDatesScroll;

    ProgressBar comfortIndex_progressBar;
    ProgressBar dayProgress_progressBar;
    ProgressBar minMaxTemp_progressBar;

    WebView radarView;

    LineChart minutelyGraph;
    LineChart forecastGraph;
    LineChart uvGraph;

    ObjectAnimator objectAnimator;
    CoordinatorLayout coordinatorLayout;

    BottomNavigationView bottomNav_bottomNavigationView;


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //    private FusedLocationProviderClient fusedLocationClient;
    Runnable refresh;
    Boolean homeInit = false;
    Boolean isAutoCitiesLoaded = false;


    private static final List<String> cityArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        currentTemperature_textView = view.findViewById(R.id.currentTemperature_textView);


        description_textView = view.findViewById(R.id.description_textView);
        minTemperature_textView = view.findViewById(R.id.minTemperature_textView);
        maxTemperature_textView = view.findViewById(R.id.maxTemperature_textView);
        timeLabels_textView = view.findViewById(R.id.timeLabels_textView);
        daylightHours_textView = view.findViewById(R.id.daylightHours_textView);

        moonPhase_textView = view.findViewById(R.id.moonPhase_textView);
        nextFullMoon_textView = view.findViewById(R.id.nextFullMoon_textView);
        nextNewMoon_textView = view.findViewById(R.id.nextNewMoon_textView);
        moonIllumination_textView = view.findViewById(R.id.moonIllumination_textView);
        dayDescription_textView = view.findViewById(R.id.dayDescription_textView);
        dailySummaryHigh_textView = view.findViewById(R.id.dailySummaryHigh_textView);
        dailySummaryLow_textView = view.findViewById(R.id.dailySummaryLow_textView);
        dailySummaryDewpoint_textView = view.findViewById(R.id.dailySummaryDewpoint_textView);
        dailySummaryPrecip_textView = view.findViewById(R.id.dailySummaryPrecip_textView);
        dailySummaryAccum_textView = view.findViewById(R.id.dailySummaryAccum_textView);
        summaryDate_textView = view.findViewById(R.id.summaryDate_textView);
        daySummarySunrise_textView = view.findViewById(R.id.daySummarySunrise_textView);
        sunrise_textView = view.findViewById(R.id.sunrise_textView);
        sunset_textView = view.findViewById(R.id.sunset_textView);
        coordText_textView = view.findViewById(R.id.coordText_textView);
        updated_textView = view.findViewById(R.id.updated_textView);
        daySummarySunset_textView = view.findViewById(R.id.daySummarySunset_textView);
        daySummaryMoonphase_textView = view.findViewById(R.id.daySummaryMoonphase_textView);
        dailyCondition_textView = view.findViewById(R.id.dailyCondition_textView);
        weatherAlert_textView = view.findViewById(R.id.weatherAlert_textView);
        weatherAlertType = view.findViewById(R.id.alertType_textView);
        hourlyExpectedAccum_textView = view.findViewById(R.id.hourlyExpectedAccum_textView);
        minutelyGraphTimeLabels_textView = view.findViewById(R.id.minutelyGraphTimeLabels_textView);
        forecastGraphViewingDate_textView = view.findViewById(R.id.forecastGraphViewingDate_textView);
        daylightLabel_textView = view.findViewById(R.id.daylightLabel_textView);
        hourlyDates_textView = view.findViewById(R.id.hourlyDates_textView);
        sunriseDifference_textView = view.findViewById(R.id.sunriseDifference_textView);
        daylightDifference_textView = view.findViewById(R.id.daylightDifference_textView);
        sunsetDifference_textView = view.findViewById(R.id.sunsetDifference_textView);
        currentIconDesc_textView = view.findViewById(R.id.currentIconDesc_textView);
        comfortConditon_textView = view.findViewById(R.id.comfortConditon_textView);

        currentConditionIcon_imageView = view.findViewById(R.id.currentConditionIcon_imageView);
        dailyCondition_imageView = view.findViewById(R.id.dailyCondition_imageView);
        moonPhaseIcon_imageView = view.findViewById(R.id.moonPhaseIcon_imageView);
        day0RadioIcon_imageView = view.findViewById(R.id.day0RadioIcon_imageView);
        day1RadioIcon_imageView = view.findViewById(R.id.day1RadioIcon_imageView);
        day2RadioIcon_imageView = view.findViewById(R.id.day2RadioIcon_imageView);
        day3RadioIcon_imageView = view.findViewById(R.id.day3RadioIcon_imageView);
        day4RadioIcon_imageView = view.findViewById(R.id.day4RadioIcon_imageView);
        day5RadioIcon_imageView = view.findViewById(R.id.day5RadioIcon_imageView);
        day6RadioIcon_imageView = view.findViewById(R.id.day6RadioIcon_imageView);


        clearSearch_imageButton = view.findViewById(R.id.clearSearch_imageButton);
        location_imageButton = view.findViewById(R.id.location_imageButton);
        uvIndexInfo_imageButton = view.findViewById(R.id.uvIndexInfo_imageButton);
        cityList_imageButton = view.findViewById(R.id.cityList_imageButton);
        poweredDarkSky_imageButton = view.findViewById(R.id.poweredDarkSky_imageButton);
        showGraphs_button = view.findViewById(R.id.showGraphs_button);
        extendHourlyButton = view.findViewById(R.id.extendHourly_button);
        timeMachine_button = view.findViewById(R.id.timeMachine_button);
        hourlySummary_radioButton = view.findViewById(R.id.hourlySummary_radioButton);
        daily_radioButton = view.findViewById(R.id.daily_radioButton);

        daySummary_radioGroup = (RadioGroup) view.findViewById(R.id.daySummary_radioGroup);
        daySummaryDates_radioGroup = (RadioGroup) view.findViewById(R.id.daySummaryDates_radioGroup);
        day0_radioButton = view.findViewById(R.id.day0_radioButton);
        day1_radioButton = view.findViewById(R.id.day1_radioButton);
        day2_radioButton = view.findViewById(R.id.day2_radioButton);
        day3_radioButton = view.findViewById(R.id.day3_radioButton);
        day4_radioButton = view.findViewById(R.id.day4_radioButton);
        day5_radioButton = view.findViewById(R.id.day5_radioButton);
        day6_radioButton = view.findViewById(R.id.day6_radioButton);
        summaryDay0_radioButton = view.findViewById(R.id.summaryDay0_radioButton);
        summaryDay1_radioButton = view.findViewById(R.id.summaryDay1_radioButton);
        summaryDay2_radioButton = view.findViewById(R.id.summaryDay2_radioButton);
        summaryDay3_radioButton = view.findViewById(R.id.summaryDay3_radioButton);
        summaryDay4_radioButton = view.findViewById(R.id.summaryDay4_radioButton);
        summaryDay5_radioButton = view.findViewById(R.id.summaryDay5_radioButton);
        summaryDay6_radioButton = view.findViewById(R.id.summaryDay6_radioButton);



        sunPosition_radioButton = view.findViewById(R.id.sunPosition_radioButton);
        uvIndex_radioButton = view.findViewById(R.id.uvIndex_radioButton);
        moonPhase_radioButton = view.findViewById(R.id.moonPhase_radioButton);


        citySearch_editText = view.findViewById(R.id.citySearch_autoCompleteTextView);

        apparentTemp_checkBox = view.findViewById(R.id.apparentTemp_checkBox);
        dewPoint_checkBox = view.findViewById(R.id.dewPoint_checkBox);
        humidity_checkBox = view.findViewById(R.id.humidity_checkBox);
        comfortIndex_checkBox = view.findViewById(R.id.comfortIndex_checkBox);
        cloudCover_checkBox = view.findViewById(R.id.cloudCover_checkBox);
        windSpeed_checkBox = view.findViewById(R.id.windSpeed_checkBox);


        hourlySummary_linearLayout = view.findViewById(R.id.hourlySummary_linearLayout);
        minutelyGraph_linearLayout = view.findViewById(R.id.minutelyGraph_linearLayout);
        dailyGraph_linearLayout = view.findViewById(R.id.dailyGraph_linearLayout);
        dayButtons_linearLayout = view.findViewById(R.id.dayButtons_linearLayout);
        dailyOutlook_linearLayout = view.findViewById(R.id.dailyOutlook_linearLayout);
        sunPosition_linearLayout = view.findViewById(R.id.sunPosition_linearLayout);
        uvIndexGraph_linearLayout = view.findViewById(R.id.uvIndexGraph_linearLayout);
        moonPhase_linearLayout = view.findViewById(R.id.moonPhase_linearLayout);
        summaryDailyButtons_linearLayout = view.findViewById(R.id.summaryDailyButtons_linearLayout);
        weatherAlertScrollable = view.findViewById(R.id.alertScrollingText_linearLayout);
        dailyGraphLegend_linearLayout = view.findViewById(R.id.dailyGraphLegend_linearLayout);
        citySearch_relativeLayout = view.findViewById(R.id.citySearch_relativeLayout);
        dailyGraphLegend_horizontalScrollView = view.findViewById(R.id.dailyGraphLegend_horizontalScrollView);
        mainScreen_scrollView = view.findViewById(R.id.homeFrag_scrollView);
        horizontalGraphDatesScroll = view.findViewById(R.id.horizontalGraphDates_horizontalScrollView);

        comfortIndex_progressBar = view.findViewById(R.id.comfortIndex_progressBar);
        dayProgress_progressBar = view.findViewById(R.id.dayProgress_progressBar);
        minMaxTemp_progressBar = view.findViewById(R.id.minMaxTemp_progressBar);

        radarView = view.findViewById(R.id.homeRadarView_webView);

        minutelyGraph = view.findViewById(R.id.minutely_graph);
        forecastGraph = view.findViewById(R.id.daily_graph);
        uvGraph = view.findViewById(R.id.uvIndex_graph);


        mDatabaseHelper = new DatabaseHelper(getActivity());
        mAutocompleteDatabaseHelper = new AutocompleteDatabaseHelper(getActivity());
        mWeatherDataDatabaseHelper = new WeatherDataDatabaseHelper(getActivity());
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);


        final Cursor data = mAutocompleteDatabaseHelper.getData();
        while (data.moveToNext()) {
            cityArrayList.add(data.getString(1));
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityArrayList);
        citySearch_editText.setThreshold(1);
        citySearch_editText.setAdapter(adapter);

        citySearch_editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                geoLocate(true);
                closeKeyboard();
            }
        });

        //INITIALIZES WEATHER DATA and LOCATION info
        init();


        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        clearSearch_imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!(citySearch_editText.getText().equals(""))) {
                    citySearch_editText.setText("");
                } else {
                    toastMessage("Can not clear text.");
                }
            }
        });

        //GETS USER LOCATION WHEN ICON CLICKED
        location_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();

                if (mLocationPermissionsGranted) {
                    getDeviceLocation();

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    //mMap.setMyLocationEnabled(true);
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);

                }
            }
        });
        //OPENS MAP VIEW FRAGMENT TO VIEW LARGER RADAR
        radarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().hide(MainActivity.active).show(MainActivity.mapviewFragment).commit();


                MainActivity.active = MainActivity.mapviewFragment;

                Log.d(TAG, "Map Selected");
                return false;
            }
        });

        final SwipeRefreshLayout pullToRefresh = getActivity().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                geoLocate(false);

                pullToRefresh.setRefreshing(false);
            }
        });

        //WEATHER ALERT SCROLLABLE AT TOP OF SCREEN
        weatherAlertScrollable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openWeatherAlertDialog(weather.alerts.getTitle().get(0).toString(), "", Integer.parseInt(weather.alerts.getExpireTime().get(0).toString()), weather.alerts.getDescription().get(0).toString(), getAlertIcon(weather.alerts.getSeverity().get(0).toString()), "", "", "");

            }
        });

        //BUTTON FOR EXTEND HOURLY FORECAST
        extendHourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExtendHourlyActivity.class);
                startActivity(intent);
                Log.d(TAG, "OPENING Extended Hourly Activity");
            }
        });

        //BUTTON FOR ATTRIBUTION WEBSITE
        poweredDarkSky_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    Log.d(TAG, "onClick: inTryBrowser");
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.e(TAG, "onClick: in inCatchBrowser", ex);
                    intent.setPackage(null);
                    startActivity(intent.createChooser(intent, "Select Browser"));
                }
            }
        });


    }

    public void openWeatherAlertDialog(String title, String expire, int expireUNIX, String desc, int icon, String geofence, String color, String id) {
        weatherAlertInstance(title, expire, expireUNIX, desc, icon, geofence, color, id).show(getActivity().getSupportFragmentManager(), "weather alert dialog");
    }

    public void openHourlyInfoDialog(int i, boolean isTimeRequest) {
        hourlyInfoInstance(i, isTimeRequest).show(getActivity().getSupportFragmentManager(), "hourly info dialog");
    }

    public WeatherAlertDialog weatherAlertInstance(String title, String expire, int expireUNIX, String desc, int icon, String geofence, String color, String id) {
        WeatherAlertDialog weatherAlertDialog = new WeatherAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("expire", expire);
        args.putInt("expireUNIX", expireUNIX);
        args.putString("desc", desc);
        args.putInt("icon", icon);
        args.putString("geofence", geofence);
        args.putString("color", color);
        args.putString("id", id);

        weatherAlertDialog.setArguments(args);
        return weatherAlertDialog;
    }

    public static HourlyInfoDialog hourlyInfoInstance(int i, boolean isTimeRequest) {
        HourlyInfoDialog hourlyInfoDialog = new HourlyInfoDialog();
        Bundle args = new Bundle();
        args.putInt("num", i);
        args.putBoolean("isTimeRequest", isTimeRequest);
        hourlyInfoDialog.setArguments(args);
        return hourlyInfoDialog;
    }


    public void AddData(String newEntry, String isFavorite) {
        boolean insertData = mDatabaseHelper.addData(newEntry, isFavorite);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Possible Error");
        }
    }

    public void AddAutocomleteData(String city) {
        boolean insertData = mAutocompleteDatabaseHelper.addData(city);

        if (insertData) {
            //toastMessage("Data Successfully Inserted!");
        } else {
            //toastMessage("Possible Error");
        }
    }


    public void RenderWeatherData(String city) {

        JSONWeatherTask task = new JSONWeatherTask();

        cityHasQueried = false;
        task.execute(new String[]{city});
        CreateWeatherMap(weather.location.getLatitude(), weather.location.getLongitude());
        weatherAlertScrollable.setVisibility(View.GONE);

        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), "UNABLE TO CONNECT TO NETWORK", Toast.LENGTH_SHORT);
            Log.d(TAG, "UNABLE TO CONNECT");
        }
    }

    public void RenderTimeWeatherData(String city, String unix) {
        JSONTimeWeatherTask task = new JSONTimeWeatherTask();
        task.execute(new String[]{city, unix});

    }


    private class JSONTimeWeatherTask extends AsyncTask<String, Void, TimeWeather> {

        @Override
        protected TimeWeather doInBackground(String... params) {
            String dataTimeWeather = null;
            try {
                dataTimeWeather = ((new TimeWeatherHttpClient().getTimeWeatherData(params[0], params[1])));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                timeWeather = JSONTimeWeatherParser.getWeather(dataTimeWeather);

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return timeWeather;
        }


        @TargetApi(Build.VERSION_CODES.O)
        @Override
        //populate data to show to user
        protected void onPostExecute(final TimeWeather timeWeather) {
            super.onPostExecute(timeWeather);

            openHourlyInfoDialog(0, true);


            Log.d(TAG, "Time Machine: TIME - " + timeWeather.dailyCondition.getTime());

        }

    }


    //makes data retrieval run in background

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            String dataWeather = null;
            try {
                dataWeather = ((new WeatherHttpClient().getWeatherData(params[0])));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                weather = JSONWeatherParser.getWeather(dataWeather);

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return weather;
        }


        @TargetApi(Build.VERSION_CODES.O)
        @Override
        //populate data to show to user
        protected void onPostExecute(final Weather weather) {
            super.onPostExecute(weather);

            initWeatherConditions();


            double temperature = weather.currentCondition.getTemperature();
            final List<Double> moonPhase = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                moonPhase.add(Double.parseDouble(weather.dailyCondition.getMoonPhase().get(i).toString()));
            }

            int currentTime = weather.currentCondition.getTime();

            List precipProbMinutely = weather.minutelyCondition.getPrecipProbability();
            List precipIntensMinutely = weather.minutelyCondition.getPrecipIntensity();

            final List temperatureHourly = weather.hourlyCondition.getTemperature();
            final List apparentTempHourly = weather.hourlyCondition.getApparentTemperature();
            final List dewpointHourly = weather.hourlyCondition.getDewPoint();
            final List cloudcoverHourly = weather.hourlyCondition.getCloudCover();
            final List windspeedHourly = weather.hourlyCondition.getWindSpeed();
            final List precipProbHourly = weather.hourlyCondition.getPrecipProbability();
            final List uvHourly = weather.hourlyCondition.getUvIndex();
            final List comfortIndexHourly = new ArrayList();
            for (int i = 0; i < WEEKLY_HOURS+1; i++) {
                comfortIndexHourly.add(calcComfortIndex(Double.parseDouble(String.valueOf(temperatureHourly.get(i))),
                        Double.parseDouble(String.valueOf(apparentTempHourly.get(i))),
                        Double.parseDouble(String.valueOf(dewpointHourly.get(i))),
                        Double.parseDouble(String.valueOf(windspeedHourly.get(i))),
                        Double.parseDouble(String.valueOf(cloudcoverHourly.get(i))),
                        Double.parseDouble(String.valueOf(precipProbHourly.get(i))),
                        Integer.parseInt(String.valueOf(uvHourly.get(i)))));
            }

            weather.hourlyCondition.setComfortIndex(comfortIndexHourly);
            final List sunsetTime = weather.dailyCondition.getSunsetTime();
            final List sunriseTime = weather.dailyCondition.getSunriseTime();
            final List temperatureHigh = weather.dailyCondition.getTemperatureHigh();
            final List temperatureLow = weather.dailyCondition.getTemperatureLow();
            final List temperatureMax = weather.dailyCondition.getTemperatureMax();
            final List windBearingDaily = weather.dailyCondition.getWindBearing();


            //creates indexes


            GradientDrawable gd = new GradientDrawable();
            gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gd.setShape(GradientDrawable.RING);
            gd.setGradientRadius(.5f);
            gd.setStroke(1, Color.rgb(40, 40, 40));
            gd.setColors(new int[]{getGradientColorMin(), getGradientColorMax()});
            gd.setCornerRadius(0f);


            minMaxTemp_progressBar.setProgressDrawable(gd);


            boolean minuteFlag = false;
            minutelyGraph_linearLayout.setVisibility(View.GONE);
            for (int j = 0; j < 60; j++) {
                if ((Double.parseDouble(precipProbMinutely.get(j).toString()) * 100) >= 10 && (Double.parseDouble(precipIntensMinutely.get(j).toString())) >= .008) {
                    minuteFlag = true;
                }
            }


            calcMoonPhaseTime(moonPhase);

            long sunriseDifference = ((Long.parseLong(sunriseTime.get(1).toString()) - 86400) - (Long.parseLong(sunriseTime.get(0).toString())));
            long sunsetDifference = ((Long.parseLong(sunsetTime.get(1).toString()) - 86400) - (Long.parseLong(sunsetTime.get(0).toString())));


            //CHECKS IF DAY TIME
            if ((Integer.parseInt(sunriseTime.get(0).toString()) < currentTime) && (currentTime < Integer.parseInt(sunsetTime.get(0).toString()))) {
                Drawable progressDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.sun_position_gradient);
                dayProgress_progressBar.setProgressDrawable(progressDrawable);
                int numerator = (Integer.parseInt(sunsetTime.get(0).toString()) - currentTime);
                int denomenator = ((Integer.parseInt(sunsetTime.get(0).toString()) - Integer.parseInt(sunriseTime.get(0).toString())));
                dayProgress_progressBar.setProgress((100 - (numerator * 100) / (denomenator)));
                long daylightSeconds = (Long.parseLong(sunsetTime.get(0).toString())) - (Long.parseLong(sunriseTime.get(0).toString()));
                int hours = (int) Math.floor(daylightSeconds / 3600);
                int remainder = (int) (daylightSeconds - hours * 3600);
                int minutes = remainder / 60;
                String daylightTime = hours + " hours " + minutes + " mins";
                daylightLabel_textView.setText("Daylight");
                daylightHours_textView.setText(daylightTime);
                //Log.d(TAG, "DAY NUMERATOR: " + numerator);
                //Log.d(TAG, "DAY DENOMENATOR: " + denomenator);
                checkDaylightHours();

            } else {
                Drawable progressDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.night_position_gradient);
                dayProgress_progressBar.setProgressDrawable(progressDrawable);
                long nightTimeSeconds = (Long.parseLong(sunriseTime.get(2).toString()) - (Long.parseLong(sunsetTime.get(1).toString())));
                int hours = (int) Math.floor(nightTimeSeconds / 3600);
                int remainder = (int) (nightTimeSeconds - hours * 3600);
                int minutes = remainder / 60;
                String nightTime = hours + " hours " + minutes + " mins";
                daylightLabel_textView.setText("Night");
                daylightHours_textView.setText(nightTime);
                checkDaylightHours();
                int numerator = 1;
                int denomentator = 1;
                if ((currentTime - (Integer.parseInt(sunsetTime.get(1).toString()) - 86400)) < 0) {
                    numerator = currentTime - (Integer.parseInt(sunsetTime.get(0).toString()) - 86400);
                } else {
                    numerator = currentTime - (Integer.parseInt(sunsetTime.get(1).toString()) - 86400);
                }
                if (((Integer.parseInt(sunriseTime.get(2).toString()) - Integer.parseInt(sunsetTime.get(1).toString()))) != 0) {
                    denomentator = ((Integer.parseInt(sunriseTime.get(2).toString()) - Integer.parseInt(sunsetTime.get(1).toString())));
                    daylightHours_textView.setVisibility(View.VISIBLE);
                } else {
                    denomentator = 1;
                    daylightHours_textView.setVisibility(View.GONE);
                }

                //Log.d(TAG, "NIGHT NUMERATOR: " + numerator);
                //Log.d(TAG, "NIGHT DENOMENATOR: " + denomentator);
                if (((numerator * 100) / (denomentator)) > 100) {
                    dayProgress_progressBar.setProgress(100);
                } else {
                    dayProgress_progressBar.setProgress(((numerator * 100) / (denomentator)));
                }
            }

            comfortIndex_progressBar.setProgress((int) (Double.parseDouble(comfortIndexHourly.get(0).toString())));
            minMaxTemp_progressBar.setMin((int) Double.parseDouble(weather.dailyCondition.getTemperatureMin().get(0).toString()) - 1);
            double minMaxUpperBound = (Double.parseDouble(weather.dailyCondition.getTemperatureMax().get(0).toString()));
            minMaxTemp_progressBar.setMax((int) (minMaxUpperBound + (0.3 * (minMaxUpperBound - minMaxTemp_progressBar.getMin()))) + 1);
            //minMaxTemp_progressBar.setProgress((int) weather.currentCondition.getTemperature());
            minMaxTemp_progressBar.setProgress((int) minMaxTemp_progressBar.getMax());


            final List sunriseDateList = new ArrayList<>();
            final List sunsetDateList = new ArrayList<>();
            final List dailyDateSummaryList = new ArrayList<>();
            final long offsetSeconds = (long) weather.location.getOffset() * 3600 + CURRENT_TIMEZONE_OFFSET;

            for (int i = 0; i < 8; i++) {
                Date dailyDateSummary = new Date(((Long.parseLong(weather.dailyCondition.getTime().get(i).toString()))) * 1000);
                int daySummaryDate = Integer.parseInt(new SimpleDateFormat("d", Locale.getDefault()).format(dailyDateSummary));
                String summaryTitleDate = new SimpleDateFormat("EEEE d", Locale.getDefault()).format(dailyDateSummary) + getDayNumberSuffix(daySummaryDate);

                Date str1 = new Date((Long.parseLong(sunriseTime.get(i).toString()) + offsetSeconds) * 1000);
                Date str2 = new Date((Long.parseLong(sunsetTime.get(i).toString()) + offsetSeconds) * 1000);

                String sunriseamPM = new SimpleDateFormat("a", Locale.getDefault()).format(str1);
                String sunsetamPM = new SimpleDateFormat("a", Locale.getDefault()).format(str2);
                String sunriseDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(str1) + morningEveningSuffix(sunriseamPM);
                String sunsetDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(str2) + morningEveningSuffix(sunsetamPM);

                sunriseDateList.add(sunriseDate);
                sunsetDateList.add(sunsetDate);
                dailyDateSummaryList.add(summaryTitleDate);


            }
            weather.dailyCondition.setSunriseDateList(sunriseDateList);
            weather.dailyCondition.setSunsetDateList(sunsetDateList);
            Date str3 = new Date(((long) (currentTime)) * 1000);
            String updateDate = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(str3);


            double maxDailyTemp = Double.parseDouble((temperatureHigh.get(0)).toString());
            double minDailyTemp = Double.parseDouble((temperatureLow.get(0)).toString());
            double tomForcastMaxTemp = Double.parseDouble((temperatureHigh.get(1)).toString());

            SpannableString ss1 = new SpannableString(" Tomorrow will be much cooler.");
            SpannableString ss2 = new SpannableString(" Tomorrow will be cooler.");
            SpannableString ss3 = new SpannableString(" Tomorrow will be warmer.");
            SpannableString ss4 = new SpannableString(" Tomorrow will be much warmer.");

            ForegroundColorSpan fcsMuchCool = new ForegroundColorSpan(Color.rgb(0, 68, 179));
            ForegroundColorSpan fcsCool = new ForegroundColorSpan(Color.rgb(66, 158, 245));
            ForegroundColorSpan fcsWarm = new ForegroundColorSpan(Color.rgb(255, 168, 168));
            ForegroundColorSpan fcsMuchWarm = new ForegroundColorSpan(Color.rgb(255, 0, 0));

            if (minuteFlag) {
                description_textView.setText(weather.minutelyCondition.getSummary());

            } else {
                description_textView.setText(weather.hourlyCondition.getFullSummary());
            }
            if (tomForcastMaxTemp - maxDailyTemp < -10) {
                ss1.setSpan(fcsMuchCool, 18, 18 + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                description_textView.append(ss1);
            } else if (tomForcastMaxTemp - maxDailyTemp < -3) {
                ss2.setSpan(fcsCool, 18, 18 + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                description_textView.append(ss2);
            } else if ((tomForcastMaxTemp - maxDailyTemp) < -2 && 2 > (tomForcastMaxTemp - maxDailyTemp)) {
                description_textView.append(" Tomorrow will have a similar temperature.");
            } else if ((tomForcastMaxTemp - maxDailyTemp) > 10) {
                ss4.setSpan(fcsMuchWarm, 18, 18 + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                description_textView.append(ss4);
            } else if ((tomForcastMaxTemp - maxDailyTemp) > 3) {
                ss3.setSpan(fcsWarm, 18, 18 + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                description_textView.append(ss3);
            } else {
                description_textView.append(" Tomorrow will have a similar temperature.");
            }

            String tempFormat = d0f.format(temperature);
            String minTempFormat = d0f.format(minDailyTemp);
            String maxTempFormat = d0f.format(maxDailyTemp);

            final List minWindSpeed = new ArrayList<>();
            final List dailyWindSpeed = new ArrayList<>();
            final List maxWindSpeed = new ArrayList<>();


            for (int i = 0; i < 7; i++) {
                long minimumData = (Math.round(getDailyWindSpeed(i) / 5.0) * 5) - 5;
                double actualData = Math.round(getDailyWindSpeed(i));
                long maximumData = (Math.round(getDailyWindSpeed(i) / 5.0) * 5) + 5;
                if (minimumData <= 0) {
                    minimumData = 5;
                }
                if (minimumData == maximumData) {
                    maximumData = maximumData + 5;
                }

                minWindSpeed.add(minimumData);
                dailyWindSpeed.add(actualData);
                maxWindSpeed.add(maximumData);
            }

            Log.d(TAG, "onPostExecute: Furthest Reached");

            if (Integer.parseInt(weather.dailyCondition.getSunriseTime().get(0).toString()) == 0) {
                sunrise_textView.setText("No sunrise");
                sunset_textView.setText("No sunset");
                sunriseDifference_textView.setText("Sunrise Difference\n" + "None");
                sunsetDifference_textView.setText("Sunset Difference\n" + "None");
                daylightDifference_textView.setText("Daylight Difference\n" + "None");
            } else {
                if (sunriseDifference > 0) {
                    sunrise_textView.setText("" + sunriseDateList.get(0));
                    sunriseDifference_textView.setText("Sunrise Difference\n" + getMinuteSeconds((int) sunriseDifference));
                } else {
                    sunrise_textView.setText("" + sunriseDateList.get(0));
                    sunriseDifference_textView.setText("Sunrise Difference\n" + getMinuteSeconds((int) sunriseDifference));
                }
                if (sunsetDifference > 0) {
                    sunset_textView.setText("" + sunsetDateList.get(0));
                    sunsetDifference_textView.setText("Sunset Difference\n" + getMinuteSeconds((int) sunsetDifference));
                } else {
                    sunset_textView.setText("" + sunsetDateList.get(0));
                    sunsetDifference_textView.setText("Sunset Difference\n" + getMinuteSeconds((int) sunsetDifference));
                }
                daylightDifference_textView.setText("Daylight Difference\n" + getMinuteSeconds((int) (sunsetDifference - sunriseDifference)));

            }


            //text setters
            weatherAlertScrollable.setVisibility(View.GONE);
            weatherAlertType.setVisibility(View.GONE);
            weatherAlert_textView.setVisibility(View.GONE);
            getWeatherAlertCondition();
            getHourlyTime();

            comfortConditon_textView.setText(ComfortIndexConditions(Double.parseDouble(comfortIndexHourly.get(0).toString())));
            coordText_textView.setText("(" + weather.location.getLatitude() + "," + weather.location.getLongitude() + ")");
            mainScreen_scrollView.setSmoothScrollingEnabled(true);
            currentTemperature_textView.setText(tempFormat + DEG_UNITS);
            maxTemperature_textView.setTextColor(Color.rgb(255, 0, 0));
            maxTemperature_textView.setText(" " + maxTempFormat + "°");
            minTemperature_textView.setTextColor(Color.rgb(0, 0, 255));
            minTemperature_textView.setText("" + minTempFormat + "° ");
            //updated_textView.setText("Last Updated:" + updateDate);
            updated_textView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Last Updated: " + updateDate, Toast.LENGTH_SHORT);
            moonPhase_textView.setText(getMoonPhaseString(moonPhase.get(0)));
            moonPhaseIcon_imageView.setImageResource(Integer.parseInt(MOON_PHASE.get(moonPhase.get(0).toString())));
            moonIllumination_textView.setText("Illumination\n" + calcMoonIllumination(moonPhase.get(0)) + "%");
            currentConditionIcon_imageView.setImageResource(getWeatherConditionIcon(weather.currentCondition.getIcon(), weather.currentCondition.getPrecipIntensity(), weather.currentCondition.getCloudCover()));
            currentIconDesc_textView.setText(getWeatherConditionString(weather.currentCondition.getIcon()));

            //initializes the daily summary information
            setDailySummaryText(0, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);


            objectAnimator = ObjectAnimator.ofInt(comfortIndex_progressBar, "progress", 0, comfortIndex_progressBar.getProgress());
            objectAnimator.setDuration(750).start();
            objectAnimator = ObjectAnimator.ofInt(dayProgress_progressBar, "progress", 0, dayProgress_progressBar.getProgress());
            objectAnimator.setDuration(750).start();





            //GRAPHS

            if (minuteFlag) {
                minutelyGraph_linearLayout.setVisibility(View.VISIBLE);
                List<Entry> minutelyPercChanceData = new ArrayList<>();
                List<Entry> minutelyPercIntensData = new ArrayList<>();
                minutelyGraph.setTouchEnabled(false);
                minutelyGraph.setDragEnabled(false);
                minutelyGraph.setPinchZoom(false);
                minutelyGraph.setDoubleTapToZoomEnabled(false);
                minutelyGraph.setDrawGridBackground(false);
                minutelyGraph.getDescription().setEnabled(false);
                hourlyExpectedAccum_textView.setText("Expected Hourly Accumulation: " + getHourlyExpectedAccum() + " " + PRECIP_ACCUM_UNITS);

                double minutelyPrecipIntensityMax = 0;

                for (int i = 0; i < 30; i++) {
                    minutelyPercChanceData.add(new Entry(i, Float.parseFloat(weather.minutelyCondition.getPrecipProbability().get(2 * i).toString()) * 100));
                    minutelyPercIntensData.add(new Entry(i, Float.parseFloat(weather.minutelyCondition.getPrecipIntensity().get(2 * i).toString())));

                    if (Double.parseDouble((precipIntensMinutely.get(2 * i)).toString()) > minutelyPrecipIntensityMax) {
                        minutelyPrecipIntensityMax = Double.parseDouble((precipIntensMinutely.get(2 * i)).toString());
                    }
                }

                final List<String> horizontalLabelsMinutely = new ArrayList<>();
                Date stringDateMinutely;
                String minuteDate;
                String minuteDateAMPM;
                for (int i = 0; i < 7; i++) {
                    stringDateMinutely = new Date(Long.parseLong(weather.minutelyCondition.getTime().get(i * 10).toString()) * 1000);
                    minuteDateAMPM = new SimpleDateFormat("a", Locale.getDefault()).format(stringDateMinutely);
                    minuteDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(stringDateMinutely) + morningEveningSuffix(minuteDateAMPM);

                    horizontalLabelsMinutely.add(minuteDate);
                }

                minutelyGraphTimeLabels_textView.setText("        " + horizontalLabelsMinutely.get(0) + "          " + horizontalLabelsMinutely.get(1) + "                " + horizontalLabelsMinutely.get(2) + "                 " + horizontalLabelsMinutely.get(3) + "                 " + horizontalLabelsMinutely.get(4) + "                 " + horizontalLabelsMinutely.get(5) + "          " + horizontalLabelsMinutely.get(6));
                //Log.d(TAG, "MINUTELY INTENSITY: " + weather.minutelyCondition.getPrecipIntensity());
                LineDataSet minutelyPrecipChance = new LineDataSet(minutelyPercChanceData, "Percent (%)"); // add entries to dataset
                LineDataSet minutelyPrecipIntens = new LineDataSet(minutelyPercIntensData, "Intensity (" + RAIN_UNITS + ")"); // add entries to dataset

                graphStyleInit(minutelyPrecipChance, 64, 159, 255, true, false);
                minutelyPrecipChance.setLineWidth(1.5f);
                graphStyleInit(minutelyPrecipIntens, 17, 44, 87, false, false);
                minutelyPrecipIntens.setLineWidth(1.5f);
                minutelyPrecipChance.setValueTextColor(Color.argb(0, 0, 0, 0));
                minutelyPrecipIntens.setValueTextColor(Color.argb(0, 0, 0, 0));


//                minutelyPrecipChance.setColor(Color.rgb(64, 159, 255));
//                minutelyPrecipIntens.setColor(Color.rgb(17, 44, 87));
//                minutelyPrecipChance.setCircleColor(Color.rgb(64, 159, 255));
//                minutelyPrecipIntens.setCircleColor(Color.argb(150, 17, 44, 87));
//                minutelyPrecipChance.setCircleHoleColor(Color.rgb(64, 159, 255));
//                minutelyPrecipIntens.setCircleHoleColor(Color.rgb(17, 44, 87));
//                minutelyPrecipChance.setCircleRadius(1f);
//                minutelyPrecipIntens.setCircleRadius(1f);
//                minutelyPrecipChance.setMode(LineDataSet.Mode.LINEAR);
//                minutelyPrecipIntens.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                minutelyPrecipChance.setAxisDependency(YAxis.AxisDependency.LEFT);
                minutelyPrecipIntens.setAxisDependency(YAxis.AxisDependency.RIGHT);
                minutelyPrecipChance.setDrawFilled(true);
                if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                    minutelyPrecipChance.setFillDrawable(drawable);
                } else {
                    minutelyPrecipChance.setFillColor(Color.argb(150, 64, 159, 255));
                }

                //minutelyPrecipChance.setValueTextColor(Color.argb(0, 0, 0, 0));
                //minutelyPrecipIntens.setValueTextColor(Color.argb(0, 0, 0, 0));

                YAxis minutelyRightAxis = minutelyGraph.getAxisRight();
                minutelyRightAxis.setDrawGridLines(false);
                minutelyRightAxis.setGranularity((float) (.20f * minutelyPrecipIntensityMax));
                minutelyRightAxis.setLabelCount(5);
                minutelyRightAxis.setAxisMinimum(0f);
                minutelyRightAxis.setAxisMaximum((float) minutelyPrecipIntensityMax + (float) (0.1f * minutelyPrecipIntensityMax));
                minutelyRightAxis.setTextSize(8f);

                YAxis minutelyLeftAxis = minutelyGraph.getAxisLeft();
                minutelyLeftAxis.setDrawGridLines(true);
                minutelyLeftAxis.setGranularity(10f);
                minutelyLeftAxis.setAxisMinimum(0f);
                minutelyLeftAxis.setAxisMaximum(100f);
                minutelyLeftAxis.setTextSize(8f);

                XAxis minutelyxAxis = minutelyGraph.getXAxis();
                minutelyxAxis.setLabelCount(6);
                minutelyxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                minutelyxAxis.setAxisMinimum(0);
                minutelyxAxis.setAxisMaximum(29);
                minutelyxAxis.setTextColor(Color.argb(0, 0, 0, 0));


                //Log.d(TAG, "MINUTELY TIMES: " + horizontalLabelsMinutely);
                LineData minutelyGraphData = new LineData(minutelyPrecipChance, minutelyPrecipIntens);
                minutelyGraph.setData(minutelyGraphData);
                minutelyGraph.animateX(1000);
                minutelyGraph.invalidate(); // refresh
            }


            boolean uvGraphFlag = true;

            if (uvGraphFlag) {
                List<Entry> hourlyUVIndex = new ArrayList<>();
                uvGraph.setTouchEnabled(true);
                uvGraph.setDragEnabled(true);
                uvGraph.setPinchZoom(false);
                uvGraph.setDoubleTapToZoomEnabled(false);


                uvGraph.setDrawGridBackground(false);
                uvGraph.getDescription().setEnabled(false);
                uvGraph.getLegend().setEnabled(false);
                

                int maxUV = 0;
                for (int i = 0; i < WEEKLY_HOURS; i++) {
                    hourlyUVIndex.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getUvIndex().get(i).toString())));
                    if (Integer.parseInt(weather.hourlyCondition.getUvIndex().get(i).toString()) > maxUV) {
                        maxUV = Integer.parseInt(weather.hourlyCondition.getUvIndex().get(i).toString());
                    }
                }

                final List<String> horizontalLabelsUV = new ArrayList<>();
                Date stringDateUV;
                String UVDate;
                String UVDateAMPM;


                //uvGraphTimeLabels_textView.setText(""  + horizontalLabelsMinutely.get(0) + "          " + horizontalLabelsMinutely.get(1) + "             " + horizontalLabelsMinutely.get(2) + "              " + horizontalLabelsMinutely.get(3) + "              " + horizontalLabelsMinutely.get(4)+ "               " + horizontalLabelsMinutely.get(5) + "          " + horizontalLabelsMinutely.get(6));
                LineDataSet hourlyUV = new LineDataSet(hourlyUVIndex, "UV Index");

                graphStyleInit(hourlyUV, 0, 0, 0, true, true);
                hourlyUV.setValueTextColor(Color.argb(0, 0, 0, 0));
                hourlyUV.setCircleRadius(1);
                hourlyUV.setLineWidth(1.5f);

                if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.uv_gradient);
                    hourlyUV.setFillDrawable(drawable);
                } else {
                    hourlyUV.setFillColor(Color.argb(150, 255, 100, 0));
                }

                //hourlyUV.setValueTextColor(Color.argb(0, 0, 0, 0));


                YAxis uvLeftAxis = uvGraph.getAxisLeft();
                uvLeftAxis.setDrawGridLines(true);
                uvLeftAxis.setGranularity(1f);
                uvLeftAxis.setAxisMinimum(0.01f);
                uvLeftAxis.setAxisMaximum(maxUV+1);
                uvLeftAxis.setTextSize(8f);

                YAxis uvRightAxis = uvGraph.getAxisRight();
                uvRightAxis.setTextColor(Color.argb(0, 0, 0, 0));


                XAxis xAxis = uvGraph.getXAxis();
                xAxis.setLabelCount(6);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setAxisMinimum(0);
                xAxis.setAxisMaximum(WEEKLY_HOURS-1);
                xAxis.setTextColor(Color.argb(0, 0, 0, 0));
                xAxis.setDrawGridLines(false);

                hourlyUV.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineData uvGraphData = new LineData(hourlyUV);
                uvGraph.setData(uvGraphData);
                uvGraph.setVisibleXRange(0, WEEKLY_HOURS/3);    //56 hours
                uvGraph.animateX(1000);
                uvGraph.invalidate(); // refresh



            }


            boolean forecastGraphFlag = true;

            horizontalGraphDatesScroll.setVisibility(View.GONE);

            List<Entry> hourlyTemperature = new ArrayList<>();
            List<Entry> hourlyApparent = new ArrayList<>();
            List<Entry> hourlyPrecip = new ArrayList<>();
            List<Entry> hourlyDewpoint = new ArrayList<>();
            List<Entry> hourlyHumidity = new ArrayList<>();
            List<Entry> hourlyComfortIndex = new ArrayList<>();
            List<Entry> hourlyCloudCover = new ArrayList<>();
            List<Entry> hourlyWindspeed = new ArrayList<>();


            forecastGraph.setTouchEnabled(true);
            forecastGraph.setDragEnabled(true);
            forecastGraph.setPinchZoom(false);
            forecastGraph.setDoubleTapToZoomEnabled(false);
            forecastGraph.setScaleEnabled(false);
            forecastGraph.setDragDecelerationEnabled(true);
            forecastGraph.setDragDecelerationFrictionCoef(0.95f);


            forecastGraph.setDrawGridBackground(false);
            forecastGraph.getDescription().setEnabled(false);
            forecastGraph.getLegend().setEnabled(false);


            double maxTemperature = 0;
            double minTemperature = 999;

            List hourlyGraphDates = new ArrayList();
            List hourlyTime = new ArrayList();
            hourlyDates_textView.setText("");
            horizontalGraphDatesScroll.setSmoothScrollingEnabled(true);
            for (int i = 0; i < WEEKLY_HOURS; i++) {
                hourlyPrecip.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getPrecipProbability().get(i).toString()) * 100));
                hourlyTemperature.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getTemperature().get(i).toString())));
                hourlyApparent.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getApparentTemperature().get(i).toString())));
                hourlyDewpoint.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getDewPoint().get(i).toString())));
                hourlyHumidity.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getHumidity().get(i).toString()) * 100));
                hourlyComfortIndex.add(new Entry(i, Float.parseFloat(comfortIndexHourly.get(i).toString())));
                hourlyCloudCover.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getCloudCover().get(i).toString()) * 100));
                hourlyWindspeed.add(new Entry(i, Float.parseFloat(weather.hourlyCondition.getWindSpeed().get(i).toString())));

                hourlyGraphDates.add(utils.getHourlyTime().get(i) + "    ");
                hourlyDates_textView.append("" + hourlyGraphDates.get(i));

                if (Double.parseDouble((apparentTempHourly.get(i)).toString()) > maxTemperature) {
                    maxTemperature = Double.parseDouble((apparentTempHourly.get(i)).toString());
                } else if (Double.parseDouble((apparentTempHourly.get(i)).toString()) < minTemperature) {
                    minTemperature = Double.parseDouble((apparentTempHourly.get(i)).toString());
                }
                if (Double.parseDouble((temperatureHourly.get(i)).toString()) > maxTemperature) {
                    maxTemperature = Double.parseDouble((temperatureHourly.get(i)).toString());
                } else if (Double.parseDouble((temperatureHourly.get(i)).toString()) < minTemperature) {
                    minTemperature = Double.parseDouble((temperatureHourly.get(i)).toString());
                }
            }
            //Log.d(TAG, "hourly Time: " + utils.getHourlyTime());
            minTemperature = (Math.round(minTemperature / 5.0) * 5) - 5;
            maxTemperature = (Math.round(maxTemperature / 5.0) * 5) + 5;
            if (minTemperature == maxTemperature) {
                maxTemperature = maxTemperature + 10;
            }

            final List<String> horizontalLabelsUV = new ArrayList<>();
            Date stringDateforecast;
            String forecastDate;
            String forecastDateAMPM;
//                for (int i = 0; i < 7; i++) {
//                    stringDateMinutely = new Date(Long.parseLong(weather.minutelyCondition.getTime().get(i * 10).toString()) * 1000);
//                    minuteDateAMPM = new SimpleDateFormat("a", Locale.getDefault()).format(stringDateMinutely);
//                    minuteDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(stringDateMinutely) + morningEveningSuffix(minuteDateAMPM);
//
//                    horizontalLabelsMinutely.add(minuteDate);
//                }

            //uvGraphTimeLabels_textView.setText(""  + horizontalLabelsMinutely.get(0) + "          " + horizontalLabelsMinutely.get(1) + "             " + horizontalLabelsMinutely.get(2) + "              " + horizontalLabelsMinutely.get(3) + "              " + horizontalLabelsMinutely.get(4)+ "               " + horizontalLabelsMinutely.get(5) + "          " + horizontalLabelsMinutely.get(6));
            final LineDataSet forecastPrecip = new LineDataSet(hourlyPrecip, "Precip Chance");
            final LineDataSet forecastTemperature = new LineDataSet(hourlyTemperature, "Temperature");
            final LineDataSet forecastApparent = new LineDataSet(hourlyApparent, "Feels Like");
            final LineDataSet forecastDewpoint = new LineDataSet(hourlyDewpoint, "Dewpoint");
            final LineDataSet forecastHumidity = new LineDataSet(hourlyHumidity, "Humidity");
            final LineDataSet forecastCloud = new LineDataSet(hourlyCloudCover, "Cloud Cover");
            final LineDataSet forecastComfort = new LineDataSet(hourlyComfortIndex, "Comfort Index");
            final LineDataSet forecastWind = new LineDataSet(hourlyWindspeed, "Wind Speed");


            graphStyleInit(forecastPrecip, 64, 159, 255, true, false);
            forecastPrecip.setLineWidth(1.5f);
            graphStyleInit(forecastTemperature, 255, 0, 0, false, false);
            forecastTemperature.setLineWidth(1.5f);
            graphStyleInit(forecastApparent, 255, 118, 118, false, false);
            forecastApparent.setLineWidth(1.5f);
            graphStyleInit(forecastDewpoint, 5, 102, 0, false, false);
            forecastDewpoint.setLineWidth(1.5f);
            graphStyleInit(forecastHumidity, 16, 172, 199, false, false);
            forecastHumidity.setLineWidth(1.5f);
            graphStyleInit(forecastCloud, 150, 150, 150, false, false);
            forecastCloud.setLineWidth(1.5f);
            graphStyleInit(forecastComfort, 206, 197, 24, false, false);
            forecastComfort.setLineWidth(1.5f);
            graphStyleInit(forecastWind, 40, 40, 40, false, false);
            forecastWind.setLineWidth(1.5f);


            forecastTemperature.setValueTextColor(Color.argb(100, 50, 0, 0));
            forecastTemperature.setValueTextSize(8);
            forecastPrecip.setValueTextColor(Color.argb(100, 0, 0, 50));
            forecastPrecip.setValueTextSize(8);
            forecastWind.setValueTextColor(Color.argb(100, 0, 0, 0));
            forecastWind.setValueTextSize(8);


            if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                forecastPrecip.setFillDrawable(drawable);
            } else {
                forecastPrecip.setFillColor(Color.argb(150, 255, 100, 0));
            }


            YAxis forecastLeftAxis = forecastGraph.getAxisLeft();
            forecastLeftAxis.setDrawGridLines(false);
            //forecastLeftAxis.setGranularity((float) (5f));
            forecastLeftAxis.setLabelCount(5);
            forecastLeftAxis.setAxisMinimum((float) minTemperature);
            forecastLeftAxis.setAxisMaximum((float) maxTemperature);
            forecastLeftAxis.setTextSize(8f);

            YAxis forecastRightAxis = forecastGraph.getAxisRight();
            forecastRightAxis.setDrawGridLinesBehindData(true);
            forecastRightAxis.setGranularity(10f);
            forecastRightAxis.setAxisMinimum(0f);
            forecastRightAxis.setAxisMaximum(100f);
            forecastRightAxis.setTextSize(8f);

            final XAxis forecastxAxis = forecastGraph.getXAxis();
            forecastxAxis.setLabelCount(6);
            forecastxAxis.setDrawGridLinesBehindData(true);
            forecastxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            forecastxAxis.setAxisMinimum(0);
            forecastxAxis.setAxisMaximum(167);
            forecastxAxis.setTextColor(Color.argb(0, 0, 0, 0));

            forecastTemperature.setAxisDependency(YAxis.AxisDependency.LEFT);
            forecastApparent.setAxisDependency(YAxis.AxisDependency.LEFT);
            forecastDewpoint.setAxisDependency(YAxis.AxisDependency.LEFT);

            forecastHumidity.setAxisDependency(YAxis.AxisDependency.RIGHT);
            forecastCloud.setAxisDependency(YAxis.AxisDependency.RIGHT);
            forecastComfort.setAxisDependency(YAxis.AxisDependency.RIGHT);
            forecastPrecip.setAxisDependency(YAxis.AxisDependency.RIGHT);
            forecastWind.setAxisDependency(YAxis.AxisDependency.RIGHT);


            final LineData forecastGraphData = new LineData(
                     forecastPrecip
                    ,forecastTemperature
                    ,forecastApparent
                    ,forecastDewpoint
                    ,forecastHumidity
                    ,forecastComfort
                    ,forecastCloud
                    ,forecastWind

            );
            forecastApparent.setVisible(false);
            forecastDewpoint.setVisible(false);
            forecastHumidity.setVisible(false);
            forecastComfort.setVisible(false);
            forecastCloud.setVisible(false);
            forecastWind.setVisible(false);


            refreshDailyGraph();
            utils.setGraphZoomed(false);
            horizontalGraphDatesScroll.setVisibility(View.GONE);
            horizontalGraphDatesScroll.scrollTo(0, 0);

            forecastGraph.setData(forecastGraphData);
            forecastGraph.animateX(1000);
            forecastGraph.getData().setHighlightEnabled(false);
            hourlyDates_textView.setTextScaleX(.9875f);
            //.985 BOUNDS

            //.9885 BEST
            //.989
            //.9915
            //Log.d(TAG, "HOURLY DATES SCALE: " + hourlyDates_textView.getTextScaleX());

            //MarkerView marker = new LineGraphMarkerView();
            //forecastGraph.setMarker(marker);

            forecastGraph.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    //Log.d(TAG, "onChartGestureStart");
                    return;
                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    //Log.d(TAG, "onChartGestureEnd");
                    return;
                }

                @Override
                public void onChartLongPressed(MotionEvent me) {
                    //Log.d(TAG,"onChartLongPressed");
                    return;
                }

                @Override
                public void onChartDoubleTapped(MotionEvent me) {
                    Log.d(TAG, "onChartDoubleTapped");
                    return;
                }

                @Override
                public void onChartSingleTapped(MotionEvent me) {
                    Log.d(TAG, "onChartSingleTapped");
                    if (!utils.isGraphZoomed) {
                        forecastGraph.moveViewToX(0);
                        forecastGraph.setVisibleXRange(0, 12);
                        forecastGraph.animateX(500);
                        forecastxAxis.setLabelCount(12);
                        forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(0));
                        timeLabels_textView.setVisibility(View.GONE);
                        horizontalGraphDatesScroll.setVisibility(View.VISIBLE);
                        forecastGraph.invalidate(); // refresh
                        utils.setGraphZoomed(true);
                    }

                    return;
                }

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                    //Log.d(TAG,"onChartFling");
                }

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                    //Log.d(TAG, "onChartScale");
                    return;
                }

                double tempXValue = 0;
                float floatXValue;
                int lowestXValue;

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {
                    if (utils.isGraphZoomed == true) {
                        floatXValue = forecastTemperature.getEntryForXValue((float) lowestXValue, 1).getX();
                        lowestXValue = Math.round(forecastGraph.getLowestVisibleX());

                        horizontalGraphDatesScroll.scrollTo(Math.round(95 * floatXValue), 0);
                        //horizontalGraphDatesScroll.scrollTo(Math.round(95 * floatXValue), 0);

                        if (tempXValue != lowestXValue) {
                            if (lowestXValue > WEEKLY_HOURS) {
                                lowestXValue = WEEKLY_HOURS;
                                tempXValue = lowestXValue;
                            }
                            //Log.d(TAG, "onChartTranslate: " + lowestXValue);
                            //Log.d(TAG, "onChartTranslate: " + lowestXValue + "     " + forecastTemperature.getEntryForXValue((float) lowestXValue, 1).getY() + DEG_UNITS + "   " + forecastApparent.getEntryForXValue((float) lowestXValue, 1).getY() + DEG_UNITS + "   " + forecastPrecip.getEntryForXValue((float) lowestXValue, 1).getY() + "%   ");
                            tempXValue = lowestXValue;
                            updateGraphLayout(lowestXValue);
                        }
                    }
                    return;
                }
            });

            //Log.d(TAG, "MAX SCROLL LENGTH: " + horizontalGraphDatesScroll.getMaxScrollAmount());
            //Log.d(TAG, "HOURLY DATES LENGTH: " + hourlyGraphDates.toString().length());
            forecastGraph.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    mainScreen_scrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });




            forecastGraph.invalidate(); // refresh

            showGraphs_button.setOnClickListener(new View.OnClickListener() {

                boolean apparentFlag = true;
                boolean dewFlag = true;
                boolean humidityFlag = true;
                boolean comFlag = true;
                boolean cloudFlag = true;
                boolean windFlag = true;

                @Override
                public void onClick(View v) {
                    if ((apparentTemp_checkBox.isChecked()) && apparentFlag) {
                        forecastApparent.setVisible(true);
                        Log.d(TAG, "Graph: Apparent added");
                        apparentFlag = false;
                    } else if (!apparentTemp_checkBox.isChecked() && !apparentFlag) {
                        forecastApparent.setVisible(false);
                        Log.d(TAG, "Graph: Apparent removed");
                        apparentFlag = true;
                    }
                    if (dewPoint_checkBox.isChecked() && dewFlag) {
                        forecastDewpoint.setVisible(true);
                        Log.d(TAG, "Graph: Dew added");
                        dewFlag = false;
                    } else if (!dewPoint_checkBox.isChecked() && !dewFlag) {
                        forecastDewpoint.setVisible(false);
                        Log.d(TAG, "Graph: Dew removed");
                        dewFlag = true;
                    }
                    if (humidity_checkBox.isChecked() && humidityFlag) {
                        forecastHumidity.setVisible(true);
                        Log.d(TAG, "Graph: Humidity added");
                        humidityFlag = false;
                    } else if (!humidity_checkBox.isChecked() && !humidityFlag) {
                        forecastHumidity.setVisible(false);
                        Log.d(TAG, "Graph: Humidity removed");
                        humidityFlag = true;
                    }
                    if (comfortIndex_checkBox.isChecked() && comFlag) {
                        forecastComfort.setVisible(true);
                        Log.d(TAG, "Graph: Comfort added");
                        comFlag = false;
                    } else if (!comfortIndex_checkBox.isChecked() && !comFlag) {
                        forecastComfort.setVisible(false);
                        Log.d(TAG, "Graph: Comfort removed");
                        comFlag = true;
                    }
                    if (cloudCover_checkBox.isChecked() && cloudFlag) {
                        forecastCloud.setVisible(true);
                        Log.d(TAG, "Graph: Cloud added");
                        cloudFlag = false;
                    } else if (!cloudCover_checkBox.isChecked() && !cloudFlag) {
                        forecastCloud.setVisible(false);
                        Log.d(TAG, "Graph: Cloud removed");
                        cloudFlag = true;
                    }
                    if (windSpeed_checkBox.isChecked() && windFlag) {
                        forecastWind.setVisible(true);
                        Log.d(TAG, "Graph: Wind added");
                        windFlag = false;
                    } else if (!windSpeed_checkBox.isChecked() && !windFlag) {
                        forecastWind.setVisible(false);
                        Log.d(TAG, "Graph: Wind removed");
                        windFlag = true;
                    }
                    forecastGraph.animateX(650);
                }
            });


            horizontalGraphDatesScroll.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            hourlySummary_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyGraph_linearLayout.setVisibility(View.GONE);
                    dayButtons_linearLayout.setVisibility(View.GONE);
                    dailyGraphLegend_linearLayout.setVisibility(View.GONE);
                    hourlySummary_linearLayout.setVisibility(View.VISIBLE);
                    summaryDailyButtons_linearLayout.setVisibility(View.VISIBLE);
                    timeLabels_textView.setVisibility(View.GONE);
                    utils.setGraphZoomed(false);
                    horizontalGraphDatesScroll.setVisibility(View.GONE);


                }
            });

            daily_radioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    refreshDailyGraph();
                    utils.setGraphZoomed(false);
                    horizontalGraphDatesScroll.setVisibility(View.GONE);
                    forecastxAxis.setLabelCount(5);

                }
            });

            initHourlyWeatherConditions();


            final List<String> radioListDate = new ArrayList<>();

            final List<String> summaryRadioListDate = new ArrayList<>();


            Date stringDateRadio;
            String dailyDateRadio;
            String dailySummaryDate;
            int dayRadio;
            for (int i = 0; i < 8; i++) {
                stringDateRadio = new Date(Long.parseLong(weather.dailyCondition.getTime().get(i).toString()) * 1000);
                dayRadio = Integer.parseInt(new SimpleDateFormat("d", Locale.getDefault()).format(stringDateRadio));
                dailyDateRadio = new SimpleDateFormat("E d", Locale.getDefault()).format(stringDateRadio) + getDayNumberSuffix(dayRadio);

                dailySummaryDate = new SimpleDateFormat("E", Locale.getDefault()).format(stringDateRadio);
                summaryRadioListDate.add(dailySummaryDate);
                radioListDate.add(dailyDateRadio);
            }
            utils.setRadioListDate(dailyDateSummaryList);


            summaryDay0_radioButton.setText(summaryRadioListDate.get(0));
            summaryDay1_radioButton.setText(summaryRadioListDate.get(1));
            summaryDay2_radioButton.setText(summaryRadioListDate.get(2));
            summaryDay3_radioButton.setText(summaryRadioListDate.get(3));
            summaryDay4_radioButton.setText(summaryRadioListDate.get(4));
            summaryDay5_radioButton.setText(summaryRadioListDate.get(5));
            summaryDay6_radioButton.setText(summaryRadioListDate.get(6));

            day0RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(0).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(0).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(0).toString())));
            day1RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(1).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(1).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(1).toString())));
            day2RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(2).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(2).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(2).toString())));
            day3RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(3).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(3).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(3).toString())));
            day4RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(4).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(4).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(4).toString())));
            day5RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(5).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(5).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(5).toString())));
            day6RadioIcon_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(6).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(6).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(6).toString())));

            day0_radioButton.setText(radioListDate.get(0) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(0).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(0).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(0)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(0).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(0)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(0)) + " " + PRECIP_ACCUM_UNITS);
            day1_radioButton.setText(radioListDate.get(1) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(1).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(1).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(1)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(1).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(1)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(1)) + " " + PRECIP_ACCUM_UNITS);
            day2_radioButton.setText(radioListDate.get(2) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(2).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(2).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(2)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(2).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(2)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(2)) + " " + PRECIP_ACCUM_UNITS);
            day3_radioButton.setText(radioListDate.get(3) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(3).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(3).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(3)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(3).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(3)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(3)) + " " + PRECIP_ACCUM_UNITS);
            day4_radioButton.setText(radioListDate.get(4) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(4).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(4).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(4)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(4).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(4)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(4)) + " " + PRECIP_ACCUM_UNITS);
            day5_radioButton.setText(radioListDate.get(5) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(5).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(5).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(5)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(5).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(5)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(5)) + " " + PRECIP_ACCUM_UNITS);
            day6_radioButton.setText(radioListDate.get(6) + "\n" + d0f.format(Double.parseDouble(temperatureMax.get(6).toString())) + DEG_UNITS + "\n" + d0f.format(Double.parseDouble(temperatureLow.get(6).toString())) + DEG_UNITS + "\n" + d0f.format(getDailyWindSpeed(6)) + " " + SPEED_UNITS + "\n" + convertDegreeToCardinalDirection(Double.parseDouble(windBearingDaily.get(6).toString())) + "\n" + d0f.format(getDailyMaxPrecipProb(6)) + "%" + "\n" + d2f.format(getPrecipAccumTotal(6)) + " " + PRECIP_ACCUM_UNITS);


            //Log.d(TAG, "VISIBLE RANGE: " + Math.floor((Float.parseFloat(weather.dailyCondition.getTime().get(1).toString()) - (weather.currentCondition.getTime()))/3600));
            day0_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(0));
                    //getHourlyBoundsRounded();
                    utils.setGraphZoomed(false);
                    forecastGraph.moveViewToX(0);
                    forecastGraph.setVisibleXRange(0, getHourlyBoundsRounded());
                    //forecastGraph.setVisibleXRange(0,24);
                    horizontalGraphDatesScroll.setVisibility(View.GONE);
                    forecastxAxis.setLabelCount(5);


                    forecastGraph.animateX(600);
                    timeLabels_textView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "DAY BOUNDS UNROUNDED: " + getHourlyBoundsUnrounded());

                    Date stringDateDaily = null;
                    String DailyDate = null;
                    List horizontalLabelsDaily = new ArrayList<>();
                    Calendar labelTime = Calendar.getInstance();


                    for (int i = 0; i < 5; i++) {
                        stringDateDaily = labelTime.getTime();
                        String amPM = new SimpleDateFormat("a", Locale.getDefault()).format(stringDateDaily);
                        DailyDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(stringDateDaily) + morningEveningSuffix(amPM);
                        horizontalLabelsDaily.add(DailyDate);
                        labelTime.add(Calendar.SECOND, (int) (getHourlyBoundsUnrounded() * 15 * 60) - 8);
                        Log.d(TAG, "LABEL TIME: " + horizontalLabelsDaily.get(i));
                    }
                    timeLabels_textView.setText(" " + horizontalLabelsDaily.get(0) + "                    " + horizontalLabelsDaily.get(1) + "                    " + horizontalLabelsDaily.get(2) + "                  " + horizontalLabelsDaily.get(3) + "                " + horizontalLabelsDaily.get(4));
                }
            });
            day1_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(1, forecastGraph, radioListDate, forecastxAxis);
                }
            });
            day2_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(2, forecastGraph, radioListDate, forecastxAxis);
                }
            });
            day3_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(3, forecastGraph, radioListDate, forecastxAxis);
                }
            });
            day4_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(4, forecastGraph, radioListDate, forecastxAxis);
                }
            });
            day5_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(5, forecastGraph, radioListDate, forecastxAxis);
                }
            });
            day6_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailyGraphRadio(6, forecastGraph, radioListDate, forecastxAxis);
                }
            });


            //RADIO BUTTONS FOR SUMMARY
            summaryDay0_radioButton.setChecked(true);
            summaryDay0_radioButton.toggle();
            summaryDay0_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(0, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay1_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(1, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay2_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(2, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay3_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(3, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay4_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(4, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay5_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(5, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });
            summaryDay6_radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDailySummaryText(6, minWindSpeed, maxWindSpeed, dailyDateSummaryList, sunriseDateList, sunsetDateList, moonPhase);
                }
            });

            cityList_imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CityListActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "OPENING City List");
                }
            });
        }
    }


    private String getMinuteSeconds(int daylightSeconds) {

        String minutesSeconds;
        int numberOfMinutes;
        int numberOfSeconds;

        numberOfMinutes = ((daylightSeconds % 86400) % 3600) / 60;
        numberOfSeconds = ((daylightSeconds % 86400) % 3600) % 60;

        if (numberOfMinutes != 0) {
            minutesSeconds = numberOfMinutes + "m " + numberOfSeconds + "s";
        } else {
            minutesSeconds = numberOfSeconds + "s";

        }
        return minutesSeconds;
    }
    //TODO Fix moon times only adjusting when new moon date is passed

    private void calcMoonPhaseTime(List moonphases) {
        final double fullMoon = 0.50;
        final double newMoon = 1;
        final int secondsPercent = 25514;
        double currentPhase = Double.parseDouble(moonphases.get(0).toString());
        long currentTime = 43200000 / 2 + 1000 * Long.parseLong(String.valueOf(weather.dailyCondition.getTime().get(1)));

        double fullDiffPhase = fullMoon - currentPhase;
        double newDiffPhase = newMoon - currentPhase;

        Date fullMoonDate = new Date(Long.parseLong(String.valueOf(Math.round((fullDiffPhase * secondsPercent * 100) * 1000 + currentTime))));
        Date newMoonDate = new Date(Long.parseLong(String.valueOf(Math.round((newDiffPhase * secondsPercent * 100) * 1000 + currentTime))));
        String fullSuffix = new SimpleDateFormat("d", Locale.getDefault()).format(fullMoonDate);
        String newSuffix = new SimpleDateFormat("d", Locale.getDefault()).format(newMoonDate);

        String fullMoonFormatDate = new SimpleDateFormat("MMMM dd", Locale.getDefault()).format(fullMoonDate) + getDayNumberSuffix(Integer.parseInt(fullSuffix));
        String newMoonFormatDate = new SimpleDateFormat("MMMM dd", Locale.getDefault()).format(newMoonDate) + getDayNumberSuffix(Integer.parseInt(newSuffix));

        nextFullMoon_textView.setText("Next Full Moon\n" + fullMoonFormatDate);
        nextNewMoon_textView.setText("Next New Moon\n" + newMoonFormatDate);

    }

    private void getHourlyTime() {
        Date stringDateHourlyGraph;
        String hourlyGraphDateAMPM = "";
        String hourlyGraphDate = "";
        List<String> hourlyDates = new ArrayList();
        for (int i = 0; i < WEEKLY_HOURS; i++) {
            stringDateHourlyGraph = new Date(Long.parseLong(weather.hourlyCondition.getTime().get(i).toString()) * 1000);
            hourlyGraphDateAMPM = new SimpleDateFormat("a", Locale.getDefault()).format(stringDateHourlyGraph);
            hourlyGraphDate = new SimpleDateFormat("hh", Locale.getDefault()).format(stringDateHourlyGraph) + morningEveningSuffix(hourlyGraphDateAMPM);
            hourlyDates.add(hourlyGraphDate);
        }

        utils.setHourlyTime(hourlyDates);
    }


    private int getGradientColorMin() {

        float colorMin = Float.parseFloat(weather.dailyCondition.getTemperatureMin().get(0).toString());

        int colorMinInt = (int) colorMin;

        colorMinInt = getColorValues(colorMinInt);
        //Log.d(TAG, "getGradientColorMax: " + getColorValues(colorMinInt));
        //Log.d(TAG, "getGradientColorMax: " + colorMinInt);

        return colorMinInt;
    }

    private int getGradientColorMax() {

        float colorMax = Float.parseFloat(weather.dailyCondition.getTemperatureMax().get(0).toString());

        int colorMaxInt = (int) colorMax;

        colorMaxInt = getColorValues(colorMaxInt);

        return colorMaxInt;
    }

    private int getColorValues(int gradientInput) {

        //TODO Change gradient to have smaller increments to give better outlook
        int color = 0;
        if (gradientInput < -10) {
            color = Color.rgb(250, 67, 237);
        } else if (gradientInput < 0) {
            color = Color.rgb(161, 54, 255);
        } else if (gradientInput < 10) {
            color = Color.rgb(76, 56, 255);
        } else if (gradientInput < 20) {
            color = Color.rgb(56, 116, 255);
        } else if (gradientInput < 30) {
            color = Color.rgb(56, 222, 255);
        } else if (gradientInput < 40) {
            color = Color.rgb(43, 255, 167);
        } else if (gradientInput < 50) {
            color = Color.rgb(43, 255, 71);
        } else if (gradientInput < 60) {
            color = Color.rgb(198, 255, 64);
        } else if (gradientInput < 70) {
            color = Color.rgb(252, 255, 64);
        } else if (gradientInput < 80) {
            color = Color.rgb(252, 168, 0);
        } else if (gradientInput < 90) {
            color = Color.rgb(255, 89, 0);
        } else if (gradientInput < 100) {
            color = Color.rgb(181, 21, 0);
        } else if (gradientInput >= 110) {
            color = Color.rgb(97, 13, 13);
        }


        return color;
    }


    public void checkButton(View v) {
        int radioId = daySummary_radioGroup.getCheckedRadioButtonId();

        radioButton = getActivity().findViewById(radioId);
    }


    public String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public double getHourlyExpectedAccum() {
        double accumAdd = 0.0;
        double sum = 0;

        for (int i = 0; i < 60; i++) {
            accumAdd = Double.parseDouble(weather.minutelyCondition.getPrecipIntensity().get(i).toString());
            sum += accumAdd;
        }
        sum = Double.parseDouble(d2f.format(sum / 60));
        Log.d(TAG, "getHourlyExpectedAccum: " + sum + " inches");
        return sum;

    }

    public String morningEveningSuffix(String time) {
        String amPM = "";
        if (time.equals("AM")) {
            amPM = "a";
        } else if (time.equals("PM")) {
            amPM = "p";
        }
        return amPM;
    }


    public int calcMoonIllumination(double moonPhase) {
        int illuminosity = (int) (100 * (0.326 * (1 / (0.13 * (Math.sqrt(2 * Math.PI)))) * Math.pow(Math.E, (-Math.pow((moonPhase - .5), 2)) / (2 * Math.pow(0.13, 2)))));

        return illuminosity;
    }


    public void refreshDailyGraph() {
        dailyGraph_linearLayout.setVisibility(View.VISIBLE);
        dayButtons_linearLayout.setVisibility(View.VISIBLE);
        dailyGraphLegend_linearLayout.setVisibility(View.VISIBLE);
        hourlySummary_linearLayout.setVisibility(View.GONE);
        summaryDailyButtons_linearLayout.setVisibility(View.GONE);
        forecastGraphViewingDate_textView.setText("Weekly Outlook");
        forecastGraph.setVisibleXRange(0, 167);
        forecastGraph.animateX(1000);
        day1_radioButton.setChecked(false);
        timeLabels_textView.setVisibility(View.GONE);
    }

    public void graphStyleInit(LineDataSet id, int r, int g, int b, boolean drawFilled, boolean drawCircles) {
        id.setColor(Color.rgb(r, g, b));
        id.setCircleColor(Color.rgb(r, g, b));
        id.setCircleHoleColor(Color.rgb(r, g, b));
        //id.setValueTextColor(Color.BLACK);
        id.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        id.setDrawFilled(drawFilled);
        id.setDrawCircles(drawCircles);
        id.setValueTextColor(Color.argb(0, 0, 0, 0));
        id.setValueTextSize(0);
    }

    public String ComfortIndexConditions(double comfortIndex) {
        String comfortIndexCondition = "";


        if (comfortIndex >= 96) {
            comfortIndexCondition = "Perfect";
        } else if (comfortIndex >= 90) {
            comfortIndexCondition = "Ideal";
        } else if (comfortIndex >= 84) {
            comfortIndexCondition = "Pleasant";
        } else if (comfortIndex >= 73) {
            comfortIndexCondition = "Tolerable";
        } else if (comfortIndex >= 60) {
            comfortIndexCondition = "Uncomfortable";
        } else if (comfortIndex >= 45) {
            comfortIndexCondition = "Poor";
        } else if (comfortIndex >= 30) {
            comfortIndexCondition = "Very Poor";
        } else if (comfortIndex >= 20) {
            comfortIndexCondition = "Unbearable";
        } else if (comfortIndex < 20) {
            comfortIndexCondition = "Intolerable";
        }

        return comfortIndexCondition;

    }

    public float getHourlyBoundsUnrounded() {
        return ((Float.parseFloat(weather.dailyCondition.getTime().get(1).toString()) - (weather.currentCondition.getTime())) / 3600);
    }

    public float getHourlyBoundsRounded() {
        return (float) Math.floor(getHourlyBoundsUnrounded());
    }

    @Nullable
    public int getWeatherConditionIcon(String icon, double precipIntensity, double cloudCover) {
        int imageResource = 0;

        if (icon.equals("clear-day")) {
            imageResource = R.drawable.ic_sunny;
        } else if (icon.equals("clear-night")) {
            imageResource = R.drawable.ic_night_clear;
        } else if (icon.equals("partly-cloudy-day")) {
            if(cloudCover > .50) {
                imageResource = R.drawable.ic_partly_cloudy;
            } else {
                imageResource = R.drawable.ic_partly_sunny;
            }
        } else if (icon.equals("partly-cloudy-night")) {
            imageResource = R.drawable.ic_night_cloudy;
        } else if (icon.equals("rain")) {
            if (precipIntensity > 0.5) {
                imageResource = R.drawable.ic_heavy_rain;
            } else {
                imageResource = R.drawable.ic_medium_rain;
            }
        } else if (icon.equals("cloudy")) {
            imageResource = R.drawable.ic_cloudy;
        } else if (icon.equals("sleet")) {
            imageResource = R.drawable.ic_sleet;
        } else if (icon.equals("snow")) {
            imageResource = R.drawable.ic_snow;
        } else if (icon.equals("wind")) {
            imageResource = R.drawable.ic_windy;
        } else if (icon.equals("fog")) {
            imageResource = R.drawable.ic_fog;
        }

        return imageResource;
    }

    public String getWeatherConditionString(String icon) {
        String condition = "";
        if (icon.equals("clear-day") || icon.equals("clear-night")) {
            condition = ("Clear");
        } else if (icon.equals("partly-cloudy-day") || icon.equals("partly-cloudy-night")) {
            condition = ("Partly Cloudy");
        } else if (icon.equals("rain")) {
            condition = ("Rainy");
        } else if (icon.equals("cloudy")) {
            condition = ("Cloudy");
        } else if (icon.equals("sleet")) {
            condition = ("Sleety");
        } else if (icon.equals("snow")) {
            condition = ("Snowy");
        } else if (icon.equals("wind")) {
            condition = ("Windy");
        } else if (icon.equals("fog")) {
            condition = ("Foggy");
        } else {
            condition = ("Cloudy");
        }
        return condition;
    }

    public int getWindSpeedIcon(double windspeed) {
        int imageResource = 0;

        //SPEED IS MEASURED IN MPH
        if (windspeed < 1) {
            imageResource = R.drawable.ic_wind_beaufort_0;
        } else if (windspeed <= 3) {
            imageResource = R.drawable.ic_wind_beaufort_1;
        } else if (windspeed <= 7) {
            imageResource = R.drawable.ic_wind_beaufort_2;
        } else if (windspeed <= 12) {
            imageResource = R.drawable.ic_wind_beaufort_3;
        } else if (windspeed <= 18) {
            imageResource = R.drawable.ic_wind_beaufort_4;
        } else if (windspeed <= 24) {
            imageResource = R.drawable.ic_wind_beaufort_5;
        } else if (windspeed <= 31) {
            imageResource = R.drawable.ic_wind_beaufort_6;
        } else if (windspeed <= 38) {
            imageResource = R.drawable.ic_wind_beaufort_7;
        } else if (windspeed <= 46) {
            imageResource = R.drawable.ic_wind_beaufort_8;
        } else if (windspeed <= 54) {
            imageResource = R.drawable.ic_wind_beaufort_9;
        } else if (windspeed <= 63) {
            imageResource = R.drawable.ic_wind_beaufort_10;
        } else if (windspeed <= 72) {
            imageResource = R.drawable.ic_wind_beaufort_11;
        } else if (windspeed >= 73) {
            imageResource = R.drawable.ic_hurricane;
        } else {
            imageResource = R.drawable.ic_windy;
        }

        return imageResource;
    }

    public String getMoonPhaseString(double tempMoonPosition) {
        int moonPosition = (int) (tempMoonPosition * 100);
        String moonPhase = "";

        if ((moonPosition >= 0) && (moonPosition <= 2)) {
            moonPhase = "New";
        } else if ((moonPosition >= 3) && (moonPosition <= 23)) {
            moonPhase = "Waxing Crescent";
        } else if ((moonPosition >= 24) && (moonPosition <= 28)) {
            moonPhase = "First Quarter";
        } else if ((moonPosition >= 29) && (moonPosition <= 48)) {
            moonPhase = "Waxing Gibbous";
        } else if ((moonPosition >= 49) && (moonPosition <= 51)) {
            moonPhase = "Full";
        } else if ((moonPosition >= 52) && (moonPosition <= 73)) {
            moonPhase = "Waning Gibbous";
        } else if ((moonPosition >= 74) && (moonPosition <= 76)) {
            moonPhase = "Third Quarter";
        } else if ((moonPosition >= 77) && (moonPosition <= 98)) {
            moonPhase = "Waning Crescent";
        } else if ((moonPosition >= 99) && (moonPosition <= 100)) {
            moonPhase = "New";
        }

        return moonPhase;
    }

    public static final Map<String, String> MOON_PHASE;

    static {
        MOON_PHASE = new HashMap<String, String>();
        MOON_PHASE.put("0.00", String.valueOf(R.drawable.ic_moon_new));
        MOON_PHASE.put("0.01", String.valueOf(R.drawable.ic_moon_new));
        MOON_PHASE.put("0.02", String.valueOf(R.drawable.ic_moon_new));
        MOON_PHASE.put("0.03", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_1));
        MOON_PHASE.put("0.04", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_1));
        MOON_PHASE.put("0.05", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_1));
        MOON_PHASE.put("0.06", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_1));
        MOON_PHASE.put("0.07", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_2));
        MOON_PHASE.put("0.08", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_2));
        MOON_PHASE.put("0.09", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_2));
        MOON_PHASE.put("0.1", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_2));
        MOON_PHASE.put("0.11", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_3));
        MOON_PHASE.put("0.12", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_3));
        MOON_PHASE.put("0.13", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_3));
        MOON_PHASE.put("0.14", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_3));
        MOON_PHASE.put("0.15", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_4));
        MOON_PHASE.put("0.16", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_4));
        MOON_PHASE.put("0.17", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_4));
        MOON_PHASE.put("0.18", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_5));
        MOON_PHASE.put("0.19", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_5));
        MOON_PHASE.put("0.2", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_5));
        MOON_PHASE.put("0.21", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_6));
        MOON_PHASE.put("0.22", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_6));
        MOON_PHASE.put("0.23", String.valueOf(R.drawable.ic_wi_moon_waxing_crescent_6));
        MOON_PHASE.put("0.24", String.valueOf(R.drawable.ic_wi_moon_first_quarter));
        MOON_PHASE.put("0.25", String.valueOf(R.drawable.ic_wi_moon_first_quarter));
        MOON_PHASE.put("0.26", String.valueOf(R.drawable.ic_wi_moon_first_quarter));
        MOON_PHASE.put("0.27", String.valueOf(R.drawable.ic_wi_moon_first_quarter));
        MOON_PHASE.put("0.28", String.valueOf(R.drawable.ic_wi_moon_first_quarter));
        MOON_PHASE.put("0.29", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_1));
        MOON_PHASE.put("0.3", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_1));
        MOON_PHASE.put("0.31", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_1));
        MOON_PHASE.put("0.32", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_1));
        MOON_PHASE.put("0.33", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_2));
        MOON_PHASE.put("0.34", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_2));
        MOON_PHASE.put("0.35", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_2));
        MOON_PHASE.put("0.36", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_2));
        MOON_PHASE.put("0.37", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_3));
        MOON_PHASE.put("0.38", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_3));
        MOON_PHASE.put("0.39", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_3));
        MOON_PHASE.put("0.4", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_4));
        MOON_PHASE.put("0.41", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_4));
        MOON_PHASE.put("0.42", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_4));
        MOON_PHASE.put("0.43", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_5));
        MOON_PHASE.put("0.44", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_5));
        MOON_PHASE.put("0.45", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_5));
        MOON_PHASE.put("0.46", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_6));
        MOON_PHASE.put("0.47", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_6));
        MOON_PHASE.put("0.48", String.valueOf(R.drawable.ic_wi_moon_waxing_gibbous_6));
        MOON_PHASE.put("0.49", String.valueOf(R.drawable.ic_moon_full));
        MOON_PHASE.put("0.5", String.valueOf(R.drawable.ic_moon_full));
        MOON_PHASE.put("0.51", String.valueOf(R.drawable.ic_moon_full));
        MOON_PHASE.put("0.52", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_1));
        MOON_PHASE.put("0.53", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_1));
        MOON_PHASE.put("0.54", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_1));
        MOON_PHASE.put("0.55", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_2));
        MOON_PHASE.put("0.56", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_2));
        MOON_PHASE.put("0.57", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_2));
        MOON_PHASE.put("0.58", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_3));
        MOON_PHASE.put("0.59", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_3));
        MOON_PHASE.put("0.6", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_3));
        MOON_PHASE.put("0.61", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_3));
        MOON_PHASE.put("0.62", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_4));
        MOON_PHASE.put("0.63", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_4));
        MOON_PHASE.put("0.64", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_4));
        MOON_PHASE.put("0.65", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_4));
        MOON_PHASE.put("0.66", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_5));
        MOON_PHASE.put("0.67", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_5));
        MOON_PHASE.put("0.68", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_5));
        MOON_PHASE.put("0.69", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_5));
        MOON_PHASE.put("0.7", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_6));
        MOON_PHASE.put("0.71", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_6));
        MOON_PHASE.put("0.72", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_6));
        MOON_PHASE.put("0.73", String.valueOf(R.drawable.ic_wi_moon_waning_gibbous_6));
        MOON_PHASE.put("0.74", String.valueOf(R.drawable.ic_wi_moon_third_quarter));
        MOON_PHASE.put("0.75", String.valueOf(R.drawable.ic_wi_moon_third_quarter));
        MOON_PHASE.put("0.76", String.valueOf(R.drawable.ic_wi_moon_third_quarter));
        MOON_PHASE.put("0.77", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_1));
        MOON_PHASE.put("0.78", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_1));
        MOON_PHASE.put("0.79", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_1));
        MOON_PHASE.put("0.8", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_1));
        MOON_PHASE.put("0.81", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_2));
        MOON_PHASE.put("0.82", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_2));
        MOON_PHASE.put("0.83", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_2));
        MOON_PHASE.put("0.84", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_3));
        MOON_PHASE.put("0.85", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_3));
        MOON_PHASE.put("0.86", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_3));
        MOON_PHASE.put("0.87", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_4));
        MOON_PHASE.put("0.88", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_4));
        MOON_PHASE.put("0.89", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_4));
        MOON_PHASE.put("0.9", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_4));
        MOON_PHASE.put("0.91", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_5));
        MOON_PHASE.put("0.92", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_5));
        MOON_PHASE.put("0.93", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_5));
        MOON_PHASE.put("0.94", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_5));
        MOON_PHASE.put("0.95", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_6));
        MOON_PHASE.put("0.96", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_6));
        MOON_PHASE.put("0.97", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_6));
        MOON_PHASE.put("0.98", String.valueOf(R.drawable.ic_wi_moon_waning_crescent_6));
        MOON_PHASE.put("0.99", String.valueOf(R.drawable.ic_moon_new));
        MOON_PHASE.put("1", String.valueOf(R.drawable.ic_moon_new));

    }

    public double calcComfortIndex(double temp, double apparent, double dewpoint, double windspeed, double cloudCov, double precipProb, int uvIndex) {
        double comfortIndex = 0;
        double hourlyTempIndex;
        double hourlyDewIndex;
        double hourlyPrecipIndex;
        double hourlyWindSpeedIndex;
        double hourlyUVIndexIndex;
        double hourlyCloudCoverIndex;
        double hourlyFeelLikeIndex;

        hourlyTempIndex = Math.max((((Math.exp(-(Math.pow((temp - 70), 2) / 1500)))) - Math.pow((0.007 * temp), 11)) + .01, 0) * 100;
        hourlyDewIndex = Math.max(((-0.5 * dewpoint) + 165) - (Math.exp(0.06 * dewpoint)), 0);
        hourlyPrecipIndex = Math.min(-Math.cbrt(10000 * precipProb) - Math.exp(.045 * precipProb) + 150, 100);
        hourlyWindSpeedIndex = Math.min(-(Math.cbrt((280000 * windspeed))) - Math.exp(.05 * windspeed) - (Math.pow((-0.005 * windspeed - 6), 3) + 5), 100);
        hourlyUVIndexIndex = Math.min(-Math.cbrt(15000 * uvIndex) - Math.exp(.43 * uvIndex) + 150, 100);
        hourlyCloudCoverIndex = Math.max(((-0.05 * cloudCov) + 101) - (Math.exp(0.046 * cloudCov)), 0);
        hourlyFeelLikeIndex = 100 * (Math.exp((-((Math.pow((apparent - 70), 2) / 2800)))) - (Math.pow((.0005 * apparent), 2)) - (.07 * Math.exp(-.05 * apparent)) - (Math.pow((0.008 * apparent), 11)) + .01);

        //Bounders
        if (Double.isNaN(hourlyUVIndexIndex)) {
            hourlyUVIndexIndex = 0;
        } else if (hourlyUVIndexIndex < 0) {
            hourlyUVIndexIndex = 0;
        }
        if (Double.isNaN(hourlyPrecipIndex)) {
            hourlyPrecipIndex = 0;
        } else if (hourlyPrecipIndex < 0) {
            hourlyPrecipIndex = 0;
        }
        if (Double.isNaN(hourlyDewIndex)) {
            hourlyDewIndex = 0;
        } else if (hourlyDewIndex > 100) {
            hourlyDewIndex = 100;
        }
        if (hourlyWindSpeedIndex < 0) {
            hourlyWindSpeedIndex = 0;
        }
        if (hourlyTempIndex > 100) {
            hourlyTempIndex = 100;
        }
        if (apparent < 25) {
            dewWeight = 3;
        }
        if (temp < 0) {
            hourlyTempIndex = 0;
            dewWeight = 1;
        }
        if (hourlyFeelLikeIndex < -100) {
            hourlyFeelLikeIndex = -100;
        } else if (hourlyFeelLikeIndex > 100) {
            hourlyFeelLikeIndex = 100;
        }

        comfortIndex = Math.min(((hourlyTempIndex * tempWeight) + (hourlyDewIndex * dewWeight) + (hourlyFeelLikeIndex * feelLikeWeight) + (hourlyWindSpeedIndex * windSpeedWeight) + (hourlyUVIndexIndex * uvWeight) + (hourlyCloudCoverIndex * cloudCoverWeight) + (hourlyPrecipIndex * precipWeight)) / (tempWeight + dewWeight + feelLikeWeight + windSpeedWeight + uvWeight + cloudCoverWeight + precipWeight), 100);
        return comfortIndex;
    }


//    public final void getUnits(){
//        Log.d(TAG, "UNITS: " + weather.flags.getUnits());
//        if(weather.flags.getUnits().equals("us")){
//            DEG_UNITS = "°F";
//            SPEED_UNITS = "mph";
//            DIST_UNITS = "mi";
//            RAIN_UNITS = "in / h";
//            PRECIP_ACCUM_UNITS = "in";
//            Log.d(TAG, "US UNITS ");
//
//        } else if(weather.flags.getUnits().equals("si")){
//            DEG_UNITS = "°C";
//            SPEED_UNITS = "m/s";
//            DIST_UNITS = "km";
//            RAIN_UNITS = "mm / h";
//            PRECIP_ACCUM_UNITS = "cm";
//            Log.d(TAG, "SI UNITS ");
//        } else if(weather.flags.getUnits().equals("ca")){
//            DEG_UNITS = "°C";
//            SPEED_UNITS = "kph";
//            DIST_UNITS = "km";
//            RAIN_UNITS = "mm / h";
//            PRECIP_ACCUM_UNITS = "cm";
//            Log.d(TAG, "CA UNITS ");
//
//        } else if(weather.flags.getUnits().equals("uk2")) {
//            DEG_UNITS = "°C";
//            SPEED_UNITS = "mph";
//            DIST_UNITS = "mi";
//            RAIN_UNITS = "mm / h";
//            PRECIP_ACCUM_UNITS = "cm";
//            Log.d(TAG, "UK UNITS ");
//
//        } else {
//            DEG_UNITS = "°F";
//            SPEED_UNITS = "mph";
//            DIST_UNITS = "mi";
//            RAIN_UNITS = "in / h";
//            PRECIP_ACCUM_UNITS = "in";
//            Log.d(TAG, "UNKNOWN UNITS ");
//
//        }
//    }


    public String convertDegreeToCardinalDirection(double directionInDegrees) {
        String cardinalDirection = null;
        if ((directionInDegrees >= 348.75) && (directionInDegrees <= 360) || (directionInDegrees >= 0) && (directionInDegrees <= 11.25)) {
            cardinalDirection = "N";
        } else if ((directionInDegrees >= 11.25) && (directionInDegrees <= 33.75)) {
            cardinalDirection = "NNE";
        } else if ((directionInDegrees >= 33.75) && (directionInDegrees <= 56.25)) {
            cardinalDirection = "NE";
        } else if ((directionInDegrees >= 56.25) && (directionInDegrees <= 78.75)) {
            cardinalDirection = "ENE";
        } else if ((directionInDegrees >= 78.75) && (directionInDegrees <= 101.25)) {
            cardinalDirection = "E";
        } else if ((directionInDegrees >= 101.25) && (directionInDegrees <= 123.75)) {
            cardinalDirection = "ESE";
        } else if ((directionInDegrees >= 123.75) && (directionInDegrees <= 146.25)) {
            cardinalDirection = "SE";
        } else if ((directionInDegrees >= 146.25) && (directionInDegrees <= 168.75)) {
            cardinalDirection = "SSE";
        } else if ((directionInDegrees >= 168.75) && (directionInDegrees <= 191.25)) {
            cardinalDirection = "S";
        } else if ((directionInDegrees >= 191.25) && (directionInDegrees <= 213.75)) {
            cardinalDirection = "SSW";
        } else if ((directionInDegrees >= 213.75) && (directionInDegrees <= 236.25)) {
            cardinalDirection = "SW";
        } else if ((directionInDegrees >= 236.25) && (directionInDegrees <= 258.75)) {
            cardinalDirection = "WSW";
        } else if ((directionInDegrees >= 258.75) && (directionInDegrees <= 281.25)) {
            cardinalDirection = "W";
        } else if ((directionInDegrees >= 281.25) && (directionInDegrees <= 303.75)) {
            cardinalDirection = "WNW";
        } else if ((directionInDegrees >= 303.75) && (directionInDegrees <= 326.25)) {
            cardinalDirection = "NW";
        } else if ((directionInDegrees >= 326.25) && (directionInDegrees <= 348.75)) {
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }
        return cardinalDirection;
    }

    public void checkDaylightHours() {
        //tests for northern hemisphere first
        Date currentDate = new Date(weather.currentCondition.getTime() * 1000);
        int dateMonth = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(currentDate));
        //Log.d(TAG, "LATITUDE: " + weather.location.getLatitude());
        if (weather.location.getLatitude() > 0) {

            if (Integer.parseInt(weather.dailyCondition.getSunriseTime().get(0).toString()) == 0) {
                if (dateMonth >= 3 && dateMonth <= 8) {
                    daylightHours_textView.setText("24 hours");
                } else {
                    daylightHours_textView.setText("0 hours");
                    dayProgress_progressBar.setProgress(0);
                }
            }
        } else {
            if (Integer.parseInt(weather.dailyCondition.getSunriseTime().get(0).toString()) == 0) {
                if (dateMonth >= 3 && dateMonth <= 7) {
                    daylightHours_textView.setText("24 hours");
                } else {
                    daylightHours_textView.setText("0 hours");
                    dayProgress_progressBar.setProgress(0);
                }
            }
        }
    }


    public double getPrecipAccumTotal(int multiple) {
        double totalPrecipAccum = 0.0;
        for (int i = 0; i < 24; i++) {
            totalPrecipAccum = totalPrecipAccum + Double.parseDouble(weather.hourlyCondition.getPrecipIntensity().get(i + (multiple * 24)).toString());

        }
        return totalPrecipAccum;
    }

    public double getDailyWindSpeed(int multiple) {
        double dailyWindSpeed = 0.0;
        for (int i = 0; i < 24; i++) {
            dailyWindSpeed = dailyWindSpeed + (Double.parseDouble(weather.hourlyCondition.getWindSpeed().get(i + (multiple * 24)).toString()) + (Double.parseDouble(weather.hourlyCondition.getWindGust().get(i + (multiple * 24)).toString()) - 5) / 2);

        }
        return dailyWindSpeed / 24;
    }

    public double getDailyAverageDewpoint(int multiple) {
        double averageDewpoint = 0.0;
        for (int i = 0; i < 24; i++) {
            averageDewpoint = averageDewpoint + Double.parseDouble(weather.hourlyCondition.getDewPoint().get(i + (multiple * 24)).toString());

        }
        return averageDewpoint / 24;
    }

    public double getDailyMaxPrecipProb(int multiple) {
        double maxPrecipProbDaily = 0;
        for (int i = 0; i < 24; i++) {
            if (Double.parseDouble(weather.hourlyCondition.getPrecipProbability().get(i + (multiple * 24)).toString()) > maxPrecipProbDaily) {
                maxPrecipProbDaily = Double.parseDouble(weather.hourlyCondition.getPrecipProbability().get(i + (multiple * 24)).toString());
            }
        }
        return maxPrecipProbDaily * 100;
    }


    public String setDailySummaryText(int i, List minWindSpeed, List maxWindSpeed, List dailyDateSummaryList, List sunriseDateList, List sunsetDateList, List moonPhase) {
        StringBuilder dailySummaryText = new StringBuilder();

        if ((getPrecipAccumTotal(i) >= 4) && Integer.parseInt(String.valueOf(maxWindSpeed.get(i))) >= 30) {
            dailySummaryText.append("Tropical storm conditions possible. ");
        } else if ((getPrecipAccumTotal(i) >= 4) && Integer.parseInt(String.valueOf(maxWindSpeed.get(i))) >= 40) {
            dailySummaryText.append("Tropical storm conditions likely. ");
        } else if ((getPrecipAccumTotal(i) >= 4) && Integer.parseInt(String.valueOf(maxWindSpeed.get(i))) >= 55) {
            dailySummaryText.append("Hurricane conditions possible. ");
        } else if ((getPrecipAccumTotal(i) >= 4) && Integer.parseInt(String.valueOf(maxWindSpeed.get(i))) >= 70) {
            dailySummaryText.append("Hurricane conditions likely. ");
        }
        dailySummaryText.append(getDailyTemperatureText(i) + "" + weather.dailyCondition.getSummary().get(i));
        dailySummaryText.append(" Winds " + convertDegreeToCardinalDirection(Double.parseDouble(weather.dailyCondition.getWindBearing().get(i).toString())) + " at " + minWindSpeed.get(i) + " to " + maxWindSpeed.get(i) + " " + SPEED_UNITS + ". ");
        if(Integer.parseInt(weather.dailyCondition.getUvIndex().get(i).toString()) >= 7) {
            dailySummaryText.append("The UV index will be " + getDailyMaxUV(Integer.parseInt(weather.dailyCondition.getUvIndex().get(i).toString())).toLowerCase() + ".");
        }

        dayDescription_textView.setText(dailySummaryText);

        dailySummaryHigh_textView.setText("High: " + d0f.format(Double.parseDouble(weather.dailyCondition.getTemperatureHigh().get(i).toString())) + DEG_UNITS);
        dailySummaryLow_textView.setText("Low: " + d0f.format(Double.parseDouble(weather.dailyCondition.getTemperatureLow().get(i).toString())) + DEG_UNITS);
        dailySummaryDewpoint_textView.setText("Dewpoint: " + d0f.format(getDailyAverageDewpoint(i)) + DEG_UNITS);
        dailySummaryPrecip_textView.setText("Precip: " + d0f.format(getDailyMaxPrecipProb(i)) + "%");
        dailySummaryAccum_textView.setText("Accum: " + d3f.format(getPrecipAccumTotal(i)) + " " + PRECIP_ACCUM_UNITS);
        summaryDate_textView.setText("" + dailyDateSummaryList.get(i));
        if (Integer.parseInt(weather.dailyCondition.getSunriseTime().get(i).toString()) == 0) {
            daySummarySunrise_textView.setText("No sunrise");
            daySummarySunset_textView.setText("No sunset");
        } else {
            daySummarySunrise_textView.setText("" + sunriseDateList.get(i));
            daySummarySunset_textView.setText("" + sunsetDateList.get(i));
        }
        daySummaryMoonphase_textView.setText(getMoonPhaseString(Double.parseDouble(moonPhase.get(i).toString())));
        dailyCondition_textView.setText("" + weather.dailyCondition.getIcon().get(i));

        dailyCondition_imageView.setImageResource(getWeatherConditionIcon(weather.dailyCondition.getIcon().get(i).toString(), Double.parseDouble(weather.dailyCondition.getPrecipIntensity().get(i).toString()), Double.parseDouble(weather.dailyCondition.getCloudCover().get(i).toString())));
        dailyCondition_textView.setText(getWeatherConditionString(weather.dailyCondition.getIcon().get(i).toString()));

        return null;
    }

    public String getDailyMaxUV(int uvIndex) {
//        int uvIndex = Integer.parseInt(weather.dailyCondition.getUvIndex().get(i).toString());
        String uvString = "";
        if (uvIndex <= 2) {
            uvString = "Low";
        } else if (uvIndex <= 5) {
            uvString = "Moderate";
        } else if (uvIndex <= 7) {
            uvString = "High";
        } else if (uvIndex <= 10) {
            uvString = "Very high";
        } else if (uvIndex >= 11) {
            uvString = "Extreme";
        }


        return uvString;
    }

    public String getDailyTemperatureText(int i) {
        double highTemp = Double.parseDouble(weather.dailyCondition.getTemperatureMax().get(i).toString());
        double highApparentTemp = Double.parseDouble(weather.dailyCondition.getApparentTemperatureMax().get(i).toString());
        double lowTemp = Double.parseDouble(weather.dailyCondition.getTemperatureLow().get(i).toString());
        double lowApparentTemp = Double.parseDouble(weather.dailyCondition.getApparentTemperatureLow().get(i).toString());
        String temperatureString = "";
        if (highTemp >= 85 || highApparentTemp >= 85) {
            if (highTemp >= 110 || highApparentTemp >= 110) {
                temperatureString = "Extremely Hot. ";
            } else if (highTemp >= 100 || highApparentTemp >= 100) {
                temperatureString = "Very Hot. ";
            } else if (highTemp >= 95 || highApparentTemp >= 95) {
                temperatureString = "Hot. ";
            }
        } else if (lowTemp <= 50 || lowApparentTemp <= 50) {
            if (lowTemp <= -20 || lowApparentTemp <= -20) {
                temperatureString = "Extremely Frigid. ";
            } else if (lowTemp <= 0 || lowApparentTemp <= 0) {
                temperatureString = "Bitterly cold. ";
            } else if (lowTemp <= 15 || lowApparentTemp <= 15) {
                temperatureString = "Frigid. ";
            } else if (lowTemp <= 30 || lowApparentTemp <= 30) {
                temperatureString = "Very cold. ";
            } else if (lowTemp <= 40 || lowApparentTemp <= 40) {
                temperatureString = "Cold. ";
            } else if (lowTemp <= 50 || lowApparentTemp <= 50) {
                temperatureString = "Cool. ";
            }
        }
        return temperatureString;
    }

    public String setDailyGraphRadio(int i, LineChart forecastGraph, List radioListDate, XAxis forecastxAxis) {

        forecastGraph.setVisibleXRange(0, 24);
        forecastGraph.setVisibleXRangeMaximum(24);
        forecastGraph.moveViewToX(((i * 24) + (int) getHourlyBoundsRounded()));
        Log.d(TAG, "getRange: " + ((i * 24) + (int) getHourlyBoundsRounded()));
        forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(i));
        utils.setGraphZoomed(true);
        forecastGraph.animateX(650);
        timeLabels_textView.setVisibility(View.VISIBLE);
        timeLabels_textView.setText("00:00a                    06:00a                  12:00p                  06:00p                  00:00a");
        horizontalGraphDatesScroll.setVisibility(View.GONE);
        forecastxAxis.setLabelCount(5);


        return null;
    }

    public int getAlertIcon(String severity) {
        int resource = 0;

        if (severity != null) {
            if (severity.equals("warning")) {
                resource = R.drawable.ic_warning_red;
            }
            if (severity.equals("watch")) {
                resource = R.drawable.ic_warning_light_red;
            }
            if (severity.equals("advisory")) {
                resource = R.drawable.ic_warning_orange;

            } else {
                Log.d(TAG, "getWeatherAlertCondition: SEVERITY" + severity);
            }
        }
        return resource;
    }

    public String getWeatherAlertCondition() {

        if (!(weather.alerts.getSeverity().get(0).toString().equals(""))) {
            String weatherAlert = weather.alerts.getSeverity().get(0).toString();

            weatherAlertScrollable.setVisibility(View.VISIBLE);
            weatherAlertType.setVisibility(View.VISIBLE);
            weatherAlert_textView.setVisibility(View.VISIBLE);

            weatherAlert_textView.setSelected(true);
            weatherAlert_textView.setText(weather.alerts.getTitle().get(0).toString() + " " + weather.alerts.getDescription().get(0).toString());
            if (weatherAlert.equals("warning") && !(weather.alerts.getTitle().equals("Severe Thunderstorm Watch"))) {
                weatherAlert_textView.setBackgroundColor(Color.rgb(255, 0, 0));
                weatherAlertType.setBackgroundColor(Color.rgb(255, 0, 0));
                weatherAlertType.setText(" WARNING ");

            }
            if (weatherAlert.equals("watch") || weather.alerts.getTitle().get(0).toString().equals("Severe Thunderstorm Watch")) {
                weatherAlert_textView.setBackgroundColor(Color.rgb(255, 102, 102));
                weatherAlertType.setBackgroundColor(Color.rgb(255, 102, 102));
                weatherAlertType.setText(" WATCH ");

            }
            if (weatherAlert.equals("advisory")) {
                weatherAlert_textView.setBackgroundColor(Color.rgb(181, 98, 38));
                weatherAlertType.setBackgroundColor(Color.rgb(181, 98, 38));
                weatherAlertType.setText(" ADVISORY ");


            } else {
                //Log.d(TAG, "getWeatherAlertCondition: SEVERITY " + weatherAlert.toUpperCase());
            }
        }
        return null;
    }


    private void init() {
        Cursor tableData = mWeatherDataDatabaseHelper.getData();
        int itemID = -1;
        String data = "";
        String dataLat = "";
        String dataLong = "";

        //Log.d(TAG, "RECENT DATA ID: " + itemID);


        while (tableData.moveToNext()) {
            itemID = tableData.getInt(0);
            data = (tableData.getString(1));
        }
        if (itemID > -1) {
            int strlatStart = data.indexOf("latitude\":") + 10;
            int strlonStart = data.indexOf(",\"longitude\":") + 13;
            int strlonEnd = data.indexOf(",\"timezone\":") + 12;

            dataLat = data.substring(strlatStart, strlonStart - 13);
            dataLong = data.substring(strlonStart, strlonEnd - 12);

            utils.setLat(Double.parseDouble(dataLat));
            utils.setLon(Double.parseDouble(dataLong));
        }

        String city = "";
        if ((dataLat.equals("") && dataLong.equals(""))) {
            dataLat = "38.90720";
            dataLong = "-77.0369";
            city = dataLat + "," + dataLong;
            Log.d(TAG, "LOADED STORED CITY: " + city);
            weather.location.setLatitude(Float.parseFloat(dataLat));
            weather.location.setLongitude(Float.parseFloat(dataLong));

            try {

                readCityExcel.readAndInsert(getActivity());
                String nameString = "";
                String stateString = "";
                String zipString = "";
                String cityString = "";

                for (int i = 0; i < readCityExcel.cityObject.getCityName().size() - 1; i++) {

                    if (readCityExcel.cityObject.getZipcode().get(i).toString().length() == 4) {
                        zipString = readCityExcel.cityObject.getZipcode().get(i).toString().substring(0, 4);
                    } else if (readCityExcel.cityObject.getZipcode().get(i).toString().length() == 3) {
                        zipString = readCityExcel.cityObject.getZipcode().get(i).toString().substring(0, 3);
                    } else {
                        zipString = readCityExcel.cityObject.getZipcode().get(i).toString();
                    }
                    nameString = readCityExcel.cityObject.getCityName().get(i).toString();
                    stateString = readCityExcel.cityObject.getState().get(i).toString();

                    cityString = (nameString + ", " + stateString + " " + zipString + ", USA");
                    AddAutocomleteData(cityString);
                    //Log.d("TEST", "id: " + i);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

            city = dataLat + "," + dataLong;
            Log.d(TAG, "LOADED STORED CITY: " + city);
            weather.location.setLatitude(Float.parseFloat(dataLat));
            weather.location.setLongitude(Float.parseFloat(dataLong));
        }

        Log.d(TAG, "DICT LON: " + weather.location.getLongitude());
        Log.d(TAG, "DICT LAT: " + weather.location.getLatitude());

        if (isNetworkAvailable()) {
            geoLocateCoords(Float.parseFloat(dataLat), Float.parseFloat(dataLong), true);
        } else {
            RenderWeatherData(city);
            Toast.makeText(getActivity(), "Unable to get internet connection.", Toast.LENGTH_LONG);
        }
        coordinatorLayout.setVisibility(View.VISIBLE);

        clearSearch_imageButton.setVisibility(View.GONE);

        citySearch_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clearSearch_imageButton.setVisibility(View.VISIBLE);

                return false;
            }
        });

        refresh = new Runnable() {
            @Override
            public void run() {
                geoLocate(false);
                Log.d(TAG, "REFRESHED HOME RUNNABLE");
            }
        };
        mainHandler.postDelayed(refresh, 1000 * 60 * 10);

        citySearch_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {


                    closeKeyboard();
                    geoLocate(true);
                }
                return false;
            }
        });

        citySearch_relativeLayout.setFocusableInTouchMode(true);
        citySearch_relativeLayout.setFocusable(true);

        citySearch_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "FOCUS CHANGED");
                    closeKeyboard();
                }
            }
        });
    }


    private void getDeviceLocation() {
        //Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation == null) {
                                buildAlertMessageTurnOnLocationProviders("Your GPS seems to be disabled, would you like to enable it?", "Yes", "No");
                                Log.d(TAG, "location failed");

                            } else {
                                Log.d(TAG, "LOCATION: " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                                weather.location.setLatitude((float) currentLocation.getLatitude());
                                weather.location.setLongitude((float) currentLocation.getLongitude());
                                geoLocateCoords(weather.location.getLatitude(), weather.location.getLongitude(), true);

                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            toastMessage("unable to get current location");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void getLocationPermission() {
        //Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public class MinutelyGraphDateFormater implements IAxisValueFormatter {

        private String[] mValues;

        public MinutelyGraphDateFormater(String[] values) {
            mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) (value);
            String label = "";
            if (val >= 0 && val < mValues.length - 1) {
                label = mValues[val];
            } else {
                label = "";
            }
            return label;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    private void buildAlertMessageTurnOnLocationProviders(String message, String positiveButtonText, String negativeButtonText) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent mIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(mIntent);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        toastMessage("Unable to get current location");
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            citySearch_editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            clearSearch_imageButton.setVisibility(View.GONE);

        }
    }


    public void geoLocate(boolean addData) {
        String searchString = citySearch_editText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            String strAddress = address.toString();
            String cityName = list.get(0).getAddressLine(0);
            Log.d(TAG, "City name: " + cityName);
            String fullCity = list.get(0).getPostalCode();
            double latitude = list.get(0).getLatitude();
            double longitude = list.get(0).getLongitude();

            citySearch_editText.setText(cityName);

            //THIS IS WHERE WE STORE DATA FOR SQLITE
            //String cityToAddName = citySearch_editText.getText().toString();
            utils.setUserCity(cityName);

            //TODO only add address to saved locations if the lat/lon coordinates are more than .1 degrees appart (disance)

            if (!cityHasQueried && addData) {
                if (cityName.length() != 0) {
                    AddData(cityName, "no");
                    Log.d(TAG, "AddData: " + cityName);
                    cityHasQueried = true;
                } else {
                    Log.d(TAG, "INVALID STRING TO STORE");
                }
            }

            Log.d(TAG, "geoLocate: found a location: " + strAddress);

            weather.location.setLatitude((float) latitude);
            weather.location.setLongitude((float) longitude);

            RenderWeatherData(latitude + "," + longitude);

        } else {
            toastMessage("Please enter a valid location.");

        }
    }


    private void geoLocateCoords(final float latitude, final float longitude, boolean renderData) {
        try {
            Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            Log.d(TAG, "geoLocateCoords: REACHED");

            if (addresses.isEmpty()) {
                Toast.makeText(getActivity(), "Unable to get location.", Toast.LENGTH_SHORT).show();
            } else {

                if (addresses.size() > 0) {

                    String cityName = addresses.get(0).getAddressLine(0);
                    Log.d(TAG, "geoLocateCoords CITY NAME: " + cityName);
                    citySearch_editText.setText(cityName);
                    if (renderData) {
                        RenderWeatherData(latitude + "," + longitude);
                        Log.d(TAG, "geoLocateCoords RENDER TRUE");
                    }

                    //BUTTON FOR TIME MACHINE
                    timeMachine_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            Log.d(TAG, "CURRENT DATE: " + month + "/" + day + "/" + year);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePickerSetListener, year, month, day);
                            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            datePickerDialog.show();
                        }
                    });

                    datePickerSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Log.d(TAG, "TIME MACHINE SELECT DATE");

                            month = month + 1;
                            String selectedCalDate = year + "-" + month + "-" + day;

                            Date date = null;
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedCalDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long unixtime = date.getTime();
                            Log.d(TAG, "UNIX DATE: " + unixtime / 1000);

                            Log.d(TAG, "SEARCHABLE DATE: " + latitude + "," + longitude + "," + unixtime / 1000);
                            RenderTimeWeatherData(latitude + "," + longitude, String.valueOf(unixtime / 1000));
                        }
                    };

                    Log.d(TAG, "LAT LONG TEST: " + weather.location.getLatitude());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void geoLocateUserPreference(String city) {

        String searchString = city;
        Log.d(TAG, "geoLocateUserPreference: STRING " + searchString);

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            String strAddress = address.toString();
            String cityName = list.get(0).getLocality() + ", " + list.get(0).getAdminArea() + ", " + list.get(0).getCountryName();
            double latitude = list.get(0).getLatitude();
            double longitude = list.get(0).getLongitude();

            citySearch_editText.setText(cityName);


            Log.d(TAG, "geoLocate: found a location: " + strAddress);

            weather.location.setLatitude((float) latitude);
            weather.location.setLongitude((float) longitude);

            RenderWeatherData(latitude + "," + longitude);


        } else {
            toastMessage("Please enter a valid location.");

        }
    }

    private void initWeatherConditions() {
        mHeaders.clear();
        mImages.clear();
        mInfo.clear();

        mHeaders.add("Feels Like");
        mImages.add(R.drawable.ic_thermometer);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getApparentTemperature()))) + utils.DEG_UNITS);
        mHeaders.add("Chance of Precip");
        mImages.add(R.drawable.new_raindrop);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getPrecipProbability() * 100))) + "%");
        mHeaders.add("Humidity");
        mImages.add(R.drawable.new_humidity);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getHumidity() * 100))) + "%");
        mHeaders.add("Dewpoint");
        mImages.add(R.mipmap.ic_dewpoint_foreground);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getDewPoint()))) + utils.DEG_UNITS);
        mHeaders.add("UV Index");
        mImages.add(R.drawable.ic_sunny);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getUvIndex()))));
        mHeaders.add("Wind");
        mImages.add(getWindSpeedIcon(weather.currentCondition.getWindSpeed()));
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getWindSpeed()))) + utils.SPEED_UNITS + "\n" + String.valueOf(convertDegreeToCardinalDirection(weather.currentCondition.getWindSpeed())));
        mHeaders.add("Pressure");
        mImages.add(R.drawable.ic_wi_barometer);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getPressure()))) + "mb");
        mHeaders.add("Visibility");
        mImages.add(R.drawable.ic_visibility);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getVisibility()))) + utils.DIST_UNITS);
        mHeaders.add("Ozone");
        mImages.add(R.drawable.ic_partly_sunny);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getOzone()))) + "DU");
        mHeaders.add("Cloud Cover");
        mImages.add(R.drawable.ic_cloudy);
        mInfo.add(d0f.format(Double.parseDouble(String.valueOf(weather.currentCondition.getCloudCover() * 100))) + "%");


        initWeatherConditionsRecyclerView();
    }

    private void initWeatherConditionsRecyclerView() {
        //Log.d(TAG, "initWeatherConditionsRecyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.weatherConditions_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        WeatherConditionsAdapter adapter = new WeatherConditionsAdapter(getActivity(), mHeaders, mInfo, mImages);
        recyclerView.setAdapter(adapter);
        //Log.d(TAG, "initWeatherConditionsRecyclerView: PASSED");

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mainScreen_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void initHourlyWeatherConditions() {
        mTime.clear();
        mTemp.clear();
        mPrecip.clear();
        mHourlyImages.clear();


        for (int i = 1; i < 49; i++) {

            mTime.add(utils.getHourlyTime().get(i).toString());
            mTemp.add(d0f.format(Double.parseDouble(String.valueOf(weather.hourlyCondition.getTemperature().get(i)))) + utils.DEG_UNITS);
            mPrecip.add(d0f.format(Double.parseDouble(String.valueOf(weather.hourlyCondition.getPrecipProbability().get(i))) * 100) + "%");
            mHourlyImages.add(getWeatherConditionIcon(weather.hourlyCondition.getIcon().get(i).toString(), Double.parseDouble(weather.hourlyCondition.getPrecipIntensity().get(i).toString()), Double.parseDouble(weather.hourlyCondition.getCloudCover().get(i).toString())));

        }
        initHourlyWeatherConditionsRecyclerView();
    }


    private void initHourlyWeatherConditionsRecyclerView() {
        //Log.d(TAG, "initHourlyWeatherConditionsRecyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.hourlyOutlook_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HourlyConditionsAdapter adapter = new HourlyConditionsAdapter(getActivity(), mTime, mTemp, mPrecip, mHourlyImages);
        recyclerView.setAdapter(adapter);
        //Log.d(TAG, "initHourlyWeatherConditionsRecyclerView: PASSED");

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mainScreen_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void CreateWeatherMap(double latitude, double longitude) {


        radarView.getSettings().setJavaScriptEnabled(true);
        radarView.setWebViewClient(new WebViewClient());
        radarView.getSettings().setLoadWithOverviewMode(true);
        radarView.getSettings().setUseWideViewPort(true);


        String customHTML = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>RainViewer API Example</title>\n" +
                "\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.css\"/>\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.js\"></script>\n" +
                "    <style type=\"text/css\">\n" +
                "        li {\n" +
                "            list-style: none;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div id=\"mapid\" style=\"position: absolute; top: 0px; left: 0; bottom: 0; right: 0;\"></div>\n" +
                "\n" +
                "<script>\n" +
                "    var map = L.map('mapid').setView([" + latitude + ", " + longitude + "], 9);\n" +
                "    map.removeControl( map.zoomControl );\n" +
                "    const attribution = 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors'\n" +
                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution }).addTo(map);\n" +
                "\n" +
                "var marker = L.marker([" + latitude + ", " + longitude + "]).addTo(map);\n" +
                "    var timestamps = [];\n" +
                "    var radarLayers = [];\n" +
                "    var animationPosition = 0;\n" +
                "    var apiRequest = new XMLHttpRequest();\n" +
                "    apiRequest.open(\"GET\", \"https://tilecache.rainviewer.com/api/maps.json\", true);\n" +
                "    apiRequest.onload = function(e) {\n" +
                "        timestamps = JSON.parse(apiRequest.response);\n" +
                "        showFrame(-1);\n" +
                "    };\n" +
                "    apiRequest.send();\n" +
                "    function addLayer(ts) {\n" +
                "        if (!radarLayers[ts]) {\n" +
                "            radarLayers[ts] = new L.TileLayer('https://tilecache.rainviewer.com/v2/radar/' + ts + '/256/{z}/{x}/{y}/6/1_1.png', {\n" +
                "                tileSize: 256,\n" +
                "                opacity: 0.001,\n" +
                "                zIndex: ts\n" +
                "            });\n" +
                "        }\n" +
                "        if (!map.hasLayer(radarLayers[ts])) {\n" +
                "            map.addLayer(radarLayers[ts]);\n" +
                "        }\n" +
                "    }\n" +
                "    function changeRadarPosition(position, preloadOnly) {\n" +
                "        while (position >= timestamps.length) {\n" +
                "            position -= timestamps.length;\n" +
                "        }\n" +
                "        while (position < 0) {\n" +
                "            position += timestamps.length;\n" +
                "        }\n" +
                "        var currentTimestamp = timestamps[animationPosition];\n" +
                "        var nextTimestamp = timestamps[position];\n" +
                "        addLayer(nextTimestamp);\n" +
                "        if (preloadOnly) {\n" +
                "            return;\n" +
                "        }\n" +
                "        animationPosition = position;\n" +
                "        if (radarLayers[currentTimestamp]) {\n" +
                "            radarLayers[currentTimestamp].setOpacity(0);\n" +
                "        }\n" +
                "        radarLayers[nextTimestamp].setOpacity(.65);\n" +
                "        document.getElementById(\"timestamp\").innerHTML = (new Date(nextTimestamp * 1000)).toString();\n" +
                "    }\n" +
                "\n" +
                "    function showFrame(nextPosition) {\n" +
                "        var preloadingDirection = nextPosition - animationPosition > 0 ? 1 : -1;\n" +
                "        changeRadarPosition(nextPosition);\n" +
                "        changeRadarPosition(nextPosition + preloadingDirection, true);\n" +
                "    }\n" +
                "    \n" +
                "   \n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";


        radarView.loadData(customHTML, "text/html", "UTF-8");
    }

    public String updateGraphLayout(int xValue) {
        //graphDateScrollview
        int offset = 1 + (int) getHourlyBoundsRounded();

        if (xValue > 144 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(7));
        } else if (xValue <= 144 + offset && xValue > 120 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(6));
        } else if (xValue <= 120 + offset && xValue > 96 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(5));
        } else if (xValue <= 96 + offset && xValue > 72 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(4));
        } else if (xValue <= 72 + offset && xValue > 48 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(3));
        } else if (xValue <= 48 + offset && xValue > 24 + offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(2));
        } else if (xValue <= 24 + offset && xValue > offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(1));
        } else if (xValue <= offset) {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(0));
        } else {
            forecastGraphViewingDate_textView.setText("" + utils.getRadioListDate().get(7));
        }
        return null;
    }




//TODO Make markers function
//    public class LineGraphMarkerView extends MarkerView {
//
//        private TextView tvContent;
//
//        public LineGraphMarkerView(Context context, int layoutResource) {
//            super(context, layoutResource);
//        }
//
//        // callbacks everytime the MarkerView is redrawn, can be used to update the
//        // content (user-interface)
//        @Override
//        public void refreshContent(Entry e, Highlight highlight) {
//
//            toastMessage("Clicked on: " + e.getX() + "," + e.getY());
//            //tvContent.setText("" + e.getY());
//
//            // this will perform necessary layouting
//            super.refreshContent(e, highlight);
//        }
//
//        private MPPointF mOffset;
//
//        @Override
//        public MPPointF getOffset() {
//
//            if(mOffset == null) {
//                // center the marker horizontally and vertically
//                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
//            }
//
//            return mOffset;
//        }}

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_imageButton) {
            //showInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof HomeFragmentListener) {
//            listener = (HomeFragmentListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement HomeFragmentListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }

}

