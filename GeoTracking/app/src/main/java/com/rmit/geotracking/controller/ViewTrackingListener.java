package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.Tracking;
import com.rmit.geotracking.view.TrackingActivity;

public class ViewTrackingListener implements View.OnClickListener {

    private Context context;
 //   private int position;
    private Tracking tracking;
    private Trackable trackable;

    public ViewTrackingListener(Context context, Tracking tracking){
        this.context = context;
        this.tracking = tracking;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View trackingview = inflater.inflate(R.layout.info_dialog, null);

        TextView trackingdetails = trackingview.findViewById(R.id.info_TextView);
        TextView title = (TextView) trackingview.findViewById(R.id.info_title_TextView);
        Button confirmbutton = (Button) trackingview.findViewById(R.id.info_button);

        title.setText(tracking.getTitle());
        trackingdetails.setText(((TrackingActivity) context).generateDetailView(tracking));

        builder.setView(trackingview);
        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));
    }
}

