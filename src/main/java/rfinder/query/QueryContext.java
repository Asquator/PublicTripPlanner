package rfinder.query;

import rfinder.dao.StopStorage;
import rfinder.dynamic.TripRepository;
import rfinder.pathfinding.QueryFootpathManager;

public record QueryContext(QueryInfo queryInfo, QueryGraphInfo queryGraphInfo, QueryFootpathManager pathFinder, TripRepository tripRepository,
                           StopStorage stopStorage) {

}
