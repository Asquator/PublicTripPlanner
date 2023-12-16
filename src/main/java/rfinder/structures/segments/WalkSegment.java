package rfinder.structures.segments;

import rfinder.structures.nodes.PathNode;

public class WalkSegment extends PathSegment{
    public WalkSegment(PathNode sourceNode, PathNode destinationNode) {
        super(sourceNode, destinationNode);
    }

    @Override
    public double evaluateFitness() {
        return 0;
    }
}
