package hu.herold.projects.snoozability.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = { }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

}
