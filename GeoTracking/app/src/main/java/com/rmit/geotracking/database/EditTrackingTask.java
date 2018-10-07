package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

/**
 *  Called when close a modifytracking activity, all changed saved to database.
 *
 */

public class EditTrackingTask extends DatabaseHandleTask {
    private final String LOG_TAG = this.getClass().getName();

    public EditTrackingTask(Context context) {
        super(context);
    }

    @Override
    public void processing(Connection con, Statement st) throws SQLException {
        Map<String, Tracking> trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();

        for (String id : trackingMap.keySet()) {
            Tracking tracking = trackingMap.get(id);
            int trackableID = tracking.getTrackableId();
            String title = tracking.getTitle();
            Date starttime = tracking.getTargetStartTime();
            Date endtime = tracking.getTargetEndTime();
            Date meettime = tracking.getMeetTime();
            String currentlocation = tracking.getCurrentLocation();
            String meetlocation = tracking.getMeetLocation();

            ResultSet rs = st.executeQuery("SELECT * FROM " + TRACKING_TABLE +" where " +
                    TRACKING_ID + " = '" + id + "'");

            // when editing a row, delete the original row first
            if (rs.next()){
                st.executeUpdate("delete from " + TRACKING_TABLE +" where " +
                        TRACKING_ID + " = '" + id + "'");
                Log.i(LOG_TAG, "Execute tracking edit: " + id);
            }

            st.executeUpdate("Insert Into " + TRACKING_TABLE + " VALUES('" + id + "', '" +
                    trackableID + "', '" + title + "', '" + starttime.toString() + "', '" +
                    endtime.toString() + "', '" + meettime.toString() + "', '" +
                    currentlocation +  "', '" + meetlocation +  "')");
            Log.i(LOG_TAG, "Execute tracking insertion: " + id);
            rs.close();
        }
    }
}
