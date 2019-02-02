package hu.herold.projects.snoozability.ui.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.polyak.iconswitch.IconSwitch;

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
@EqualsAndHashCode(callSuper = true)
public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {
    private Context context;
    private List<Alarm> alarms;

    private int lastDeletedIndex;

    protected AlarmEnabledChangeListener alarmEnabledChangeListener;

    public AlarmsAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms= alarms;

        if (!(context instanceof AlarmEnabledChangeListener)) {
            throw new IllegalArgumentException("Activity must implement AlarmEnabledChangeListener!");
        } else {
            alarmEnabledChangeListener = (AlarmEnabledChangeListener) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_alarm, viewGroup, false);

        return new ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Alarm alarm = alarms.get(position);

        viewHolder.setAlarm(alarm);

        viewHolder.alarmTitleTextView.setText(alarm.getLabel());
        viewHolder.alarmTimeTextView.setText(String.format(context.getString(R.string.time_format), alarm.getAlarmHour(), alarm.getAlarmMinutes()));
        if (alarm.isEnabled()) {
            viewHolder.alarmEnabledSwitch.setChecked(IconSwitch.Checked.RIGHT);
        } else {
            viewHolder.alarmEnabledSwitch.setChecked(IconSwitch.Checked.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void removeItem(int position) {
        alarms.remove(position);
        notifyItemRemoved(position);
        lastDeletedIndex = position;
    }

    public void removeItem(Alarm alarm) {
        int position = alarms.indexOf(alarm);
        removeItem(position);
    }

    public void restoreItem(Alarm alarm) {
        alarms.add(lastDeletedIndex, alarm);
        notifyItemInserted(lastDeletedIndex);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.alarmTimeTextView)
        TextView alarmTimeTextView;
        @BindView(R.id.alarmEnabledSwitch)
        IconSwitch alarmEnabledSwitch;
        @BindView(R.id.alarmTitleTextView)
        TextView alarmTitleTextView;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.viewBackground)
        RelativeLayout viewBackground;
        @BindView(R.id.viewForeground)
        MaterialCardView viewForeground;

        private final Context context;
        private Alarm alarm;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);

            this.alarmEnabledSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
                @Override
                public void onCheckChanged(IconSwitch.Checked current) {
                    alarmEnabledChangeListener.alarmEnabledChanged(alarm, current.equals(IconSwitch.Checked.RIGHT));
                }
            });
        }

        @OnClick(R.id.layout)
        public void onViewClicked() {
            Intent intent = new Intent(context, AlarmDetailsActivity.class);
            intent.putExtra(AlarmsActivity.ALARM_KEY, (new Gson()).toJson(alarm));
            context.startActivity(intent);
        }
    }
}
