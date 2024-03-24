package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.List;

public record GraphPath<T extends GraphNode> (List<T> path, double length) {
}