package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.List;
import java.util.Objects;

public class GraphPath<T extends GraphNode> {
    private final List<T> path;
    private final double length;

    public GraphPath(List<T> path, double length) {
        this.path = path;
        this.length = length;
    }

    public List<T> path() {
        return path;
    }

    public double length() {
        return length;
    }

    public GraphPath<T> reversed(){
        if (path == null || path.isEmpty())
            return this;

        return new GraphPath<>(path.reversed(), length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GraphPath) obj;
        return Objects.equals(this.path, that.path) &&
                Double.doubleToLongBits(this.length) == Double.doubleToLongBits(that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, length);
    }

    @Override
    public String toString() {
        return "GraphPath[" +
                "path=" + path + ", " +
                "length=" + length + ']';
    }

}