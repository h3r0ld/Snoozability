package hu.herold.projects.snoozability.ui.alarms;

import hu.herold.projects.snoozability.model.Alarm;

public interface AlarmEnabledChangeListener {
    void alarmEnabledChanged(Alarm alarm, boolean enabled);
}
