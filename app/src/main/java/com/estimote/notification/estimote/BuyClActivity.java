package com.estimote.notification.estimote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.estimote.notification.R;

public class BuyClActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_cl);
    }

    public void setNotification(Integer time) {
        Toast.makeText(this, "Bought ticket " +  time,
                Toast.LENGTH_SHORT).show();
        CommonVars.sharedPreferences.edit().putLong("clValidTimestampMs", System.currentTimeMillis()).putLong("validityOffsetMs", time * 1000L).commit();
        NotificationsManager.validityOffsetMs = time * 1000L;
        finishAndRemoveTask();
    }

    public void a(View v) {
        setNotification(15);
    }

    public void b(View v) {
        setNotification(30);
    }

    public void c(View v) {
        setNotification(60);
    }

    public void d(View v) {
        setNotification(90);
    }

    public void back(View v) {
        finishAndRemoveTask();
    }
}
