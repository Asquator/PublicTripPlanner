package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.dao.FootpathDAO;
import rfinder.pathfinding.heuristic.EuclideanDistanceEvaluator;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.Set;

class TotalFootpathSourceContextFactory implements FactoryCache.FactoryFunction<PathNode, TotalSharedSourcePathContext<PathNode>> {
    private final FootpathDAO dao;
    private final double pruningScore;

    public TotalFootpathSourceContextFactory(FootpathDAO dao, double pruningScore) {
        this.dao = dao;
        this.pruningScore = pruningScore;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TotalSharedSourcePathContext<PathNode> create(PathNode source, Object... args) {

        RoutableGraph<PathNode, ?> graph = (RoutableGraph<PathNode, ?>) args[0];

        AsSourceContext<PathNode> context = new AsSourceContext<>(source, new EuclideanDistanceEvaluator());
        context.setMaxScore(pruningScore);

        Set<StopNode> footpaths = source.getFootpathsWith(dao, pruningScore);
        context.tryComputeToAll(graph, footpaths);

        return context;
    }

}
