package rfinder.structures.graph;

import java.util.Set;

public interface Graph <T extends GraphNode>{
    Set<? extends WeightedLink<T>> getLinks(T node);
}
