package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

public class SaveTrackingTask implements Runnable {

    private TrackManager manager;
    private Context context;
    private final String LOG_TAG = this.getClass().getName();

    public SaveTrackingTask(Context context) {
        this.context = context;
        this.manager = TrackManager.getSingletonInstance(context);
    }

    @Override
    public void run() {
        String db = "jdbc:sqldroid" + context.getDatabasePath("GeoTracking.db").getAbsolutePath();
        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
            Log.i(LOG_TAG, String.format("opening: %s", db));

            Connection con = DriverManager.getConnection(db);
            Statement st = con.createStatement();


            Map<String, Tracking> trackingMap = manager.getTrackingMap();

            for (String id : trackingMap.keySet()) {
                Tracking tracking = trackingMap.get(id);
                int trackableID = tracking.getTrackableId();
                String title = tracking.getTitle();
                Date starttime = tracking.getTargetStartTime();
                Date endtime = tracking.getTargetEndTime();
                Date meettime = tracking.getMeetTime();
                String currentlocation = tracking.getCurrentLocation();
                String meetlocation = tracking.getMeetLocation();

                ResultSet rs = st.executeQuery("SELECT * FROM tracking where id = '" + id + "'");

                // when editing a row, delete the original row first
                if (rs.next()){
                    st.executeUpdate("delete from tracking where id = '" + id + "'");
                    Log.i(LOG_TAG, String.format("Execute tracking edit: " + id));
                }

                st.executeUpdate("Insert Into tracking VALUES('" + id + "', '" + trackableID + "', '" + title +
                        "', '" + starttime.toString() + "', '" + endtime.toString() + "', '" + meettime.toString() + "', '" +
                        currentlocation +  "', '" + meetlocation +  "')");
                Log.i(LOG_TAG, String.format("Execute tracking insertion: " + id));
                rs.close();
            }

            st.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
