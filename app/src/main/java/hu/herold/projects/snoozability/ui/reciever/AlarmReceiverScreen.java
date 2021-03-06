package hu.herold.projects.snoozability.ui.reciever;

import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.model.Quote;
import hu.herold.projects.snoozability.ui.base.Screen;

public interface AlarmReceiverScreen extends Screen {

    void showAlarm(Alarm alarm, Quote quote);

    void close();
}
