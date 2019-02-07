package hu.herold.projects.snoozability.interactor.alarms.event;

import hu.herold.projects.snoozability.interactor.base.EventBase;
import hu.herold.projects.snoozability.model.Alarm;
import hu.herold.projects.snoozability.model.Quote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetAlarmEvent extends EventBase {
    private Alarm alarm;
    private Quote quote;
}
