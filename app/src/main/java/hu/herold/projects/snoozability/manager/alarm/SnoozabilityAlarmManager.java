package hu.herold.projects.snoozability.manager.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.receiver.AlarmReceiver;

public class SnoozabilityAlarmManager {

    public static final String ID_KEY = "ID";

    private static final String PENDING_INTENT_ACTION = "hu.herold.projects.snoozability.ALARM";

    @Inject
    Context context;

    @Inject
    AlarmManager alarmManager;

    public SnoozabilityAlarmManager() {
        SnoozabilityApplication.injector.inject(this);
    }

    public void setAlarm(Alarm alarm) {
        PendingIntent pendingIntent = createPendingIntent(alarm.getId());

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarm.getAlarmHour());
        alarmCalendar.set(Calendar.MINUTE, alarm.getAlarmMinutes());
        alarmCalendar.set(Calendar.SECOND, 0);

        // If the alarm would be set to a past time, add +1 day
        if (alarmCalendar.before(currentCalendar)) {
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // If snooze is not allowed
        if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            }
        } else {
            // If its a custom or infinite snooze count
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
                    1000 * 60 * alarm.getSnoozeTime(), pendingIntent);
        }
    }

    public void cancelAlarm(Long alarmId) {
        PendingIntent pendingIntent = createPendingIntent(alarmId);
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent createPendingIntent(Long alarmId) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(PENDING_INTENT_ACTION);
        alarmIntent.putExtra(ID_KEY, alarmId);
        return PendingIntent.getBroadcast(context, alarmId.intValue(), alarmIntent, 0);
    }
}
