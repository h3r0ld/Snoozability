package hu.herold.projects.snoozability.interactor;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;

@Module
public class InteractorModule {

    @Provides
    @Singleton
    Mapper provideMapper() {
        return new Mapper();
    }

    @Singleton
    @Provides
    AlarmsInteractor provideAlarmsInteractor() {
        return new AlarmsInteractor();
    }
}
