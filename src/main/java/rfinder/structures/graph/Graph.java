package rfinder.structures.graph;

import rfinder.structures.links.Link;

import java.util.Set;

public interface Graph <T extends GraphNode>{
    Set<? extends Link<? super T>> getLinks(T node);
}
