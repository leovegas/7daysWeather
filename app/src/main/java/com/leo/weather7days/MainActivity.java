package com.leo.weather7days;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.FrameLayout;
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
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.leo.weather7days.jsonclasses.Daily;
import com.leo.weather7days.jsonclasses.Weather7days;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.leo.weather7days.MainActivity" ;
    public static final String EXTRA_MESSAGE2 = "com.leo.weather7days.MainActivity2" ;
    public static String placeName;

    private GpsTracker gpsTracker;
    private TextView tvLatitude, city, date, degree, windspeed, windgusts, clouds, humidity, sunset, sunrise;
    private double latitude,longitude;
    private final String API_KEY = "1e62b9efe3587050f170062726048d1e3db6cbe2d93b2006f1e";
    private Weather7days weather7days;
    private ArrayList<Float> values = new ArrayList<>();
    private boolean flagValues = false;
    private Date today;
    private long dayStep;
    private BarChart barChart;
    private String quer = "";
    private String[] dates, temps, tempsmin, winds, rains;
    ArrayList<String> buf = new ArrayList<>();
    private int chartClicks = 0;
    private View viewMenu;
    private FrameLayout stub;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Check internet connection.", Toast.LENGTH_SHORT);

        date = (TextView)findViewById(R.id.date);
        city = (TextView)findViewById(R.id.city);
        degree = (TextView)findViewById(R.id.degree);
        windspeed = (TextView)findViewById(R.id.wind_speed);
        windgusts = (TextView)findViewById(R.id.wind_gusts_speed);
        clouds = (TextView)findViewById(R.id.clouds);
        humidity = (TextView)findViewById(R.id.humidity);
        sunset = (TextView)findViewById(R.id.sunset);
        sunrise = (TextView)findViewById(R.id.sunrise);
        stub = (FrameLayout)findViewById(R.id.stub);

        if (!isOnline()) {
            stub.setVisibility(View.VISIBLE);
        }else {
            stub.setVisibility(View.GONE);
        }

        activate7daysListner();

        today = new Date();
        dayStep = (1000 * 60 * 60 * 24);
        String currentDate = new SimpleDateFormat("d MMMM, EEEE", Locale.getDefault()).format(today);
        date.setText(currentDate);

        if (isOnline())
        getLocalWeather();
        else toast.show();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        barChart = (BarChart) findViewById(R.id.barchart);

        barChart.getAxisRight().setTextColor(Color.TRANSPARENT);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getXAxis().setGridColor(Color.TRANSPARENT);
        barChart.getXAxis().setTextSize(8);

        barChart.setBorderColor(Color.TRANSPARENT);

        if (weather7days!=null) {
            for (Daily e : weather7days.data.daily) {
                values.add((float) e.rain);
            }
        }else {
            for (int i = 0; i < 7; i++) {
                values.add((float) 0);
            }
        }

        if (isOnline()){
            if (weather7days!=null)
                updateChart("Rain (mm) next 7 days");
        }
        else toast.show();

        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartClicks++;
                if (chartClicks>4) chartClicks = 0;
                    if (chartClicks==0){
                        values.clear();
                        if (weather7days!=null){
                            for (Daily e : weather7days.data.daily) {
                                values.add((float) e.temperature);
                            }
                        }
                        if (isOnline())
                            updateChart("Temperature next 7 days");
                        else toast.show();
                        System.out.println("chart "+chartClicks);

                    }
                if (chartClicks==1){
                        values.clear();
                        if (weather7days!=null){
                            for (Daily e : weather7days.data.daily) {
                                values.add((float) e.rain);
                            }
                        }
                        if (isOnline())
                            updateChart("Rain (mm) next 7 days");
                        else toast.show();
                    }
                if (chartClicks==2){
                        values.clear();
                        if (weather7days!=null){
                            for (Daily e : weather7days.data.daily) {
                                values.add((float) e.windSpeed);
                            }
                        }
                        if (isOnline())
                            updateChart("Wind speed (m/s) next 7 days");
                        else toast.show();
                    }
                if (chartClicks==3){
                        values.clear();
                        if (weather7days!=null){
                            for (Daily e : weather7days.data.daily) {
                                values.add((float) e.relHumidity);
                            }
                        }
                        if (isOnline())
                            updateChart("Humidity (%) next 7 days");
                        else toast.show();
                    }
                if (chartClicks==4){
                        values.clear();
                        if (weather7days!=null){
                            for (Daily e : weather7days.data.daily) {
                                values.add((float) e.preasure);
                            }
                        }
                        if (isOnline())
                            updateChart("Preasure (P) next 7 days");
                        else toast.show();
                    }

            }
        });
    }

    public static class FormattingDate {
        public static Date StringToDate(String dob) throws java.text.ParseException {
            System.out.println(ISO8601Utils.parse(dob, new ParsePosition(0)));
            return ISO8601Utils.parse(dob, new ParsePosition(0));
        }
    }

    private void activate7daysListner() {
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                Weather7days message = weather7days;
                i.putExtra(EXTRA_MESSAGE, message);
                i.putExtra(EXTRA_MESSAGE2, city.getText());
                startActivity(i);
            }
        });
        degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                Weather7days message = weather7days;
                i.putExtra(EXTRA_MESSAGE, message);
                i.putExtra(EXTRA_MESSAGE2, city.getText());
                startActivity(i);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostResume() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check internet connection.", Toast.LENGTH_SHORT);

        if (isOnline()) {
            stub.setVisibility(View.GONE);
            getLocalWeather();
        } else {
            stub.setVisibility(View.VISIBLE);
            toast.show();
        }

        if (weather7days!=null) {
            for (Daily e : weather7days.data.daily) {
                values.add((float) e.rain);
            }
        }else {
            for (int i = 0; i < 7; i++) {
                values.add((float) 0);
            }
        }
        if (isOnline())
            updateChart("Rain (mm) next 7 days");

        super.onPostResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getLocalWeather() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check internet connection.", Toast.LENGTH_SHORT);
        if (isOnline())
            getLocation();
        else toast.show();

        if (latitude!=0) {
            if (isOnline())
                getAddress(getApplicationContext(),latitude,longitude);
            else toast.show();
            weather7days = displayTemp(longitude,latitude);
            if (weather7days!=null){
                for (Daily e : weather7days.data.daily) {
                    values.add((float) e.rain);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateChart(String str) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry((float) values.get(0), 0));
        entries.add(new BarEntry((float) values.get(1), 1));
        entries.add(new BarEntry((float) values.get(2), 2));
        entries.add(new BarEntry((float) values.get(3), 3));
        entries.add(new BarEntry((float) values.get(4), 4));
        entries.add(new BarEntry((float) values.get(5), 5));
        entries.add(new BarEntry((float) values.get(6), 6));

        BarDataSet bardataset = new BarDataSet(entries, str);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*2)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*3)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*4)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*5)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*6)));
        labels.add(new SimpleDateFormat("d-EEE", Locale.getDefault()).format(new Date(today.getTime()+dayStep*7)));

        BarData data = new BarData(labels, bardataset);
        data.setValueTextColor(Color.WHITE);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription(str);  // set the description
        barChart.setDescriptionColor(Color.TRANSPARENT);
        barChart.setGridBackgroundColor(Color.TRANSPARENT);
        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        barChart.animateY(2000);
        barChart.getBarData().setValueTextSize(10);

    }

    public void getLocation(){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void getLocationCoordinates(Context context, String location) {

        List<Address> listOfAddress;
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            listOfAddress = geocoder.getFromLocationName(location, 5);
            if (listOfAddress != null && listOfAddress.size() > 0) {
                    Address address = listOfAddress.get(0);
                    String country = address.getCountryCode();
                    String adminArea= address.getAdminArea();
                    String locality= address.getLocality();

                city.setText(address.getLocality());
                placeName = address.getLocality();

                Log.d(TAG, "getAddress:  address " + country);
                    Log.d(TAG, "getAddress:  city " + adminArea);
                    Log.d(TAG, "getAddress:  state " + locality);

                double latitude1= address.getLatitude();
                double longitude1 = address.getLongitude();
                latitude = latitude1;
                longitude = longitude1;

                    Log.d(TAG, "lat " + latitude1);
                    Log.d(TAG, "lon " + longitude1);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String place = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                city.setText(address);

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + place);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Weather7days displayTemp(double lon, double lat) {
        String jsonText = "https://api.troposphere.io/forecast/"+lat+","+lon+"?token="+API_KEY;
        URL url = null;
        InputStreamReader reader = null;
        Exception exception = null;

        try {
            url = new URL(jsonText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            exception = e;
          //  e.printStackTrace();
        }
        if (exception != null) {
            stub.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Update app", Toast.LENGTH_LONG);
            toast.show();

        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.getAsJsonObject(jsonText);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            weather7days = gson.fromJson(reader, Weather7days.class);

            degree.setText((int) (weather7days.data.current.temperature) + "°");
            windspeed.setText((int) (weather7days.data.current.windSpeed) + " m/s");
            windgusts.setText((int) (weather7days.data.current.windGustsSpeed) + " m/s");
            clouds.setText((weather7days.data.current.type).replace("-", " "));
            humidity.setText((int) weather7days.data.current.relHumidity + " %");
            sunrise.setText(String.valueOf(weather7days.data.daily.get(0).sun.sunrise.substring(11, 19)));
            sunset.setText(String.valueOf(weather7days.data.daily.get(0).sun.sunset.substring(11, 19)));

            buf.clear();
            for (Daily e : weather7days.data.daily) {
                buf.add(String.valueOf(e.rain));
            }

//        Intent intent = new Intent(this, ListActivity.class);
//        String message = "Temp: "+String.valueOf(weather7days.data.current.temperature);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
            //
        }
        return weather7days;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View menuItemView = findViewById(R.id.frameLayout);
                about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        PopUpClass popUpClass = new PopUpClass();
                        popUpClass.showPopupWindow(menuItemView);

                        return false;
                    }
                });
            }
        });



        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isOnline())
                    toastUpdate.show();
                else toast.show();
                if (isOnline()) {
                    if (quer.equals("")) {
                        getLocalWeather();
                        barChart.performClick();
                    }else {
                        getLocationCoordinates(getApplicationContext(),quer);
                        displayTemp(longitude, latitude);
                        barChart.performClick();
                    }
                }
                else toast.show();
                return false;
            }
        });



        getLocal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isOnline()) {
                    quer = "";
                    getLocalWeather();
                    updateChart("Rain (mm) next 7 days");
                    barChart.performClick();
                }
                else toast.show();

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
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            SearchView finalSearchView1 = searchView;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                public boolean onQueryTextSubmit(String query) {
                    if (isOnline()){
                        quer = query;
                        getLocationCoordinates(getApplicationContext(),query);
                        weather7days = displayTemp(longitude,latitude);
                        updateChart("Rain (mm) next 7 days");
                        barChart.performClick();
                        finalSearchView1.clearFocus();
                        finalSearchView1.setIconified(true);
                        finalSearchView1.onActionViewCollapsed();
                    }
                    else toast.show();

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
            viewMenu = findViewById(id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
