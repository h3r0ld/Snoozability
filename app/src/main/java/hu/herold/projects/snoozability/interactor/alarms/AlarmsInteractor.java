package hu.herold.projects.snoozability.interactor.alarms;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.interactor.alarms.event.GetAlarmsEvent;
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
            List<AlarmEntity> alarms = new ArrayList<>();
            alarms.add(AlarmEntity.builder()
                    .id(1L)
                    .label("Alarm1")
                    .alarmHour(8)
                    .alarmMinutes(10)
                    .enabled(true)
                    .maxSnoozeCount(10)
                    .snoozeTime(9)
                    .build());

            alarms.add(AlarmEntity.builder()
                    .id(2L)
                    .label("Alarm2")
                    .alarmHour(11)
                    .alarmMinutes(10)
                    .enabled(false)
                    .snoozeTime(15)
                    .build());

            alarms.add(AlarmEntity.builder()
                    .id(3L)
                    .label("Alarm3")
                    .alarmHour(18)
                    .alarmMinutes(10)
                    .enabled(true)
                    .maxSnoozeCount(0)
                    .build());
            //List<AlarmEntity> alarms = alarmRepository.getAlarms();
            event.setAlarms(mapper.mapAlarmEntityList(alarms));
        } catch (Exception e) {
            event.setThrowable(e);
        } finally {
            EventBus.getDefault().post(event);
        }
    }
}
