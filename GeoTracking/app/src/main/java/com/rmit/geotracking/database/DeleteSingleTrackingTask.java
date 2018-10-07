package com.rmit.geotracking.database;

import android.content.Context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  Called when handle delete a single tracking during reminder notification shown up
 *
 */

public class DeleteSingleTrackingTask extends DatabaseHandleTask {

    private String trackingID;

    public DeleteSingleTrackingTask(String trackingID, Context context) {
        super(context);
        this.trackingID = trackingID;
    }

    @Override
    public void processing(Connection con, Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT * FROM " + TRACKING_TABLE +
                " where id = '" + trackingID + "'");
        if (rs.next()){
            st.executeUpdate("delete from " + TRACKING_TABLE + " where "+ TRACKING_ID
                    + " = '" + trackingID + "'" );
        }
    }
}
