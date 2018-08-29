package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.model.Tracking;

public class RemoveTrackingDialogListener implements AdapterView.OnItemLongClickListener {

    private Context context;
    private BaseAdapter adapter;

    public RemoveTrackingDialogListener(Context context, BaseAdapter adapter){
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("Long click" + adapterView.getItemAtPosition(i).toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(((Tracking)adapterView.getItemAtPosition(i)).getTitle())
                .setMessage(context.getResources().getString(R.string.removetracking_message))
                .setPositiveButton(android.R.string.yes, new RemoveTrackingListener(context, (Tracking)adapterView.getItemAtPosition(i), adapter))
                .setNegativeButton(android.R.string.no, null)
                .show();

        return true;
    }
}
