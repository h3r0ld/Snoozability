package hu.herold.projects.snoozability.ui.reciever;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.base.BaseActivity;

public class AlarmReceiverActivity extends BaseActivity implements AlarmReceiverScreen {

    @BindView(R.id.alarmTimeTextView)
    TextView alarmTimeTextView;
    @BindView(R.id.alarmTitleTextView)
    TextView alarmTitleTextView;
    @BindView(R.id.snoozeButton)
    MaterialButton snoozeButton;
    @BindView(R.id.stopButton)
    MaterialButton stopButton;

    @Inject
    AlarmReceiverPresenter alarmReceiverPresenter;

    private Alarm alarm;

    public AlarmReceiverActivity() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmReceiverPresenter.attachScreen(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(SnoozabilityAlarmManager.ID_KEY)) {
            long alarmId = intent.getExtras().getLong(SnoozabilityAlarmManager.ID_KEY);
            alarmReceiverPresenter.getAlarm(alarmId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmReceiverPresenter.detachScreen();
    }

    @Override
    public void showAlarm(Alarm alarm) {
        this.alarm = alarm;

        if (alarm.getMaxSnoozeCount() != null && (alarm.getMaxSnoozeCount() == 0 || alarm.getRemainingSnoozeCount() == 0)) {
            snoozeButton.setVisibility(View.GONE);
        }

        alarmTimeTextView.setText(String.format(getString(R.string.time_format), alarm.getAlarmHour(), alarm.getAlarmMinutes()));
        alarmTitleTextView.setText(alarm.getLabel());
    }

    @Override
    public void close() {
        finish();
    }

    @OnClick(R.id.snoozeButton)
    public void onSnoozeButtonClicked(View view) {
        alarmReceiverPresenter.snoozeAlarm(alarm);
    }

    @OnClick(R.id.stopButton)
    public void onStopButtonClicked(View view) {
        alarmReceiverPresenter.stopAlarm(alarm);
    }
}
