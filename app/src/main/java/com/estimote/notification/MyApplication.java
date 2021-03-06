package com.estimote.notification;

import android.app.Application;
import android.util.Log;

import com.estimote.notification.estimote.NotificationsManager;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    public EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials("clooney-2co", "4a0adf69876bd8d67e8a98a9c1171f6a");
    public NotificationsManager notificationsManager;

    public void enableBeaconNotifications() {
        notificationsManager = new NotificationsManager(this);
        notificationsManager.startMonitoring();
        Log.d("Clooney", "Initialize");
    }

}
