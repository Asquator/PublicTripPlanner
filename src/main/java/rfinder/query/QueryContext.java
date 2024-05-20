package rfinder.query;

import rfinder.dao.StopStorage;
import rfinder.dao.TripRepository;
import rfinder.dynamic.label.UpdatePrunePolicy;
import rfinder.pathfinding.QueryFootpathFinder;

public record QueryContext(QueryInfo queryInfo, QueryGraphInfo queryGraphInfo, QueryFootpathFinder pathFinder,
                           UpdatePrunePolicy prunePolicy, TripRepository tripRepository, StopStorage stopStorage) {}
