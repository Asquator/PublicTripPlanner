package rfinder.structures.links;

import org.jetbrains.annotations.NotNull;
import rfinder.structures.graph.GraphNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class EdgeData<T extends GraphNode<?>> {

    Set<T> linkedNodes = new HashSet<>();

    private final Object edgeId;

    public EdgeData(Object edgeId) {
        this.edgeId = edgeId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeData<?> edgeData)) return false;
        return Objects.equals(edgeId, edgeData.edgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(edgeId);
    }
}
