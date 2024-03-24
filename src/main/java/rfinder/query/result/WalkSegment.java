package rfinder.query.result;

import rfinder.structures.common.Location;

import java.util.List;

public class WalkSegment extends PathSegment implements PathElement{
    public WalkSegment(NominalPathElement sourceNode, NominalPathElement destinationNode, List<Location> shape) {
        super(sourceNode, destinationNode, shape);
    }
    @Override
    public List<Location> getShape() {
        return null;
    }
}
