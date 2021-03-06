package hu.herold.projects.snoozability.interactor.mapper;

import java.util.ArrayList;
import java.util.List;

import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.model.QuoteEntity;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.model.Quote;

public class Mapper {

    public AlarmEntity mapAlarm(Alarm alarm) {
        if (alarm == null) {
            return null;
        }

        return AlarmEntity.builder()
                .id(alarm.getId())
                .alarmHour(alarm.getAlarmHour())
                .alarmMinutes(alarm.getAlarmMinutes())
                .alarmVolume(alarm.getAlarmVolume())
                .label(alarm.getLabel())
                .maxSnoozeCount(alarm.getMaxSnoozeCount())
                .remainingSnoozeCount(alarm.getRemainingSnoozeCount())
                .snoozeTime(alarm.getSnoozeTime())
                .enabled(alarm.isEnabled())
                .build();
    }

    public List<AlarmEntity> mapAlarmList(List<Alarm> alarms) {
        if (alarms == null) {
            return null;
        }

        List<AlarmEntity> alarmEntities = new ArrayList<>();

        for (Alarm alarm : alarms) {
            alarmEntities.add(mapAlarm(alarm));
        }

        return alarmEntities;
    }

    public Alarm mapAlarmEntity(AlarmEntity alarmEntity) {
        if (alarmEntity == null) {
            return null;
        }

        return Alarm.builder()
                .id(alarmEntity.getId())
                .alarmHour(alarmEntity.getAlarmHour())
                .alarmMinutes(alarmEntity.getAlarmMinutes())
                .alarmVolume(alarmEntity.getAlarmVolume())
                .label(alarmEntity.getLabel())
                .maxSnoozeCount(alarmEntity.getMaxSnoozeCount())
                .remainingSnoozeCount(alarmEntity.getRemainingSnoozeCount())
                .snoozeTime(alarmEntity.getSnoozeTime())
                .enabled(alarmEntity.isEnabled())
                .build();
    }

    public List<Alarm> mapAlarmEntityList(List<AlarmEntity> alarmEntities) {
        if (alarmEntities == null) {
            return null;
        }

        List<Alarm> alarms= new ArrayList<>();

        for (AlarmEntity alarmEntity : alarmEntities) {
            alarms.add(mapAlarmEntity(alarmEntity));
        }

        return alarms;
    }

    public Quote mapToQuote(QuoteEntity quoteEntity) {
        return Quote.builder()
                .Author(quoteEntity.getAuthor())
                .Quote(quoteEntity.getQuote())
                .build();
    }
}
