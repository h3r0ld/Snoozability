package hu.herold.projects.snoozability;

import javax.inject.Singleton;

import dagger.Component;
import hu.herold.projects.snoozability.db.DBModule;
import hu.herold.projects.snoozability.interactor.InteractorModule;
import hu.herold.projects.snoozability.interactor.alarms.AlarmsInteractor;
import hu.herold.projects.snoozability.manager.ManagerModule;
import hu.herold.projects.snoozability.manager.alarm.AlarmRestoreManager;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.manager.notification.SnoozabilityNotificationManager;
import hu.herold.projects.snoozability.receiver.AlarmDisableReceiver;
import hu.herold.projects.snoozability.receiver.DeviceBootReceiver;
import hu.herold.projects.snoozability.ui.UIModule;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;
import hu.herold.projects.snoozability.ui.alarms.AlarmsPresenter;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsPresenter;
import hu.herold.projects.snoozability.ui.reciever.AlarmReceiverActivity;
import hu.herold.projects.snoozability.ui.reciever.AlarmReceiverPresenter;
import hu.herold.projects.snoozability.ui.validation.Validator;

@Singleton
@Component(modules = {UIModule.class, DBModule.class, InteractorModule.class, ManagerModule.class})
public interface SnoozabilityApplicationComponent {

    void inject(AlarmsActivity alarmsActivity);

    void inject(AlarmsPresenter alarmsPresenter);

    void inject(AlarmsInteractor alarmsInteractor);

    void inject(AlarmDetailsActivity alarmDetailsActivity);

    void inject(AlarmDetailsPresenter alarmDetailsPresenter);

    void inject(Validator validator);

    void inject(SnoozabilityAlarmManager snoozabilityAlarmManager);

    void inject(SnoozabilityNotificationManager snoozabilityNotificationManager);

    void inject(AlarmRestoreManager alarmRestoreManager);

    void inject(AlarmReceiverActivity alarmReceiverActivity);

    void inject(AlarmReceiverPresenter alarmReceiverPresenter);

    void inject(DeviceBootReceiver deviceBootReceiver);

    void inject(AlarmDisableReceiver alarmDisableReceiver);
}
