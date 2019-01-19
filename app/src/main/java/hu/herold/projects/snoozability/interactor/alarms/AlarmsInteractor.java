package hu.herold.projects.snoozability.interactor.alarms;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.interactor.alarms.event.DeleteAlarmEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
import hu.herold.projects.snoozability.interactor.alarms.event.SaveAlarmEvent;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;
import hu.herold.projects.snoozability.model.Alarm;

public class AlarmsInteractor {

    @Inject
    AlarmRepository alarmRepository;

    @Inject
    Mapper mapper;

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

    public void saveAlarm(Alarm alarm) {
        SaveAlarmEvent event = SaveAlarmEvent.builder().build();

        try {
            AlarmEntity alarmEntity = mapper.mapAlarm(alarm);
            alarmRepository.saveAlarm(alarmEntity);
            alarm = mapper.mapAlarmEntity(alarmEntity);
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
}
