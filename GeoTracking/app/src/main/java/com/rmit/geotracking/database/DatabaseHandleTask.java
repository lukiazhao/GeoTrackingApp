package com.rmit.geotracking.database;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

    public final String SQLDROID_NAME = "jdbc:sqldroid";
    public final String DATABASE_PATHNAME = "GeoTracking.db";
    public final String SQLDROID_DRIVER = "org.sqldroid.SQLDroidDriver";

    // rows in tracking table
    public final String TRACKING_TABLE = "TRACKING";
    public final String TRACKING_ID = "ID";


    public final String TRACKABLE_TABLE = "TRACKING";


    public DatabaseHandleTask(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        String db = SQLDROID_NAME + context.getDatabasePath(DATABASE_PATHNAME).getAbsolutePath();
        try {
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

    public abstract void processing(Connection con, Statement st) throws SQLException, ParseException;
}
