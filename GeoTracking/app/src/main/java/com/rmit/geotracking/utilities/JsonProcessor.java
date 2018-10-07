package com.rmit.geotracking.utilities;

import android.location.Location;
import android.util.Log;

import com.rmit.geotracking.service.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This helper class process sending json request receiving and parsing json
 */

public class JsonProcessor {

    private final String LOG_TAG = LocationService.class.getName();
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial";
    private static final String PUNCTATION =  "&";
    private static final String apiKey = "key=AIzaSyDWSXxSpQiv5N68Ax3WT-FwUFrQ_a3dUbc";
    private static final String mode = "mode=walking";


    public JSONObject getJson(Location currLocation, String destination){
        HttpURLConnection connection = null;
        JSONObject json = null;
        StringBuilder sb = buildUrl(currLocation, destination);

        try {

            URL url = new URL(sb.toString());
            Log.i(LOG_TAG, url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setReadTimeout(15000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setDoInput(true);

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader((new InputStreamReader(connection.getInputStream())));


                StringBuilder sr = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sr.append(line);
                }

                String jsonString = sr.toString();

                System.out.println(jsonString);
                json = new JSONObject(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally
        {
            if (connection != null)
                connection.disconnect();
        }
        return json;
    }

    // build the url with current location and destination location
    private StringBuilder buildUrl(Location currLocation, String destination){
        String origins = "origins=" + currLocation.getLatitude() + "," + currLocation.getLongitude();
        StringBuilder sb = new StringBuilder();

        sb.append(BASE_URL);
        sb.append(PUNCTATION);
        sb.append(origins);
        sb.append(PUNCTATION);
        sb.append("destinations=");
        sb.append(destination);
        sb.append(PUNCTATION);
        sb.append(mode);
        sb.append(PUNCTATION);
        sb.append(apiKey);

        return sb;
    }

    public String parseJson(JSONObject jsonObject) throws JSONException {
        JSONArray rows = (JSONArray) jsonObject.get("rows");
        JSONObject jsonObject1 = rows.getJSONObject(0);
        JSONArray elements = jsonObject1.getJSONArray("elements");

        JSONObject element = elements.getJSONObject(0);
        JSONObject durationObj = element.getJSONObject("duration");
        String duration = durationObj.getString("text");
        Log.i(LOG_TAG, "duration" + duration);
        return duration;

    }

}
