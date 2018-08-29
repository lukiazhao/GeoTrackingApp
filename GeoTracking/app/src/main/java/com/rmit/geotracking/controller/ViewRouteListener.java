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
import com.rmit.geotracking.model.Trackable;
import com.rmit.geotracking.service.TrackingService;
import com.rmit.geotracking.view.TrackableActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewRouteListener implements View.OnClickListener {

    private Trackable trackable;
    private List<TrackingService.TrackingInfo> trackingInfoList;
    private Context context;
    private boolean hasInfo;


    public ViewRouteListener(Context context, Trackable trackable){
        this.trackable = trackable;
        this.context = context;
        trackingInfoList = TrackingService.getSingletonInstance(context).getTrackingInfoList();
        hasInfo = false;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.route_dialog, null);

        TextView title1 = (TextView) v.findViewById(R.id.route_title);
        TextView title2 = (TextView) v.findViewById(R.id.route_trackablename);
        ListView routelv = (ListView) v.findViewById(R.id.route_ListView);
        Button confirmbutton = (Button) v.findViewById(R.id.route_confirm);

        title2.setText(trackable.getName());
        List<String> routeList = createRouteList();

        if(hasInfo) {
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

    public List<String> createRouteList() {
        List<String> routelist = new ArrayList<String>();

        for(TrackingService.TrackingInfo trackingInfo : trackingInfoList) {
            if(trackingInfo.trackableId == trackable.getId()) {
                routelist.add(trackingInfo.latitude + "  " + trackingInfo.longitude);
                hasInfo = true;
            }
        }

        return routelist;
    }
}
