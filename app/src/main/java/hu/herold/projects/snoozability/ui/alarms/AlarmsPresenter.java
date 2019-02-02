package hu.herold.projects.snoozability.ui.alarms;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.channels.AlreadyBoundException;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.alarms.event.DeleteAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.EnableAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SaveAlarmEvent;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.base.Presenter;

public class AlarmsPresenter extends Presenter<AlarmsScreen> {

    @Inject
    AlarmsInteractor alarmsInteractor;

    @Inject
    Executor executor;

    public AlarmsPresenter() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public void attachScreen(AlarmsScreen screen) {
        EventBus.getDefault().register(this);
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void getAlarms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.getAlarms();
            }
        });
    }

    public void deleteAlarm(final Alarm alarm) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.deleteAlarm(alarm);
            }
        });
    }

    public void saveAlarm(final Alarm alarm) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmsInteractor.saveAlarm(alarm);
            }
        });
    }

    public void enableAlarm(final Alarm alarm, final boolean enabled) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (alarm.isEnabled() != enabled) {
                    alarmsInteractor.enableAlarm(alarm, enabled);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleGetAlarmsEvent(final GetAlarmsEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
                screen.showAlarms(event.getAlarms());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleDeleteAlarmsEvent(final DeleteAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.handleAlarmDeleted(event.getAlarm());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSaveAlarmEvent(final SaveAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.handleAlarmRestored(event.getAlarm());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEnableAlarmEvent(final EnableAlarmEvent event) {
        boolean isExceptionPresent = handleEventException(event);

        if (!isExceptionPresent && screen != null) {
            screen.handleAlarmEnabled(event.isEnabled());
        }
    }
}
