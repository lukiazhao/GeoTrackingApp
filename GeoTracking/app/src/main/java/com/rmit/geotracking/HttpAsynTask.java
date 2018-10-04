package com.rmit.geotracking;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.rmit.geotracking.controller.LocationMonitorListener;
import com.rmit.geotracking.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpAsynTask extends AsyncTask<String, Integer, Void> {
    private final String LOG_TAG = HttpAsynTask.class.getName();
    protected StringBuilder htmlStringBuilder = new StringBuilder();
    private int charsRead = 0;
    private MainActivity activity;

    public HttpAsynTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=-37.810045,144.964220&destinations=-37.810828,144.947005&key=AIzaSyDWSXxSpQiv5N68Ax3WT-FwUFrQ_a3dUbc");
            Log.i(LOG_TAG, url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader((new InputStreamReader(connection.getInputStream())));
                StringBuilder sr = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sr.append(line);
                }

                String json = sr.toString();
                Log.i(LOG_TAG, json);
                JSONObject root = new JSONObject(json);
                String destination = root.getString("destination_addresses");
                Log.i("Http des", destination);

                JSONArray array_rows = root.getJSONArray("rows");

            }

            // finished
//            publishProgress(100);



        } catch (Exception e) {
            e.printStackTrace();
        }

        finally
        {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... progress) {

        if (activity == null)
            Log.w(LOG_TAG, "onProgressUpdate() skipped -- no activity");
        else
        {
             Log.i(LOG_TAG, "Task progress=" + progress[0] + "%");
//             activity.updateProgress(progress[0]);
        }
    }
    protected void doProgress(int charsRead, int length)
    {
        this.charsRead += charsRead;
        // delay allows us to see progress on fast network!
        //Thread.sleep(1);
        // convert to percentage for progress update (standard 0..100 range)
        int progress = (int) ((double) this.charsRead / length * 100.0);
        Log.i(LOG_TAG, Integer.toString(progress) + "%");
        publishProgress(progress);       // still in background thread, but updateProgressUpdate (in UI thread) will run after publish progress
        // update on UI
    }



}
