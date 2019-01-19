package hu.herold.projects.snoozability.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "alarm")
public class AlarmEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "alarmHour")
    private int alarmHour;

    @ColumnInfo(name = "alarmMinutes")
    private Integer alarmMinutes;

    @ColumnInfo(name = "snoozeTime")
    private Integer snoozeTime;

    @ColumnInfo(name = "maxSnoozeCount")
    private Integer maxSnoozeCount;

    @ColumnInfo(name = "currentSnoozeCount")
    private Integer currentSnoozeCount;

    @ColumnInfo(name = "alarmVolume")
    private Integer alarmVolume;

    @ColumnInfo(name = "enabled")
    private boolean enabled;

}
