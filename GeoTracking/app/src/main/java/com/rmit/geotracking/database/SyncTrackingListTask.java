package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.model.TrackingInfoProcessor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 *  Called when start up the app and every time open tracking list activity.
 *
 *  Sync data from the model and in tracking list on the screen and model;
 *
 */

public class SyncTrackingListTask extends DatabaseHandleTask {
    private final String LOG_TAG = this.getClass().getName();

    // Info processor for parse date to string.
    private TrackingInfoProcessor processor;

    public SyncTrackingListTask(Context context) {
        super(context);
    }

    @Override
    public void processing(Connection con, Statement st) throws SQLException, ParseException {
        // creat the table tracking when first init the program
        if(!checkTrackingExist(con)) {
            st.executeUpdate("CREATE TABLE " + TRACKING_TABLE + " ( " + TRACKING_ID +
                    " CHAR(10), TrackableID VARCHAR(40), " +
                    "Title VARCHAR(40), Starttime VARCHAR(40), Endtime VARCHAR(40), \" +\n" +
                    "                    \"Meettime VARCHAR(40), CurrentLocation VARCHAR(40), " +
                    "Meetlocation VARCHAR(40), PRIMARY KEY (ID))");
            Log.i(LOG_TAG, "Create table tracking");
            st.close();
            con.close();
            Log.i(LOG_TAG, "Tables created");
            return;
        }

        ResultSet rs = st.executeQuery("SELECT * FROM " + TRACKING_TABLE);

        processor = TrackManager.getSingletonInstance(context)
                .getTrackingInfoProcessor();

        while (rs.next()) {
            Tracking tracking =  TrackManager.getSingletonInstance(context)
                    .getTrackingMap().get(rs.getString(1));

            if(tracking == null) {
                saveSingleTrackingToModel(rs);
            }
        }
        rs.close();
    }

    // Check if the tracking table already exist in the database.
    private boolean checkTrackingExist(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null,null,  TRACKING_TABLE, null );
        return rs.next();
    }

    // Save a tracking from data to model
    private void saveSingleTrackingToModel(ResultSet rs) throws SQLException, ParseException {
        Tracking tracking =  TrackManager.getSingletonInstance(context)
                .getTrackingMap().get(rs.getString(1));
        Log.i(LOG_TAG, "Get tracking  " + (tracking == null));

        if(tracking == null) {
            TrackManager.getSingletonInstance(context).getTrackingManager()
                    .createTracking(rs.getString(1), Integer.parseInt(rs.getString(2)),
                            rs.getString(3),
                            processor.parseStringToDateDatabase(rs.getString(4)),
                            processor.parseStringToDateDatabase(rs.getString(5)),
                            processor.parseStringToDateDatabase(rs.getString(6)),
                            rs.getString(7),
                            rs.getString(8));
            Log.i(LOG_TAG, "Add tracking to model: [title] " +
                    rs.getString(3) + " [ID] + " + rs.getString(1));
        }
    }
}
