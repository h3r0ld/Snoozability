package hu.herold.projects.snoozability.manager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    SnoozabilityAlarmManager provideSnoozabilityAlarmManager() {
        return new SnoozabilityAlarmManager();
    }
}
