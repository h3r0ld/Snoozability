package hu.herold.projects.snoozability.ui.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.utils.WakeLocker;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);

        if (intent != null && intent.getExtras() != null &&
                intent.getExtras().containsKey(SnoozabilityAlarmManager.ID_KEY)) {

            Long alarmId = intent.getExtras().getLong(SnoozabilityAlarmManager.ID_KEY);

            Intent receiveActivityIntent = new Intent(context, AlarmReceiverActivity.class);
            receiveActivityIntent.putExtra(SnoozabilityAlarmManager.ID_KEY, alarmId);
            receiveActivityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(receiveActivityIntent);
        }
    }
}
