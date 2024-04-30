package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AsSourceContext<T extends GraphNode> implements TotalSharedSourcePathContext<T> {
    private final HeuristicEvaluator<T> heuristicEvaluator;
    private final Map<T, RoutingNode<T>> exploredSet = new ConcurrentHashMap<>();
    private final List<PathRecord<T>> computedList = new ArrayList<>();
    private final PriorityBlockingQueue<RoutingNode<T>> openSet = new PriorityBlockingQueue<>();
    private double maxPruningScore = Double.POSITIVE_INFINITY;
    private final Lock openSetLock = new ReentrantLock();


    public AsSourceContext(T source, HeuristicEvaluator<T> heuristicEvaluator) {
        this.heuristicEvaluator = heuristicEvaluator;

        RoutingNode<T> rSource = new RoutingNode<>(source, null, 0, Double.MAX_VALUE / 2);
        openSet.add(rSource);
        exploredSet.put(source, rSource);
    }

    public void setMaxScore(double newMaxScore) {
        this.maxPruningScore = newMaxScore;
    }

    @Override
    public void tryComputeToAll(RoutableGraph<T, ? extends RouteLink<T>> graph, Collection<? extends T> nodes) {
        PathRecord<T> record;

        for (T node : nodes) {
            record = tryCompute(graph, node);

            if (record != null)
                computedList.add(new PathRecord<>(node, exploredSet.get(node).getRouteScore()));
        }
    }

    @Override
    public List<PathRecord<T>> getAllComputed(RoutableGraph<T, ? extends RouteLink<T>> graph) {
        return computedList;
    }


    @Override
    public GraphPath<T> findPath(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination) {
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);

        if (last != null) {
            return reconstructPath(exploredSet, last);
        }

        if (findFurtherPath(graph, destination))
            return reconstructPath(exploredSet, exploredSet.get(destination));

        return null;
    }

    @Override
    public OptionalDouble pathCost(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination) {

        // check if the path has already been found and return its score
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);

        if (last != null) {
            return OptionalDouble.of(last.getRouteScore());
        }

        // if not, continue exploring
        if (findFurtherPath(graph, destination))
            return OptionalDouble.of(exploredSet.get(destination).getRouteScore());

        // if not found, return empty score
        return OptionalDouble.empty();
    }

    private PathRecord<T> tryCompute(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination) {
        // check if the path has already been found and return its score
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);
        boolean success = true;

        // if not, continue exploring
        if (last == null)
            success = findFurtherPath(graph, destination);

        if (success)
            return new PathRecord<>(destination, exploredSet.get(destination).getRouteScore());

        // return null if can't find a path
        return null;
    }



    /**
     * Accumulating A-star algorithm
     * 
     * @param destination target node
     * @return return value
     */
    private boolean findFurtherPath(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination) {
        openSetLock.lock();

        while (!openSet.isEmpty()) {

            RoutingNode<T> best = openSet.poll();
            openSetLock.unlock();

            // Lowest scored node is the target

            if (best.getCurrent().equals(destination)) {
                return true;
            }

            Set<? extends RouteLink<T>> connections;
            connections = graph.getLinks(best.getCurrent());
            for (RouteLink<T> link : connections) {
                T node = link.target();

                // Compute the new minimal distance from source
                double newScore = best.getRouteScore() + link.weight();

              /*  if(newScore > maxPruningScore)
                    continue;*/
/*

                 Retrieve the routing node associated with node
                If it doesn't exist, create a new one
*/

                RoutingNode<T> rNode = exploredSet.getOrDefault(node, new RoutingNode<>(node));

                // In case a new routing node was created, record it
                exploredSet.put(node, rNode);


                // If a closer distance was found, update the rNode
                if (newScore < rNode.getRouteScore()) {
                    rNode.setPrevious(best.getCurrent());
                    rNode.setRouteScore(newScore);
                    rNode.setEstimatedScore(newScore + heuristicEvaluator.evaluateHeuristic(node, destination));
                    openSet.add(rNode);
                }
            }

            openSetLock.lock();
        }

        return false;
    }

    private GraphPath<T> reconstructPath(Map<T, RoutingNode<T>> pathTree, RoutingNode<T> last) {
        LinkedList<T> list = new LinkedList<>();
        T previous;
        double length = last.getRouteScore();

        do {
            list.addFirst(last.getCurrent());
            previous = last.getPrevious();
            last = previous == null ? null : pathTree.get(previous);

        } while (last != null);

        return new GraphPath<>(list, length);
    }
}
