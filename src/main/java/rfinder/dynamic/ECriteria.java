package rfinder.dynamic;

import rfinder.dynamic.label.*;

public enum ECriteria {
    ARRIVAL_TIME(TimeMinLabel.class,"Arrival time", "Quickest"),
    WALKING_KM(DoubleMinLabel.class, "Walking distance", "Less walking"),
    N_TRIPS(ByteMinLabel.class, "Trips", "Less trips"),
    WAITING_TIME(DurationMinLabel.class, "Waiting time", "Less waiting");

    ECriteria(Class<? extends Label> labelClass, String paramName, String characteristicName){
        this.labelClass = labelClass;
        this.characteristicName = characteristicName;
        this.paramName = paramName;
    }

    private final Class<? extends Label> labelClass;


    private final String characteristicName;

    private final String paramName;

    public String getCharacteristicName() {
        return characteristicName;
    }

    public String getParamName() {
        return paramName;
    }


    public Class<? extends Label> getLabelClass() {
        return labelClass;
    }

}