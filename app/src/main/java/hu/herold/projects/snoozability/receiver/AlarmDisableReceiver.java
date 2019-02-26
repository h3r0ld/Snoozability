package hu.herold.projects.snoozability.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.manager.alarm.AlarmRestoreManager;

public class AlarmDisableReceiver extends BroadcastReceiver {

    @Inject
    Executor executor;

    @Inject
    AlarmRestoreManager alarmRestoreManager;

    public AlarmDisableReceiver() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmRestoreManager.disableAlarms();
            }
        });
    }
}
