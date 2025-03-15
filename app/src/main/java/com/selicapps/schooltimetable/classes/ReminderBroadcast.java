package com.selicapps.schooltimetable.classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.selicapps.schooltimetable.R;
import com.selicapps.schooltimetable.activities.SplashActivity;

public class ReminderBroadcast extends BroadcastReceiver {

    private static final String CHANNEL_ID = "reminder_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the task name from the intent
        String taskName = intent.getStringExtra("task_name");

        // Create a notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }

        // Intent to open SplashActivity when notification is tapped
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logobig) // Ensure this drawable exists
                        .setContentTitle("Reminder: " + (taskName != null ? taskName : "Task"))
                        .setContentText("Your task is scheduled in 12 hours!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(200, mBuilder.build());
        } else {
            Toast.makeText(context, "Notifications are disabled for this app!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates a notification channel for Android 8.0+.
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = context.getResources().getString(R.string.nottitle); // Notification channel name
            String description = context.getResources().getString(R.string.notification1); // Channel description
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}