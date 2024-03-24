package rfinder.structures.graph;

import java.util.Set;

public interface RoutableGraph <T extends GraphNode, L extends RouteLink<T>> extends Graph<T>{
    @Override
    Set<L> getLinks(T node);
}
