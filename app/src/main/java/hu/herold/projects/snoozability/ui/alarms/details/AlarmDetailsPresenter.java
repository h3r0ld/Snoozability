package hu.herold.projects.snoozability.ui.alarms.details;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.SnoozabilityApplicationComponent;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SaveAlarmEvent;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.alarms.AlarmsScreen;
import hu.herold.projects.snoozability.ui.base.Presenter;

public class AlarmDetailsPresenter extends Presenter<AlarmDetailsScreen> {

    @Inject
    AlarmsInteractor alarmsInteractor;

    @Inject
    Executor executor;

    public AlarmDetailsPresenter() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public void attachScreen(AlarmDetailsScreen screen) {
        EventBus.getDefault().register(this);
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void saveAlarm(final Alarm alarm) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.saveAlarm(alarm);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSaveAlarmEvent(final SaveAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.closeAlarmDetailsScreen();
        }
    }
}
