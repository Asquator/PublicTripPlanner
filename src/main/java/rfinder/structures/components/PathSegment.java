package rfinder.structures.components;

import rfinder.structures.nodes.PathNode;

public abstract class PathSegment  {
    private final PathNode sourceNode;
    private final PathNode destinationNode;

    private double distance = 0;

    public PathSegment(PathNode sourceNode, PathNode destinationNode){
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }
    
    public PathNode getSource() {
        return sourceNode;
    }

    public PathNode getDestination() {
        return destinationNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        String ret = "[source " + sourceNode + "][target " + destinationNode + "][distance " + distance + "]";
        return ret;
    }
}
