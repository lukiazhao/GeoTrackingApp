package com.rmit.geotracking.database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rmit.geotracking.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncTrackableTask implements Runnable {
    private final String LOG_TAG = this.getClass().getName();
    private Context context;

    public SyncTrackableTask(Context context) {
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

                //import raw data to database if table not exist
                if(!checkTrackableExist(con)) {
                    st.executeUpdate("CREATE TABLE trackable ( ID CHAR(5), Name VARCHAR(40), Description VARCHAR(200), URL VARCHAR(100), Category VARCHAR(40), PRIMARY KEY (ID))");
                    Log.i(LOG_TAG, "Create table trackable");
                    importRawTrackableList(st);
                    st.close();
                    con.close();
                    Log.i(LOG_TAG, "Tables created");
                    return;
                }

                st.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    private void importRawTrackableList(Statement st) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.food_truck_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        try {

            while ((line = bufferedReader.readLine()) != null) {
                String[] arrOfElement = line.split(",\"");

                int id = Integer.parseInt(arrOfElement[0]);
                String name = arrOfElement[1].replaceAll("\"", "");
                String desc = arrOfElement[2].replaceAll("\"", "");
                String url = arrOfElement[3].replaceAll("\"", "");
                String category = arrOfElement[4].replaceAll("\"", "");

                st.executeUpdate("Insert Into trackable VALUES('" + id + "', '" + name +
                        "', '" + desc + "', '" + url + "', '" + category + "')");
            }

            Log.i(LOG_TAG, "Finish loading");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkTrackableExist(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null,null,  "TRACKABLE", null );
        return rs.next();
    }
}
