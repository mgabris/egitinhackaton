package com.estimote.notification.estimote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.notification.MainActivity;
import com.estimote.notification.estimote.NotificationActivity;
import com.estimote.notification.MyApplication;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class NotificationsManager {

    private Context context;
    private NotificationManager notificationManager;
    private Notification helloNotification;
    private Notification goodbyeNotification;
    private int notificationId = 1;

    private static Long validityOffsetMs = 60000L;
    private static Long enterCooldownMs = 60000L;
    private Long lastEnterTimestampMs = 0L;

    public NotificationsManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.helloNotification = buildNotification("Hello", "You're near your beacon");
        this.goodbyeNotification = buildNotification("Bye bye", "You've left the proximity of your beacon");
    }

    private Notification buildNotification(String title, String text) {
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
                        //
                        Long currentTimestampMs = System.currentTimeMillis();
                        if ((currentTimestampMs - lastEnterTimestampMs) < enterCooldownMs) {
                            Log.d("Clooney", "early exit");
                            return null;
                        }
                        lastEnterTimestampMs = System.currentTimeMillis();

                        // Verify purchased ticket PCL.
                        if (currentTimestampMs < (CommonVars.pclValidTimestampMs - validityOffsetMs)) {
                            Log.d("Clooney", "PCL detected");
                            return null;
                        }

                        // Verify purchased ticket CL.
                        if (currentTimestampMs < (CommonVars.clValidTimestampMs - validityOffsetMs)) {
                            Log.d("Clooney", "CL detected");
                            return null;
                        }

                        // Switch to new screen.
                        String title = proximityContext.getAttachments().get("jan-sehnal-s-proximity-for-5be/title");
                        if (title == null) {
                            title = "unknown";
                        }
                        Log.d("Clooney", "Notification triggered");
                        notificationManager.notify(notificationId, buildNotification(title, "Kup si listok"));

                        return null;
                    }
                })
                .build();
        proximityObserver.startObserving(zone);
    }

}
