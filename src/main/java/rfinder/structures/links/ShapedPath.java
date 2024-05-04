package rfinder.structures.links;

import rfinder.pathfinding.GraphPath;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.PathNode;

import java.util.List;
import java.util.Objects;

public class ShapedPath extends GraphPath<PathNode> {
    private final List<PathNode> path;
    private final List<Location> shape;

    public ShapedPath(List<PathNode> path, double length, List<Location> shape) {
        super(path, length);
        this.path = path;
        this.shape = shape;
    }

    public List<Location> getShape() {
        return shape;
    }

    public List<PathNode> getPath() {
        return path;
    }


    @Override
    public ShapedPath reversed() {
        return new ShapedPath(path.reversed(), length(), shape.reversed());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShapedPath) obj;
        return Objects.equals(this.path, that.path) &&
                Double.doubleToLongBits(this.length()) == Double.doubleToLongBits(that.length());
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, length());
    }

    @Override
    public String toString() {
        return "Path[" +
                "path=" + path + ", " +
                "length=" + length() + ']';
    }

}
