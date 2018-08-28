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

import static java.lang.Integer.parseInt;

public class AddToTrackingActivity extends AppCompatActivity {

    private static final String TAG = "AddToTrackingActivity";

    private Integer selectedTrackableId;
    private String selectedTrackableName;

    public AddToTrackingActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_tracking);

        //get incoming intent's data
        getIncomingIntentExtras();

        // set intent data to text (trackable name is shown as title for customer to refer to)
        TextView itemName = findViewById(R.id.item_name);
        itemName.setText(this.selectedTrackableName);

        // set listener to "finish" button
        Button finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new AddTrackingListener(this, this.selectedTrackableId));

    }

    public void getIncomingIntentExtras() {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            this.selectedTrackableId = extras.getInt("Trackable_Id");
            this.selectedTrackableName = extras.getString("Trackable_Name");
        }
    }

    public TextView getTrackingTitle(){
        TextView title = findViewById(R.id.edit_title);
//        String title1 = title.getText().toString();
        return title;
    }

    

}
