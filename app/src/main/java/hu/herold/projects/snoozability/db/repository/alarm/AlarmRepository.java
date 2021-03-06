package hu.herold.projects.snoozability.db.repository.alarm;

import java.util.List;

import hu.herold.projects.snoozability.db.model.AlarmEntity;

public interface AlarmRepository {

    List<AlarmEntity> getAlarms() throws Exception;

    List<AlarmEntity> getEnabledAlarms() throws Exception;

    AlarmEntity getAlarmById(long id) throws Exception;

    AlarmEntity saveAlarm(AlarmEntity alarmEntity) throws Exception;

    void updateAlarms(List<AlarmEntity> alarmEntities) throws Exception;

    void deleteAlarm(AlarmEntity alarmEntity);

    void deleteAlarmById(long id);
}
