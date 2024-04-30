package rfinder.dynamic;

import rfinder.dynamic.label.DoubleMinLabel;
import rfinder.dynamic.label.DurationMinLabel;
import rfinder.dynamic.label.Label;
import rfinder.dynamic.label.TimeMinLabel;

public enum ECriteria {
    ARRIVAL_TIME(TimeMinLabel.class, "Arrival time"),
    WALKING_KM(DoubleMinLabel.class, "Walking distance"),
    WAITING_TIME(DurationMinLabel.class, "Waiting time");

    ECriteria(Class<? extends Label> labelClass, String name) {
        this.labelClass = labelClass;
        this.name = name;
    }

    private final Class<? extends Label> labelClass;

    private final String name;

    public String getName() {
        return name;
    }

    public Class<? extends Label> getLabelClass() {
        return labelClass;
    }
}