package hu.herold.projects.snoozability;

import javax.inject.Singleton;

import dagger.Component;
import hu.herold.projects.snoozability.db.DBModule;
import hu.herold.projects.snoozability.interactor.InteractorModule;
import hu.herold.projects.snoozability.ui.UIModule;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;

@Singleton
@Component(modules = {UIModule.class, DBModule.class, InteractorModule.class })
public interface SnoozabilityApplicationComponent {

    void inject(AlarmsActivity alarmsActivity);
}
