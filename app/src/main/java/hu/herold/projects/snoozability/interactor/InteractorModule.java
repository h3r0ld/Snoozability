package hu.herold.projects.snoozability.interactor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;

@Module
public class InteractorModule {

    @Provides
    @Singleton
    public Mapper provideMapper() {
        return new Mapper();
    }

    @Provides
    public AlarmsInteractor provideAlarmsInteractor() {
        return new AlarmsInteractor();
    }
}
