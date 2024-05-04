package rfinder.query;

import rfinder.dao.StopStorage;
import rfinder.dao.TripRepository;
import rfinder.dynamic.label.UpdatePrunePolicy;
import rfinder.pathfinding.QueryFootpathManager;

public record QueryContext(QueryInfo queryInfo, QueryGraphInfo queryGraphInfo, QueryFootpathManager pathFinder,
                           UpdatePrunePolicy prunePolicy, TripRepository tripRepository, StopStorage stopStorage) {}
