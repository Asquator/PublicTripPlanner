package rfinder.dynamic;

import rfinder.structures.common.RouteID;

import java.util.List;

public interface TripRepositoryi {
    List<TripInstance> getRelevantTrips(RouteID routeID);
}
