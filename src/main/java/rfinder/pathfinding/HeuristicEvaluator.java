package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

public interface HeuristicEvaluator<T extends GraphNode>{

    double evaluateHeuristic(T from, T to);
}
