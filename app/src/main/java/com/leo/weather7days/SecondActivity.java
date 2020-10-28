package com.leo.weather7days;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.leo.weather7days.jsonclasses.Weather7days;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GpsTracker gpsTracker;
    private TextView tvLatitude, city, date, degree, windspeed, windgusts, clouds, humidity, pressure, sunset, sunrise;
    private double latitude,longitude;
    private final String API_KEY = "e62b9efe3587050f170062726048d1e3db6cbe2d93b2006f1e";
    private Weather7days weather7days;
    private ArrayList<Float> values = new ArrayList<>();
    private boolean flagValues = false;
    private Date today;
    private long dayStep;
    private BarChart barChart;
    private String quer = "";
    private String placeN;

    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        Serializable weather = intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);
        Weather7days weather7days = (Weather7days) weather;
        placeN = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        simpleList = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), weather7days.data.daily);
        simpleList.setAdapter(customAdapter);
        TextView place = (TextView) findViewById(R.id.placeName);

        place.setText(placeN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check internet connection.", Toast.LENGTH_SHORT);
        Toast toastUpdate = Toast.makeText(getApplicationContext(),
                "Updated.", Toast.LENGTH_SHORT);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        MenuItem getLocal = menu.findItem(R.id.get_local);
        MenuItem refresh = menu.findItem(R.id.resfresh);
        MenuItem about = menu.findItem(R.id.action_settings);

        searchItem.setVisible(false);
        getLocal.setVisible(false);
        refresh.setVisible(false);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View menuItemView = findViewById(R.id.layout_second);
                about.setOnMenuItemClickListener(item -> {

                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(menuItemView);

                    return false;
                });
                // SOME OF YOUR TASK AFTER GETTING VIEW REFERENCE

            }
        });

        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });

        getLocal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });


        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        SearchView finalSearchView = searchView;


        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SecondActivity.this.getComponentName()));
            SearchView finalSearchView1 = searchView;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText)
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            });


            searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focused) {
                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (focused)
                        keyboard.showSoftInput(finalSearchView, 0);
                    else
                        keyboard.hideSoftInputFromWindow(finalSearchView.getWindowToken(), 0);
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
