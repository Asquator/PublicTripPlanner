package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.Collection;
import java.util.List;

public interface TotalSourcePathContext<T extends GraphNode> extends SourcePathContext<T> {

    List<PathRecord<? extends T>> getAllComputed(); // <node, score>

    List<PathRecord<? extends T>> tryComputeToAll(Collection<? extends T> nodes);
    void clearContext();
}
