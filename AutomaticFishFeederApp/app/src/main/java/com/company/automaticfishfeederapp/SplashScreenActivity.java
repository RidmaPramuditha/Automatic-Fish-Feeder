package com.company.automaticfishfeederapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*3000);

                    // After 5 seconds redirect to another intent
                    LoginSession loginSession = new LoginSession(getApplicationContext());
                    loginSession.checkLogin();

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }

}