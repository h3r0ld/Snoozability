package hu.herold.projects.snoozability.manager.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.SnoozabilityApplicationComponent;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.reciever.AlarmReceiver;

public class SnoozabilityAlarmManager {

    public static final String ID_KEY = "ID";

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

        // If the alarm would be set to a past time, add +1 day
        if (alarmCalendar.before(currentCalendar)) {
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // If snooze is not allowed
        if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() == 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
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
        alarmIntent.putExtra(ID_KEY, alarmId);
        return PendingIntent.getBroadcast(context, alarmId.intValue(), alarmIntent, 0);
    }
}
