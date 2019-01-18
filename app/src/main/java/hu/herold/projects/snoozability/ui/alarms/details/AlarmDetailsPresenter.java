package hu.herold.projects.snoozability.ui.alarms.details;

import android.util.Log;

import com.google.gson.Gson;

import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.base.Presenter;

public class AlarmDetailsPresenter extends Presenter<AlarmDetailsScreen> {

    public void saveAlarm(Alarm alarm) {
        Log.i("AlarmASD", "ASD:" + (new Gson().toJson(alarm)));

        screen.closeAlarmDetailsScreen();
    }
}
