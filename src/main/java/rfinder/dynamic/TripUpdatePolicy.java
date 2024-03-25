package rfinder.dynamic;

import rfinder.structures.components.PathLink;
import rfinder.structures.components.RideLink;
import rfinder.structures.nodes.StopNode;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TripUpdatePolicy implements LabelUpdatePolicy<> {
    @Override
    public void update(Multilabel transferLabel, Object newValue, PathLink backwardLink) {
        OffsetDateTime departureTime = routeTrips.get(earliestTripSequence).stopTimes().get(stopSequence);

        RouteMultilabel newLabel = new RouteMultilabel(label,
                new RideLink((StopNode) currentRouteStops.get(stopSequence).getPathNode(), routeID, earliestTripSequence, stopSequence));

        DurationMinLabel durationLabel = (DurationMinLabel) newLabel.getLabel(ECriteria.WAITING_TIME);
        durationLabel.addDuration(Duration.between(arrivalTime, departureTime));
    }
}
