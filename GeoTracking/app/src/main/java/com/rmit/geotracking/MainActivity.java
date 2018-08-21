package com.rmit.geotracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.TrackManager;
import com.rmit.geotracking.service.TestTrackingService;
import com.rmit.geotracking.view.TrackableActivity;
import com.rmit.geotracking.view.TrackingActivity;

public class MainActivity extends AppCompatActivity {

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TrackManager trackManager = TrackManager.getSingletonInstance(this);

        View trackableBut = findViewById(R.id.trackable_button);
        View trackingBut = findViewById(R.id.tracking_button);

        trackableBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTrackable();
            }
        });

        trackingBut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goTracking();
            }
        });

        //create action bar
//        Toolbar actionbar = (Toolbar) findViewById(R.id.action_bar);
//        setSupportActionBar(actionbar);

        //Test tracking services
//        TestTrackingService.test(this);

    }

    //Import methods related to menu options and selections
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trackable_list:
                goTrackable();
                //Test
                Toast.makeText(this, "trackable list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tracking_list:
                goTracking();
                Toast.makeText(this, "tracking list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_tracking:
                Toast.makeText(this, "add tracking", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }


    public void goTrackable (){
        Intent myIntent = new Intent(this, TrackableActivity.class);
        startActivity(myIntent);
    }

    public void goTracking (){
        Intent myIntent = new Intent(this, TrackingActivity.class);
        startActivity(myIntent);
    }


}
