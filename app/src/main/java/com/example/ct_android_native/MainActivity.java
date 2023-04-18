package com.example.ct_android_native;

import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements DisplayUnitListener{
    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"P01","Your Channel Name","Your Channel Description", NotificationManager.IMPORTANCE_MAX,true);

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);
        CleverTapAPI.getDefaultInstance(this).pushEvent("Native Display");
        sliderView = findViewById(R.id.slider);
    }
    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        if (sliderDataArrayList != null) {
            sliderDataArrayList.removeAll(sliderDataArrayList);
        }
        Log.d("CleverTap123","test");

        CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDisplayUnitViewedEventForID(units.get(0).getUnitID());
        for (int i = 0; i < units.size(); i++) {
            CleverTapDisplayUnit unit = units.get(i);

            for (CleverTapDisplayUnitContent j : unit.getContents()) {
                //getting urls and adding to array list
                sliderDataArrayList.add(new SliderData(j.getMedia()));

                //Notification Clicked Event
                //sliderView.setOnClickListener(v -> CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDisplayUnitClickedEventForID(unit.getUnitID()));
                CleverTapAPI.getDefaultInstance(this).pushDisplayUnitClickedEventForID(units.get(0).getUnitID());

            }
            Log.d("Clevertap", "Native display" + units);
        }
        //CleverTapAPI.getDefaultInstance(this).pushDisplayUnitViewedEventForID(units.get(1).getUnitID());
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

}