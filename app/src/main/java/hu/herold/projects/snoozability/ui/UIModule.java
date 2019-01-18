package hu.herold.projects.snoozability.ui;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;
import hu.herold.projects.snoozability.ui.alarms.AlarmsPresenter;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsPresenter;

@Module
public class UIModule {
    private Context context;

    public UIModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public AlarmsActivity provideAlarmsActivity() {
        return new AlarmsActivity();
    }

    @Provides
    @Singleton
    public AlarmsPresenter provideAlarmsPresenter() {
        return new AlarmsPresenter();
    }

    @Provides
    @Singleton
    public AlarmDetailsPresenter provideAlarmDetailsPresenter() {
        return new AlarmDetailsPresenter();
    }

    @Provides
    @Singleton
    public AlarmDetailsActivity provideAlarmDetailsActivity() {
        return new AlarmDetailsActivity();
    }


    @Provides
    @Singleton
    public Executor provideExecutor() {
        return Executors.newFixedThreadPool(1);
    }
}
