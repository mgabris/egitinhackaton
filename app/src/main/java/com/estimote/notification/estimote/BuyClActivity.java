package com.estimote.notification.estimote;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.estimote.notification.R;

public class BuyClActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_cl);
    }

    public void setNotification(Integer time, String price) {
        Long timeMs = time * 1000L;

        Toast.makeText(this, "Kupil si si " +  time + " minutovy listok", Toast.LENGTH_SHORT).show();
        CommonVars.sharedPreferences.edit().putLong("clValidTimestampMs", System.currentTimeMillis()).putLong("validityOffsetMs", timeMs).commit();
        NotificationsManager.self.doNotification("SLSP: Mobile banking", "Debet: " + price + " €, na účet: DPB, a.s.");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CommonVars.sharedPreferences.getBoolean("isInside", false)) {
                    OnEnterFunction.run(null);
                }
            }
        }, timeMs);

        finishAndRemoveTask();
    }

    public void a(View v) {
        setNotification(15, "0,63");
    }

    public void b(View v) {
        setNotification(30, "0,81");
    }

    public void c(View v) {
        setNotification(60, "1,08");
    }

    public void d(View v) {
        setNotification(90, "1,62");
    }

    public void back(View v) {
        finishAndRemoveTask();
    }
}
