package rfinder.structures.graph;

public interface WeightedLink<T extends GraphNode> extends Link<T>{
    double getWeight();
}
