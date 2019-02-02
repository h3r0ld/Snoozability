package hu.herold.projects.snoozability.manager.alarm;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;
import hu.herold.projects.snoozability.model.Alarm;

public class AlarmRestoreManager {

    @Inject
    AlarmRepository alarmRepository;

    @Inject
    Mapper mapper;

    @Inject
    SnoozabilityAlarmManager snoozabilityAlarmManager;

    public AlarmRestoreManager() {
        SnoozabilityApplication.injector.inject(this);
    }

    public void restoreAlarms() {
        Log.i("AlarmRestore", "Restoring alarms...");

        try {
            List<AlarmEntity> alarmEntities = alarmRepository.getEnabledAlarms();

            List<Alarm> alarms = mapper.mapAlarmEntityList(alarmEntities);

            for (Alarm alarm : alarms) {
                Log.i("AlarmRestore", String.format("Restored alarm: %s (%d)", alarm.getLabel(), alarm.getId()));
                snoozabilityAlarmManager.setAlarm(alarm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AlarmRestoreManager", "Could not restore alarms.");
        }
    }

    public void disableAlarms() {
        Log.i("AlarmRestore", "Disabling alarms...");

        try {
            List<AlarmEntity> alarmEntities = alarmRepository.getEnabledAlarms();

            for (AlarmEntity alarm : alarmEntities) {
                Log.i("AlarmRestore", String.format("Canceled alarm: %s (%d)", alarm.getLabel(), alarm.getId()));
                alarm.setEnabled(false);
                snoozabilityAlarmManager.cancelAlarm(alarm.getId());
            }
            alarmRepository.updateAlarms(alarmEntities);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AlarmRestoreManager", "Could not restore alarms.");
        }
    }
}
