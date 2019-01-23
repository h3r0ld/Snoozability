package hu.herold.projects.snoozability.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import java.util.List;

import hu.herold.projects.snoozability.db.model.AlarmEntity;

@Dao
public interface AlarmDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM ALARM")
    List<AlarmEntity> getAlarms() throws Exception;

    @Query("SELECT * FROM ALARM WHERE :id = id")
    AlarmEntity getAlarmById(long id) throws Exception;

    @Insert
    long saveAlarm(AlarmEntity alarmEntity);

    @Update
    void updateAlarm(AlarmEntity alarmEntity);

    @Delete
    void deleteAlarm(AlarmEntity alarmEntity);

    @Query("DELETE FROM ALARM WHERE :id = id")
    void deleteAlarmById(long id);
}
