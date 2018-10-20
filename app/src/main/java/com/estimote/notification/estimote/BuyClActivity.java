package com.estimote.notification.estimote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
        finishAndRemoveTask();
        NotificationsManager.enterCooldownMs = time * 1000L;
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
}
