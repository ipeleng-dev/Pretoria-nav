package com.poe.preotrianav;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class make_up {

    public make_up(Window get_window){
        make_me_up(get_window);
    }

    public void make_me_up(Window get_window){
        get_window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = get_window.getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        decorView.setSystemUiVisibility(uiOptions);

    }
}
