package com.estimote.notification.estimote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.estimote.notification.R;


import android.content.Intent;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void buyPclOnClick(View v) {
        Log.d("Clooney", "Buy PCL Click");

        //boolean cart = CommonVars.sharedPreferences.getBoolean("cart", false);
        //if (!cart) {
        //    Toast.makeText(this, "FU not carted!",
        //            Toast.LENGTH_SHORT).show();
        //} else {
        CommonVars.sharedPreferences.edit().putString("pcl", "active").commit();
        finishAndRemoveTask();
        //}
    }

    public void buyClOnClick(View v) {
        Log.d("Clooney", "Buy CL Click");

        //boolean cart = CommonVars.sharedPreferences.getBoolean("cart", false);
        //if (!cart) {
        //    Toast.makeText(this, "FU not carted!",
        //            Toast.LENGTH_SHORT).show();
        //} else {
            Intent intent = new Intent(this, BuyClActivity.class);
            startActivity(intent);
        //}

    }

    public void ignoreOnClick(View v) {
        Log.d("Clooney", "Ignore Click");

        NotificationsManager.enterCooldownMs = 60000L;
        finishAndRemoveTask();
    }
}
