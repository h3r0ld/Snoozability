package hu.herold.projects.snoozability.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import hu.herold.projects.snoozability.db.converter.Converters;
import hu.herold.projects.snoozability.db.dao.AlarmDao;
import hu.herold.projects.snoozability.db.dao.QuoteDao;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.model.QuoteEntity;

@Database(entities = { AlarmEntity.class, QuoteEntity.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    public abstract QuoteDao quoteDao();
}
