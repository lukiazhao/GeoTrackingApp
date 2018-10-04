package com.rmit.geotracking.database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.SimpleTrackable;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class SyncTrackableTask implements Runnable {
    private final String LOG_TAG = this.getClass().getName();
    private Context context;

    public SyncTrackableTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        String db = "jdbc:sqldroid" + context.getDatabasePath("GeoTracking.db").getAbsolutePath();
        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
            Log.i(LOG_TAG, String.format("opening: %s", db));

            Connection con = DriverManager.getConnection(db);
            Statement st = con.createStatement();

            //import raw data to database if table not exist
            if(!checkTrackableExist(con)) {
                st.executeUpdate("CREATE TABLE trackable ( ID CHAR(5), Name VARCHAR(40), Description VARCHAR(200), URL VARCHAR(100), Category VARCHAR(40), PRIMARY KEY (ID))");
                Log.i(LOG_TAG, "Create table trackable");
                st.executeUpdate("CREATE TABLE tracking ( ID CHAR(10), TrackableID VARCHAR(40), " +
                        "Title VARCHAR(40), Starttime VARCHAR(40), Endtime VARCHAR(40), \" +\n" +
                        "                    \"Meettime VARCHAR(40), CurrentLocation VARCHAR(40), " +
                        "Meetlocation VARCHAR(40), PRIMARY KEY (ID))");
                Log.i(LOG_TAG, "Create table tracking");

                importTrackablesFromModel(st);
                st.close();
                con.close();
                Log.i(LOG_TAG, "Tables created");
                return;
            }

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importTrackablesFromModel(Statement st) throws SQLException {
        Map<Integer, Trackable> trackableMap = TrackManager.getSingletonInstance(context)
                .getTrackableMap();
        for (int key : trackableMap.keySet()) {
            String name = trackableMap.get(key).getName();
            String desc = trackableMap.get(key).getDescription();
            String url = trackableMap.get(key).getUrl();
            String category = trackableMap.get(key).getCategory();

            st.executeUpdate("Insert Into trackable VALUES('" + key + "', '" + name +
                    "', '" + desc + "', '" + url + "', '" + category + "')");
        }

        Log.i(LOG_TAG, "Finish save trackables from model");
    }

    private boolean checkTrackableExist(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null,null,  "TRACKABLE", null );
        return rs.next();
    }
}
