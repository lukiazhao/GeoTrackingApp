package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RemoveTrackingTask implements Runnable {

    private Context context;
    private final String LOG_TAG = this.getClass().getName();

    public RemoveTrackingTask(Context context) {
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

            ResultSet rs = st.executeQuery("SELECT * FROM tracking ");
            while (rs.next()){
                String trackingID = rs.getString(1);
                if(trackingDeletedFromModel(trackingID)) {
                    st.executeUpdate("delete from tracking where id = '" + trackingID + "'" );
                }
                Log.i(LOG_TAG, String.format("Delete tracking: " + trackingID));
            }

            rs.close();
            st.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private boolean trackingDeletedFromModel(String trackingID){
        boolean deleted = true;
        for (String id : TrackManager.getSingletonInstance(context).getTrackingMap().keySet()) {
            if (id.equals(trackingID)) {
                deleted = false;
            }
        }

        return deleted;
    }
}
