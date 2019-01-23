package hu.herold.projects.snoozability.ui.alarms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
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

public class AlarmsActivity extends BaseActivity implements AlarmsScreen, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AlarmEnabledChangeListener {

    public static final String ALARM_KEY = "ALARM_KEY";

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

        alarms = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(AlarmsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        alarmsRecyclerView.setLayoutManager(llm);

        alarmsAdapter = new AlarmsAdapter(AlarmsActivity.this, alarms);
        alarmsRecyclerView.setAdapter(alarmsAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(alarmsRecyclerView);
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

    @Override
    public void handleAlarmDeleted(final Alarm alarm) {
        // get the removed item name to display it in snack bar
        String name = alarm.getLabel();

        if (name.length() > 20) {
            name = name.substring(0, 20) + getString(R.string.dotdotdot);
        }

        // remove the item from recycler view
        alarmsAdapter.removeItem(alarm);

        Snackbar.make(alarmsRecyclerView, name + " removed from alarms!", Snackbar.LENGTH_LONG)
            .setAction("Restore", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmsPresenter.saveAlarm(alarm);
                }
            })
            .setActionTextColor(Color.YELLOW)
            .show();

    }

    @Override
    public void handleAlarmRestored(Alarm alarm) {
        String name = alarm.getLabel();

        if (name.length() > 20) {
            name = name.substring(0, 20) + getString(R.string.dotdotdot);
        }

        alarmsAdapter.restoreItem(alarm);

        Snackbar.make(alarmsRecyclerView, name + " restored!", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void handleAlarmEnabled(boolean enabled) {
        Snackbar.make(alarmsRecyclerView, "Alarm " + (enabled ? "enabled" : "disabled") + "!", Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AlarmsAdapter.ViewHolder) {
            alarmsPresenter.deleteAlarm(((AlarmsAdapter.ViewHolder) viewHolder).getAlarm());
        }
    }

    @OnClick(R.id.fab)
    public void createNewAlarm() {
        Intent intent = new Intent(AlarmsActivity.this, AlarmDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void alarmEnabledChanged(Alarm alarm, boolean enabled) {
        alarmsPresenter.enableAlarm(alarm, enabled);
    }
}
