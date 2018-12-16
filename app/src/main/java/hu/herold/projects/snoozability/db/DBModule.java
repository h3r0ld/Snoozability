package hu.herold.projects.snoozability.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DBModule {

    private AppDatabase appDatabase;

    public DBModule(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-db").build();
    }

    @Provides
    @Singleton
    AppDatabase provideRoomDatabase() {
        return appDatabase;
    }
}
