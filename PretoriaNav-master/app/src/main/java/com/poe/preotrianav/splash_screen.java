package com.poe.preotrianav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

    private static int opened = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        make_up look_good = new make_up(getWindow());

        // handler for delaying the transition animations
        Handler handler_layout = new Handler();
        handler_layout.postDelayed(new Runnable() {
            @Override
            public void run() {


                // intent to open the sign in page
                Intent open = new Intent(splash_screen.this,log_in.class);
                startActivity(open);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        },1200);


    }


    //function for making sure the splash screen opens only once
    @Override
    protected void onStart() {
        if(opened > 0){

            finish();
            opened = -1;
        }
        opened++;
        super.onStart();
    }


}