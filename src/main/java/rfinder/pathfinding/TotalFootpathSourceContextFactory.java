package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.dao.FootpathDAO;
import rfinder.dao.TransferDAO;
import rfinder.pathfinding.heuristic.EuclideanDistanceEvaluator;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

class TotalFootpathSourceContextFactory implements FactoryCache.FactoryFunction<PathNode, TotalSourcePathContext<PathNode>> {
    private final FootpathDAO dao;
    private final TransferDAO transferDAO;
    private final double freeRadius;

    private final HeuristicEvaluator<PathNode> heuristicEvaluator = new EuclideanDistanceEvaluator();

    public TotalFootpathSourceContextFactory(FootpathDAO footpathDAO, TransferDAO transferDAO, double freeRadius) {
        this.dao = footpathDAO;
        this.transferDAO = transferDAO;
        this.freeRadius = freeRadius;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TotalSourcePathContext<PathNode> create(PathNode source, Object... args) {

        RoutableGraph<PathNode, ?> graph = (RoutableGraph<PathNode, ?>) args[0];

        PrecomputedAsContext<PathNode> context;

        // if source is a stop, use transfer data
        if(source instanceof StopNode stopNode) {
            var transfers = transferDAO.getByStop(stopNode);
            context = new PrecomputedAsContext<>(graph, source, heuristicEvaluator, transfers);
        }

        // if source is not a stop, use footpath data and compute paths to all nearby stops
        else {
            context = new PrecomputedAsContext<>(graph, source, heuristicEvaluator);
            context.tryComputeToAll(source.getFootpathsWith(dao, freeRadius));
            context.setMaxScore(freeRadius);
        }
        return context;
    }

}
