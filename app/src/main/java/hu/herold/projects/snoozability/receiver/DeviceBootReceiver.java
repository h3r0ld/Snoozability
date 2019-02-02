package hu.herold.projects.snoozability.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.manager.alarm.AlarmRestoreManager;

public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON_ACTION = "android.intent.action.QUICKBOOT_POWERON";

    @Inject
    Executor executor;

    @Inject
    AlarmRestoreManager alarmRestoreManager;

    public DeviceBootReceiver() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmRestore", "Restoring alarms...");

        String intentAction = intent.getAction();

        if (BOOT_COMPLETED_ACTION.equals(intentAction) || QUICKBOOT_POWERON_ACTION.equals(intentAction)) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    alarmRestoreManager.restoreAlarms();
                }
            });
        }
    }
}
