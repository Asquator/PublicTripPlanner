package rfinder.pathfinding.graph;

import java.util.Set;

public interface Graph <T extends GraphNode>{
    Set<T> getConnections(T node);


}
