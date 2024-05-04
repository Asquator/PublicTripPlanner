package rfinder.structures.links;

import org.jetbrains.annotations.NotNull;
import rfinder.structures.graph.GraphNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class EdgeData<T extends GraphNode<?>> {

    Set<T> linkedNodes = new HashSet<>();


    public void addLinkage(T pathNode) {
        linkedNodes.add(pathNode);
    }

    public void removeLinkage(T pathNode) {
        linkedNodes.remove(pathNode);
    }

    @NotNull
    public Iterator<T> linkIterator() {
        return linkedNodes.iterator();
    }

}
