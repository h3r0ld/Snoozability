package hu.herold.projects.snoozability;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import hu.herold.projects.snoozability.db.DBModule;
import hu.herold.projects.snoozability.manager.ManagerModule;
import hu.herold.projects.snoozability.ui.UIModule;

public class SnoozabilityApplication extends Application{

    public static SnoozabilityApplicationComponent injector;

    @Override
    public void onCreate() {
        super.onCreate();

        injector = DaggerSnoozabilityApplicationComponent.builder()
                .uIModule(new UIModule(this))
                .dBModule(new DBModule(this))
                .build();
    }
}
