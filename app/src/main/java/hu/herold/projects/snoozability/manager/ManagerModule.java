package hu.herold.projects.snoozability.manager;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.manager.alarm.AlarmRestoreManager;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.manager.notification.SnoozabilityNotificationManager;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    SnoozabilityAlarmManager provideSnoozabilityAlarmManager() {
        return new SnoozabilityAlarmManager();
    }

    @Singleton
    @Provides
    SnoozabilityNotificationManager provideSnoozabilityNotificationManager() {
        return new SnoozabilityNotificationManager();
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    @Singleton
    @Provides
    NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    AlarmRestoreManager provideAlarmRestoreManager() {
        return new AlarmRestoreManager();
    }
}
