package hu.herold.projects.snoozability.db.repository.alarm;

import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.db.dao.AlarmDao;
import hu.herold.projects.snoozability.db.model.AlarmEntity;

public class AlarmRepositoryImpl implements AlarmRepository {

    private final AlarmDao alarmDao;

    @Inject
    public AlarmRepositoryImpl(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    @Override
    public List<AlarmEntity> getAlarms() throws Exception {
        return alarmDao.getAlarms();
    }

    @Override
    public List<AlarmEntity> getEnabledAlarms() throws Exception {
        return alarmDao.getEnabledAlarms();
    }

    @Override
    public AlarmEntity getAlarmById(long id) throws Exception {
        return alarmDao.getAlarmById(id);
    }

    @Override
    public AlarmEntity saveAlarm(AlarmEntity alarmEntity) {
        if (alarmEntity.getId() != null) {
            alarmDao.updateAlarm(alarmEntity);
            return alarmEntity;
        } else {
            long insertedId = alarmDao.saveAlarm(alarmEntity);
            alarmEntity.setId(insertedId);
            return alarmEntity;
        }
    }

    @Override
    public void updateAlarms(List<AlarmEntity> alarmEntities) throws Exception {
        alarmDao.updateAlarms(alarmEntities);
    }

    @Override
    public void deleteAlarm(AlarmEntity alarmEntity) {
        alarmDao.deleteAlarm(alarmEntity);
    }

    @Override
    public void deleteAlarmById(long id) {
        alarmDao.deleteAlarmById(id);
    }
}
