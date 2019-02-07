package hu.herold.projects.snoozability.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.db.model.QuoteEntity;
import hu.herold.projects.snoozability.db.repository.quote.QuoteRepository;

public class QuotesSyncService extends Service {

    public static boolean IsRunning = false;

    @Inject
    DatabaseReference databaseReference;

    @Inject
    QuoteRepository quoteRepository;

    @Inject
    Executor executor;

    public QuotesSyncService() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        QuotesSyncService.IsRunning = true;
        databaseReference.addValueEventListener(new QuoteValueEventListener());
        return super.onStartCommand(intent, flags, startId);
    }

    private class QuoteValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    synchronize(dataSnapshot);
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(this.getClass().getSimpleName(), databaseError.toString());
        }

        private void synchronize(DataSnapshot dataSnapshot) {
            try {
                if (quoteRepository.getQuoteCount() == 0) {
                    quoteRepository.deleteQuotes();

                    Map<String, Object> quoteObjects = (Map<String, Object>) dataSnapshot.getValue();

                    int index = 0;
                    for (Object entry : quoteObjects.values()) {
                        Map<String, String> attributeMap = (Map<String, String>) entry;
                        String author = attributeMap.get("Author");
                        String quote = attributeMap.get("Quote");

                        QuoteEntity quoteEntity = QuoteEntity.builder()
                                .id(index++)
                                .author(author)
                                .quote(quote)
                                .build();

                        quoteRepository.saveQuote(quoteEntity);
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuotesSyncService.IsRunning = false;
    }
}
