package hu.herold.projects.snoozability.ui.reciever;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SnoozeAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.StopAlarmEvent;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.alarms.AlarmsScreen;
import hu.herold.projects.snoozability.ui.base.Presenter;

public class AlarmReceiverPresenter extends Presenter<AlarmReceiverScreen> {

    @Inject
    AlarmsInteractor alarmsInteractor;

    @Inject
    Executor executor;

    public AlarmReceiverPresenter() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public void attachScreen(AlarmReceiverScreen screen) {
        EventBus.getDefault().register(this);
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void getAlarm(final long alarmId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.getAlarm(alarmId);
            }
        });
    }

    public void snoozeAlarm(final Alarm alarm) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.snoozeAlarm(alarm);
            }
        });
    }

    public void stopAlarm(final Alarm alarm) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.stopAlarm(alarm);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleGetAlarmEvent(final GetAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.showAlarm(event.getAlarm());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSnoozeAlarmEvent(final SnoozeAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.close();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleStopAlarmEvent(final StopAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.close();
        }
    }
}
