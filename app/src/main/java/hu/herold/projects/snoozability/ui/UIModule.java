package hu.herold.projects.snoozability.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.receiver.AlarmReceiver;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;
import hu.herold.projects.snoozability.ui.alarms.AlarmsPresenter;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsPresenter;
import hu.herold.projects.snoozability.ui.reciever.AlarmReceiverActivity;
import hu.herold.projects.snoozability.ui.reciever.AlarmReceiverPresenter;
import hu.herold.projects.snoozability.ui.validation.Validator;

@Module
public class UIModule {
    private Context context;

    public UIModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    AlarmsActivity provideAlarmsActivity() {
        return new AlarmsActivity();
    }

    @Provides
    @Singleton
    AlarmsPresenter provideAlarmsPresenter() {
        return new AlarmsPresenter();
    }

    @Provides
    @Singleton
    AlarmDetailsPresenter provideAlarmDetailsPresenter() {
        return new AlarmDetailsPresenter();
    }

    @Provides
    @Singleton
    AlarmDetailsActivity provideAlarmDetailsActivity() {
        return new AlarmDetailsActivity();
    }


    @Provides
    @Singleton
    Executor provideExecutor() {
        return Executors.newFixedThreadPool(1);
    }

    @Provides
    @Singleton
    AudioManager provideAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Validator provideValidator() {
        return new Validator();
    }

    @Provides
    @Singleton
    AlarmReceiver provideAlarmReceiver() {
        return new AlarmReceiver();
    }

    @Provides
    @Singleton
    AlarmReceiverActivity provideAlarmReceiverActivity() {
        return new AlarmReceiverActivity();
    }

    @Provides
    @Singleton
    AlarmReceiverPresenter provideAlarmReceiverPresenter() {
        return new AlarmReceiverPresenter();
    }
}
