package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.adapters.AdapterClickListener;
import com.example.myapplication.adapters.CityListAdapter;
import com.example.myapplication.models.CityDataModel;
import com.example.myapplication.models.SunriseResponseModel;
import com.example.myapplication.network.SunriseSunsetApi;
import com.example.myapplication.widgets.CustomAnalogClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterClickListener {

    private static final String API_BASE_URL = "http://api.sunrise-sunset.org";
    private static final long DAY_LENGTH_IN_SECONDS = 24 * 60 * 60;
    private View mContainer;
    private CustomAnalogClock mAnalogClock;
    private List<CityDataModel> mCityList;

    private Callback<SunriseResponseModel> mListener = new Callback<SunriseResponseModel>() {
        @Override
        public void onResponse(Call<SunriseResponseModel> call, Response<SunriseResponseModel> response) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZ", Locale.US);
            try {
                Date date = format.parse(response.body().getResults().getSunrise());
                Calendar sunriseCalendar = Calendar.getInstance();
                sunriseCalendar.setTime(date);

                date = format.parse(response.body().getResults().getSunset());
                Calendar sunsetCalendar = Calendar.getInstance();
                sunsetCalendar.setTime(date);

                int clockHour = mAnalogClock.getHour();
                int clockMinute = mAnalogClock.getMinute();
                Calendar clockCalendar = Calendar.getInstance();
                clockCalendar.setTime(date);
                clockCalendar.set(Calendar.HOUR_OF_DAY, clockHour);
                clockCalendar.set(Calendar.MINUTE, clockMinute);

                if ((sunriseCalendar.before(sunsetCalendar) &&
                        clockCalendar.after(sunriseCalendar) &&
                        clockCalendar.before(sunsetCalendar)) ||
                        (sunriseCalendar.after(sunsetCalendar) &&
                                clockCalendar.after(sunriseCalendar))) {
                    long elapsedInSeconds = (clockCalendar.getTimeInMillis() - sunriseCalendar.getTimeInMillis()) / 1000;
                    if (elapsedInSeconds < (response.body().getResults().getDay_length() / 2)) {
                        int color = 255 - (int) (((double) elapsedInSeconds / (response.body().getResults().getDay_length() / 2)) * 255);
                        setBackgroundColor(255, 255, color);
                    } else {
                        int color = 255 - (int) (((double) (elapsedInSeconds -
                                (response.body().getResults().getDay_length() / 2)) /
                                (response.body().getResults().getDay_length() / 2)) * 255);
                        setBackgroundColor(color, color, 0);
                    }
                } else {
                    long nightLengthInSeconds = DAY_LENGTH_IN_SECONDS - response.body().getResults().getDay_length();
                    long elapsedInSeconds = Math.abs(clockCalendar.getTimeInMillis() - sunsetCalendar.getTimeInMillis()) / 1000;
                    int color = (int) ((double) elapsedInSeconds / nightLengthInSeconds * 255);
                    setBackgroundColor(color, color, color);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<SunriseResponseModel> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Retrofit Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void setBackgroundColor(int r, int g, int b) {
        mContainer.setBackgroundColor(Color.rgb(r, g, b));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initViews() {
        mContainer = findViewById(R.id.activity_main_parent_layout);
        mAnalogClock = (CustomAnalogClock) findViewById(R.id.activity_main_analog_clock);
        RecyclerView list = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new CityListAdapter(mCityList, this));
    }

    private void initData() {
        mCityList = new ArrayList<>();
        mCityList.add(new CityDataModel("Tehran", 35.6961, 51.4231, 210));
        mCityList.add(new CityDataModel("Beijing", 39.9167, 116.3833, 480));
        mCityList.add(new CityDataModel("New Delhi", 28.6139, 77.2090, 330));
        mCityList.add(new CityDataModel("Brasilia", 15.7939, 47.8828, -180));
        mCityList.add(new CityDataModel("Ankara", 39.9333, 32.8667, 120));
    }

    @Override
    public void onItemClick(View view, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SunriseSunsetApi service = retrofit.create(SunriseSunsetApi.class);
        service.getData(mCityList.get(position).getLatitude(), mCityList.get(position).getLongitude(), 0)
                .enqueue(mListener);
        TimeZone zone = TimeZone.getDefault();
        Date now = new Date();
        int offset = zone.getOffset(now.getTime()) / 60000;
        mAnalogClock.setMinuteDiff(mCityList.get(position).getZoneMinuteDiff() - offset);
    }
}
