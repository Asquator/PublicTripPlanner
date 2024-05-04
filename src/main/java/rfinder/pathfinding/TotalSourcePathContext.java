package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.List;

public interface TotalSourcePathContext<T extends GraphNode> extends SourcePathContext<T> {

    List<PathRecord<? extends T>> getAllComputed(); // <node, score>

    void clearContext();
}
