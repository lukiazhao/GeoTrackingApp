package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.adapter.RouteListAdapter;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.model.TrackingInfoProcessor;
import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.view.TrackableActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewRouteListener implements View.OnClickListener {

    private int trackableID;
    private Context context;
    private TrackingInfoProcessor dataprocesser;

    public ViewRouteListener(Context context, int trackableID){
        this.trackableID = trackableID;
        this.context = context;
        dataprocesser = TrackManager.getSingletonInstance(context).getTrackingInfoProcessor();
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.route_dialog, null);

        TextView title2 = v.findViewById(R.id.route_trackablename);
        ListView routelv = v.findViewById(R.id.route_ListView);
        Button confirmbutton = v.findViewById(R.id.route_confirm);

        title2.setText(TrackManager.getSingletonInstance(context).getTrackableMap().get(trackableID).getName());
        List<String[]> routeList = dataprocesser.createRouteList(trackableID);

        if(routeList.size() != 0) {
            routelv.setAdapter(new RouteListAdapter(context, routeList));
        } else {
            ((TrackableActivity) context).showNoRouteToast();
            return;
        }
        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.show();
        confirmbutton.setOnClickListener(new DialogDismissListener(dialog));
    }
}
