package com.rmit.geotracking.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.controller.DialogDismissListener;
import com.rmit.geotracking.controller.RemoveTrackingDialogListener;
import com.rmit.geotracking.database.RemoveTrackingTask;
import com.rmit.geotracking.database.SaveTrackingTask;
import com.rmit.geotracking.database.SyncTrackingTask;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

import java.util.Objects;

/**
 * This activity is mainly for providing UI to let user viewing all trackings and
 * providing AlertDialog functions
 *
 * Related UI components such as Buttons are also created with this activity.
 *
 */

public class TrackingActivity extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.actionbar_trackinglist));
        setContentView(R.layout.activity_tracking);
        loadListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new SyncTrackingTask(this)).run();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new Thread(new RemoveTrackingTask(this)).run();
    }

    public void loadListView() {
        ListView trackingView = findViewById(R.id.tracking_list);
        trackingView.setAdapter(new TrackingListAdapter(this));
        trackingView.setOnItemLongClickListener(RemoveTrackingDialogListener.getSingletonInstance(this));
    }

    //Show user all detail info related to tracking in a dialog
    public void viewTrackingView(Tracking tracking){
        Builder builder = new Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("InflateParams") View trackingview = inflater.inflate(R.layout.info_dialog, null);

        TextView trackingdetails = trackingview.findViewById(R.id.info_TextView);
        TextView title = trackingview.findViewById(R.id.info_title_TextView);
        Button confirmbutton = trackingview.findViewById(R.id.info_button);

        title.setText(tracking.getTitle());
        trackingdetails.setText(this.generateDetailView(tracking));

        builder.setView(trackingview);
        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));
    }

    //Tool method to help generate text in view tracking dialog.
    public String generateDetailView(Tracking tracking){
        TrackManager trackManager = TrackManager.getSingletonInstance(this);
        String[] sections = generateTracingDetailSections();
        @SuppressLint("DefaultLocale") String output = String.format("%s:   %d\n\n%s:   %s\n\n" +
                        "%s:   %s\n\n" + "%s:   %s\n\n%s:   %s\n\n%s:   %s\n\n%s:   %s\n\n%s:   %s",
                sections[0], tracking.getTrackableId(),
                sections[1], tracking.getTrackingId(),
                sections[2], tracking.getTitle(),
                sections[3], trackManager.getTrackingInfoProcessor().getFormatedDate(tracking.getMeetTime()),
                sections[4], trackManager.getTrackingInfoProcessor().getFormatedDate(tracking.getTargetStartTime()),
                sections[5], trackManager.getTrackingInfoProcessor().getFormatedDate(tracking.getTargetEndTime()),
                sections[6], tracking.getMeetLocation(),
                sections[7], tracking.getCurrentLocation());
        //output.
        return output;
    }

    //Tool method to help generate string from resources.
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

    //Old tracking activity would be finished automaticly
    //to let the user back to home page.
    public void onRestart(){
        super.onRestart();
        this.finish();
    }
}
