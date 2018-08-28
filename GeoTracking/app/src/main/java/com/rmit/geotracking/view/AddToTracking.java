package com.rmit.geotracking.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.controller.AddTrackingListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class AddToTracking extends AppCompatActivity {

    private static final String TAG = "AddToTrackingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_tracking);





        final String trackableId = getIntent().getStringExtra("Trackable_id");

        Log.i(TAG, trackableId);
        TextView itemName = findViewById(R.id.item_name);
        itemName.setText("Trackable Name: " + trackableId);

        Button finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new AddTrackingListener(this, trackableId));

    }



}
