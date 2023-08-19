package com.company.automaticfishfeederapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;

import io.github.muddz.styleabletoast.StyleableToast;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!isOnline()) {

            int white = Color.parseColor("#FFFFFF");
            new StyleableToast.Builder(getApplicationContext())
                    .text("You are in offline mode")
                    .backgroundColor(white)
                    .solidBackground()
                    .textColor(Color.RED)
                    .gravity(Gravity.TOP)
                    .cornerRadius(50)
                    .textSize(12)
                    .show();
        }else {
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(1 * 3000);

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

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            return false;
        }
        return true;
    }
}