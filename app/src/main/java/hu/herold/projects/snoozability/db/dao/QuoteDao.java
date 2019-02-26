package hu.herold.projects.snoozability.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.db.model.QuoteEntity;
import hu.herold.projects.snoozability.model.Quote;

@Dao
public interface QuoteDao {

    @Query("SELECT * FROM QUOTE WHERE id = :id")
    QuoteEntity getQuote(int id);

    @Query("SELECT COUNT(*) FROM QUOTE")
    int getQuoteCount();

    @Insert
    void saveQuote(QuoteEntity... quoteEntity);

    @Query("DELETE FROM QUOTE")
    void deleteQuotes();
}
