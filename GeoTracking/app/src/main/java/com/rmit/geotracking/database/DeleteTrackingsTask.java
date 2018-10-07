package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeleteTrackingsTask extends DatabaseHandleTask {
    private final String LOG_TAG = this.getClass().getName();
    private ArrayList<String> deletedIDs;

    public DeleteTrackingsTask(Context context) {
        super(context);
    }

    @Override
    public void processing(Connection con, Statement st) throws SQLException {
        deletedIDs = new ArrayList<>();

        ResultSet rs = st.executeQuery("SELECT * FROM " + TRACKING_TABLE);
        while (rs.next()){
            String trackingID = rs.getString(1);
            if(trackingDeletedFromModel(trackingID)) {
                deletedIDs.add(trackingID);
                Log.i(LOG_TAG, String.format("Add delete " + TRACKING_ID + ": " + trackingID));
            }
        }

        deleteAllTrackingInList(st);
    }

    private boolean trackingDeletedFromModel(String trackingID){
        boolean deleted = true;
        for (String id : TrackManager.getSingletonInstance(context).getTrackingMap().keySet()) {
            if (id.equals(trackingID)) {
                deleted = false;
                Log.i(LOG_TAG, String.format("Delete check true: " + trackingID));
            }
        }

        return deleted;
    }

    private void deleteAllTrackingInList(Statement st) throws SQLException {
        for(String id : deletedIDs) {
            st.executeUpdate("delete from " + TRACKING_TABLE + " where " + TRACKING_ID
                    + " = '" + id + "'" );
        }
    }
}
