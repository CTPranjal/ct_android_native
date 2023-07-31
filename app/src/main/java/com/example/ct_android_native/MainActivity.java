package com.example.ct_android_native;

import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DisplayUnitListener{
    public Button btn;
    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"P01","Your Channel Name","Your Channel Description", NotificationManager.IMPORTANCE_MAX,true);
        clevertapDefaultInstance.setOptOut(true);
        btn=findViewById(R.id.b1);


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);
        CleverTapAPI.getDefaultInstance(this).pushEvent("Native Display");
        sliderView = findViewById(R.id.slider);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // each of the below mentioned fields are optional
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Name", "Ct Native");    // String
                profileUpdate.put("Identity", 1234567);      // String or number
                profileUpdate.put("Email", "ctnative@gmail.com"); // Email address of the user
                profileUpdate.put("Phone", "+917737051770");   // Phone (with the country code, starting with +)
                profileUpdate.put("Gender", "F");             // Can be either M or F
                // optional fields. controls whether the user will be sent email, push etc.
                profileUpdate.put("MSG-email", true);        // Disable email notifications
                profileUpdate.put("MSG-push", true);          // Enable push notifications
                profileUpdate.put("MSG-sms", true);          // Disable SMS notifications
                profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
                ArrayList<String> stuff = new ArrayList<String>();
                stuff.add("bag");
                stuff.add("shoes");
                profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings
                String[] otherStuff = {"Jeans","Perfume"};
                profileUpdate.put("MyStuff", otherStuff);                   //String Array

                clevertapDefaultInstance.onUserLogin(profileUpdate);
            }

        });
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