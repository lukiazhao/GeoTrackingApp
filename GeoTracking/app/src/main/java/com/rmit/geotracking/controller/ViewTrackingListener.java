package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.TrackingListAdapter;
import com.rmit.geotracking.model.SimpleTracking;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.TrackingActivity;

import java.util.Map;

public class ViewTrackingListener implements View.OnClickListener {

    private Context context;
 //   private int position;
    private Map<String, Tracking> trackingMap;
    private Tracking tracking;

    public ViewTrackingListener(Context context, Tracking tracking){
        this.context = context;
        this.tracking = tracking;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        trackingMap = TrackManager.getSingletonInstance(context).getTrackingMap();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mview = inflater.inflate(R.layout.check_tracking_dialog, null);

        TextView trackingdetails = mview.findViewById(R.id.trackingdetail_TextView);
        TextView title = (TextView) mview.findViewById(R.id.trackingdetail_title_TextView);
        Button confirmbutton = (Button) mview.findViewById(R.id.trackingdetail_Button);

        title.setText(tracking.getTitle());
        trackingdetails.setText(((TrackingActivity) context).generateDetailView(tracking));

        builder.setView(mview);
        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));
    }
}

