package com.example.standup_82;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notifications_channel";
    private NotificationManager mNotificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("paok", "onReceive: ");
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context);
    }

    private void deliverNotification(Context context) {
        Log.d("paok", "deliverNotification: ");
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_MUTABLE);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_stand_up)
                    .setContentTitle("Stand Up Alert")
                    .setContentText("You should stand up and walk around now!")
                    .setContentIntent(contentPendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}