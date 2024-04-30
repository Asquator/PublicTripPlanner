package rfinder.query.result;


import rfinder.structures.common.Location;

import java.util.List;

public abstract class PathSegment implements PathElement{

    private final List<Location> shape;

    public PathSegment(List<Location> shape) {
        this.shape = shape;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
