package com.rmit.geotracking.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class PermissionHelper {
    private Activity activity;
    private View msgView;
    private int requestCode;
    private String[] permissions;
    private String rationale;
    private static final String LOG_TAG = PermissionHelper.class.getName();


    PermissionHelper(Activity activity, View msgView, int requestCode,
                     String rationale, String... permissions)
    {
        this.activity = activity;
        this.msgView = msgView;
        this.requestCode = requestCode;
        this.rationale = rationale;
        this.permissions = permissions;
    }

    /**
     * called with appropriate parameters from associated PermissionActivity
     *
     * @param requestCode - from AppCompatActivity.onRequestPermissionsResult()
     * @param permissions - as above
     * @param grantResults - as above
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        // check this is a valid response to a permission request issued by this class
        if (requestCode == this.requestCode && Arrays.equals(permissions, this.permissions))
        {
            if (verifyPermissions(grantResults))
                showPermissionMsg(getGrantedString());
            else
                showPermissionMsg(getDeniedString());
        }
    }

    /**
     * check the permissions and issue a runtime permission request if necessary
     * includes display additional context specific rationale as required
     *
     * @return true if the permission has been granted and you can proceed with the operation
     */
    public boolean checkPermission()
    {
        boolean showRationale = false;
        boolean checkPermissions = false;
        // check all permisions and set flags accordingly for following block
        for (String permission : permissions)
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED)
            {
                checkPermissions = true;
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                    showRationale = true;
            }

        if (checkPermissions)
        {
            // Provide additional rationale to the user if the permission was previoously not granted
            // and the user would benefit from additional context for the use of the permission
            // for example if the required permission is not obvious/implicit for the task
            if (showRationale)
                showRationaleMsg();
            else
                requestPermission();
            // need to wait for callback/permission to be set then try again
            return false;
        }
        // permission already granted
        return true;
    }

    // PRIVATE section (implementation only)

    // show a snackbar and log permission request outcome
    private void showPermissionMsg(String msg)
    {
        Log.i(LOG_TAG, msg);
        Snackbar.make(msgView, msg, Snackbar.LENGTH_SHORT).show();
    }

    // show a snackbar and log rationale
    private void showRationaleMsg()
    {
        Log.i(LOG_TAG, String.format("Displaying permission rationale: %s", rationale));
        Snackbar.make(msgView, rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, view -> requestPermission())
                .show();
    }

    // this will trigger the onRequestPermissionsResult() callback on the associated activity
    private void requestPermission()
    {
        Log.i(LOG_TAG, String.format("requesting: %s", getPermissionString()));
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * since we might have multiple permissions in a single request
     *
     * @param grantResults - permission statuses from onRequestPermissionsResult()
     * @return true if all permission granted
     */
    private boolean verifyPermissions(int[] grantResults)
    {
        // check all requested permissions
        for (int result : grantResults)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;

        // at least one result must have been checked
        return grantResults.length >= 1;
    }

    // some basic String generating helper methods
    private String getPermissionStringImpl(String label)
    {
        return String.format("%s %s", getPermissionString(), label);
    }

    private String getGrantedString()
    {
        return getPermissionStringImpl("granted");
    }

    private String getDeniedString()
    {
        return getPermissionStringImpl("denied");
    }

    // build a comma separated String for all requested permissions
    private String getPermissionString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < permissions.length; i++)
        {
            builder.append(permissions[i]);
            if (i != permissions.length - 1)
                builder.append(", ");
        }
        return builder.toString();
    }
}
