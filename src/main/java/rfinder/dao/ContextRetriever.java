package rfinder.dao;

import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;

import java.util.Set;

public interface ContextRetriever {
    public Set<RouteID> getRelevantRoutes(QueryInfo context);

    //public List<TripID> getRelevantTrips(StopNode stop, OffsetDateTime time);
}
