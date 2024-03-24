package rfinder.query.result;


import rfinder.structures.common.Location;

import java.util.List;

public abstract class PathSegment implements PathElement{
    private final NominalPathElement sourceNode;
    private final NominalPathElement destinationNode;

    private final List<Location> shape;

    public PathSegment(NominalPathElement sourceNode, NominalPathElement destinationNode, List<Location> shape) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.shape = shape;
    }


    public NominalPathElement getSource() {
        return sourceNode;
    }

    public NominalPathElement getDestination() {
        return destinationNode;
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
