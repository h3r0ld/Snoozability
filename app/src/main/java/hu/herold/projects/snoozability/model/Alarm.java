package hu.herold.projects.snoozability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Alarm {

    private Long id;

    private String label;

    private int alarmHour;

    private int alarmMinutes;

    private Integer snoozeTime;

    private Integer maxSnoozeCount;

    private Integer alarmVolume;

    private boolean enabled;
}
