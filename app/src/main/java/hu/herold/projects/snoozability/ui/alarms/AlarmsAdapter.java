package hu.herold.projects.snoozability.ui.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.db.model.AlarmEntity;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.ui.alarms.details.AlarmDetailsActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {
    private Context context;
    private List<Alarm> alarms;

    public AlarmsAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms= alarms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_alarm, viewGroup, false);

        return new ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Alarm alarm = alarms.get(position);

        viewHolder.setAlarm(alarm);

        viewHolder.alarmTitleTextView.setText(alarm.getLabel());
        viewHolder.alarmTimeTextView.setText(String.format(context.getString(R.string.time_format), alarm.getAlarmHour(), alarm.getAlarmMinutes()));
        viewHolder.alarmEnabledSwitch.setChecked(alarm.isEnabled());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.alarmTimeTextView)
        TextView alarmTimeTextView;
        @BindView(R.id.alarmEnabledSwitch)
        SwitchCompat alarmEnabledSwitch;
        @BindView(R.id.alarmTitleTextView)
        TextView alarmTitleTextView;
        @BindView(R.id.layout)
        LinearLayout layout;

        private final Context context;
        private Alarm alarm;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.alarmEnabledSwitch)
        public void onCheckedChanged() {
            // TODO: Enable/disable logic, service call!
        }

        @OnClick(R.id.layout)
        public void onViewClicked() {
            Intent intent = new Intent(context, AlarmDetailsActivity.class);
            intent.putExtra(AlarmsActivity.ALARM_KEY, (new Gson()).toJson(alarm));
            context.startActivity(intent);
        }
    }
}
