package hu.herold.projects.snoozability.ui.alarms;

import java.util.List;

import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.base.Screen;

public interface AlarmsScreen extends Screen {

    void showAlarms(List<Alarm> alarms);

    void showError(String message);
}
