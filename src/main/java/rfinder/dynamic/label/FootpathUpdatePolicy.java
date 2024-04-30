package rfinder.dynamic.label;

import rfinder.aux.Converters;
import rfinder.dynamic.ECriteria;

public class FootpathUpdatePolicy implements LabelUpdatePolicy<Double> {

    @Override
    public void update(Multilabel transferLabel, Double newValue) {
        TimeMinLabel timeLabel = (TimeMinLabel) transferLabel.getLabel(ECriteria.ARRIVAL_TIME);
        timeLabel.setTimestamp(timeLabel.getTimestamp().plus(Converters.kmToDuration(newValue)));

        DoubleMinLabel walkLabel = (DoubleMinLabel) transferLabel.getLabel(ECriteria.WALKING_KM);
        walkLabel.setCost(walkLabel.getCost() + newValue);
    }

}
