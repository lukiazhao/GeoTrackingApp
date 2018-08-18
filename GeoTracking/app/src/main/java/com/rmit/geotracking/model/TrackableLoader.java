package com.rmit.geotracking.model;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.service.TrackingService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrackableLoader {

//    private Map<String,Trackable> trackablesMap = new HashMap<>();
    private Context context;
    private String LOG_TAG = this.getClass().getName();

    public TrackableLoader(Context context){
//        this.context = context;
        Log.i(LOG_TAG, "trackable loader constructor");
    }


    public Map<Integer, Trackable> readFile(){
        Map<Integer,Trackable> trackablesMap = new HashMap<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.food_truck_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {

            while ((line = bufferedReader.readLine()) != null) {
                Trackable trackable;
                String[] arrOfElement = line.split(",");
                int id = Integer.parseInt(arrOfElement[0]);
                String name = arrOfElement[1];
                String desc = arrOfElement[2];
                String url = arrOfElement[3];
                String category = arrOfElement[4];

                if(arrOfElement.length == 5){
                    trackable = new SimpleTrackable(id, name, desc, url, category);
                } else {
                    trackable = new SimpleTrackable(id, name, desc, url, category,arrOfElement[5]);
                }

                trackablesMap.put(id, trackable);

                Log.i(LOG_TAG, trackable.getId() + "");

            }
            Log.i(LOG_TAG, " size : " +trackablesMap.keySet().size());

        } catch (Exception e) {

        }
        return trackablesMap;

    }
}
