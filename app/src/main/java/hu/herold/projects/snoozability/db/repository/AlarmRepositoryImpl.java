package hu.herold.projects.snoozability.db.repository;

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
    public AlarmEntity getAlarmById(long id) throws Exception {
        return alarmDao.getAlarmById(id);
    }

    @Override
    public void saveAlarm(AlarmEntity alarmEntity) throws Exception {
        alarmDao.saveAlarm(alarmEntity);
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
