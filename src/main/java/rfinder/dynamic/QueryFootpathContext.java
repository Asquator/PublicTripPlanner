/*
package rfinder.dynamic;

import rfinder.dao.RoadDAO;
import rfinder.model.network.walking.InMemoryVertexGraph;
import rfinder.pathfinding.*;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.nodes.GraphNodeAdapter;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.VertexNode;

import java.util.Optional;
import java.util.Set;

public class QueryFootpathContext<T extends GraphNode> implements FootpathFinder {

    private final GraphPathFinder<VertexNode> graphPathFinder;

    private final RoadDAO roadDAO;

    public class SourceContextAdapter implements SourcePathContext<T>{

        private GraphNodeAdapter source;

        public SourceContextAdapter(PathNode node, int id, Set<RouteLink<T>> links) {
            source = new GraphNodeAdapter(node, id);
        }

        @Override
        public GraphPath<T> findPath(T target) {
            return null;
        }

        @Override
        public Optional<Double> pathCost(T target) {
            return Optional.empty();
        }


        public GraphPath<GraphNode> findSourcePath(GraphNode target) {
            return null;
        }
    }

    private SourceContextAdapter<T> sourceContext;
    private SourceContextAdapter<T> destinationContext;

    public QueryFootpathContext(RoadDAO dao, QueryInfo queryInfo) {
        RoutableGraph<VertexNode> graph = new InMemoryVertexGraph(dao);
        this.graphPathFinder = new CachedAsPathFinder<>(graph, new HaversineDistanceEvaluator(), 3 * queryInfo.maxTrips());
        sourceContext = new SourceContextAdapter<>();

    }


    @Override
    public FootPath findPath(PathNode from, PathNode to) {
        return null;
    }

    @Override
    public Optional<Double> pathCost(PathNode from, PathNode to) {
        return Optional.empty();
    }


}
*/
