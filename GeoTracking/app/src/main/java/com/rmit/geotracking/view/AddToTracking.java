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

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_tracking);



        mDisplayDate = (TextView) findViewById(R.id.select_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddToTracking.this, android.R.style.Theme_Holo_Dialog,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + (month+1) + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime = (TextView) findViewById(R.id.select_time);

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                NumberFormat formatter = new DecimalFormat("00");
                String time = formatter.format(hour) + " : " + formatter.format(minute);
                mDisplayTime.setText(time);
            }
        };

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int minute = cal.get(cal.MINUTE);
                int hour = cal.get(cal.HOUR);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddToTracking.this, android.R.style.Theme_Holo_Dialog,
                        mTimeSetListener, minute, hour, true);
                timePickerDialog.show();
            }
        });



        final String trackableId = getIntent().getStringExtra("Trackable_id");

        Log.i(TAG, trackableId);
        TextView itemName = findViewById(R.id.item_name);
        itemName.setText("Trackable Name: " + trackableId);

        Button finishButton = (Button) findViewById(R.id.finish_button);
//        finishButton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(AddToTracking.this, "clicked: " + trackableId, Toast.LENGTH_SHORT).show();
//            }
//        });

        finishButton.setOnClickListener(new AddTrackingListener(this, trackableId));
//

    }



}
