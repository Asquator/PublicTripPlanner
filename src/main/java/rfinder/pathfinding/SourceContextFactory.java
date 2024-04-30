package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

public interface SourceContextFactory<T extends GraphNode> {

    SharedSourcePathContext<T> createContext(T source);
}
