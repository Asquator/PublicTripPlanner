package rfinder.pathfinding;

import java.util.List;
import java.util.Objects;

public class Path<T>{
    private final List<? extends T> path;
    private final double length;

    public Path(List<? extends T> path, double length) {
        this.path = path;
        this.length = length;
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
        var that = (Path) obj;
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
