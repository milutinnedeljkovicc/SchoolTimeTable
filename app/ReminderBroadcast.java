import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


import com.selicapps.schooltimetable.R;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_notification)
                        .setContentTitle("It works!")
                        .setContentText("LOOOL")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        ;

        NotificationManagerCompat mNotMrg = NotificationManagerCompat.from(this);
    }
}
