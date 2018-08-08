package com.rmit.geotracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rmit.geotracking.service.TestTrackingService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTrackable(v);
            }
        });

        //create action bar
        Toolbar actionbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(actionbar);

        //Test tracking services
        //TestTrackingService.test(this);

        /* spinner **/
////        String[] data = new String[]{"Category first", "selection 2", "selection 3", "selection 4", "Category last"};
//        ArrayList<String> data = new ArrayList<>();
//        data.add("selection 1"); data.add("selection 3"); data.add("category last");
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter adapterSpin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
//        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapterSpin);
////        spinner.setSelection(adapterSpin.getCount());
//
//
//        /* Recycler view of trackables **/
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_trackables);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // create an adapter object
//        RecyclerAdapter adapter = new RecyclerAdapter(this);
//
//        // associate the recycler view with the adapter
//        recyclerView.setAdapter(adapter);
    }


    public void goTrackable (View v){
        Intent myIntent = new Intent(this, TrackableActivity.class);
        startActivity(myIntent);
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
        return super.onOptionsItemSelected(item);
    }
}
