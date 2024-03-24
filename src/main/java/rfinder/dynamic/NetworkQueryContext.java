package rfinder.dynamic;

import rfinder.structures.nodes.PathNode;

import java.util.Map;

public record DynamicSearchContext(TripRepository trips, Map<PathNode, RoundNodeContext> labels) {

}
