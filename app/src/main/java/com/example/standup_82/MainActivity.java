package com.example.standup_82;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                notifyIntent,PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE) != null);
        alarmToggle.setChecked(alarmUp);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                String toastMessage;
                if (isOn) {
                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);
                    }
                    toastMessage = "Alarm is on";

                } else {
                    mNotificationManager.cancelAll();
                    if (alarmManager != null) {
                        alarmManager.cancel(notifyPendingIntent);
                    }
                    toastMessage = "Alarm is off";
                }
                Toast.makeText(MainActivity.this, toastMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button nextAlarmButton = findViewById(R.id.next_alarm_button);
        nextAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmManager.getNextAlarmClock() != null){
                    String i = alarmManager.getNextAlarmClock().toString();
                    Toast.makeText(MainActivity.this, i, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

            }
        });

        createNotificationChannel();
    }


    public void createNotificationChannel() {
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Stand up notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


}