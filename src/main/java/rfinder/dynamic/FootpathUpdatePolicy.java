package rfinder.dynamic;

import rfinder.structures.components.PathLink;

public class FootpathUpdatePolicy implements LabelUpdatePolicy<Double>{

    private FootpathUpdatePolicy() {

    }
    private static FootpathUpdatePolicy instance = new FootpathUpdatePolicy();
    @Override
    public void update(Multilabel transferLabel, Double newValue, PathLink backwardLink) {
        TimeMinLabel timeLabel = (TimeMinLabel) transferLabel.getLabel(ECriteria.ARRIVAL_TIME);
        timeLabel.setTimestamp(timeLabel.getTimestamp().plus(Converters.kmToDuration(newValue)));

        DoubleMinLabel walkLabel = (DoubleMinLabel) transferLabel.getLabel(ECriteria.WALKING_KM);
        walkLabel.setCost(walkLabel.getCost() + newValue);
        transferLabel.setBackwardLink(backwardLink);
    }

    public static FootpathUpdatePolicy getInstance() {
        return instance;
    }
}
