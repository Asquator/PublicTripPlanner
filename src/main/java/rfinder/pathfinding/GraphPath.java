package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.nodes.PathNode;

import java.util.List;

public final class GraphPath<T extends GraphNode> extends Path<T>{

    public GraphPath(List<T> path, double length) {
        super(path, length);
    }

}