package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 * Represent all database handle related tasks in this application (remove, save, sync..)
 *
 * Provide based database connection function and store attributes names
 *
 * All implmentation should be defined in subclasses.
 */

public abstract class DatabaseHandleTask implements Runnable {
    private final String LOG_TAG = this.getClass().getName();
    public Context context;

    // Key names in SQL database
    final String TRACKING_TABLE = "TRACKING";
    final String TRACKING_ID = "ID";
    final String TRACKABLE_TABLE = "TRACKING";

    DatabaseHandleTask(Context context){
        this.context = context;
    }

    // open the sql database and close it after processing data
    @Override
    public void run() {
        String SQLDROID_NAME = "jdbc:sqldroid";
        String DATABASE_PATHNAME = "GeoTracking.db";
        String db = SQLDROID_NAME + context.getDatabasePath(DATABASE_PATHNAME).getAbsolutePath();

        try {
            String SQLDROID_DRIVER = "org.sqldroid.SQLDroidDriver";
            Class.forName(SQLDROID_DRIVER);
            Log.i(LOG_TAG, String.format("opening: %s", db));

            Connection con = DriverManager.getConnection(db);
            Statement st = con.createStatement();

            processing(con, st);

            st.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Process details handle in subclasses
    public abstract void processing(Connection con, Statement st) throws SQLException, ParseException;
}
