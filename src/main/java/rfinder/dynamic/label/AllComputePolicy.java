package rfinder.dynamic.label;

import rfinder.aux.Converters;
import rfinder.dynamic.ECriteria;

import java.time.Duration;
import java.time.LocalDateTime;

public class AllComputePolicy implements UpdatePrunePolicy{

    @Override
    public boolean updatePruneMergeRoute(Multilabel transferLabel, Object... params) {
        final long LOCAL_PRUNE_SEC = 6500L;

        LocalDateTime departureTime = (LocalDateTime) params[0];
        LocalDateTime arrivalTime = transferLabel.getArrivalTime();

        if(departureTime.isAfter(arrivalTime.plusSeconds(LOCAL_PRUNE_SEC)))
            return true;

        // add waiting duration to the corresponding label
        DurationMinLabel durationLabel = (DurationMinLabel) transferLabel.getLabel(ECriteria.WAITING_TIME);
        durationLabel.setDuration(durationLabel.getDuration().plus(Duration.between(arrivalTime, departureTime)));

        // increase trips by one
        ByteMinLabel minLabel = (ByteMinLabel) transferLabel.getLabel(ECriteria.N_TRIPS);
        minLabel.setCost((byte) (minLabel.getCost() + 1));
        return false;
    }

    @Override
    public void updateFootpaths(Multilabel transferLabel, Object... params) {
        double newValue = (double) params[0];
        TimeMinLabel timeLabel = (TimeMinLabel) transferLabel.getLabel(ECriteria.ARRIVAL_TIME);
        timeLabel.setTimestamp(timeLabel.getTimestamp().plus(Converters.kmToDuration(newValue)));

        DoubleMinLabel walkLabel = (DoubleMinLabel) transferLabel.getLabel(ECriteria.WALKING_KM);
        walkLabel.setCost(walkLabel.getCost() + newValue);
    }

}
