package rfinder.dynamic;

import rfinder.structures.components.PathLink;
import rfinder.structures.components.RideLink;
import rfinder.structures.nodes.StopNode;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TripUpdatePolicy implements LabelUpdatePolicy<OffsetDateTime> {

    private static TripUpdatePolicy instance = new TripUpdatePolicy();

    public static TripUpdatePolicy getInstance() {
        return instance;
    }

    private TripUpdatePolicy() {

    }
    @Override
    public void update(Multilabel transferLabel, OffsetDateTime departureTime, PathLink backwardLink) {

        OffsetDateTime arrivalTime = transferLabel.getArrivalTime();

        // add waiting duration to the corresponding label
        DurationMinLabel durationLabel = (DurationMinLabel) transferLabel.getLabel(ECriteria.WAITING_TIME);
        durationLabel.addDuration(Duration.between(arrivalTime, departureTime));

        transferLabel.setBackwardLink(backwardLink);
    }
}
