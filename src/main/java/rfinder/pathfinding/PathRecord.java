package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;

public class PathRecord<T extends GraphNode> extends RouteLink<T> {
    public PathRecord(T destination, double weight) {
        super(destination, weight);
    }
}
