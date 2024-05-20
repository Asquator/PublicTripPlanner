package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.LinkedList;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AsSourceContext<T extends GraphNode> implements SourcePathContext<T> {

    private final HeuristicEvaluator<T> heuristicEvaluator; // heuristic
    private final Map<T, RoutingNode<T>> exploredSet = new ConcurrentHashMap<>();
    private final PriorityBlockingQueue<RoutingNode<T>> openSet = new PriorityBlockingQueue<>(); // set of nodes to be explored
    private final RoutableGraph<T, ? extends RouteLink<T>> graph;
    private double maxPruningScore = Double.MAX_VALUE; // max score to prune the search
    private final ReadWriteLock clearLock = new ReentrantReadWriteLock(); // lock to prevent clearing the context in the middle of a search

    private final T source;
    private final RoutingNode<T> sourceNode;

    public AsSourceContext(RoutableGraph<T, ? extends RouteLink<T>> graph, T source, HeuristicEvaluator<T> heuristicEvaluator) {
        this.heuristicEvaluator = heuristicEvaluator;
        this.graph = graph;
        this.source = source;

        // add the source to the open set
        sourceNode = new RoutingNode<>(source, null, 0, Double.MAX_VALUE / 2);
        openSet.add(sourceNode);
        exploredSet.put(source, sourceNode);
    }


    public void setMaxScore(double newMaxScore) {
        this.maxPruningScore = newMaxScore;
    }

    public void clearContext(){
        clearLock.writeLock().lock();
        exploredSet.clear();
        openSet.clear();
        openSet.add(sourceNode);
        exploredSet.put(source, sourceNode);
        clearLock.writeLock().unlock();
    }

    @Override
    public GraphPath<T> findPath(T destination, RoutableGraph<T, ? extends RouteLink<T>> graph) {
        clearLock.readLock().lock();

        GraphPath<T> ret = null;

        // check if the path has already been found
        RoutingNode<T> last = exploredSet.getOrDefault(destination, null);

        if (last != null) {
            ret = reconstructPath(last);
        }

        // if not, continue exploring
        else synchronized (this) {
            // check again if the path has already been found
            last = exploredSet.getOrDefault(destination, null);

            if (last == null)
                last = findFurtherPath(destination, graph);

            if(last != null)
                ret = reconstructPath(last);

        }

        clearLock.readLock().unlock();
        return ret;
    }

    @Override
    public OptionalDouble pathCost(T destination, RoutableGraph<T, ? extends RouteLink<T>> graph) {
        clearLock.readLock().lock();
        // check if the path has already been found and return its score
        exploredSet.getOrDefault(destination, null);
        RoutingNode<T> last;
        OptionalDouble ret;

        // if not, continue exploring
        synchronized (this) {
            last = exploredSet.getOrDefault(destination, null);
            if (last == null)
                last = findFurtherPath(destination, graph);

            if(last != null)
                ret = OptionalDouble.of(last.getRouteScore());

            else
                ret = OptionalDouble.empty();
        }

        clearLock.readLock().unlock();
        // if not found, return empty score
        return ret;
    }

    @Override
    public GraphPath<T> findPath(T destination) {
        return findPath(destination, this.graph);
    }

    @Override
    public OptionalDouble pathCost(T destination) {
        return pathCost(destination, this.graph);
    }


    /**
     * Accumulating A-star algorithm
     * 
     * @param destination target node
     * @return return value
     */

    /*
      Necessary condition: clearLock is held by one of the calling functions
     */
    private RoutingNode<T> findFurtherPath(T destination, RoutableGraph<T, ? extends RouteLink<T>> graph) {

        while (!openSet.isEmpty()) {

            RoutingNode<T> best = openSet.poll();

            // Lowest scored node is the target

            if (best.getCurrent().equals(destination)) {
                return best;
            }

            Set<? extends RouteLink<T>> connections;
            connections = graph.getLinks(best.getCurrent());
            for (RouteLink<T> link : connections) {
                T node = link.target();

                // Compute the new minimal distance from source
                double newScore = best.getRouteScore() + link.weight();

                if(newScore > maxPruningScore)
                    continue;

               /* Retrieve the routing node associated with node
                If it doesn't exist, create a new one*/

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

        }

        return null;
    }

    private GraphPath<T> reconstructPath(RoutingNode<T> last) {
        clearLock.readLock().lock();
        LinkedList<T> list = new LinkedList<>();
        T previous;
        double length = last.getRouteScore();

        do {
            list.addFirst(last.getCurrent());
            previous = last.getPrevious();
            last = previous == null ? null : exploredSet.get(previous);

        } while (last != null);

        clearLock.readLock().unlock();
        return new GraphPath<>(list, length);
    }
}
