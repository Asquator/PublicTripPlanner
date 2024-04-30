package rfinder.service;

import rfinder.client.QuerySubmitter;
import rfinder.dao.*;
import rfinder.dynamic.DynamicContext;
import rfinder.dynamic.InMemoryTripStorage;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.dynamic.TripRepository;
import rfinder.dynamic.label.ParetoBag;
import rfinder.pathfinding.*;
import rfinder.pathfinding.heuristic.EuclideanDistanceEvaluator;
import rfinder.query.NodeLinkageResolver;
import rfinder.query.QueryContext;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.query.result.FullBagExtractor;
import rfinder.query.result.QuerySolution;
import rfinder.query.result.ResultExtractor;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalQuerySubmitter implements QuerySubmitter {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final GraphDAO graphDAO = new DefaultGraphDAO();

    private final FootpathDAO footpathDAO = new DefaultFootpathDAO();

    private RoutableGraph<PathNode, ShapedLink> baseRoadGraph;

    private final HeuristicEvaluator<PathNode> heuristicEvaluator = new EuclideanDistanceEvaluator();

    private final NodeLinkageResolver linkageResolver = new NodeLinkageResolver();

    private final InMemoryTripStorage tripStorage = new InMemoryTripStorage("5m", "3h");

    private final StopStorage stopStorage = new StopStorage(new DefaultStopDAO(), new DefaultRouteDAO());

    private FootpathCache footpathCache;

    private static final double WALK_RADIUS = 1.3;


    public LocalQuerySubmitter() {
        initializeStorage();
    }

    public void initializeStorage(){
        baseRoadGraph = new InMemoryNetworkGraph(graphDAO);
        footpathCache = new FootpathCache(footpathDAO, 1.5 * WALK_RADIUS);
    }


    @Override
    public CompletableFuture<List<QuerySolution>> submit(QueryInfo queryInfo) {
        ResultExtractor extractor = new FullBagExtractor();

        DynamicContext context = new DynamicContext(new DefaultContextRetriever(), new DefaultRouteDAO(), new DefaultFootpathDAO());
        QueryGraphInfo graphInfo = new QueryGraphInfo(queryInfo, linkageResolver);

        ExtendedQueryGraph qgraph = new ExtendedQueryGraph(baseRoadGraph, graphInfo);

        TotalCachedPathFinder<PathNode, ShapedLink> graphPathFinder =
                new CachedFootpathFinder(qgraph, footpathCache);

        QueryFootpathManager qPathFinder = new QueryFootpathManager(qgraph, graphPathFinder, graphInfo);

        TripRepository tripRepo = tripStorage.createTripRepo(queryInfo);
        context.initializeStorage(ParetoBag.class, new QueryContext(queryInfo, graphInfo, qPathFinder, tripRepo, stopStorage));

        return CompletableFuture.supplyAsync(() ->
        {
            NetworkQueryContext nContext = context.compute();
            List<QuerySolution> res = extractor.extract(nContext);
            footpathCache.remove(graphInfo.sourceRepr());
            footpathCache.remove(graphInfo.destinationRepr());

            return res;
        }, executor);
    }

}
