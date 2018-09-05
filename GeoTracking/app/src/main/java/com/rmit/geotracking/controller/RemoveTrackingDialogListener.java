package com.rmit.geotracking.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.rmit.geotracking.R;
import com.rmit.geotracking.model.Tracking;

/**
 * A long click listener registered on each item in Tracking List .
 *
 * After the user long click the item, a dialog will pop-up for comfirmation.
 *
 */
public class RemoveTrackingDialogListener implements AdapterView.OnItemLongClickListener {

    private static Context context;

    private static class LazyHolder
    {
        static final RemoveTrackingDialogListener INSTANCE = new RemoveTrackingDialogListener();
    }

    // singleton
    public static RemoveTrackingDialogListener getSingletonInstance(Context context)
    {
        RemoveTrackingDialogListener.context = context;
        return LazyHolder.INSTANCE;
    }

    // An addtional remove tracking listener is registered on confirm button in the dialog.
    // A further remove action will perform if positive button clicked to remove tracking in the model.
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(((Tracking)adapterView.getItemAtPosition(i)).getTitle())
                .setMessage(context.getResources().getString(R.string.removetracking_message))
                .setPositiveButton(android.R.string.yes, new RemoveTrackingListener(context,
                        (Tracking)adapterView.getItemAtPosition(i)))
                .setNegativeButton(android.R.string.no, null)
                .show();

        return true;
    }
}
