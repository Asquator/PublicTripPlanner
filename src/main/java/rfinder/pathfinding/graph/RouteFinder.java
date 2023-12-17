package rfinder.pathfinding.graph;

import java.util.*;

public class RouteFinder <T extends GraphNode>{
    private final Graph<T> graph;
    private final CostEvaluator<T> nextNodeEvaluator;
    private final CostEvaluator<T> heuristicEvaluator;

    public RouteFinder(Graph<T> graph, CostEvaluator<T> nextNodeEvaluator, CostEvaluator<T> heuristicEvaluator) {
        this.graph = graph;
        this.nextNodeEvaluator = nextNodeEvaluator;
        this.heuristicEvaluator = heuristicEvaluator;
    }

    public List<T> computeAStar(T source, T destination){
        PriorityQueue<RoutingNode<T>> openSet = new PriorityQueue<>();
        Map<T, RoutingNode<T>> exploredSet = new HashMap<>();

        openSet.add(new RoutingNode<>(source, null, 0, heuristicEvaluator.evaluate(source, destination)));
        while (!openSet.isEmpty()){
            RoutingNode<T> best = openSet.poll();

            // Lowest scored node is the destination
            if(best.getCurrent().equals(destination))
                return reconstructPath(exploredSet, best);


            openSet.remove(best);
            Set<T> connections = graph.getConnections(best.getCurrent());
            for(T node : connections){

                /* Retrieve the routing node associated with node
                If it doesn't exist, create a new one */
                RoutingNode<T> rNode = exploredSet.getOrDefault(node, new RoutingNode<>(node));

                // In case a new routing node was created, record it
                exploredSet.put(node, rNode);

                // Compute the new minimal distance from source
                double newScore = best.getRouteScore() + nextNodeEvaluator.evaluate(best.getCurrent(), node);

                // If a closer distance was found, update the rNode
                if(newScore < rNode.getRouteScore()) {
                    rNode.setPrevious(best.getCurrent());
                    rNode.setRouteScore(newScore);
                    rNode.setEstimatedScore(newScore + heuristicEvaluator.evaluate(node, destination));
                    openSet.add(rNode);
                }
            }
        }

        return new LinkedList<T>();
    }

    public List<T> reconstructPath(Map<T, RoutingNode<T>> exploredSet, RoutingNode<T> last){
        LinkedList<T> ret = new LinkedList<>();
        do{
            ret.addFirst(last.getCurrent());
            last = exploredSet.get(last.getPrevious());
        } while (last != null);

        return ret;
    }
}
