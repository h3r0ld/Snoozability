package hu.herold.projects.snoozability.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.db.dao.AlarmDao;
import hu.herold.projects.snoozability.db.repository.AlarmRepository;
import hu.herold.projects.snoozability.db.repository.AlarmRepositoryImpl;
import hu.herold.projects.snoozability.interactor.mapper.Mapper;

@Module
public class DBModule {

    private AppDatabase appDatabase;

    public DBModule(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-db").build();
    }

    @Provides
    @Singleton
    public AppDatabase provideRoomDatabase() {
        return appDatabase;
    }


    @Provides
    @Singleton
    public AlarmDao provideAlarmDao(AppDatabase appDatabase) {
        return appDatabase.alarmDao();
    }

    @Provides
    @Singleton
    public AlarmRepository provideAlarmRepository(AlarmDao alarmDao) {
        return  new AlarmRepositoryImpl(alarmDao);
    }
}
