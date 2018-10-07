package com.rmit.geotracking.permission;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rmit.geotracking.R;

import java.util.HashMap;
import java.util.Map;

public abstract class GetPermissionActivity extends AppCompatActivity {

    @SuppressLint("UseSparseArrays")
    private Map<Integer, PermissionHelper> helpers = new HashMap<>();
    // store main layout for use by permissions Snackbar
    private View layout;

    // forward request codes to appropriate helper to handle
    // this is called automatically by the PermissionHelper when calling checkPermission()
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        // the only thing we need to do is match the request code
        // to the correct PermissionHelper instance
        for (Integer nextRequest : helpers.keySet())
            if (requestCode == nextRequest)
                helpers.get(requestCode).onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
    }

    /**
     * @param requestCode - the permission request code you want to check
     * @return - true if the permission has been granted by the user/system
     */
    protected boolean checkPermission(int requestCode)
    {
        PermissionHelper helper=helpers.get(requestCode);
        return helper!=null && helper.checkPermission();
    }

    /**
     * @param requestCode - the any arbitrary unique request code (used by checkPermission())
     * @param permissions - variable length arg of String permissions to request
     */
    protected void addPermissionHelper(int requestCode, String... permissions)
    {
        // lazy initialisation and pre-condition checking
        if(layout == null)
        {
            layout = findViewById(R.id.main_view);
            if (layout == null)
                throw new IllegalArgumentException("Root view must contain a permission_view ID in the layout hierarchy");
        }
        helpers.put(requestCode, new PermissionHelper(this, layout, requestCode,
                "REQUEST LOCATION PERMISSION", permissions));
    }
}
