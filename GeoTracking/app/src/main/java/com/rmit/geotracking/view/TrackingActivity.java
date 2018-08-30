package com.rmit.geotracking.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.controller.DialogDismissListener;
import com.rmit.geotracking.controller.EditTrackingListener;
import com.rmit.geotracking.controller.RemoveTrackingDialogListener;
import com.rmit.geotracking.controller.ViewTrackingListener;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.MainActivity;
import com.rmit.geotracking.model.Tracking;

import java.util.Map;


public class TrackingActivity extends MainActivity {

    private static final String LOG_TAG = "TrackingActivity";
    private TrackManager trackManager;
    private ListView trackingView;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_trackinglist));
        trackManager = TrackManager.getSingletonInstance(this);
        setContentView(R.layout.activity_tracking);
        Log.i(LOG_TAG, "start");
        loadListView();
    }

    public void loadListView(){
        Map<String, Tracking> trackingMap = trackManager.getTrackingMap();
        adapter = new TrackingListAdapter(this, trackManager);
        trackingView = findViewById(R.id.tracking_list);
        trackingView.setAdapter(adapter);
        trackingView.setOnItemLongClickListener(new RemoveTrackingDialogListener(this, adapter));
    }

    public void viewTrackingView(Tracking tracking){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View trackingview = inflater.inflate(R.layout.info_dialog, null);

        TextView trackingdetails = trackingview.findViewById(R.id.info_TextView);
        TextView title = (TextView) trackingview.findViewById(R.id.info_title_TextView);
        Button confirmbutton = (Button) trackingview.findViewById(R.id.info_button);

        title.setText(tracking.getTitle());
        trackingdetails.setText(this.generateDetailView(tracking));

        builder.setView(trackingview);
        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));

    }

    public String generateDetailView(Tracking tracking){
        String[] sections = generateTracingDetailSections();
        @SuppressLint("DefaultLocale") String output = String.format("%s:   %d\n\n%s:   %s\n\n" +
                        "%s:   %s\n\n" + "%s:   %s\n\n%s:   %s\n\n%s:   %s\n\n%s:   %s\n\n%s:   %s",
                sections[0], tracking.getTrackableId(),
                sections[1], tracking.getTrackingId(),
                sections[2], tracking.getTitle(),
                sections[3], tracking.getMeetTime(),
                sections[4], tracking.getTargetStartTime(),
                sections[5], tracking.getTargetEndTime(),
                sections[6], tracking.getMeetLocation(),
                sections[7], tracking.getCurrentLocation());
        //output.
        return output;
    }

    public String[] generateTracingDetailSections(){
        Resources resources = getResources();
        String[] sections = new String[8];
        sections[0] = resources.getString(R.string.viewtracking_trackableID);
        sections[1] = resources.getString(R.string.viewtracking_trackingID);
        sections[2] = resources.getString(R.string.viewtracking_title);
        sections[3] = resources.getString(R.string.viewtracking_meettime);
        sections[4] = resources.getString(R.string.viewtracking_starttime);
        sections[5] = resources.getString(R.string.viewtracking_endtime);
        sections[6] = resources.getString(R.string.viewtracking_meetlocation);
        sections[7] = resources.getString(R.string.viewtracking_currentlocation);
        return sections;
    }
}
