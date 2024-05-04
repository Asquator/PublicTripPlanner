package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

public interface SourceContextFactory<T extends GraphNode> {

    SourcePathContext<T> createContext(T source);
}
