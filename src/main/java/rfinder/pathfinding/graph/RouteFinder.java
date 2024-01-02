package rfinder.pathfinding.graph;

import rfinder.structures.graph.*;

import java.util.*;

public class RouteFinder <T extends GraphNode>{
    private final RoutableGraph<T> graph;
    private final HeuristicEvaluator<T> heuristicEvaluator;

    public RouteFinder(RoutableGraph<T> graph, HeuristicEvaluator<T> heuristicEvaluator){
        this.graph = graph;
        this.heuristicEvaluator = heuristicEvaluator;
    }

    /**
     * A-star algorithm implementation
     * @param source source node
     * @param destination desitnation node
     * @return shortest path between the nodes
     */
    public List<T> computeAStar(T source, T destination){
        PriorityQueue<RoutingNode<T>> openSet = new PriorityQueue<>();
        Map<T, RoutingNode<T>> exploredSet = new HashMap<>();

        RoutingNode<T> rSource = new RoutingNode<>(source, null, 0, heuristicEvaluator.evaluateHeuristic(source, destination));
        openSet.add(rSource);
        exploredSet.put(source, rSource);

        while (!openSet.isEmpty()){
            RoutingNode<T> best = openSet.poll();
            // Lowest scored node is the destination

            if(best.getCurrent().equals(destination)) {
                return reconstructPath(exploredSet, best);
            }

            openSet.remove(best);
            Set<RouteLink<T>> connections;
            connections = graph.getLinks(best.getCurrent());
            for(RouteLink<T> link : connections){
                T node = link.getDestination();

                /* Retrieve the routing node associated with node
                If it doesn't exist, create a new one */
                RoutingNode<T> rNode = exploredSet.getOrDefault(node, new RoutingNode<>(node));

                // In case a new routing node was created, record it
                exploredSet.put(node, rNode);

                // Compute the new minimal distance from source
                double newScore = best.getRouteScore() + link.getWeight();

                // If a closer distance was found, update the rNode
                if(newScore < rNode.getRouteScore()) {
                    rNode.setPrevious(best.getCurrent());

                    rNode.setRouteScore(newScore);
                    rNode.setEstimatedScore(newScore + heuristicEvaluator.evaluateHeuristic(node, destination));
                    openSet.add(rNode);
                }
            }
        }

        System.out.println(exploredSet.size());
        for(T node : exploredSet.keySet())
            System.out.println(node);
        return new LinkedList<T>();
    }

    private List<T> reconstructPath(Map<T, RoutingNode<T>> exploredSet, RoutingNode<T> last){
        LinkedList<T> ret = new LinkedList<>();

        do{
            ret.addFirst(last.getCurrent());
            last = exploredSet.get(last.getPrevious());

        } while (last != null);

        return ret;
    }
}
