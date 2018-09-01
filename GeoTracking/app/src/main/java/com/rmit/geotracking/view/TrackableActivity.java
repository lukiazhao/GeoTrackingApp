package com.rmit.geotracking.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.RouteListAdapter;
import com.rmit.geotracking.adapter.TrackableListAdapter;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.controller.DialogDismissListener;
import com.rmit.geotracking.controller.SortCategoryListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;

import java.util.List;
import java.util.Map;

/**
 * This activity is mainly for providing UI to let user viewing all trackables and
 * providing AlertDialog functions
 *
 * Related UI components such as Buttons are also created with this activity.
 *
 */

public class TrackableActivity extends MainActivity {

    private TrackManager trackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_trackablelist));
        trackManager = TrackManager.getSingletonInstance(this);
        setContentView(R.layout.activity_trackable_list);

        Map<Integer, Trackable> trackableMap = trackManager.getTrackableMap();
        ListView listView = findViewById(R.id.trackable_list);
        // add spinner
        loadSpinner();
        // set adapter into list view
        listView.setAdapter(TrackableListAdapter.getSingletonInstance(this));
    }

    public void loadSpinner() {

        List<String> category = trackManager.readAllCategories();

        // get reference of widgets from xml layout.
        Spinner spinner = findViewById(R.id.spinner);

        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, category);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(SortCategoryListener.getSingletonInstance(this));
    }

    // Call when user click view button for a specific trackable item.
    public void showRouteDialog(int trackableID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.route_dialog, null);

        TextView title2 = v.findViewById(R.id.route_trackablename);
        ListView routelv = v.findViewById(R.id.route_ListView);
        Button confirmbutton = v.findViewById(R.id.route_confirm);

        title2.setText(TrackManager.getSingletonInstance(this).getTrackableMap().get(trackableID).getName());
        List<String[]> routeList = trackManager.getTrackingInfoProcessor().createRouteList(trackableID);

        if (routeList.size() != 0) {
            routelv.setAdapter(RouteListAdapter.getSingletonInstance(this, routeList));
        } else {
            this.showNoTrackingInfoAlertDialog();
            return;
        }
        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));
    }

    //This app will block user to view or add upon a trackable without any route info
    public void showNoTrackingInfoAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getResources().getString(R.string.trackablelist_noinfo))
                    .setNeutralButton(this.getResources()
                            .getString(R.string.viewtracking_confirmButton), null)
                    .setCancelable(false).show();
    }

    //Old trackable activity would be finished automaticly
    //to let the user back to home page.
    public void onRestart() {
        super.onRestart();
        this.finish();
    }
}
