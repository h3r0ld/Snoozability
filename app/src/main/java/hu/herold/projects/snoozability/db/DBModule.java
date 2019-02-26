package hu.herold.projects.snoozability.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.herold.projects.snoozability.db.dao.AlarmDao;
import hu.herold.projects.snoozability.db.dao.QuoteDao;
import hu.herold.projects.snoozability.db.repository.alarm.AlarmRepository;
import hu.herold.projects.snoozability.db.repository.alarm.AlarmRepositoryImpl;
import hu.herold.projects.snoozability.db.repository.quote.QuoteRepository;
import hu.herold.projects.snoozability.db.repository.quote.QuoteRepositoryImpl;

@Module
public class DBModule {

    private AppDatabase appDatabase;

    public DBModule(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    AppDatabase provideRoomDatabase() {
        return appDatabase;
    }


    @Provides
    @Singleton
    AlarmDao provideAlarmDao(AppDatabase appDatabase) {
        return appDatabase.alarmDao();
    }

    @Provides
    @Singleton
    QuoteDao provideQuoteDao(AppDatabase appDatabase) {
        return appDatabase.quoteDao();
    }

    @Provides
    @Singleton
    AlarmRepository provideAlarmRepository(AlarmDao alarmDao) {
        return new AlarmRepositoryImpl(alarmDao);
    }

    @Provides
    @Singleton
    QuoteRepository provideQuoteRepository(QuoteDao quoteDao) {
        return new QuoteRepositoryImpl(quoteDao);
    }

    @Provides
    @Singleton
    DatabaseReference provideDatabaseReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        return database.getReference("quotes");
    }
}
