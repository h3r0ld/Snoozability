package hu.herold.projects.snoozability.ui.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.herold.projects.snoozability.BuildConfig;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import hu.herold.projects.snoozability.ui.base.BaseActivity;

public class AlarmsActivity extends BaseActivity implements AlarmsScreen {

    public static final String ALARM_KEY = "ALARM_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.alarmsRecyclerView)
    RecyclerView alarmsRecyclerView;

    @Inject
    AlarmsPresenter alarmsPresenter;

    private AlarmsAdapter alarmsAdapter;
    private List<Alarm> alarms;

    public AlarmsActivity() {
        SnoozabilityApplication.injector.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AppCenter Analytics, Crashes and Distribute initialization
        AppCenter.start(getApplication(), BuildConfig.APP_CENTER_SECRET,
                Analytics.class, Crashes.class, Distribute.class);

        setContentView(R.layout.activity_alarms);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        alarms = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(AlarmsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        alarmsRecyclerView.setLayoutManager(llm);

        alarmsAdapter = new AlarmsAdapter(AlarmsActivity.this, alarms);
        alarmsRecyclerView.setAdapter(alarmsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmsPresenter.attachScreen(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmsPresenter.getAlarms();
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmsPresenter.detachScreen();
    }

    @Override
    public void showAlarms(List<Alarm> alarmsList) {
        alarms.clear();
        alarms.addAll(alarmsList);

        alarmsAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab)
    public void createNewAlarm() {
        Intent intent = new Intent(AlarmsActivity.this, AlarmDetailsActivity.class);
        startActivity(intent);
    }
}
