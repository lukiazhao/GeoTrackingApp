package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class SyncTrackableListTask extends DatabaseHandleTask {
    private final String LOG_TAG = this.getClass().getName();

    public SyncTrackableListTask(Context context) {
        super(context);
    }

    @Override
    public void processing(Connection con, Statement st) throws SQLException {
        //import raw data to database if table not exist
        if(!checkTrackableExist(con)) {
            st.executeUpdate("CREATE TABLE " + TRACKABLE_TABLE + " ( ID CHAR(5), Name VARCHAR(40)," +
                    " Description VARCHAR(200), URL VARCHAR(100), Category VARCHAR(40), PRIMARY KEY (ID))");
            Log.i(LOG_TAG, "Create table trackable");

            importTrackablesFromModel(st);
            st.close();
            con.close();
            Log.i(LOG_TAG, "Tables created");
            return;
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

            st.executeUpdate("Insert Into " + TRACKABLE_TABLE + " VALUES('" + key + "', '" + name +
                    "', '" + desc + "', '" + url + "', '" + category + "')");
        }

        Log.i(LOG_TAG, "Finish save trackables from model");
    }

    private boolean checkTrackableExist(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null,null,  TRACKABLE_TABLE, null );
        return rs.next();
    }
}
