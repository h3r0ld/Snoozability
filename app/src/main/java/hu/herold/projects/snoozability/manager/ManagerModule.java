package hu.herold.projects.snoozability.manager;

import android.app.AlarmManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.manager.alarm.AlarmRestoreManager;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    SnoozabilityAlarmManager provideSnoozabilityAlarmManager() {
        return new SnoozabilityAlarmManager();
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    @Singleton
    AlarmRestoreManager provideAlarmRestoreManager() {
        return new AlarmRestoreManager();
    }
}
