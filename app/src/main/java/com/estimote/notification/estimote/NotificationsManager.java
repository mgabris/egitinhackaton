package com.estimote.notification.estimote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.notification.MyApplication;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class NotificationsManager {

    private Context context;
    private NotificationManager notificationManager;
    private int notificationId = 1;

    public static NotificationsManager self = null;


    public NotificationsManager(Context context) {
        self = this;
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void doNotification(String title, String text) {
        notificationManager.notify(notificationId, buildNotification(title, text));
    }

    public Notification buildNotification(String title, String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel contentChannel = new NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(contentChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, "content_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public void startMonitoring() {
        ProximityObserver proximityObserver =
                new ProximityObserverBuilder(context, ((MyApplication) context).cloudCredentials)
                        .onError(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withLowLatencyPowerMode()
                        .build();

        ProximityZone zone = new ProximityZoneBuilder()
                .forTag("clooney-2co")
                .inCustomRange(1.0)
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext proximityContext) {
                        CommonVars.sharedPreferences.edit().putBoolean("isInside", true).commit();

                        String lineName = "Unknown";
                        if (proximityContext != null) {
                            lineName = proximityContext.getAttachments().get("jan-sehnal-s-proximity-for-5be/title");
                        }
                        OnEnterFunction.run(lineName);

                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext proximityContext) {
                        CommonVars.sharedPreferences.edit().putBoolean("isInside", false).commit();

                        return null;
                    }
                })
                .build();
        proximityObserver.startObserving(zone);
    }

}
