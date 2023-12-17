package rfinder.pathfinding.graph;

public interface GraphNode  extends Comparable<GraphNode>{
    int getId();

    @Override
    public default int compareTo(GraphNode otherNode){
        return Integer.compare(getId(), otherNode.getId());
    }
}
