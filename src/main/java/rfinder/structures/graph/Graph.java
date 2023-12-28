package rfinder.structures.graph;

import rfinder.structures.nodes.VertexNode;

import java.util.Set;

public interface Graph <T extends GraphNode>{
    Set<? extends Link<T>> getLinks(T node);
}
