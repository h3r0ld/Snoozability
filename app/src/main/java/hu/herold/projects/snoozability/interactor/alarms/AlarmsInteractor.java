package hu.herold.projects.snoozability.interactor.alarms;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.interactor.alarms.event.DeleteAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.EnableAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SaveAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SnoozeAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.StopAlarmEvent;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.model.Alarm;

public class AlarmsInteractor {

    @Inject
    AlarmRepository alarmRepository;

    @Inject
    Mapper mapper;

    @Inject
    SnoozabilityAlarmManager snoozabilityAlarmManager;

    public AlarmsInteractor() {
        SnoozabilityApplication.injector.inject(this);
    }

    public void getAlarms() {
        GetAlarmsEvent event = GetAlarmsEvent.builder().build();

        try {
            List<AlarmEntity> alarms = alarmRepository.getAlarms();
            event.setAlarms(mapper.mapAlarmEntityList(alarms));
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void getAlarm(long alarmId) {
        GetAlarmEvent event = GetAlarmEvent.builder().build();

        try {
            AlarmEntity alarmEntity = alarmRepository.getAlarmById(alarmId);
            Alarm alarm = mapper.mapAlarmEntity(alarmEntity);
            event.setAlarm(alarm);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void saveAlarm(Alarm alarm) {
        SaveAlarmEvent event = SaveAlarmEvent.builder().build();

        try {
            if (alarm.getId() != null) {
                snoozabilityAlarmManager.cancelAlarm(alarm.getId());
            }

            AlarmEntity alarmEntity = mapper.mapAlarm(alarm);
            alarmEntity.setEnabled(true);
            alarmRepository.saveAlarm(alarmEntity);
            alarm = mapper.mapAlarmEntity(alarmEntity);

            snoozabilityAlarmManager.setAlarm(alarm);

            event.setAlarm(alarm);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void deleteAlarm(Alarm alarm) {
        DeleteAlarmEvent event = DeleteAlarmEvent.builder().build();

        try {
            alarmRepository.deleteAlarmById(alarm.getId());
            event.setAlarm(alarm);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void stopAlarm(Alarm alarm) {
        StopAlarmEvent event = StopAlarmEvent.builder().build();

        try {
            snoozabilityAlarmManager.cancelAlarm(alarm.getId());

            if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() > 0) {
                alarm.setRemainingSnoozeCount(alarm.getMaxSnoozeCount());
            }
            alarm.setEnabled(false);

            AlarmEntity alarmEntity = mapper.mapAlarm(alarm);
            alarmRepository.saveAlarm(alarmEntity);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void snoozeAlarm(Alarm alarm) {
        SnoozeAlarmEvent event = SnoozeAlarmEvent.builder().build();

        try {
            if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() > 0) {
                alarm.setRemainingSnoozeCount(alarm.getRemainingSnoozeCount() - 1);
            }

            AlarmEntity alarmEntity = mapper.mapAlarm(alarm);
            alarmRepository.saveAlarm(alarmEntity);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }

    public void enableAlarm(Alarm alarm, boolean enabled) {
        EnableAlarmEvent event = EnableAlarmEvent.builder().enabled(enabled).build();

        try {
            alarm.setEnabled(enabled);

            if (!enabled) {
                snoozabilityAlarmManager.cancelAlarm(alarm.getId());

                if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() > 0) {
                    alarm.setRemainingSnoozeCount(alarm.getMaxSnoozeCount());
                }
            } else {
                snoozabilityAlarmManager.setAlarm(alarm);
            }

            AlarmEntity alarmEntity = mapper.mapAlarm(alarm);
            alarmRepository.saveAlarm(alarmEntity);
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }
}
