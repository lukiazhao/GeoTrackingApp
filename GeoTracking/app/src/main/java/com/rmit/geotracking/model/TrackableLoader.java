package com.rmit.geotracking.model;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.R;

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
        this.context = context;
        Log.i(LOG_TAG, "trackable loader constructor");
    }


    public Map<String, Trackable> readFile(){
        Map<String,Trackable> trackablesMap = new HashMap<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.food_truck_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {

            while ((line = bufferedReader.readLine()) != null) {
                String[] arrOfElement = line.split(",");
//                String id = arrOfElement[0];
//                String name = arrOfElement[1];
//                String desc = arrOfElement[2];

                // need to check if the length of the array is 5 or 6
                Trackable trackable = new SimpleTrackable(arrOfElement[0],
                                                    arrOfElement[1],
                                                    arrOfElement[2],
                                                    arrOfElement[3],
                                                    arrOfElement[4]);
                trackablesMap.put(arrOfElement[0], trackable);
                Log.i(LOG_TAG, trackable.getId());

            }
            Log.i(LOG_TAG, " size : " +trackablesMap.keySet().size());

        } catch (Exception e) {

        }
        return trackablesMap;

    }
}
