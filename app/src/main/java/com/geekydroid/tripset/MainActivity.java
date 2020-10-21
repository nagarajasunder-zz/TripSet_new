package com.geekydroid.tripset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("startup", MODE_PRIVATE);
        final boolean first = preferences.getBoolean("startup_anim", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (first) {
                    startActivity(new Intent(getApplicationContext(), Startup.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }
        }, 1500);

    }
}