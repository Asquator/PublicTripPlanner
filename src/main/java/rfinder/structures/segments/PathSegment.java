package rfinder.structures.segments;

import genetic.composite.Evaluable;
import rfinder.structures.nodes.PathNode;

public abstract class PathSegment implements Evaluable {
    private PathNode sourceNode;
    private PathNode destinationNode;

    public PathSegment(PathNode sourceNode, PathNode destinationNode){
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public PathNode getSourceNode() {
        return sourceNode;
    }

    public PathNode getDestinationNode() {
        return destinationNode;
    }
}
