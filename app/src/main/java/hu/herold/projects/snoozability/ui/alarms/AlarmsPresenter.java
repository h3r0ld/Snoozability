package hu.herold.projects.snoozability.ui.alarms;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleGetAlarmsEvent(final GetAlarmsEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();

            if (screen != null) {
                screen.showError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                screen.showAlarms(event.getAlarms());
            }
        }

    }
}
