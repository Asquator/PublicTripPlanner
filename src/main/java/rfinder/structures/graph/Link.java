package rfinder.structures.graph;

public interface Link <T extends GraphNode>{
    T getDestination();
    double getWeight();
}
