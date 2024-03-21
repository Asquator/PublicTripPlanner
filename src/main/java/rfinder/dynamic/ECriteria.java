package rfinder.dynamic;

public enum ECriteria {
    ARRIVAL_TIME(TimeMinLabel.class),
    WAITING_TIME(DurationMinLabel.class),
    WALKING_KM(DoubleMinLabel.class);

    ECriteria(Class<? extends Label> labelClass){
        this.labelClass = labelClass;
    }

    private final Class<? extends Label> labelClass;

    public Class<? extends Label> getLabelClass() {
        return labelClass;
    }
}