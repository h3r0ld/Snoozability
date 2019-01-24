package hu.herold.projects.snoozability.ui.alarms.details;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.clans.fab.FloatingActionButton;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.model.SnoozeType;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;
import hu.herold.projects.snoozability.ui.base.BaseActivity;
import hu.herold.projects.snoozability.ui.validation.Validator;

public class AlarmDetailsActivity extends BaseActivity implements AlarmDetailsScreen {

    @BindView(R.id.alarmTimeTextView)
    TextView alarmTimeTextView;
    @BindView(R.id.snoozeTextView)
    TextView snoozeTextView;
    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    @BindView(R.id.snoozeTypeMSTB)
    MultiStateToggleButton snoozeTypeMSTB;
    @BindView(R.id.snoozeCountTextInput)
    TextInputLayout snoozeCountTextInput;
    @BindView(R.id.snoozeTimeTextInput)
    TextInputLayout snoozeTimeTextInput;
    @BindView(R.id.alarmTitleEditText)
    TextInputEditText alarmTitleEditText;
    @BindView(R.id.alarmTitleTextInput)
    TextInputLayout alarmTitleTextInput;
    @BindView(R.id.snoozeCountEditText)
    TextInputEditText snoozeCountEditText;
    @BindView(R.id.snoozeTimeEditText)
    TextInputEditText snoozeTimeEditText;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.volumeSeekBar)
    AppCompatSeekBar volumeSeekBar;

    @Inject
    AlarmDetailsPresenter alarmDetailsPresenter;

    @Inject
    Validator validator;

    @Inject
    AudioManager audioManager;

    private Alarm alarm;
    private int maxVolume;

    public AlarmDetailsActivity() {
        SnoozabilityApplication.injector.inject(this);

        this.maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);
        ButterKnife.bind(this);

        this.snoozeTypeMSTB.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                setSnoozeInputs();
            }
        });

        volumeSeekBar.setMax(maxVolume);
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmDetailsPresenter.attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmDetailsPresenter.detachScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(AlarmsActivity.ALARM_KEY)) {
            String json = intent.getExtras().getString(AlarmsActivity.ALARM_KEY);
            alarm = Alarm.fromJson(json);
        } else {
            alarm = Alarm.builder()
                    .alarmHour(8)
                    .alarmMinutes(0)
                    .snoozeTime(9)
                    .label(getString(R.string.alarm_label_placeholder))
                    .alarmVolume(maxVolume / 2)
                    .enabled(true)
                    .build();
        }

        showAlarm();
    }

    @OnClick({R.id.alarmTimeTextView, R.id.fabSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alarmTimeTextView:
                showTimePickerDialog();
                break;
            case R.id.fabSave:
                saveAlarm();
                break;
        }
    }

    @Override
    public void closeAlarmDetailsScreen() {
        finish();
    }

    private void showAlarm() {
        if (alarm.getMaxSnoozeCount() == null) {
            // Infinite snoozes
            snoozeTypeMSTB.setValue(2);
            snoozeTimeEditText.setText(alarm.getSnoozeTime().toString());
        } else if (alarm.getMaxSnoozeCount() == 0) {
            // Never snooze
            snoozeTypeMSTB.setValue(0);
        } else {
            // Custom snooze
            snoozeTypeMSTB.setValue(1);
            snoozeTimeEditText.setText(alarm.getSnoozeTime().toString());
            snoozeCountEditText.setText(alarm.getMaxSnoozeCount().toString());
        }

        alarmTitleEditText.setText(alarm.getLabel());
        alarmTimeTextView.setText(String.format(getString(R.string.time_format), alarm.getAlarmHour(), alarm.getAlarmMinutes()));


        volumeSeekBar.setProgress(alarm.getAlarmVolume());

        setSnoozeInputs();
    }

    private void setSnoozeInputs() {
        if (snoozeTypeMSTB.getValue() == SnoozeType.NEVER) {
            setSnoozeInputsVisibility(View.GONE, View.GONE);
        }

        if (snoozeTypeMSTB.getValue() == SnoozeType.CUSTOM) {
            setSnoozeInputsVisibility(View.VISIBLE, View.VISIBLE);
        }

        if (snoozeTypeMSTB.getValue() == SnoozeType.INFINITE) {
            setSnoozeInputsVisibility(View.VISIBLE, View.GONE);
        }
    }

    private void setSnoozeInputsVisibility(int timeVisibility, int countVisibility) {
        snoozeTimeTextInput.setVisibility(timeVisibility);
        snoozeCountTextInput.setVisibility(countVisibility);
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                alarm.setAlarmHour(hourOfDay);
                alarm.setAlarmMinutes(minute);
                alarmTimeTextView.setText(String.format(getString(R.string.time_format), hourOfDay, minute));
            }
        }, alarm.getAlarmHour(), alarm.getAlarmMinutes(), true);

        timePickerDialog.setTitle(getString(R.string.alarm_time_dialog_title));
        timePickerDialog.show();
    }

    private void saveAlarm() {
        updateAlarmBySelection();

        boolean isValid = true;

        if (snoozeTypeMSTB.getValue() != SnoozeType.NEVER) {
            isValid = validator.validateEditText(snoozeTimeTextInput, snoozeTimeEditText);
        }

        if (snoozeTypeMSTB.getValue() == SnoozeType.CUSTOM) {
            isValid &= validator.validateEditText(snoozeCountTextInput, snoozeCountEditText);
        }

        if (isValid) {
            alarmDetailsPresenter.saveAlarm(alarm);
        } else {
            Snackbar.make(coordinatorLayout, R.string.validation_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateAlarmBySelection() {
        if (snoozeTypeMSTB.getValue() == SnoozeType.NEVER) {
            alarm.setSnoozeTime(null);
            alarm.setMaxSnoozeCount(0);
        }

        if (snoozeTypeMSTB.getValue() == SnoozeType.CUSTOM) {
            String snoozeCountEditTextValue = snoozeCountEditText.getText().toString();
            if (!snoozeCountEditTextValue.isEmpty()) {
                int snoozeCount = Integer.parseInt(snoozeCountEditTextValue);
                alarm.setMaxSnoozeCount(snoozeCount);
                alarm.setRemainingSnoozeCount(snoozeCount);
            }

            String snoozeTimeEditTextValue = snoozeTimeEditText.getText().toString();
            if (!snoozeTimeEditTextValue.isEmpty()) {
                alarm.setSnoozeTime(Integer.parseInt(snoozeTimeEditTextValue));
            }
        }

        if (snoozeTypeMSTB.getValue() == SnoozeType.INFINITE) {
            alarm.setMaxSnoozeCount(null);
        }

        alarm.setAlarmVolume(volumeSeekBar.getProgress());

        alarm.setLabel(alarmTitleEditText.getText().toString());
    }
}
