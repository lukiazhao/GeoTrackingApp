package com.rmit.geotracking.controller;

import android.app.Dialog;
import android.view.View;

public class DialogDismissListener implements View.OnClickListener {

    private Dialog dialog;

    public DialogDismissListener(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onClick(View view) {
        dialog.dismiss();
    }
}
