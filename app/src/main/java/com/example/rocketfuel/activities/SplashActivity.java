package com.example.rocketfuel.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rocketfuel.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TimerTask timerTask = new TimerTask() {
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        };
        Timer timer = new Timer();timer.schedule(timerTask,3000);
    }
}
