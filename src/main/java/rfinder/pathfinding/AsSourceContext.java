package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.VertexNode;

import java.util.*;

public class AsSourceContext<T extends GraphNode> implements SourcePathContext<T> {
    private final RoutableGraph<T> graph;
    private final HeuristicEvaluator<T> heuristicEvaluator;
    private final Map<T, RoutingNode<T>> exploredSet = new HashMap<>();
    private final PriorityQueue<RoutingNode<T>> openSet = new PriorityQueue<>();
    private double maxPruningScore = Double.POSITIVE_INFINITY;


    public AsSourceContext(RoutableGraph<T> graph, T source, HeuristicEvaluator<T> heuristicEvaluator) {
        this.graph = graph;
        this.heuristicEvaluator = heuristicEvaluator;

        RoutingNode<T> rSource = new RoutingNode<>(source, null, 0, Double.MAX_VALUE / 2);
        openSet.add(rSource);
        exploredSet.put(source, rSource);
    }

    public void setMaxScore(double newMaxScore) {
        this.maxPruningScore = newMaxScore;
    }

    public GraphPath<T> findPath(T destination) {
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);

        if (last != null) {
            return reconstructPath(exploredSet, last);
        }

        if (findFurtherPath(destination))
            return reconstructPath(exploredSet, exploredSet.get(destination));

        return null;
    }

    public OptionalDouble pathCost(T destination) {

        // check if the path has already been found and return its score
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);

        if (last != null) {
            return OptionalDouble.of(last.getRouteScore());
        }

        // if not, continue exploring
        if (findFurtherPath(destination))
            return OptionalDouble.of(exploredSet.get(destination).getRouteScore());

        // if not found, return empty score
        return OptionalDouble.empty();
    }

    /**
     * Accumulating A-star algorithm
     *z
     * @param destination target node
     * @return return value
     */
    private boolean findFurtherPath(T destination) {
        while (!openSet.isEmpty()) {
            RoutingNode<T> best = openSet.poll();
            // Lowest scored node is the target

            /*
                if (best.getRouteScore() > maxPruningScore) {
                    continue;
            }*/

            if (best.getCurrent().equals(destination)) {
                return true;
            }

            Set<RouteLink<T>> connections;
            connections = graph.getLinks(best.getCurrent());
            for (RouteLink<T> link : connections) {
                T node = link.target();

                /* Retrieve the routing node associated with node
                If it doesn't exist, create a new one */
                RoutingNode<T> rNode = exploredSet.getOrDefault(node, new RoutingNode<>(node));

                // In case a new routing node was created, record it
                exploredSet.put(node, rNode);

                // Compute the new minimal distance from source
                double newScore = best.getRouteScore() + link.weight();

                // If a closer distance was found, update the rNode
                if (newScore < rNode.getRouteScore()) {
                    rNode.setPrevious(best.getCurrent());
                    rNode.setRouteScore(newScore);
                    rNode.setEstimatedScore(newScore + heuristicEvaluator.evaluateHeuristic(node, destination));
                    openSet.add(rNode);
                }
            }
        }

        return false;
    }

    private GraphPath<T> reconstructPath(Map<T, RoutingNode<T>> pathTree, RoutingNode<T> last) {
        LinkedList<T> list = new LinkedList<>();
        double totalLength = 0;

        do {
            list.addFirst(last.getCurrent());
            totalLength += last.getRouteScore();
            last = pathTree.get(last.getPrevious());

        } while (last != null);

        return new GraphPath<>(list, totalLength);
    }
}
