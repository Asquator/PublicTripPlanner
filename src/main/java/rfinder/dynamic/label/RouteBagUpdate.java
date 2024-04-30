package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;
import rfinder.dynamic.label.DurationMinLabel;
import rfinder.dynamic.label.LabelUpdatePolicy;
import rfinder.dynamic.label.Multilabel;

import java.time.Duration;
import java.time.LocalDateTime;

public class RouteBagUpdate implements LabelUpdatePolicy<LocalDateTime> {


    @Override
    public void update(Multilabel transferLabel, LocalDateTime departureTime) {
        LocalDateTime arrivalTime = transferLabel.getArrivalTime();

        // add waiting duration to the corresponding label
        DurationMinLabel durationLabel = (DurationMinLabel) transferLabel.getLabel(ECriteria.WAITING_TIME);
        durationLabel.setDuration(durationLabel.getDuration().plus(Duration.between(arrivalTime, departureTime)));

    }
}
