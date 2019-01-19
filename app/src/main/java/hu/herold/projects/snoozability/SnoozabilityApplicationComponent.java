package hu.herold.projects.snoozability;

import javax.inject.Singleton;

import dagger.Component;
import hu.herold.projects.snoozability.db.DBModule;
import hu.herold.projects.snoozability.interactor.InteractorModule;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.ui.UIModule;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;
import hu.herold.projects.snoozability.ui.alarms.AlarmsPresenter;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsPresenter;
import hu.herold.projects.snoozability.ui.validation.Validator;

@Singleton
@Component(modules = {UIModule.class, DBModule.class, InteractorModule.class })
public interface SnoozabilityApplicationComponent {

    void inject(AlarmsActivity alarmsActivity);

    void inject(AlarmsPresenter alarmsPresenter);

    void inject(AlarmsInteractor alarmsInteractor);

    void inject(AlarmDetailsActivity alarmDetailsActivity);

    void inject(AlarmDetailsPresenter alarmDetailsPresenter);

    void inject(Validator validator);
}
