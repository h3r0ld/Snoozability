package hu.herold.projects.snoozability.db.repository.quote;

import java.util.List;

import hu.herold.projects.snoozability.db.model.QuoteEntity;

public interface QuoteRepository {
    void saveQuote(QuoteEntity quoteEntity) throws Exception;

    void saveQuotes(List<QuoteEntity> quoteEntities) throws Exception;

    int getQuoteCount() throws Exception;

    QuoteEntity getQuote(int id) throws Exception;

    void deleteQuotes() throws Exception;
}
