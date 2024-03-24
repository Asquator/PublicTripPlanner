package rfinder.pathfinding;

import rfinder.structures.common.Location;

import java.util.List;
import java.util.Objects;

public class FootPath<T>{
    private final List<? extends T> path;
    private final double length;
    private final List<Location> shape;

    public FootPath(List<? extends T> path, double length, List<Location> shape) {
        this.path = path;
        this.length = length;
        this.shape = shape;
    }

    public List<Location> getShape() {
        return shape;
    }

    public List<? extends T> getPath() {
        return path;
    }

    public double getLength() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FootPath) obj;
        return Objects.equals(this.path, that.path) &&
                Double.doubleToLongBits(this.length) == Double.doubleToLongBits(that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, length);
    }

    @Override
    public String toString() {
        return "Path[" +
                "path=" + path + ", " +
                "length=" + length + ']';
    }

}
