
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.rmit.geotracking.broadcast_receiver.NetworkReceiver;
import com.rmit.geotracking.database.SyncTrackingTask;
import com.rmit.geotracking.model.TrackManager;

public class StartUp extends Application {
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        //register connectivity broadcast
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        //import existing tracking data from SQLite
        TrackManager.getSingletonInstance(this);
        new Thread(new SyncTrackingTask(this));
      
        // set first alarm
        AlarmGenerator.getSingletonInstance(this).setAlarm();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(networkReceiver);

    }
}