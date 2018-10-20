package com.estimote.notification.estimote;

import android.util.Log;

public class OnEnterFunction {
    public static void run(String lineName) {
        //
        Long currentTimestampMs = System.currentTimeMillis();
        if (
            (currentTimestampMs - CommonVars.sharedPreferences.getLong("lastEnterTimestampMs", 0L))
            < CommonVars.sharedPreferences.getLong("enterCooldownMs", 0L)
        ) {
            Log.d("Clooney", "Early exit");
            return;
        }
        CommonVars.sharedPreferences.edit().putLong("enterCooldownMs", 0L).commit();
        CommonVars.sharedPreferences.edit().putLong("lastEnterTimestampMs", currentTimestampMs).commit();

        // Verify purchased ticket PCL.
        if (CommonVars.sharedPreferences.getString("pcl", "none").contentEquals("active")) {
            Log.d("Clooney", "PCL detected");
            return;
        }

        // Verify purchased ticket CL.
        if (currentTimestampMs < (CommonVars.sharedPreferences.getLong("clValidTimestampMs",0L) + CommonVars.sharedPreferences.getLong("validityOffsetMs",0L))) {
            Log.d("Clooney", "CL detected");
            return;
        }

        // Switch to new screen.
        String title = "Prave vyprsal listok";
        if (lineName != null) {
            title = "Nastupil si do MHD spoja: " + lineName;
        }
        Log.d("Clooney", "Notification triggered");
        NotificationsManager.self.doNotification(title, "Kup si listok");

        return;
    }
}
