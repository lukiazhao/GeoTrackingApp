package com.rmit.geotracking.database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.view.TrackingActivity;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncTrackingTask implements Runnable {
    private final String LOG_TAG = this.getClass().getName();
    private Context context;

    public SyncTrackingTask(Context context) {
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

            // creat the table tracking when first init the program
            if(!checkTrackingExist(con, st)) {
                st.executeUpdate("CREATE TABLE tracking ( ID CHAR(10), TrackableID VARCHAR(40), " +
                        "Title VARCHAR(40), Starttime VARCHAR(40), Endtime VARCHAR(40), \" +\n" +
                        "                    \"Meettime VARCHAR(40), CurrentLocation VARCHAR(40), " +
                        "Meetlocation VARCHAR(40), PRIMARY KEY (ID))");
                Log.i(LOG_TAG, "Create table tracking");
                st.close();
                con.close();
                Log.i(LOG_TAG, "Tables created");
                return;
            }

            ResultSet rs = st.executeQuery("SELECT * FROM tracking");

            TrackingInfoProcessor processor = TrackManager.getSingletonInstance(context)
                    .getTrackingInfoProcessor();

            while (rs.next()) {
                Tracking tracking =  TrackManager.getSingletonInstance(context)
                        .getTrackingMap().get(rs.getString(1));
                Log.i(LOG_TAG, String.format("Get tracking  " + (tracking == null)));

                if(tracking == null) {
                    TrackManager.getSingletonInstance(context).getTrackingManager()
                            .createTracking(rs.getString(1), Integer.parseInt(rs.getString(2)),
                                    rs.getString(3),
                                    processor.parseStringToDateDatabase(rs.getString(4)),
                                    processor.parseStringToDateDatabase(rs.getString(5)),
                                    processor.parseStringToDateDatabase(rs.getString(6)),
                                    rs.getString(7),
                                    rs.getString(8));
                }
            }

            st.close();
            rs.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkTrackingExist(Connection con, Statement st) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null,null,  "Tracking", null );
        return rs.next();
    }
}
