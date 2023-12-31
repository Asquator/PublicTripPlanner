package rfinder.structures.segments;

import rfinder.structures.graph.Link;
import rfinder.structures.nodes.PathNode;

public class PathLink implements Link<PathNode> {

    private final PathNode destinationNode;

    public PathLink(PathNode destinationNode){
        this.destinationNode = destinationNode;
    }

    @Override
    public PathNode getDestination() {
        return destinationNode;
    }

    @Override
    public String toString() {
        return destinationNode.toString();
    }
}
