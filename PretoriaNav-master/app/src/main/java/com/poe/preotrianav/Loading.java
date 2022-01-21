package com.poe.preotrianav;

import android.view.View;
import android.widget.ProgressBar;

public class Loading {
    private View Layout;
    private ProgressBar progressBar;

    public Loading(View layout, ProgressBar progressBar) {
        Layout = layout;
        this.progressBar = progressBar;

        //Making sure the layout with the main information is invisible
        Layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void done(){
        //Making sure the layout with the main information is visible
        Layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
