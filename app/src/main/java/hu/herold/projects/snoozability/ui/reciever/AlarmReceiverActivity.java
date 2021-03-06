package hu.herold.projects.snoozability.ui.reciever;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.manager.alarm.SnoozabilityAlarmManager;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.model.Quote;
import hu.herold.projects.snoozability.ui.base.BaseActivity;
import hu.herold.projects.snoozability.utils.WakeLocker;

public class AlarmReceiverActivity extends BaseActivity implements AlarmReceiverScreen {

    @BindView(R.id.alarmTimeTextView)
    TextView alarmTimeTextView;
    @BindView(R.id.alarmTitleTextView)
    TextView alarmTitleTextView;
    @BindView(R.id.snoozeButton)
    MaterialButton snoozeButton;
    @BindView(R.id.stopButton)
    MaterialButton stopButton;
    @BindView(R.id.currentTimeTextView)
    TextView currentTimeTextView;
    @BindView(R.id.alarmTitleScrollView)
    ScrollView alarmTitleScrollView;
    @BindView(R.id.buttonsLinearLayout)
    LinearLayout buttonsLinearLayout;
    @BindView(R.id.quoteTextView)
    TextView quoteTextView;
    @BindView(R.id.authorTextView)
    TextView authorTextView;
    @BindView(R.id.quoteLayout)
    LinearLayout quoteLayout;

    @Inject
    AlarmReceiverPresenter alarmReceiverPresenter;

    @Inject
    AudioManager audioManager;

    @Inject
    SnoozabilityAlarmManager snoozabilityAlarmManager;

    private MediaPlayer mediaPlayer;
    private int deviceVolume;
    private Alarm alarm;

    public AlarmReceiverActivity() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);
        ButterKnife.bind(this);

        deviceVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
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

            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmReceiverPresenter.detachScreen();
        mediaPlayer.release();
        WakeLocker.release();
    }

    @Override
    public void showAlarm(Alarm alarm, Quote quote) {
        this.alarm = alarm;

        if (alarm.getMaxSnoozeCount() != null && (alarm.getMaxSnoozeCount() == 0 || alarm.getRemainingSnoozeCount() == 0)) {
            snoozeButton.setVisibility(View.GONE);
        }

        if (alarm.getMaxSnoozeCount() != null && alarm.getMaxSnoozeCount() > 0 && alarm.getRemainingSnoozeCount() > 0) {
            snoozeButton.setText(String.format(getString(R.string.snooze_with_remaining), alarm.getRemainingSnoozeCount()));
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        currentTimeTextView.setText(String.format(getString(R.string.time_format), currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE)));

        alarmTimeTextView.setText(String.format(getString(R.string.time_format), alarm.getAlarmHour(), alarm.getAlarmMinutes()));
        alarmTitleTextView.setText(alarm.getLabel());

        quoteTextView.setText(String.format(getString(R.string.quote_template), quote.getQuote()));
        authorTextView.setText(String.format(getString(R.string.author_template), quote.getAuthor()));

        if (alarm.getLabel().length() >= 300) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonsLinearLayout.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ABOVE, alarmTimeTextView.getId());

            buttonsLinearLayout.setLayoutParams(params);

            quoteLayout.setVisibility(View.INVISIBLE);
        }

        playSound();
    }

    @Override
    public void close() {
        finish();
    }

    @OnClick(R.id.snoozeButton)
    public void onSnoozeButtonClicked(View view) {
        alarmReceiverPresenter.snoozeAlarm(alarm);
        stopSound();
    }

    @OnClick(R.id.stopButton)
    public void onStopButtonClicked(View view) {
        alarmReceiverPresenter.stopAlarm(alarm);
        stopSound();
    }

    private void playSound() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getAlarmVolume(), 0);

        mediaPlayer.setLooping(true);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();
    }

    private void stopSound() {
        // reset the volume to what it was before we changed it.
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, deviceVolume, 0);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
}
