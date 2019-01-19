package hu.herold.projects.snoozability.ui.alarms.details;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.alarms.AlarmsActivity;

public class AlarmDetailsActivity extends AppCompatActivity implements AlarmDetailsScreen {

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

    @Inject
    AlarmDetailsPresenter alarmDetailsPresenter;


    private Alarm alarm;

    public AlarmDetailsActivity() {
        SnoozabilityApplication.injector.inject(this);
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
            alarm = (new Gson()).fromJson(json, Alarm.class);
        } else {
            alarm = Alarm.builder()
                    .alarmHour(8)
                    .alarmMinutes(0)
                    .label(getString(R.string.alarm_label_placeholder))
                    .enabled(true)
                    .build();
        }

        showAlarm();
    }

    @Override
    public void showError(String message) {

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

        setSnoozeInputs();
    }

    private void setSnoozeInputs() {
        switch (snoozeTypeMSTB.getValue()) {
            case 0:
                setSnoozeInputsVisibility(View.GONE, View.GONE);
                break;
            case 1:
                setSnoozeInputsVisibility(View.VISIBLE, View.VISIBLE);
                break;
            case 2:
                setSnoozeInputsVisibility(View.VISIBLE, View.GONE);
                break;
            default:
                break;
        }
    }

    private void setSnoozeInputsVisibility(int timeVisibility, int countVisibility) {
        // Snooze Time
        snoozeTimeTextInput.setVisibility(timeVisibility);

        // Max Snooze Count
        snoozeCountTextInput.setVisibility(countVisibility);
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

    private void updateAlarmBySelection() {
        switch (snoozeTypeMSTB.getValue()) {
            case 0:
                alarm.setSnoozeTime(null);
                alarm.setMaxSnoozeCount(0);
                break;
            case 1:
                String snoozeCountEditTextValue = snoozeCountEditText.getText().toString();
                if (!snoozeCountEditTextValue.isEmpty()) {
                    alarm.setMaxSnoozeCount(Integer.parseInt(snoozeCountEditTextValue));
                }

                String snoozeTimeEditTextValue = snoozeTimeEditText.getText().toString();
                if (!snoozeTimeEditTextValue.isEmpty()) {
                    alarm.setSnoozeTime(Integer.parseInt(snoozeTimeEditTextValue));
                }
                break;
            case 2:
                alarm.setMaxSnoozeCount(null);
                break;
            default:
                break;
        }

        alarm.setLabel(alarmTitleEditText.getText() + "");
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

        timePickerDialog.setTitle("Alarm time");
        timePickerDialog.show();
    }

    private void saveAlarm() {
        updateAlarmBySelection();

        boolean isValid = true;

        if (snoozeTypeMSTB.getValue() > 0) {
            isValid &= validateEditText(snoozeTimeTextInput, snoozeTimeEditText);
        }

        if (snoozeTypeMSTB.getValue() == 1) {
            isValid &= validateEditText(snoozeCountTextInput, snoozeCountEditText);
        }

        if (isValid) {
            alarmDetailsPresenter.saveAlarm(alarm);
        } else {
            Snackbar.make(coordinatorLayout, R.string.validation_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean validateEditText(TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        String editTextValue = textInputEditText.getText().toString();

        if (editTextValue.isEmpty()) {
            textInputLayout.setError(getResources().getText(R.string.required_field));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}
