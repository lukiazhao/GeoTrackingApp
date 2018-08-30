package com.rmit.geotracking.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/*
 * Hide software keyboard whenever the user clicks somewhere else on the screen.
 */
public class HideKeyboardListener implements View.OnFocusChangeListener {

    private Context context;
    public HideKeyboardListener(Context context){
        this.context = context;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            hideKeyboard(view);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) context
                                                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
