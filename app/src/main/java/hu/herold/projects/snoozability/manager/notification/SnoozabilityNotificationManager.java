package hu.herold.projects.snoozability.manager.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.receiver.AlarmDisableReceiver;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;

public class SnoozabilityNotificationManager {

    private final int NOTIFICATION_ID = 666;
    private final String NOTIFICATION_CHANNEL = "hu.herold.projects.snoozability";

    @Inject
    Context context;

    @Inject
    NotificationManager notificationManager;

    public SnoozabilityNotificationManager() {
        SnoozabilityApplication.injector.inject(this);
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.baseline_snooze_black_18)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("You will have to get up, eventually.")
                .setContentIntent(createContentPendingIntent())
                .setDeleteIntent(createDeletePendingIntent())
                .setBadgeIconType(R.drawable.baseline_snooze_black_18)
                .setWhen(System.currentTimeMillis());


        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }


    private PendingIntent createContentPendingIntent() {
        Intent alarmsActivityIntent = new Intent(context, AlarmsActivity.class);
        return PendingIntent.getActivity(context, 0, alarmsActivityIntent, 0);
    }

    private PendingIntent createDeletePendingIntent() {
        Intent alarmDisableReceiverIntent = new Intent(context, AlarmDisableReceiver.class);
        return PendingIntent.getBroadcast(context, 0, alarmDisableReceiverIntent, 0);
    }
}
