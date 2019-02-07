package hu.herold.projects.snoozability.db.repository.quote;

import java.util.List;

import javax.inject.Inject;

import hu.herold.projects.snoozability.db.dao.QuoteDao;
import hu.herold.projects.snoozability.db.model.QuoteEntity;

public class QuoteRepositoryImpl implements QuoteRepository {

    QuoteDao quoteDao;

    @Inject
    public QuoteRepositoryImpl(QuoteDao quoteDao) {
        this.quoteDao = quoteDao;
    }

    @Override
    public void saveQuote(QuoteEntity quoteEntity) throws Exception {
        quoteDao.saveQuote(quoteEntity);
    }

    @Override
    public void saveQuotes(List<QuoteEntity> quoteEntities) throws Exception {
        quoteDao.saveQuote(quoteEntities.toArray(new QuoteEntity[0]));
    }

    @Override
    public int getQuoteCount() throws Exception {
        return quoteDao.getQuoteCount();
    }

    @Override
    public QuoteEntity getQuote(int id) throws Exception {
        return quoteDao.getQuote(id);
    }

    @Override
    public void deleteQuotes() throws Exception {
        quoteDao.deleteQuotes();
    }
}
