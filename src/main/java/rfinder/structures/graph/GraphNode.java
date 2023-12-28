package rfinder.structures.graph;

public interface GraphNode<T extends Comparable<T>>  extends Comparable<GraphNode<? extends T>>{
    T getId();

    @Override
    public default int compareTo(GraphNode<? extends T> otherNode){
        return getId().compareTo(otherNode.getId());
    }
}
