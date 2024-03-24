package rfinder.dynamic;

import rfinder.pathfinding.QueryPathFinder;

public record NetworkQueryContext(QueryPathFinder pathFinder, TripRepository trips, LabelMap labels, MultilabelBag finalBag) {

}
