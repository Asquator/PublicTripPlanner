package rfinder.pathfinding.graph;

public interface CostEvaluator<T extends GraphNode>{
    double evaluate(T from, T to);
}
