package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RemoveSingleTrackingTask implements Runnable {
    private final String LOG_TAG = this.getClass().getName();

    private String trackingID;
    private Context context;

    public RemoveSingleTrackingTask(String trackingID, Context context) {
        this.trackingID = trackingID;
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

            ResultSet rs = st.executeQuery("SELECT * FROM tracking where id = '" + trackingID + "'");
            if (rs.next()){
                st.executeUpdate("delete from tracking where id = '" + trackingID + "'" );
            }

            st.close();
            rs.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
